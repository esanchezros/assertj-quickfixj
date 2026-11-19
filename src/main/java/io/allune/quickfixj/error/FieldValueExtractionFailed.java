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
 * Failure message for dictionary conversion or assertion-factory errors during field extraction.
 */
public class FieldValueExtractionFailed extends BasicErrorMessageFactory {

	private static final String EXTRACTION_FAILURE =
			"Could not extract field <%s> with tag <%s> and dictionary type <%s> in Message:%n"
					+ " <%s>%n"
					+ "raw value:%n"
					+ " <%s>%n"
					+ "assertion factory:%n"
					+ " <%s>%n"
					+ "problem:%n"
					+ " <%s>";

	private FieldValueExtractionFailed(Object message, Object fieldName, Object fieldTag, Object fieldType,
			Object rawValue, Object assertFactory, Object problem) {
		super(EXTRACTION_FAILURE, fieldName, fieldTag, fieldType, message, rawValue, assertFactory, problem);
	}

	public static ErrorMessageFactory fieldValueExtractionFailed(Object message, Object fieldName, Object fieldTag,
			Object fieldType, Object rawValue, Object assertFactory, Object problem) {
		return new FieldValueExtractionFailed(
				message, fieldName, fieldTag, fieldType, rawValue, assertFactory, problem);
	}
}
