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

import org.junit.Before;
import org.junit.Test;
import quickfix.field.Account;
import quickfix.field.ClOrdID;
import quickfix.field.OrdType;
import quickfix.field.Side;
import quickfix.fix41.NewOrderSingle;

import static io.allune.quickfixj.api.Assertions.assertThat;
import static io.allune.quickfixj.support.TestNewOrderSingleMessageFactory.newOrderSingleBuilder;
import static quickfix.FixVersions.BEGINSTRING_FIX41;

/**
 * @author Eduardo Sanchez-Ros
 */
public class NewOrderSingle41AssertTest {

	private NewOrderSingle message;

	@Before
	public void setUp() throws Exception {
		message = newOrderSingleBuilder(BEGINSTRING_FIX41)
				.clientOrderId("13346")
				.handlInst('1')
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
	public void shouldAssertNewOrderSingle41HasClOrdID() {
		// Given

		// When/Then
		assertThat(message)
				.isNewOrderSingle()
				.hasFieldValue(ClOrdID.FIELD, "13346");
	}

	@Test
	public void shouldAssertNewOrderSingle41HasAccount() {
		// Given

		// When/Then
		assertThat(message)
				.isNewOrderSingle()
				.hasFieldValue(Account.FIELD, "Marcel");
	}

	@Test
	public void shouldAssertNewOrderSingle41HasSide() {
		// Given

		// When/Then
		assertThat(message)
				.isNewOrderSingle()
				.hasFieldValue(Side.FIELD, String.valueOf(Side.BUY));
	}
}
