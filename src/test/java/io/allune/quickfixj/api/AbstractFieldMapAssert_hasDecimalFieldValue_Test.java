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
import quickfix.field.AccruedInterestRate;
import quickfix.field.AllocNetMoney;
import quickfix.field.ApplVerID;
import quickfix.field.BidForwardPoints;
import quickfix.field.OrderQty;
import quickfix.field.Price;
import quickfix.field.SettlCurrFxRate;
import quickfix.field.Symbol;

import java.math.BigDecimal;

import static io.allune.quickfixj.api.Assertions.assertThat;
import static io.allune.quickfixj.support.CustomDataDictionaryFactory.dictionaryWithCustomFieldType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AbstractFieldMapAssert_hasDecimalFieldValue_Test {

	private static final int CUSTOM_FIELD = 6000;

	@Test
	public void shouldAssertAllDecimalFixTypesWithoutBinaryFloatingPointLoss() {
		Message message = fix44Message();
		message.setString(Price.FIELD, "9007199254740993.01");
		message.setString(AllocNetMoney.FIELD, "1234567890.123456789");
		message.setString(OrderQty.FIELD, "0.000000000000000001");
		message.setString(SettlCurrFxRate.FIELD, "1.234567890123456789");
		message.setString(BidForwardPoints.FIELD, "-0.000000000000000001");
		message.setString(AccruedInterestRate.FIELD, "99.999999999999999999");

		assertThat(message)
				.hasDecimalFieldValue(Price.FIELD, new BigDecimal("9007199254740993.01"))
				.hasDecimalFieldValue(AllocNetMoney.FIELD, new BigDecimal("1234567890.123456789"))
				.hasDecimalFieldValue(OrderQty.FIELD, new BigDecimal("0.000000000000000001"))
				.hasDecimalFieldValue(SettlCurrFxRate.FIELD, new BigDecimal("1.234567890123456789"))
				.hasDecimalFieldValue(BidForwardPoints.FIELD, new BigDecimal("-0.000000000000000001"))
				.hasDecimalFieldValue(AccruedInterestRate.FIELD, new BigDecimal("99.999999999999999999"));
	}

	@Test
	public void shouldCompareDecimalValuesNumericallyWithoutRequiringEqualScale() {
		Message message = fix44Message();
		message.setString(Price.FIELD, "1.00");

		assertThat(message).hasDecimalFieldValue(Price.FIELD, new BigDecimal("1.0"));
	}

	@Test
	public void shouldPreserveExistingDoubleBasedFieldAssertionBehavior() {
		String rawValue = "9007199254740993.01";
		Message message = fix44Message();
		message.setString(Price.FIELD, rawValue);

		assertThat(message)
				.hasFieldValue(Price.FIELD, Double.valueOf(rawValue))
				.hasDecimalFieldValue(Price.FIELD, new BigDecimal(rawValue));
	}

	@Test
	public void shouldReportFieldMetadataAndRawValueForAMismatch() {
		Message message = fix44Message();
		message.setString(Price.FIELD, "12.3400");

		assertThatThrownBy(() -> assertThat(message)
				.hasDecimalFieldValue(Price.FIELD, new BigDecimal("12.35")))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("decimal field <\"Price\">")
				.hasMessageContaining("tag <44>")
				.hasMessageContaining("dictionary type <\"PRICE\">")
				.hasMessageContaining("<12.35>")
				.hasMessageContaining("<\"12.3400\">");
	}

	@Test
	public void shouldReportTheConversionProblemForMalformedDecimalData() {
		Message message = fix44Message();
		message.setString(Price.FIELD, "not-a-decimal");

		assertThatThrownBy(() -> assertThat(message)
				.hasDecimalFieldValue(Price.FIELD, new BigDecimal("12.35")))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("decimal field <\"Price\">")
				.hasMessageContaining("tag <44>")
				.hasMessageContaining("dictionary type <\"PRICE\">")
				.hasMessageContaining("<\"not-a-decimal\">")
				.hasMessageContaining("<12.35>")
				.hasMessageContaining("NumberFormatException");
	}

	@Test
	public void shouldRejectNonDecimalDictionaryTypesWithUsefulMetadata() {
		Message message = fix44Message();
		message.setString(Symbol.FIELD, "AAPL");

		assertThatThrownBy(() -> assertThat(message)
				.hasDecimalFieldValue(Symbol.FIELD, BigDecimal.ONE))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("decimal field <\"Symbol\">")
				.hasMessageContaining("tag <55>")
				.hasMessageContaining("dictionary type <\"STRING\">")
				.hasMessageContaining("<\"AAPL\">")
				.hasMessageContaining("not an exact-decimal FIX type");
	}

	@Test
	public void shouldUseAnAssertionScopedCustomDictionary() throws Exception {
		Message message = fix44Message();
		message.setString(CUSTOM_FIELD, "12345678901234567890.123456789");

		assertThat(message)
				.withDataDictionary("FIX.4.4", dictionaryWithCustomFieldType("PRICE"))
				.hasDecimalFieldValue(CUSTOM_FIELD, new BigDecimal("12345678901234567890.123456789"));
	}

	@Test
	public void shouldPropagateTheDecimalAssertionThroughEveryFieldMapChain() throws Exception {
		DataDictionary dictionary = dictionaryWithCustomFieldType("PRICE");
		Message message = fix44Message();
		message.setString(CUSTOM_FIELD, "1.00");
		message.getHeader().setString(CUSTOM_FIELD, "2.00");
		message.getTrailer().setString(CUSTOM_FIELD, "3.00");

		Group group = new Group(453, CUSTOM_FIELD);
		group.setString(CUSTOM_FIELD, "4.00");
		Group nestedGroup = new Group(802, CUSTOM_FIELD);
		nestedGroup.setString(CUSTOM_FIELD, "5.00");
		group.addGroup(nestedGroup);
		message.addGroup(group);

		assertThat(message)
				.withDataDictionary("FIX.4.4", dictionary)
				.hasDecimalFieldValue(CUSTOM_FIELD, new BigDecimal("1"))
				.header().hasDecimalFieldValue(CUSTOM_FIELD, new BigDecimal("2")).and()
				.trailer().hasDecimalFieldValue(CUSTOM_FIELD, new BigDecimal("3")).and()
				.group(453, 1)
					.hasDecimalFieldValue(CUSTOM_FIELD, new BigDecimal("4"))
					.group(802, 1)
						.hasDecimalFieldValue(CUSTOM_FIELD, new BigDecimal("5"))
					.and()
				.and();
	}

	@Test
	public void shouldUseTheScopedApplicationDictionaryForFixt() throws Exception {
		Message message = new Message();
		message.getHeader().setString(8, "FIXT.1.1");
		message.getHeader().setString(35, "Z");
		message.getHeader().setString(ApplVerID.FIELD, ApplVerID.FIX50SP2);
		message.setString(CUSTOM_FIELD, "9876543210.123456789");

		assertThat(message)
				.withApplicationDataDictionary(ApplVerID.FIX50SP2, dictionaryWithCustomFieldType("PRICE"))
				.hasDecimalFieldValue(CUSTOM_FIELD, new BigDecimal("9876543210.123456789"));
	}

	private Message fix44Message() {
		Message message = new Message();
		message.getHeader().setString(8, "FIX.4.4");
		message.getHeader().setString(35, "D");
		return message;
	}
}
