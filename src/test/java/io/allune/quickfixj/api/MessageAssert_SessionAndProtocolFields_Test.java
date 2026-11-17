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
import quickfix.SessionID;
import quickfix.field.EncryptMethod;
import quickfix.field.ExecType;
import quickfix.field.OrdStatus;
import quickfix.field.SessionRejectReason;

import static io.allune.quickfixj.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MessageAssert_SessionAndProtocolFields_Test {

	@Test
	public void shouldAssertSessionAndReverseSessionIdentifiers() {
		Message message = messageWithSessionHeader();

		SessionID expected = new SessionID(
				"FIX.4.4", "BUY", "TRADER", "LONDON", "SELL", "ORDER", "NEW_YORK", "QUALIFIER");
		SessionID reverse = new SessionID(
				"FIX.4.4", "SELL", "ORDER", "NEW_YORK", "BUY", "TRADER", "LONDON", "IGNORED");

		assertThat(message)
				.hasSessionID(expected)
				.hasReverseSessionID(reverse);
	}

	@Test
	public void shouldReportExpectedAndActualSessionIdentifiers() {
		Message message = messageWithSessionHeader();
		SessionID expected = new SessionID("FIX.4.4", "OTHER", "SELL");

		assertThatThrownBy(() -> assertThat(message).hasSessionID(expected))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("to have SessionID")
				.hasMessageContaining("OTHER")
				.hasMessageContaining("BUY");
	}

	@Test
	public void shouldAssertSequenceRejectLogonAndExecutionReportFields() {
		Message message = messageWithSessionHeader();
		message.setInt(36, 12);
		message.setInt(45, 9);
		message.setInt(373, SessionRejectReason.VALUE_IS_INCORRECT);
		message.setString(58, "Invalid value");
		message.setInt(98, EncryptMethod.NONE_OTHER);
		message.setInt(108, 30);
		message.setBoolean(141, true);
		message.setChar(150, ExecType.NEW);
		message.setChar(39, OrdStatus.NEW);

		assertThat(message)
				.hasNewSeqNo(12)
				.hasRefSeqNum(9)
				.hasSessionRejectReason(SessionRejectReason.VALUE_IS_INCORRECT)
				.hasText("Invalid value")
				.hasEncryptMethod(EncryptMethod.NONE_OTHER)
				.hasHeartBtInt(30)
				.hasResetSeqNumFlag(true)
				.hasExecType(ExecType.NEW)
				.hasOrdStatus(OrdStatus.NEW);
	}

	@Test
	public void malformedSequenceValueShouldReportTagExpectedAndRawActualValue() {
		Message message = messageWithSessionHeader();
		message.setString(36, "invalid-sequence");

		assertThatThrownBy(() -> assertThat(message).hasNewSeqNo(12))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("tag <36>")
				.hasMessageContaining("<12>")
				.hasMessageContaining("invalid-sequence");
	}

	private Message messageWithSessionHeader() {
		Message message = new Message();
		message.getHeader().setString(8, "FIX.4.4");
		message.getHeader().setString(35, "8");
		message.getHeader().setString(49, "BUY");
		message.getHeader().setString(50, "TRADER");
		message.getHeader().setString(142, "LONDON");
		message.getHeader().setString(56, "SELL");
		message.getHeader().setString(57, "ORDER");
		message.getHeader().setString(143, "NEW_YORK");
		return message;
	}
}
