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
package io.allune.quickfixj.api;

import io.allune.quickfixj.exception.FieldNotFoundException;
import io.allune.quickfixj.internal.Dictionaries;
import io.allune.quickfixj.internal.Messages;
import io.allune.quickfixj.internal.Versions;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.internal.Failures;
import quickfix.FieldMap;

import static io.allune.quickfixj.error.FieldShouldHaveValue.fieldShouldHaveValue;
import static io.allune.quickfixj.error.ShouldHaveField.shouldHaveField;

/**
 * Base class for Message, Header and Trailer assertions
 *
 * @param <SELF>   the "self" type of this assertion class.
 * @param <ACTUAL> the type of the "actual" value which is {@link FieldMap}.
 *
 * @author Eduardo Sanchez-Ros
 */
public abstract class AbstractFieldMapAssert<SELF extends AbstractFieldMapAssert<SELF, ACTUAL>, ACTUAL extends FieldMap>
		extends AbstractAssert<SELF, ACTUAL> {

	Failures failures = Failures.instance();

	Messages messages = Messages.instance();

	Versions versions = Versions.instance();

	Dictionaries dictionaries = Dictionaries.instance();

	/**
	 * Creates a new <code>{@link AbstractFieldMapAssert}</code>.
	 *
	 * @param actual   the actual value to verify.
	 * @param selfType the "self type".
	 */
	protected AbstractFieldMapAssert(ACTUAL actual, Class<SELF> selfType) {
		super(actual, selfType);
	}

	/**
	 * Verifies that the actual FieldMap has a field with the provided tag number.
	 *
	 * @param expectedTag The expected tag number of the field.
	 * @return {@code this} assertion object.
	 */
	public SELF hasField(int expectedTag) {
		isNotNull();
		if (!actual.isSetField(expectedTag))
			throw failures.failure(info, shouldHaveField(actual, expectedTag));
		return (SELF) this;
	}

	/**
	 * Verifies that the actual FieldMap has the fields with the provided tag numbers.
	 *
	 * @param expectedFieldTags The expected tag numbers of the fields.
	 * @return {@code this} assertion object.
	 */
	public SELF hasFields(int... expectedFieldTags) {
		isNotNull();
		for (int field : expectedFieldTags) {
			hasField(field);
		}
		return (SELF) this;
	}

	/**
	 * Verifies that the actual FieldMap has a field with the expected value.
	 *
	 * @param expectedFieldTag The expected tag number of the field.
	 * @param expectedFieldValue The expected value.
	 * @return {@code this} assertion object.
	 */
	public SELF hasFieldValue(int expectedFieldTag, Object expectedFieldValue) {
		isNotNull();

		if (expectedFieldTag <= 0) {
			throw new IllegalArgumentException("'expectedFieldTag' must be greater than 0");
		}

		if (expectedFieldValue == null) {
			throw new IllegalArgumentException("'expectedFieldValue' must not be null.");
		}

		hasField(expectedFieldTag);

		try {
			Object actualFieldValue = messages.getFieldValue(expectedFieldTag, getBeginString(), this.actual);
			if (!expectedFieldValue.equals(actualFieldValue))
				throw failures.failure(info, fieldShouldHaveValue(actual, expectedFieldTag, actualFieldValue, expectedFieldValue));
		} catch (FieldNotFoundException ex) {
			throw failures.failure(info, shouldHaveField(actual, expectedFieldTag));
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}

		return (SELF) this;
	}

	/**
	 * @return Returns the BeginString value for the actual message.
	 */
	public abstract String getBeginString();
}
