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
 * Creates an error message indicating that an assertion that verifies that two objects are not equal failed.
 *
 * @author Eduardo Sanchez-Ros
 */
public class ShouldBeEqual extends BasicErrorMessageFactory {

	private ShouldBeEqual(Object message, Object fieldClass, Object fieldTag, Object actualValue, Object expectedValue) {
		// TODO: refactor message wording
		super("Expecting value for field <%s> (tag=<%s>) in Message:%n"
				+ " <%s>%n"
				+ "to be:%n"
				+ " <%s>%n"
				+ "but was:%n"
				+ " <%s>", fieldClass, fieldTag, message, expectedValue, actualValue);
	}

	/**
	 * Creates a new <code>{@link ShouldBeEqual}</code>.
	 *
	 * @param message       the actual value in the failed assertion.
	 * @param fieldClass
	 * @param fieldTag
	 * @param actualValue
	 * @param expectedValue the value used in the failed assertion to compare the actual value to.
	 * @return @return the created {@code ErrorMessageFactory}.
	 */
	public static ErrorMessageFactory shouldBeEqual(Object message, Object fieldClass, Object fieldTag, Object actualValue, Object expectedValue) {
		return new ShouldBeEqual(message, fieldClass, fieldTag, actualValue, expectedValue);
	}

}
