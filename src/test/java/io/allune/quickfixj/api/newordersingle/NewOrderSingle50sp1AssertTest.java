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
import static io.allune.quickfixj.internal.Messages.getSessionDataDictionary;
import static quickfix.FixVersions.FIX50SP2;

import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;

import quickfix.field.HandlInst;
import quickfix.field.OrdType;
import quickfix.field.Side;
import quickfix.fix50sp2.NewOrderSingle;

/**
 * @author Eduardo Sanchez-Ros
 */
public class NewOrderSingle50sp1AssertTest {

	private NewOrderSingle message;

	@Before
	public void setUp() throws Exception {
		message = new NewOrderSingle();
		message.fromString(
				"8=FIXT.1.1\u00019=122\u000135=D\u000134=215\u000149=CLIENT12\u000152=20100225-19:41:57.316\u000155=GBP/USD\u000138=1000\u00011128=9\u000156=B\u00011=Marcel\u000111=13346\u000121=1\u000140=2\u000144=5\u000154=1\u000159=0\u000160=20100225-19:39:52.020\u000110=068\u0001",
				getSessionDataDictionary(FIX50SP2),
				false);
	}

	@Test
	public void shouldAssertNewOrderSingle50sp1HasClOrdID() {
		// Given

		// When/Then
		assertThat(message)
				.hasClOrdID("13346");
	}

	@Test
	public void shouldAssertNewOrderSingle50sp1HasHandlInst() {
		// Given

		// When/Then
		assertThat(message)
				.hasHandlInst(HandlInst.AUTOMATED_EXECUTION_ORDER_PRIVATE_NO_BROKER_INTERVENTION);
	}

	@Test
	public void shouldAssertNewOrderSingle50sp1HasSymbol() {
		// Given

		// When/Then
		assertThat(message)
				.hasSymbol("GBP/USD");
	}

	@Test
	public void shouldAssertNewOrderSingle50sp1HasSide() {
		// Given

		// When/Then
		assertThat(message)
				.hasSide(Side.BUY);
	}

	@Test
	public void shouldAssertNewOrderSingle50sp1HasOrderQty() {
		// Given

		// When/Then
		assertThat(message)
				.hasOrderQty(1000D);
	}

	@Test
	public void shouldAssertNewOrderSingle50sp1HasOrdType() {
		// Given

		// When/Then
		assertThat(message)
				.hasOrdType(OrdType.LIMIT);
	}

	@Test
	public void shouldAssertNewOrderSingle50sp1HasTransactTime() {
		// Given

		// When/Then
		assertThat(message)
				.hasTransactTime(LocalDateTime.parse("2010-02-25T19:39:52.020"));
	}

	@Test
	public void shouldAssertNewOrderSingle50sp1HasAccount() {
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
				.hasTransactTime(LocalDateTime.parse("2010-02-25T19:39:52.020"))
				.hasAccount("Marcel");
	}
}
