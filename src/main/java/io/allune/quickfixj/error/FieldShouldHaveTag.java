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
 * @author Eduardo Sanchez-Ros
 */
public class FieldShouldHaveTag extends BasicErrorMessageFactory {

	private FieldShouldHaveTag(Object actual) {
		// TODO: re do
		super("Expecting field of type <%s> to have static field \"FIELD\" with tag id%n"
				+ "but did not.", actual);
	}

	public static ErrorMessageFactory fieldShouldHaveTag(Object actual) {
		return new FieldShouldHaveTag(actual);
	}
}
