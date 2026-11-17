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

import quickfix.Message.Trailer;
import quickfix.field.CheckSum;
import quickfix.field.Signature;
import quickfix.field.SignatureLength;

/**
 * @author Eduardo Sanchez-Ros
 */
public class MessageTrailerAssert extends AbstractFieldMapAssert<MessageTrailerAssert, Trailer> {

	private final MessageAssert messageAssert;

	private final String beginString;

	/**
	 * Creates a new <code>{@link MessageTrailerAssert}</code>.
	 *
	 * @param trailer       the actual value to verify
	 * @param messageAssert
	 * @param beginString
	 */
	MessageTrailerAssert(Trailer trailer, MessageAssert messageAssert, String beginString) {
		super(trailer, MessageTrailerAssert.class, messageAssert.dictionaries);
		this.messageAssert = messageAssert;
		this.beginString = beginString;
	}

	@Override
	public String getBeginString() {
		return beginString;
	}

	public MessageTrailerAssert hasSignature(String expectedSignature) {
		isNotNull();
		messages.assertFieldHasValue(info, actual, Signature.class, expectedSignature);
		return this;
	}

	public MessageTrailerAssert hasSignatureLength(int expectedSignatureLength) {
		isNotNull();
		messages.assertFieldHasValue(info, actual, SignatureLength.class, expectedSignatureLength);
		return this;
	}

	public MessageTrailerAssert hasChecksum(String expectedChecksum) {
		isNotNull();
		messages.assertFieldHasValue(info, actual, CheckSum.class, expectedChecksum);
		return this;
	}

	public MessageAssert and() {
		return messageAssert;
	}
}
