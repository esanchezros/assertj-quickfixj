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

import quickfix.Message;
import quickfix.field.Account;
import quickfix.field.ClOrdID;
import quickfix.field.HandlInst;
import quickfix.field.OrdType;
import quickfix.field.OrderQty;
import quickfix.field.Price;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.field.TransactTime;

import java.time.LocalDateTime;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static quickfix.FixVersions.BEGINSTRING_FIX40;
import static quickfix.FixVersions.BEGINSTRING_FIX41;
import static quickfix.FixVersions.BEGINSTRING_FIX42;
import static quickfix.FixVersions.BEGINSTRING_FIX43;
import static quickfix.FixVersions.BEGINSTRING_FIX44;
import static quickfix.FixVersions.FIX50;
import static quickfix.FixVersions.FIX50SP1;
import static quickfix.FixVersions.FIX50SP2;

/**
 * @author Eduardo Sanchez-Ros
 */
public class NewOrderSingle extends AbstractMessage {

	private static Map<String, Supplier<? extends Message>> map = Stream.of(
			new SimpleEntry<String, Supplier<? extends Message>>(BEGINSTRING_FIX40, quickfix.fix40.NewOrderSingle::new),
			new SimpleEntry<String, Supplier<? extends Message>>(BEGINSTRING_FIX41, quickfix.fix41.NewOrderSingle::new),
			new SimpleEntry<String, Supplier<? extends Message>>(BEGINSTRING_FIX42, quickfix.fix42.NewOrderSingle::new),
			new SimpleEntry<String, Supplier<? extends Message>>(BEGINSTRING_FIX43, quickfix.fix43.NewOrderSingle::new),
			new SimpleEntry<String, Supplier<? extends Message>>(BEGINSTRING_FIX44, quickfix.fix44.NewOrderSingle::new),
			new SimpleEntry<String, Supplier<? extends Message>>(FIX50, quickfix.fix50.NewOrderSingle::new),
			new SimpleEntry<String, Supplier<? extends Message>>(FIX50SP1, quickfix.fix50sp1.NewOrderSingle::new),
			new SimpleEntry<String, Supplier<? extends Message>>(FIX50SP2, quickfix.fix50sp2.NewOrderSingle::new))
			.collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

	private String clientOrderId;

	private Character handlInst;

	private String symbol;

	private Character side;

	private Double orderQty;

	private Character orderType;

	private Double price;

	private String account;

	private LocalDateTime transactTime;

	public static NewOrderSingleBuilder builder() {
		return new NewOrderSingleBuilder();
	}

	public static final class NewOrderSingleBuilder {
		private final NewOrderSingle value = new NewOrderSingle();

		public NewOrderSingleBuilder beginString(String beginString) {
			value.beginString = beginString;
			return this;
		}

		public NewOrderSingleBuilder applVerId(String applVerId) {
			value.applVerId = applVerId;
			return this;
		}

		public NewOrderSingleBuilder sender(String sender) {
			value.sender = sender;
			return this;
		}

		public NewOrderSingleBuilder target(String target) {
			value.target = target;
			return this;
		}

		public NewOrderSingleBuilder mesSeqNum(int mesSeqNum) {
			value.mesSeqNum = mesSeqNum;
			return this;
		}

		public NewOrderSingleBuilder sendingTime(LocalDateTime sendingTime) {
			value.sendingTime = sendingTime;
			return this;
		}

		public NewOrderSingleBuilder signature(String signature) {
			value.signature = signature;
			return this;
		}

		public NewOrderSingleBuilder clientOrderId(String clientOrderId) {
			value.clientOrderId = clientOrderId;
			return this;
		}

		public NewOrderSingleBuilder handlInst(char handlInst) {
			value.handlInst = handlInst;
			return this;
		}

		public NewOrderSingleBuilder symbol(String symbol) {
			value.symbol = symbol;
			return this;
		}

		public NewOrderSingleBuilder side(char side) {
			value.side = side;
			return this;
		}

		public NewOrderSingleBuilder orderQty(double orderQty) {
			value.orderQty = orderQty;
			return this;
		}

		public NewOrderSingleBuilder orderType(char orderType) {
			value.orderType = orderType;
			return this;
		}

		public NewOrderSingleBuilder price(double price) {
			value.price = price;
			return this;
		}

		public NewOrderSingleBuilder account(String account) {
			value.account = account;
			return this;
		}

		public NewOrderSingleBuilder transactTime(LocalDateTime transactTime) {
			value.transactTime = transactTime;
			return this;
		}

		public NewOrderSingle build() {
			return value;
		}
	}

	@Override
	protected <T extends Message> void addMessageFields(T order) {
		ofNullable(clientOrderId).ifPresent(value -> order.setField(new ClOrdID(clientOrderId)));
		ofNullable(handlInst).ifPresent(value -> order.setField(new HandlInst(handlInst)));
		ofNullable(symbol).ifPresent(value -> order.setField(new Symbol(symbol)));
		ofNullable(side).ifPresent(value -> order.setField(new Side(side)));
		ofNullable(orderQty).ifPresent(value -> order.setField(new OrderQty(orderQty)));
		ofNullable(orderType).ifPresent(value -> order.setField(new OrdType(orderType)));
		ofNullable(price).ifPresent(value -> order.setField(new Price(price)));
		ofNullable(account).ifPresent(value -> order.setField(new Account(account)));
		ofNullable(transactTime).ifPresent(value -> order.setField(new TransactTime(transactTime)));
	}

	@Override
	protected Map<String, Supplier<? extends Message>> getMap() {
		return map;
	}
}
