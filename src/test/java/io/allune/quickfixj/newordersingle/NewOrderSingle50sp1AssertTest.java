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

import io.allune.quickfixj.support.NewOrderSingle.NewOrderSingleBuilder;
import org.junit.Before;
import org.junit.Test;
import quickfix.field.Account;
import quickfix.field.ClOrdID;
import quickfix.field.HandlInst;
import quickfix.field.OrdType;
import quickfix.field.OrderQty;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.field.TransactTime;

import java.time.LocalDateTime;

import static io.allune.quickfixj.api.Assertions.assertThat;
import static io.allune.quickfixj.support.TestNewOrderSingleMessageFactory.newOrderSingleBuilder;
import static quickfix.FixVersions.FIX50SP2;

/**
 * @author Eduardo Sanchez-Ros
 */
public class NewOrderSingle50sp1AssertTest {

	private NewOrderSingleBuilder<?, ?> messageBuilder;

	@Before
	public void setUp() {
		messageBuilder = newOrderSingleBuilder(FIX50SP2)
				.clientOrderId("13346")
				.handlInst('1')
				.symbol("GBP/USD")
				.side(Side.BUY)
				.orderQty(1000D)
				.orderType(OrdType.LIMIT)
				.price(300.00)
				.account("Marcel")
				.transactTime(LocalDateTime.now());
	}

	@Test
	public void shouldAssertNewOrderSingle50sp1HasClOrdID() {
		// Given
		quickfix.fix50sp2.NewOrderSingle message = messageBuilder.build().toMessage();

		// When/Then
		assertThat(message)
				.isNewOrderSingle()
				.hasFieldValue(ClOrdID.FIELD, "13346");
	}

	@Test
	public void shouldAssertNewOrderSingle50sp1HasHandlInst() {
		// Given
		quickfix.fix50sp2.NewOrderSingle message = messageBuilder.build().toMessage();

		// When/Then
		assertThat(message)
				.isNewOrderSingle()
				.hasFieldValue(HandlInst.FIELD, '1');
	}

	@Test
	public void shouldAssertNewOrderSingle50sp1HasSymbol() {
		// Given
		quickfix.fix50sp2.NewOrderSingle message = messageBuilder.build().toMessage();

		// When/Then
		assertThat(message)
				.isNewOrderSingle()
				.hasFieldValue(Symbol.FIELD, "GBP/USD");
	}

	@Test
	public void shouldAssertNewOrderSingle50sp1HasSide() {
		// Given
		quickfix.fix50sp2.NewOrderSingle message = messageBuilder.build().toMessage();

		// When/Then
		assertThat(message)
				.isNewOrderSingle()
				.hasFieldValue(Side.FIELD, Side.BUY);
	}

	@Test
	public void shouldAssertNewOrderSingle50sp1HasOrderQty() {
		// Given
		quickfix.fix50sp2.NewOrderSingle message = messageBuilder.build().toMessage();

		// When/Then
		assertThat(message)
				.isNewOrderSingle()
				.hasFieldValue(OrderQty.FIELD, 1000D);
	}

	@Test
	public void shouldAssertNewOrderSingle50sp1HasOrdType() {
		// Given
		quickfix.fix50sp2.NewOrderSingle message = messageBuilder.build().toMessage();

		// When/Then
		assertThat(message)
				.isNewOrderSingle()
				.hasFieldValue(OrdType.FIELD, OrdType.LIMIT);
	}

	@Test
	public void shouldAssertNewOrderSingle50sp1HasTransactTime() {
		// Given
		LocalDateTime time = LocalDateTime.parse("2020-08-30T20:00:00");
		messageBuilder.transactTime(time);
		quickfix.fix50sp2.NewOrderSingle message = messageBuilder.build().toMessage();

		// When/Then
		assertThat(message)
				.isNewOrderSingle()
				.hasFieldValue(TransactTime.FIELD, time);
	}

	@Test
	public void shouldAssertNewOrderSingle50sp1HasAccount() {
		// Given
		quickfix.fix50sp2.NewOrderSingle message = messageBuilder.build().toMessage();

		// When/Then
		assertThat(message)
				.isNewOrderSingle()
				.hasFieldValue(Account.FIELD, "Marcel");
	}

	@Test
	public void shouldAssertNewOrderSingleAllFields() {
		// Given
		LocalDateTime time = LocalDateTime.parse("2020-08-30T20:00:00");
		messageBuilder.transactTime(time);
		quickfix.fix50sp2.NewOrderSingle message = messageBuilder.build().toMessage();

		// When/Then
		assertThat(message)
				.isNewOrderSingle()
				.hasFieldValue(ClOrdID.FIELD, "13346")
				.hasFieldValue(HandlInst.FIELD, '1')
				.hasFieldValue(Symbol.FIELD, "GBP/USD")
				.hasFieldValue(Side.FIELD, Side.BUY)
				.hasFieldValue(OrderQty.FIELD, 1000D)
				.hasFieldValue(OrdType.FIELD, OrdType.LIMIT)
				.hasFieldValue(TransactTime.FIELD, time)
				.hasFieldValue(Account.FIELD, "Marcel");
	}
}
