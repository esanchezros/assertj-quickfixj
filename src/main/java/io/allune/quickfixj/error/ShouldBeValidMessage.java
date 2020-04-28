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
public class ShouldBeValidMessage extends BasicErrorMessageFactory {

	public static ErrorMessageFactory shouldBeValidMessage(Object actual, Object error) {
		return new ShouldBeValidMessage(actual, error);
	}

	private ShouldBeValidMessage(Object actual, Object error) {
		super("%nExpecting Message to be a valid message <%s>, but was not. Error <%s>", actual, error);
	}
}
