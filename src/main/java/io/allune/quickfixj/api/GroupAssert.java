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

import io.allune.quickfixj.internal.Dictionaries;
import quickfix.Group;

/**
 * Assertions for an individual QuickFIX/J repeating-group entry.
 *
 * @param <PARENT> the assertion to return to after inspecting the entry
 */
public class GroupAssert<PARENT> extends AbstractFieldMapAssert<GroupAssert<PARENT>, Group> {

	private final PARENT parent;
	private final String beginString;

	GroupAssert(Group group, PARENT parent, String beginString, Dictionaries dictionaries) {
		super(group, selfType(), dictionaries);
		this.parent = parent;
		this.beginString = beginString;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private static <PARENT> Class<GroupAssert<PARENT>> selfType() {
		return (Class) GroupAssert.class;
	}

	@Override
	public String getBeginString() {
		return beginString;
	}

	/**
	 * Returns to the assertion that selected this group entry.
	 *
	 * @return the parent assertion
	 */
	public PARENT and() {
		return parent;
	}
}
