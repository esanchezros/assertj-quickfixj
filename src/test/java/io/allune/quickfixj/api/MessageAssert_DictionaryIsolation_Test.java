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

import org.junit.Test;
import quickfix.DataDictionary;
import quickfix.Group;
import quickfix.Message;
import quickfix.field.ApplVerID;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static io.allune.quickfixj.api.Assertions.assertThat;
import static io.allune.quickfixj.support.CustomDataDictionaryFactory.dictionaryWithCustomFieldType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MessageAssert_DictionaryIsolation_Test {

	private static final int CUSTOM_FIELD = 6000;

	@Test
	public void scopedDictionariesShouldNotContaminateOtherAssertions() throws Exception {
		DataDictionary integerDictionary = dictionaryWithCustomFieldType("INT");
		DataDictionary stringDictionary = dictionaryWithCustomFieldType("STRING");

		MessageAssert integerAssertion = assertThat(customMessage("FIX.4.4"))
				.withDataDictionary("FIX.4.4", integerDictionary);
		MessageAssert stringAssertion = assertThat(customMessage("FIX.4.4"))
				.withDataDictionary("FIX.4.4", stringDictionary);

		integerAssertion.hasFieldValue(CUSTOM_FIELD, 7);
		stringAssertion.hasFieldValue(CUSTOM_FIELD, "7");
		integerAssertion.hasFieldValue(CUSTOM_FIELD, 7);

		assertThatThrownBy(() -> assertThat(customMessage("FIX.4.4")).hasFieldValue(CUSTOM_FIELD, "7"))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("tag <6000>")
				.hasMessageContaining("but was:%n <null>".replace("%n", System.lineSeparator()));
	}

	@Test
	public void scopedDictionariesShouldRemainIsolatedDuringParallelAssertions() throws Exception {
		DataDictionary integerDictionary = dictionaryWithCustomFieldType("INT");
		DataDictionary stringDictionary = dictionaryWithCustomFieldType("STRING");
		ExecutorService executor = Executors.newFixedThreadPool(2);
		CountDownLatch start = new CountDownLatch(1);

		try {
			Future<?> integerTask = executor.submit(() -> assertRepeatedly(start, integerDictionary, 7));
			Future<?> stringTask = executor.submit(() -> assertRepeatedly(start, stringDictionary, "7"));
			start.countDown();
			integerTask.get(10, TimeUnit.SECONDS);
			stringTask.get(10, TimeUnit.SECONDS);
		} finally {
			executor.shutdownNow();
		}
	}

	@Test
	public void applicationDictionaryShouldBeScopedForFixtMessages() throws Exception {
		Message message = customMessage("FIXT.1.1");
		message.getHeader().setString(ApplVerID.FIELD, ApplVerID.FIX50SP2);
		Group group = new Group(453, CUSTOM_FIELD);
		group.setString(CUSTOM_FIELD, "7");
		message.addGroup(group);

		assertThat(message)
				.withApplicationDataDictionary(ApplVerID.FIX50SP2, dictionaryWithCustomFieldType("INT"))
				.isVersion50sp2()
				.hasFieldValue(CUSTOM_FIELD, 7)
				.anyGroupSatisfies(453, entry -> entry.hasFieldValue(CUSTOM_FIELD, 7));
	}

	@Test
	public void malformedTypedValueShouldReportTagExpectedAndRawActualValue() throws Exception {
		Message message = customMessage("FIX.4.4");
		message.setString(CUSTOM_FIELD, "not-a-number");

		assertThatThrownBy(() -> assertThat(message)
				.withDataDictionary("FIX.4.4", dictionaryWithCustomFieldType("INT"))
				.hasFieldValue(CUSTOM_FIELD, 7))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("tag <6000>")
				.hasMessageContaining("<7>")
				.hasMessageContaining("not-a-number");
	}

	@Test
	public void scopedDictionaryShouldPropagateThroughHeaderTrailerAndGroupChains() throws Exception {
		DataDictionary dictionary = dictionaryWithCustomFieldType("INT");
		Message message = customMessage("FIX.4.4");
		message.getHeader().setString(CUSTOM_FIELD, "7");
		message.getTrailer().setString(CUSTOM_FIELD, "7");
		Group group = new Group(453, CUSTOM_FIELD);
		group.setString(CUSTOM_FIELD, "7");
		message.addGroup(group);

		assertThat(message)
				.withDataDictionary("FIX.4.4", dictionary)
				.header().hasFieldValue(CUSTOM_FIELD, 7).and()
				.trailer().hasFieldValue(CUSTOM_FIELD, 7).and()
				.group(453, 1).hasFieldValue(CUSTOM_FIELD, 7).and();
	}

	@Test
	public void scopedDictionaryShouldPropagateToContentBasedGroupAssertions() throws Exception {
		DataDictionary dictionary = dictionaryWithCustomFieldType("INT");
		Message message = customMessage("FIX.4.4");
		Group group = new Group(453, CUSTOM_FIELD);
		group.setString(CUSTOM_FIELD, "7");
		message.addGroup(group);

		assertThat(message)
				.withDataDictionary("FIX.4.4", dictionary)
				.anyGroupSatisfies(453, entry -> entry.hasFieldValue(CUSTOM_FIELD, 7));
	}

	@Test
	@SuppressWarnings("deprecation")
	public void deprecatedDictionaryMethodShouldRetainItsGlobalBehavior() throws Exception {
		String legacyBeginString = "FIX.LEGACY.1";
		DataDictionary dictionary = dictionaryWithCustomFieldType("INT");

		assertThat(customMessage(legacyBeginString))
				.usingDataDictionary(legacyBeginString, dictionary)
				.hasFieldValue(CUSTOM_FIELD, 7);

		assertThat(customMessage(legacyBeginString)).hasFieldValue(CUSTOM_FIELD, 7);
	}

	private void assertRepeatedly(CountDownLatch start, DataDictionary dictionary, Object expected) {
		try {
			start.await();
			for (int i = 0; i < 100; i++) {
				assertThat(customMessage("FIX.4.4"))
						.withDataDictionary("FIX.4.4", dictionary)
						.hasFieldValue(CUSTOM_FIELD, expected);
			}
		} catch (InterruptedException interrupted) {
			Thread.currentThread().interrupt();
			throw new AssertionError("Parallel dictionary assertion was interrupted", interrupted);
		}
	}

	private Message customMessage(String beginString) {
		Message message = new Message();
		message.getHeader().setString(8, beginString);
		message.getHeader().setString(35, "Z");
		message.setString(CUSTOM_FIELD, "7");
		return message;
	}
}
