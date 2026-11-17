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
 * Creates an error message for a missing repeating-group entry.
 */
public class ShouldHaveGroup extends BasicErrorMessageFactory {

	private ShouldHaveGroup(Object actual, Object groupTag, Object index, Object actualCount) {
		super("Expecting Message:%n"
				+ " <%s>%n"
				+ "to have repeating group with tag <%s> at index <%s>%n"
				+ "but group count was <%s>.", actual, groupTag, index, actualCount);
	}

	public static ErrorMessageFactory shouldHaveGroup(Object actual, Object groupTag, Object index, Object actualCount) {
		return new ShouldHaveGroup(actual, groupTag, index, actualCount);
	}
}
