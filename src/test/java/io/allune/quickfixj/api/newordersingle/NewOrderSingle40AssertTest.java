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
package io.allune.quickfixj.api.newordersingle;

import static io.allune.quickfixj.api.newordersingle.NewOrderSingleAssertions.assertThat;
import static io.allune.quickfixj.api.support.TestNewOrderSingleMessageFactory.newOrderSingleBuilder;
import static org.assertj.core.api.Assertions.fail;
import static quickfix.FixVersions.BEGINSTRING_FIX40;
import static quickfix.field.HandlInst.AUTOMATED_EXECUTION_ORDER_PRIVATE_NO_BROKER_INTERVENTION;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import quickfix.field.HandlInst;
import quickfix.field.OrdType;
import quickfix.field.Side;
import quickfix.fix40.NewOrderSingle;

/**
 * @author Eduardo Sanchez-Ros
 */
public class NewOrderSingle40AssertTest {

	private NewOrderSingle message;

	@Before
	public void setUp() {
		message = newOrderSingleBuilder(BEGINSTRING_FIX40)
				.clientOrderId("13346")
				.handlInst(AUTOMATED_EXECUTION_ORDER_PRIVATE_NO_BROKER_INTERVENTION)
				.symbol("GBP/USD")
				.side(Side.BUY)
				.orderQty(1000D)
				.orderType(OrdType.LIMIT)
				.price(300.00)
				.account("Marcel")
				.build()
				.toMessage();
	}

	@Test
	public void shouldAssertNewOrderSingle40HasClOrdID() {
		// Given

		// When/Then
		assertThat(message)
				.hasClOrdID("13346");
	}

	@Test
	public void shouldFailToAssertNewOrderSingle40HasClOrdID() {
		// Given

		// When/Then
		try {
			assertThat(message)
					.hasClOrdID("13342");
		} catch (AssertionError e) {
			Assertions.assertThat(e).hasMessage("\nExpecting Message with field <quickfix.field.ClOrdID> (field number 11)\n"
					+ "to have value:\n"
					+ " <\"13342\">\n"
					+ "but was:\n"
					+ " <\"13346\">");
			return;
		}
		fail("Should have thrown AssertionError");
	}

	@Test
	public void shouldAssertNewOrderSingle40HasHandlInst() {
		// Given

		// When/Then
		assertThat(message)
				.hasHandlInst(HandlInst.AUTOMATED_EXECUTION_ORDER_PRIVATE_NO_BROKER_INTERVENTION);
	}

	@Test
	public void shouldAssertNewOrderSingle40HasSymbol() {
		// Given

		// When/Then
		assertThat(message)
				.hasSymbol("GBP/USD");
	}

	@Test
	public void shouldAssertNewOrderSingle40HasSide() {
		// Given

		// When/Then
		assertThat(message)
				.hasSide(Side.BUY);
	}

	@Test
	public void shouldAssertNewOrderSingle40HasOrderQty() {
		// Given

		// When/Then
		assertThat(message)
				.hasOrderQty(1000D);
	}

	@Test
	public void shouldAssertNewOrderSingle40HasOrdType() {
		// Given

		// When/Then
		assertThat(message)
				.hasOrdType(OrdType.LIMIT);
	}

	@Test
	public void shouldAssertNewOrderSingle40HasAccount() {
		// Given

		// When/Then
		assertThat(message)
				.hasAccount("Marcel");
	}

	@Test
	public void shouldAssertNewOrderSingleAllFields() {
		// Given

		// When/Then
		assertThat(message)
				.hasClOrdID("13346")
				.hasHandlInst(HandlInst.AUTOMATED_EXECUTION_ORDER_PRIVATE_NO_BROKER_INTERVENTION)
				.hasSymbol("GBP/USD")
				.hasSide(Side.BUY)
				.hasOrderQty(1000D)
				.hasOrdType(OrdType.LIMIT)
				.hasAccount("Marcel")
				.hasPrice(300.00);
	}
}