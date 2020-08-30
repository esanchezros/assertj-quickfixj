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

import io.allune.quickfixj.support.NewOrderSingle;
import org.junit.Before;
import org.junit.Test;
import quickfix.Message;
import quickfix.field.OrdType;
import quickfix.field.Side;

import static io.allune.quickfixj.api.Assertions.assertThat;
import static quickfix.FixVersions.BEGINSTRING_FIX40;
import static quickfix.field.HandlInst.AUTOMATED_EXECUTION_ORDER_PRIVATE_NO_BROKER_INTERVENTION;

public class MessageAssert_isOfTypeName_Test {

	NewOrderSingle.NewOrderSingleBuilder<?, ?> messageBuilder;

	@Before
	public void setUp() {
		messageBuilder = NewOrderSingle.builder()
				.sender("BANZAI")
				.target("EXEC")
				.clientOrderId("13346")
				.handlInst(AUTOMATED_EXECUTION_ORDER_PRIVATE_NO_BROKER_INTERVENTION)
				.symbol("GBP/USD")
				.side(Side.BUY)
				.orderQty(1000D)
				.orderType(OrdType.LIMIT)
				.price(300.00)
				.account("Marcel");
	}

	@Test
	public void should() {
		// Given
		Message message = messageBuilder.build()
				.toMessage(BEGINSTRING_FIX40);

		// When/Then
		assertThat(message)
				.hasMsgTypeName("NewOrderSingle");
	}
}
