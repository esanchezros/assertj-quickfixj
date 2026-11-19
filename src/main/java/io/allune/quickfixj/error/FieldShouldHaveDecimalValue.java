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
package io.allune.quickfixj.error;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

/**
 * Failure messages for exact decimal field assertions.
 */
public class FieldShouldHaveDecimalValue extends BasicErrorMessageFactory {

	private static final String VALUE_MISMATCH =
			"Expecting decimal field <%s> with tag <%s> and dictionary type <%s> in Message:%n"
					+ " <%s>%n"
					+ "to have numeric value:%n"
					+ " <%s>%n"
					+ "but raw value was:%n"
					+ " <%s>";

	private static final String CONVERSION_FAILURE =
			"Could not assert decimal field <%s> with tag <%s> and dictionary type <%s> in Message:%n"
					+ " <%s>%n"
					+ "raw value:%n"
					+ " <%s>%n"
					+ "expected numeric value:%n"
					+ " <%s>%n"
					+ "conversion problem:%n"
					+ " <%s>";

	private FieldShouldHaveDecimalValue(Object message, Object fieldName, Object fieldTag, Object fieldType,
			Object rawValue, Object expectedValue) {
		super(VALUE_MISMATCH, fieldName, fieldTag, fieldType, message, expectedValue, rawValue);
	}

	private FieldShouldHaveDecimalValue(Object message, Object fieldName, Object fieldTag, Object fieldType,
			Object rawValue, Object expectedValue, Object conversionProblem) {
		super(CONVERSION_FAILURE, fieldName, fieldTag, fieldType, message, rawValue, expectedValue, conversionProblem);
	}

	public static ErrorMessageFactory fieldShouldHaveDecimalValue(Object message, Object fieldName, Object fieldTag,
			Object fieldType, Object rawValue, Object expectedValue) {
		return new FieldShouldHaveDecimalValue(message, fieldName, fieldTag, fieldType, rawValue, expectedValue);
	}

	public static ErrorMessageFactory decimalFieldConversionFailed(Object message, Object fieldName, Object fieldTag,
			Object fieldType, Object rawValue, Object expectedValue, Object conversionProblem) {
		return new FieldShouldHaveDecimalValue(
				message, fieldName, fieldTag, fieldType, rawValue, expectedValue, conversionProblem);
	}
}
