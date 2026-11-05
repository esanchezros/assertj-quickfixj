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
package io.allune.quickfixj.support;

import io.allune.quickfixj.support.NewOrderSingle.NewOrderSingleBuilder;
import quickfix.field.OrdType;
import quickfix.field.Side;

import java.time.LocalDateTime;


/**
 * @author Eduardo Sanchez-Ros
 */
public class TestNewOrderSingleMessageFactory {

	public static NewOrderSingleBuilder<?, ?> newOrderSingleBuilder(String beginString) {
		return NewOrderSingle.builder()
				.beginString(beginString);
	}

	public static NewOrderSingleBuilder<?, ?> newOrderSingleBuilderWithDefaults(String beginString) {
		return NewOrderSingle.builder()
				.beginString(beginString)
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
}
