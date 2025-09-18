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
import quickfix.Message;
import quickfix.field.*;

import java.time.LocalDateTime;

import static io.allune.quickfixj.api.Assertions.assertThat;

/**
 * Demonstrates a single chained assertion that validates:
 * - FIX version
 * - message type (NewOrderSingle)
 * - header fields presence
 * - trailer checksum value
 * - body field values
 */
public class MessageAssert_Chain_All_Test {

	@Test
	public void shouldAssertVersionTypeHeaderTrailerAndAllRelevantFields() throws Exception {
		// Given
		Message message = new Message(
				"8=FIX.4.0\u00019=122\u000135=D\u000134=215\u000149=CLIENT12\u000152=20100225-19:41:57.316\u000138=1000\u000156=B\u00011=Marcel\u000111=13346\u000121=1\u000140=2\u000144=5\u000154=1\u000155=GBP/USD\u000159=0\u000160=20100225-19:39:52.020\u000110=074\u0001");

		// When/Then
		//@formatter:off
		assertThat(message)
				.isVersion40()
				.isNewOrderSingle()
				.header()
					.hasField(BeginString.FIELD)
					.hasField(BodyLength.FIELD)
					.hasField(MsgType.FIELD)
					.hasField(MsgSeqNum.FIELD)
					.hasField(SenderCompID.FIELD)
					.hasField(SendingTime.FIELD)
					.hasField(TargetCompID.FIELD)
				.and()
				.trailer()
					.hasFieldValue(CheckSum.FIELD, "074")
				.and()
					// body fields and their expected values
					.hasFieldValue(Account.FIELD, "Marcel")
					.hasFieldValue(ClOrdID.FIELD, "13346")
					.hasFieldValue(Side.FIELD, "1")
					.hasFieldValue(Symbol.FIELD, "GBP/USD")
					.hasFieldValue(OrdType.FIELD, "2")
					.hasFieldValue(Price.FIELD, 5.0)
					.hasFieldValue(OrderQty.FIELD, 1000)
					.hasFieldValue(HandlInst.FIELD, "1")
					.hasFieldValue(TimeInForce.FIELD, "0")
					.hasFieldValue(TransactTime.FIELD, LocalDateTime.parse("2010-02-25T19:39:52.020"));
		//@formatter:on
	}
}
