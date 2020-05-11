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
package io.allune.quickfixj.api.support;

import static io.allune.quickfixj.internal.Messages.getSessionDataDictionary;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;
import static quickfix.FixVersions.BEGINSTRING_FIXT11;
import static quickfix.FixVersions.FIX50;
import static quickfix.FixVersions.FIX50SP1;
import static quickfix.FixVersions.FIX50SP2;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.Supplier;

import io.allune.quickfixj.internal.InvalidMessageException;
import io.allune.quickfixj.internal.InvalidVersion;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.MessageUtils;
import quickfix.field.ApplVerID;
import quickfix.field.BeginString;
import quickfix.field.MsgSeqNum;
import quickfix.field.SenderCompID;
import quickfix.field.SendingTime;
import quickfix.field.TargetCompID;

@SuperBuilder
public abstract class AbstractMessage {

	String beginString;

	String applVerId;

	String sender;

	String target;

	@Builder.Default
	Integer mesSeqNum = 1;

	@Builder.Default
	LocalDateTime sendingTime = now();

	void addMessageFields(Message message) {
		message.getHeader().setField(new BeginString(beginString));
		ofNullable(applVerId).ifPresent(value -> message.getHeader().setField(new ApplVerID(applVerId)));
		ofNullable(sender).ifPresent(value -> message.getHeader().setField(new SenderCompID(value)));
		ofNullable(target).ifPresent(value -> message.getHeader().setField(new TargetCompID(value)));
		message.getHeader().setField(new MsgSeqNum(mesSeqNum));
		message.getHeader().setField(new SendingTime(sendingTime));
	}

	protected abstract Map<String, Supplier<? extends Message>> getMap();

	public <T extends Message> T toMessage() {
		if (beginString == null) {
			throw new IllegalStateException("beginString must not be null. Set the value via the builder or call toMessage(beginString)");
		}
		return toMessage(beginString);
	}

	@SuppressWarnings("unchecked")
	public <T extends Message> T toMessage(String beginString) {
		T order = (T) getMap().get(beginString).get();
		if (order == null) {
			throw new InvalidVersion(format("Version %s not supported", beginString));
		}

		try {
			determineVersion(beginString);
			addMessageFields(order);
			getSessionDataDictionary(beginString).validate(order, true);
		} catch (IncorrectTagValue | FieldNotFound | IncorrectDataFormat e) {
			throw new InvalidMessageException(e.getMessage(), e);
		}
		return order;
	}

	private void determineVersion(String beginString) {
		if (!beginString.equals(this.beginString)) {
			this.beginString = beginString;
		}

		if (beginString.equals(FIX50) || beginString.equals(FIX50SP1) || beginString.equals(FIX50SP2)) {
			this.beginString = BEGINSTRING_FIXT11;
			this.applVerId = MessageUtils.toApplVerID(beginString).getValue();
		}
	}
}