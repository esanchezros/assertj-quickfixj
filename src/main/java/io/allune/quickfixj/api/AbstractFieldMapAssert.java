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
import quickfix.Group;

import java.math.BigDecimal;

import static io.allune.quickfixj.error.FieldShouldHaveValue.fieldShouldHaveValue;
import static io.allune.quickfixj.error.ShouldHaveGroup.shouldHaveGroup;
import static io.allune.quickfixj.error.ShouldHaveGroupCount.shouldHaveGroupCount;
import static io.allune.quickfixj.error.ShouldHaveField.shouldHaveField;
import static io.allune.quickfixj.error.ShouldNotHaveField.shouldNotHaveField;

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
		this(actual, selfType, Dictionaries.instance());
	}

	/**
	 * Creates a new assertion using the supplied dictionary registry.
	 *
	 * @param actual       the actual value to verify
	 * @param selfType     the "self type"
	 * @param dictionaries the dictionary registry for this assertion chain
	 */
	protected AbstractFieldMapAssert(ACTUAL actual, Class<SELF> selfType, Dictionaries dictionaries) {
		super(actual, selfType);
		this.dictionaries = dictionaries;
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
	 * Verifies that the actual FieldMap does not have a field with the provided tag number.
	 *
	 * @param unexpectedTag the field tag that must be absent
	 * @return {@code this} assertion object
	 */
	public SELF doesNotHaveField(int unexpectedTag) {
		isNotNull();
		if (actual.isSetField(unexpectedTag)) {
			throw failures.failure(info, shouldNotHaveField(actual, unexpectedTag));
		}
		return (SELF) this;
	}

	/**
	 * Verifies that the actual FieldMap contains at least one entry for the repeating-group count tag.
	 *
	 * @param groupTag the repeating-group count tag
	 * @return {@code this} assertion object
	 */
	public SELF hasGroup(int groupTag) {
		isNotNull();
		if (!actual.hasGroup(groupTag)) {
			throw failures.failure(info, shouldHaveGroup(actual, groupTag, 1, actual.getGroupCount(groupTag)));
		}
		return (SELF) this;
	}

	/**
	 * Verifies the number of entries in a repeating group.
	 *
	 * @param groupTag the repeating-group count tag
	 * @param expectedCount the expected number of group entries
	 * @return {@code this} assertion object
	 */
	public SELF hasGroupCount(int groupTag, int expectedCount) {
		isNotNull();
		if (expectedCount < 0) {
			throw new IllegalArgumentException("'expectedCount' must not be negative");
		}
		int actualCount = actual.getGroupCount(groupTag);
		if (actualCount != expectedCount) {
			throw failures.failure(info, shouldHaveGroupCount(actual, groupTag, actualCount, expectedCount));
		}
		return (SELF) this;
	}

	/**
	 * Selects a one-based entry from a repeating group.
	 *
	 * @param groupTag the repeating-group count tag
	 * @param index the one-based group entry index
	 * @return an assertion for the selected group entry
	 */
	public GroupAssert<SELF> group(int groupTag, int index) {
		isNotNull();
		if (index <= 0) {
			throw new IllegalArgumentException("'index' must be greater than 0");
		}
		if (!actual.hasGroup(index, groupTag)) {
			throw failures.failure(info, shouldHaveGroup(actual, groupTag, index, actual.getGroupCount(groupTag)));
		}
		try {
			Group group = actual.getGroup(index, groupTag);
			return new GroupAssert<>(group, (SELF) this, getBeginString(), dictionaries);
		} catch (quickfix.FieldNotFound fieldNotFound) {
			throw failures.failure(info, shouldHaveGroup(actual, groupTag, index, actual.getGroupCount(groupTag)));
		}
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
			Object actualFieldValue = messages.getFieldValue(expectedFieldTag, getBeginString(), this.actual, dictionaries);
			if (!expectedFieldValue.equals(actualFieldValue))
				throw failures.failure(info, fieldShouldHaveValue(actual, expectedFieldTag, actualFieldValue, expectedFieldValue));
		} catch (FieldNotFoundException ex) {
			throw failures.failure(info, shouldHaveField(actual, expectedFieldTag));
		} catch (RuntimeException ex) {
			try {
				String rawValue = actual.getString(expectedFieldTag);
				throw failures.failure(info,
						fieldShouldHaveValue(actual, expectedFieldTag, rawValue, expectedFieldValue));
			} catch (quickfix.FieldNotFound fieldNotFound) {
				throw failures.failure(info, shouldHaveField(actual, expectedFieldTag));
			}
		}

		return (SELF) this;
	}

	/**
	 * Verifies that a decimal FIX field has the expected exact numeric value.
	 * <p>
	 * The raw FIX value is converted directly to {@link BigDecimal}. Values are compared numerically, so scale is not
	 * significant: for example, {@code 1.0} and {@code 1.00} are equal. The field must use one of the decimal FIX
	 * dictionary types: {@code PRICE}, {@code AMT}, {@code QTY}, {@code FLOAT}, {@code PRICEOFFSET}, or
	 * {@code PERCENTAGE}.
	 *
	 * @param expectedFieldTag the expected tag number of the field
	 * @param expectedFieldValue the expected exact decimal value
	 * @return {@code this} assertion object
	 */
	public SELF hasDecimalFieldValue(int expectedFieldTag, BigDecimal expectedFieldValue) {
		isNotNull();

		if (expectedFieldTag <= 0) {
			throw new IllegalArgumentException("'expectedFieldTag' must be greater than 0");
		}

		if (expectedFieldValue == null) {
			throw new IllegalArgumentException("'expectedFieldValue' must not be null.");
		}

		hasField(expectedFieldTag);
		messages.assertDecimalFieldHasValue(
				info, actual, expectedFieldTag, getBeginString(), expectedFieldValue, dictionaries);
		return (SELF) this;
	}

	/**
	 * @return Returns the BeginString value for the actual message.
	 */
	public abstract String getBeginString();
}
