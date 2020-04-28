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
package io.allune.quickfixj.api.message;

import static io.allune.quickfixj.api.Assertions.assertThat;

import org.junit.Test;

import quickfix.Message;
import quickfix.field.ClOrdID;

/**
 * @author Eduardo Sanchez-Ros
 */
public class MessageAssertIsNewOrderSingleTest {

	@Test
	public void shouldAssertMessageTypeIsNewOrderSingle40FromMessage() throws Exception {
		// Given
		Message message = new Message(
				"8=FIX.4.0\u00019=122\u000135=D\u000134=215\u000149=CLIENT12\u000152=20100225-19:41:57.316\u000156=B\u00011=Marcel\u000111=13346\u000121=1\u000140=2\u000144=5\u000154=1\u000159=0\u000160=20100225-19:39:52.020\u000110=068\u0001");

		// When/Then
		assertThat(message)
				.isNewOrderSingle40()
				.hasClOrdID(new ClOrdID("13346"));
	}

	@Test
	public void shouldAssertMessageTypeIsNewOrderSingle41FromMessage() throws Exception {
		// Given
		Message message = new Message(
				"8=FIX.4.1\u00019=122\u000135=D\u000134=215\u000149=CLIENT12\u000152=20100225-19:41:57.316\u000156=B\u00011=Marcel\u000111=13346\u000121=1\u000140=2\u000144=5\u000154=1\u000159=0\u000160=20100225-19:39:52.020\u000110=069\u0001");

		// When/Then
		assertThat(message)
				.isNewOrderSingle41()
				.hasClOrdID(new ClOrdID("13346"));
	}

	@Test
	public void shouldAssertMessageTypeIsNewOrderSingle42FromMessage() throws Exception {
		// Given
		Message message = new Message(
				"8=FIX.4.2\u00019=122\u000135=D\u000134=215\u000149=CLIENT12\u000152=20100225-19:41:57.316\u000156=B\u00011=Marcel\u000111=13346\u000121=1\u000140=2\u000144=5\u000154=1\u000159=0\u000160=20100225-19:39:52.020\u000110=070\u0001");

		// When/Then
		assertThat(message)
				.isNewOrderSingle42()
				.hasClOrdID(new ClOrdID("13346"));
	}
}