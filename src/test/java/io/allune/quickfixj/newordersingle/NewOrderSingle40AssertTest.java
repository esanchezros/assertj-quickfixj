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
package io.allune.quickfixj.newordersingle;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import quickfix.field.Account;
import quickfix.field.ClOrdID;
import quickfix.field.HandlInst;
import quickfix.field.OrdType;
import quickfix.field.OrderQty;
import quickfix.field.Price;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.fix40.NewOrderSingle;

import java.time.LocalDateTime;

import static io.allune.quickfixj.api.Assertions.assertThat;
import static io.allune.quickfixj.support.TestNewOrderSingleMessageFactory.newOrderSingleBuilder;
import static org.assertj.core.api.Assertions.fail;
import static quickfix.FixVersions.BEGINSTRING_FIX40;

/**
 * @author Eduardo Sanchez-Ros
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class NewOrderSingle40AssertTest {

	private NewOrderSingle message;

	@Before
	public void setUp() {
		message = newOrderSingleBuilder(BEGINSTRING_FIX40)
				.clientOrderId("13346")
				.handlInst('1')
				.symbol("GBP/USD")
				.side(Side.BUY)
				.orderQty(1000D)
				.orderType(OrdType.LIMIT)
				.price(300.00)
				.account("Marcel")
				.sendingTime(LocalDateTime.parse("2020-05-25T17:13:21.193"))
				.build()
				.toMessage();
	}

	@Test
	public void shouldAssertNewOrderSingle40AllFields() {
		// Given

		// When/Then
		assertThat(message)
				.hasFieldValue(ClOrdID.FIELD, "13346")
				.hasFieldValue(HandlInst.FIELD, String.valueOf('1'))
				.hasFieldValue(Symbol.FIELD, "GBP/USD")
				.hasFieldValue(Side.FIELD, String.valueOf(Side.BUY))
				.hasFieldValue(OrderQty.FIELD, 1000)
				.hasFieldValue(OrdType.FIELD, String.valueOf(OrdType.LIMIT))
				.hasFieldValue(Account.FIELD, "Marcel")
				.hasFieldValue(Price.FIELD, 300.00);
	}

	@Test
	public void shouldFailToAssertNewOrderSingle40HasClOrdID() {
		// Given

		// When/Then
		try {
			assertThat(message)
					.isNewOrderSingle()
					.hasFieldValue(ClOrdID.FIELD, "13342");
		} catch (AssertionError e) {
			Assertions.assertThat(e).hasMessage(
					"Expecting field with tag <11> in Message:\n"
							+ " <8=FIX.4.0\u00019=94\u000135=D\u000134=1\u000152=20200525-17:13:21.193\u00011=Marcel\u000111=13346\u000121=1\u000138=1000\u000140=2\u000144=300\u000154=1\u000155=GBP/USD\u000110=178\u0001>\n"
							+ "to have value:\n"
							+ " <\"13342\">\n"
							+ "but was:\n"
							+ " <\"13346\">");
			return;
		}
		fail("Should have thrown AssertionError");
	}
}
