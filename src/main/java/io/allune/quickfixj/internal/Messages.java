/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2020-2020 the original author or authors.
 */
package io.allune.quickfixj.internal;

import io.allune.quickfixj.exception.FieldNotFoundException;
import io.allune.quickfixj.exception.ThrownByLambdaException;
import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;
import quickfix.BooleanField;
import quickfix.CharField;
import quickfix.DataDictionary;
import quickfix.DecimalField;
import quickfix.DoubleField;
import quickfix.Field;
import quickfix.FieldMap;
import quickfix.FieldNotFound;
import quickfix.FieldType;
import quickfix.FixVersions;
import quickfix.IncorrectDataFormat;
import quickfix.IntField;
import quickfix.InvalidMessage;
import quickfix.Message;
import quickfix.StringField;
import quickfix.UtcDateOnlyField;
import quickfix.UtcTimeOnlyField;
import quickfix.UtcTimeStampField;
import quickfix.field.ApplVerID;
import quickfix.field.BeginString;
import quickfix.field.MsgType;

import java.util.HashMap;
import java.util.Map;

import static io.allune.quickfixj.error.FieldShouldHaveTag.fieldShouldHaveTag;
import static io.allune.quickfixj.error.UnsupportedFieldType.unsupportedFieldType;
import static io.allune.quickfixj.error.FieldShouldHaveValue.fieldShouldHaveValue;
import static io.allune.quickfixj.error.ShouldBeOfType.shouldBeOfType;
import static io.allune.quickfixj.error.ShouldHaveField.shouldHaveField;
import static quickfix.FixVersions.BEGINSTRING_FIXT11;
import static quickfix.MessageUtils.getMessageType;
import static quickfix.MessageUtils.toBeginString;

/**
 * @author Eduardo Sanchez-Ros
 */
public class Messages {

	private static final Messages INSTANCE = new Messages();

	private static final String FIELD_TAG_FIELD_NAME = "FIELD";

	private static final Map<Class<? extends Field<?>>, ThrowingBiFunction<FieldMap, Integer, Object>> quickfixClassToObject =
			new HashMap<Class<? extends Field<?>>, ThrowingBiFunction<FieldMap, Integer, Object>>() {{

				put(StringField.class, FieldMap::getString);
				put(BooleanField.class, FieldMap::getBoolean);
				put(CharField.class, FieldMap::getChar);
				put(IntField.class, FieldMap::getInt);
				put(DoubleField.class, FieldMap::getDouble);
				put(DecimalField.class, FieldMap::getDecimal);
				put(UtcTimeStampField.class, FieldMap::getUtcTimeStamp);
				put(UtcTimeOnlyField.class, FieldMap::getUtcTimeOnly);
				put(UtcDateOnlyField.class, FieldMap::getUtcDateOnly);
			}};
	Objects objects = Objects.instance();
	Failures failures = Failures.instance();
	Dictionaries dictionaries = Dictionaries.instance();

	Messages() {
	}

	public static Messages instance() {
		return INSTANCE;
	}

	public void assertMessageIsOfType(AssertionInfo info, Message actual, String expectedMessageType) {
		objects.assertNotNull(info, actual);

		assertSameMsgType(info, actual, expectedMessageType);
	}

	public void assertMessageIsOfTypeName(AssertionInfo info, Message actual, String beginString, String expectedMessageTypeName) {
		objects.assertNotNull(info, actual);

		String expectedMessageType = dictionaries.getSessionDataDictionary(beginString).getMsgType(expectedMessageTypeName);
		assertSameMsgType(info, actual, expectedMessageType);
	}

	private void assertSameMsgType(AssertionInfo info, Message actual, String expectedMessageType) {
		try {
			String actualMessageType = actual.getHeader().getString(MsgType.FIELD);
			if (!actualMessageType.equals(expectedMessageType)) {
				throw failures.failure(info, shouldBeOfType(actual, actualMessageType, expectedMessageType));
			}
		} catch (FieldNotFound e) {
			throw failures.failure(info, shouldHaveField(actual, MsgType.FIELD));
		}
	}

	public <T> void assertFieldHasValue(AssertionInfo info, FieldMap actual, Class<? extends Field<T>> actualFieldClass, T expectedFieldValue) {
		objects.assertNotNull(info, actual);

		int actualFieldTag = getFieldTagFromFieldClass(info, actualFieldClass);
		Object actualFieldValue = getActualValue(info, actual, actualFieldClass, actualFieldTag);
		if (!actualFieldValue.equals(expectedFieldValue)) {
			throw failures.failure(info, fieldShouldHaveValue(actual, actualFieldClass, actualFieldTag, actualFieldValue, expectedFieldValue));
		}
	}

	public <T> void assertFieldHasValue(AssertionInfo info, Message actual, Field<T> actualField, Object expectedFieldValue) {
		objects.assertNotNull(info, actual);

		if (!actualField.getObject().equals(expectedFieldValue))
			throw failures.failure(info, fieldShouldHaveValue(actual, actualField.getTag(), actualField.getObject(), expectedFieldValue));
	}

	private int getFieldTagFromFieldClass(AssertionInfo info, Class<? extends Field<?>> actualFieldClass) {
		try {
			return actualFieldClass.getField(FIELD_TAG_FIELD_NAME).getInt(null);
		} catch (IllegalAccessException | NoSuchFieldException e) {
			throw failures.failure(info, fieldShouldHaveTag(actualFieldClass));
		}
	}

	private Object getActualValue(AssertionInfo info, FieldMap actual, Class<? extends Field<?>> actualFieldClass, int actualFieldTag) {
		ThrowingBiFunction<FieldMap, Integer, Object> fieldValueFunction = quickfixClassToObject.get(actualFieldClass.getSuperclass());
		if (fieldValueFunction == null) {
			throw failures.failure(info, unsupportedFieldType(actualFieldClass));
		}

		try {
			return fieldValueFunction.apply(actual, actualFieldTag);
		} catch (ThrownByLambdaException e) {
			if (e.getCause().getClass().equals(FieldNotFound.class)) {
				throw failures.failure(info, shouldHaveField(actualFieldClass, actualFieldTag));
			} else {
				// TODO: other exception throw failure
				throw failures.failure(info, shouldHaveField(actualFieldClass, actualFieldTag));
			}
		}
	}

	public String determineBeginString(AssertionInfo info, Message message) {
		objects.assertNotNull(info, message);

		String beginString = getBeginString(info, message);
		if (beginString.equals(BEGINSTRING_FIXT11)) {
			String applVerId = getApplVerId(info, message);
			return toBeginString(new ApplVerID(applVerId));
		}
		return beginString;
	}

	public String getBeginString(AssertionInfo info, Message message) {
		return getHeaderFieldValue(info, message, BeginString.class, BeginString.FIELD);
	}

	public String getApplVerId(AssertionInfo info, Message message) {
		return getHeaderFieldValue(info, message, ApplVerID.class, ApplVerID.FIELD);
	}

	private String getHeaderFieldValue(AssertionInfo info, Message message, Class<?> actualClazz, int actualField) {
		try {
			return message.getHeader().getString(actualField);
		} catch (FieldNotFound fieldNotFound) {
			throw failures.failure(info, shouldHaveField(actualClazz, actualField));
		}
	}

	public Object getFieldValue(int fieldTag, String beginString, FieldMap actual) throws FieldNotFoundException {
		DataDictionary dataDictionary = dictionaries.getSessionDataDictionary(beginString);
		FieldType fieldType = dataDictionary.getFieldType(fieldTag);
		if (fieldType == null) {
			return null;
		}

		try {
			switch (fieldType) {
				case STRING:
				case MULTIPLEVALUESTRING:
				case MULTIPLESTRINGVALUE:
				case EXCHANGE:
				case LOCALMKTDATE:
				case DATA:
				case MONTHYEAR:
				case DAYOFMONTH:
				case COUNTRY:
					// String
					return actual.getString(fieldTag);
				case INT:
				case NUMINGROUP:
				case SEQNUM:
				case LENGTH:
					return actual.getInt(fieldTag);
				case PRICE:
				case AMT:
				case QTY:
				case FLOAT:
				case PRICEOFFSET:
				case PERCENTAGE:
					return actual.getDouble(fieldTag);
				case BOOLEAN:
					return actual.getBoolean(fieldTag);
				case UTCDATE:
					return actual.getUtcDateOnly(fieldTag);
				case UTCTIMEONLY:
					return actual.getUtcTimeOnly(fieldTag);
				case UTCTIMESTAMP:
				case TIME:
					return actual.getUtcTimeStamp(fieldTag);
				case CHAR:
					if (beginString.compareTo(FixVersions.BEGINSTRING_FIX41) > 0) {
						return actual.getChar(fieldTag);
					} // otherwise it's a String, for older FIX versions
					return actual.getString(fieldTag);
				default:
					throw new RuntimeException("Unsupported");
			}
		} catch (FieldNotFound e) {
			throw new FieldNotFoundException(e.getMessage(), e);
		}
	}
}
