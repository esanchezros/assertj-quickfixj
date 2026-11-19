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

import java.util.List;

/**
 * Creates an error message when no repeating-group entry satisfies the supplied assertions.
 */
public class ShouldHaveGroupSatisfying extends BasicErrorMessageFactory {

	private ShouldHaveGroupSatisfying(Object actual, Object groupTag, List<String> assertionFailures) {
		super("Expecting repeating group with tag <%s> in Message:%n"
				+ " <%s>%n"
				+ "to have an entry satisfying the requirements%n"
				+ "but none of the <%s> entries matched:%s",
				groupTag, actual, assertionFailures.size(), new FormattedFailures(assertionFailures));
	}

	public static ErrorMessageFactory shouldHaveGroupSatisfying(
			Object actual, Object groupTag, List<String> assertionFailures) {
		return new ShouldHaveGroupSatisfying(actual, groupTag, assertionFailures);
	}

	private static final class FormattedFailures {

		private final List<String> assertionFailures;

		private FormattedFailures(List<String> assertionFailures) {
			this.assertionFailures = assertionFailures;
		}

		@Override
		public String toString() {
			String lineSeparator = System.lineSeparator();
			StringBuilder formatted = new StringBuilder();
			for (int index = 0; index < assertionFailures.size(); index++) {
				String message = String.valueOf(assertionFailures.get(index));
				formatted.append(lineSeparator)
						.append("  index <")
						.append(index + 1)
						.append(">: ")
						.append(message.replace(lineSeparator, lineSeparator + "    "));
			}
			return formatted.toString();
		}
	}
}
