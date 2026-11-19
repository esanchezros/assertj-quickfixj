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

import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.InstanceOfAssertFactory;
import org.junit.Test;
import quickfix.DataDictionary;
import quickfix.Group;
import quickfix.Message;
import quickfix.field.ApplVerID;
import quickfix.field.MsgSeqNum;
import quickfix.field.OrderQty;
import quickfix.field.PossDupFlag;
import quickfix.field.Price;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.field.TransactTime;

import java.time.LocalDateTime;

import static io.allune.quickfixj.api.Assertions.assertThat;
import static io.allune.quickfixj.support.CustomDataDictionaryFactory.dictionaryWithCustomFieldType;
import static org.assertj.core.api.InstanceOfAssertFactories.BOOLEAN;
import static org.assertj.core.api.InstanceOfAssertFactories.CHARACTER;
import static org.assertj.core.api.InstanceOfAssertFactories.DOUBLE;
import static org.assertj.core.api.InstanceOfAssertFactories.INTEGER;
import static org.assertj.core.api.InstanceOfAssertFactories.LOCAL_DATE_TIME;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AbstractFieldMapAssert_extractingFieldValue_Test {

	private static final int CUSTOM_FIELD = 6000;

	@Test
	public void shouldExtractDictionaryValuesIntoSpecializedAssertJAssertions() {
		Message message = fix44Message();
		message.setString(Symbol.FIELD, "AAPL");
		message.setString(OrderQty.FIELD, "125.5");
		message.setChar(Side.FIELD, Side.BUY);
		message.setString(TransactTime.FIELD, "20261119-10:15:30.123");
		message.getHeader().setInt(MsgSeqNum.FIELD, 42);
		message.getHeader().setBoolean(PossDupFlag.FIELD, true);

		assertThat(message).extractingFieldValue(Symbol.FIELD, STRING)
				.startsWith("AAP")
				.endsWith("PL");
		assertThat(message).extractingFieldValue(OrderQty.FIELD, DOUBLE)
				.isPositive()
				.isBetween(125.0, 126.0);
		assertThat(message).extractingFieldValue(Side.FIELD, CHARACTER)
				.isEqualTo(Side.BUY);
		assertThat(message).extractingFieldValue(TransactTime.FIELD, LOCAL_DATE_TIME)
				.isBefore(LocalDateTime.parse("2026-11-20T00:00:00"));
		assertThat(message).header().extractingFieldValue(MsgSeqNum.FIELD, INTEGER)
				.isPositive()
				.isLessThan(50);
		assertThat(message).header().extractingFieldValue(PossDupFlag.FIELD, BOOLEAN)
				.isTrue();
	}

	@Test
	public void shouldPreserveTheFix40CharAsStringBoundary() {
		Message message = new Message();
		message.getHeader().setString(8, "FIX.4.0");
		message.getHeader().setString(35, "D");
		message.setString(Side.FIELD, "1");

		assertThat(message).extractingFieldValue(Side.FIELD, STRING)
				.isEqualTo("1");
	}

	@Test
	public void shouldSupportCustomInstanceOfAssertFactories() {
		Message message = fix44Message();
		message.setString(Symbol.FIELD, "AAPL");
		InstanceOfAssertFactory<String, AbstractStringAssert<?>> customStringFactory =
				new InstanceOfAssertFactory<>(String.class, org.assertj.core.api.Assertions::assertThat);

		assertThat(message).extractingFieldValue(Symbol.FIELD, customStringFactory)
				.contains("AP")
				.hasSize(4);
	}

	@Test
	public void shouldPropagateScopedDictionariesThroughEveryFieldMapChain() throws Exception {
		DataDictionary dictionary = dictionaryWithCustomFieldType("STRING");
		Message message = fix44Message();
		message.setString(CUSTOM_FIELD, "BODY");
		message.getHeader().setString(CUSTOM_FIELD, "HEADER");
		message.getTrailer().setString(CUSTOM_FIELD, "TRAILER");

		Group group = new Group(453, CUSTOM_FIELD);
		group.setString(CUSTOM_FIELD, "GROUP");
		Group nestedGroup = new Group(802, CUSTOM_FIELD);
		nestedGroup.setString(CUSTOM_FIELD, "NESTED");
		group.addGroup(nestedGroup);
		message.addGroup(group);

		MessageAssert messageAssert = assertThat(message).withDataDictionary("FIX.4.4", dictionary);
		messageAssert.extractingFieldValue(CUSTOM_FIELD, STRING).startsWith("BO");
		messageAssert.header().extractingFieldValue(CUSTOM_FIELD, STRING).startsWith("HEAD");
		messageAssert.trailer().extractingFieldValue(CUSTOM_FIELD, STRING).endsWith("LER");
		GroupAssert<MessageAssert> groupAssert = messageAssert.group(453, 1);
		groupAssert.extractingFieldValue(CUSTOM_FIELD, STRING).isEqualTo("GROUP");
		groupAssert.group(802, 1).extractingFieldValue(CUSTOM_FIELD, STRING).isEqualTo("NESTED");
	}

	@Test
	public void shouldUseTheScopedApplicationDictionaryForFixt() throws Exception {
		Message message = new Message();
		message.getHeader().setString(8, "FIXT.1.1");
		message.getHeader().setString(35, "Z");
		message.getHeader().setString(ApplVerID.FIELD, ApplVerID.FIX50SP2);
		message.setString(CUSTOM_FIELD, "42");

		assertThat(message)
				.withApplicationDataDictionary(ApplVerID.FIX50SP2, dictionaryWithCustomFieldType("INT"))
				.extractingFieldValue(CUSTOM_FIELD, INTEGER)
				.isBetween(40, 45);
	}

	@Test
	public void shouldReportDictionaryConversionFailures() throws Exception {
		Message message = fix44Message();
		message.setString(CUSTOM_FIELD, "not-an-integer");

		assertThatThrownBy(() -> assertThat(message)
				.withDataDictionary("FIX.4.4", dictionaryWithCustomFieldType("INT"))
				.extractingFieldValue(CUSTOM_FIELD, INTEGER))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("field <\"CustomField\">")
				.hasMessageContaining("tag <6000>")
				.hasMessageContaining("dictionary type <\"INT\">")
				.hasMessageContaining("<\"not-an-integer\">")
				.hasMessageContaining("invalid integral value");
	}

	@Test
	public void shouldReportFactoryTypeMismatchesWithFieldContext() {
		Message message = fix44Message();
		message.setString(Price.FIELD, "12.5");

		assertThatThrownBy(() -> assertThat(message).extractingFieldValue(Price.FIELD, INTEGER))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("field <\"Price\">")
				.hasMessageContaining("tag <44>")
				.hasMessageContaining("dictionary type <\"PRICE\">")
				.hasMessageContaining("<\"12.5\">")
				.hasMessageContaining("java.lang.Double")
				.hasMessageContaining("Integer");
	}

	@Test
	public void shouldDescribeTheReturnedAssertionWithTheFieldNameAndTag() {
		Message message = fix44Message();
		message.setString(Symbol.FIELD, "AAPL");

		assertThatThrownBy(() -> assertThat(message).extractingFieldValue(Symbol.FIELD, STRING)
				.isEqualTo("MSFT"))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("[FIX field Symbol (tag 55)]");
	}

	@Test
	public void shouldReportMissingFieldsWithTheExistingFieldDiagnostic() {
		Message message = fix44Message();

		assertThatThrownBy(() -> assertThat(message).extractingFieldValue(Symbol.FIELD, STRING))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("to have field with tag <55>");
	}

	@Test
	public void shouldReportFieldsMissingFromTheActiveDictionary() {
		Message message = fix44Message();
		message.setString(CUSTOM_FIELD, "value");

		assertThatThrownBy(() -> assertThat(message).extractingFieldValue(CUSTOM_FIELD, STRING))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("field <\"unknown\">")
				.hasMessageContaining("tag <6000>")
				.hasMessageContaining("dictionary type <\"unknown\">")
				.hasMessageContaining("<\"value\">")
				.hasMessageContaining("active data dictionary does not define this field");
	}

	@Test
	public void shouldValidateTheTagAndFactory() {
		Message message = fix44Message();

		assertThatIllegalArgumentException()
				.isThrownBy(() -> assertThat(message).extractingFieldValue(0, STRING))
				.withMessage("'fieldTag' must be greater than 0");
		assertThatIllegalArgumentException()
				.isThrownBy(() -> assertThat(message).extractingFieldValue(Symbol.FIELD, null))
				.withMessage("'assertFactory' must not be null.");
	}

	private Message fix44Message() {
		Message message = new Message();
		message.getHeader().setString(8, "FIX.4.4");
		message.getHeader().setString(35, "D");
		return message;
	}
}
