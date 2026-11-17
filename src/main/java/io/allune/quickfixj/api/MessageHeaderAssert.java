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

import quickfix.FieldNotFound;
import quickfix.Message.Header;
import quickfix.field.ApplExtID;
import quickfix.field.ApplVerID;
import quickfix.field.BeginString;
import quickfix.field.BodyLength;
import quickfix.field.CstmApplVerID;
import quickfix.field.DeliverToCompID;
import quickfix.field.DeliverToLocationID;
import quickfix.field.DeliverToSubID;
import quickfix.field.LastMsgSeqNumProcessed;
import quickfix.field.MessageEncoding;
import quickfix.field.MsgSeqNum;
import quickfix.field.MsgType;
import quickfix.field.OnBehalfOfCompID;
import quickfix.field.OnBehalfOfLocationID;
import quickfix.field.OnBehalfOfSendingTime;
import quickfix.field.OnBehalfOfSubID;
import quickfix.field.OrigSendingTime;
import quickfix.field.PossDupFlag;
import quickfix.field.PossResend;
import quickfix.field.SecureData;
import quickfix.field.SecureDataLen;
import quickfix.field.SenderCompID;
import quickfix.field.SenderLocationID;
import quickfix.field.SenderSubID;
import quickfix.field.SendingTime;
import quickfix.field.TargetCompID;
import quickfix.field.TargetLocationID;
import quickfix.field.TargetSubID;
import quickfix.field.XmlData;
import quickfix.field.XmlDataLen;

import java.time.LocalDateTime;

import static io.allune.quickfixj.error.ShouldBeEqual.shouldBeEqual;
import static io.allune.quickfixj.error.ShouldHaveField.shouldHaveField;

/**
 * @author Eduardo Sanchez-Ros
 */
public class MessageHeaderAssert extends AbstractFieldMapAssert<MessageHeaderAssert, Header> {

	private final MessageAssert messageAssert;

	private final String beginString;

	/**
	 * Creates a new <code>{@link MessageHeaderAssert}</code>.
	 *
	 * @param header        the actual value to verify
	 * @param messageAssert
	 * @param beginString
	 */
	MessageHeaderAssert(Header header, MessageAssert messageAssert, String beginString) {
		super(header, MessageHeaderAssert.class, messageAssert.dictionaries);
		this.messageAssert = messageAssert;
		this.beginString = beginString;
	}

	@Override
	public String getBeginString() {
		return beginString;
	}

	@Override
	public MessageHeaderAssert hasGroup(int expectedGroupTag) {
		super.hasGroup(expectedGroupTag);
		return this;
	}

	public MessageHeaderAssert hasBeginString(String expectedBeginString) {
		isNotNull();
		try {
			String actualBeginString = actual.getString(BeginString.FIELD);
			if (!actualBeginString.equals(expectedBeginString))
				throw failures.failure(info, shouldBeEqual(actual, BeginString.class, BeginString.FIELD, actualBeginString, expectedBeginString));
		} catch (FieldNotFound fieldNotFound) {
			throw failures.failure(info, shouldHaveField(BeginString.class, BeginString.FIELD));
		}
		return this;
	}

	public MessageAssert and() {
		return messageAssert;
	}

	public MessageHeaderAssert hasBodyLength(int expectedBodyLength) {
		messages.assertFieldHasValue(info, actual, BodyLength.class, expectedBodyLength);
		return this;
	}

	public MessageHeaderAssert hasMsgType(String expectedMsgType) {
		messages.assertFieldHasValue(info, actual, MsgType.class, expectedMsgType);
		return this;
	}

	public MessageHeaderAssert hasSenderCompID(String expectedSenderCompID) {
		messages.assertFieldHasValue(info, actual, SenderCompID.class, expectedSenderCompID);
		return this;
	}

	public MessageHeaderAssert hasTargetCompID(String expectedTargetCompID) {
		messages.assertFieldHasValue(info, actual, TargetCompID.class, expectedTargetCompID);
		return this;
	}

	public MessageHeaderAssert hasOnBehalfOfCompID(String expectedOnBehalfOfCompID) {
		messages.assertFieldHasValue(info, actual, OnBehalfOfCompID.class, expectedOnBehalfOfCompID);
		return this;
	}

	public MessageHeaderAssert hasDeliverToCompID(String expectedDeliverToCompID) {
		messages.assertFieldHasValue(info, actual, DeliverToCompID.class, expectedDeliverToCompID);
		return this;
	}

	public MessageHeaderAssert hasSecureDataLen(int expectedSecureDataLen) {
		messages.assertFieldHasValue(info, actual, SecureDataLen.class, expectedSecureDataLen);
		return this;
	}

	public MessageHeaderAssert hasSecureData(String expectedSecureData) {
		messages.assertFieldHasValue(info, actual, SecureData.class, expectedSecureData);
		return this;
	}

	public MessageHeaderAssert hasMsgSeqNum(int expectedMsgSeqNum) {
		messages.assertFieldHasValue(info, actual, MsgSeqNum.class, expectedMsgSeqNum);
		return this;
	}

	public MessageHeaderAssert hasSenderSubID(String expectedSenderSubID) {
		messages.assertFieldHasValue(info, actual, SenderSubID.class, expectedSenderSubID);
		return this;
	}

	public MessageHeaderAssert hasSenderLocationID(String expectedSenderLocationID) {
		messages.assertFieldHasValue(info, actual, SenderLocationID.class, expectedSenderLocationID);
		return this;
	}

	public MessageHeaderAssert hasTargetSubID(String expectedTargetSubID) {
		messages.assertFieldHasValue(info, actual, TargetSubID.class, expectedTargetSubID);
		return this;
	}

	public MessageHeaderAssert hasTargetLocationID(String expectedTargetLocationID) {
		messages.assertFieldHasValue(info, actual, TargetLocationID.class, expectedTargetLocationID);
		return this;
	}

	public MessageHeaderAssert hasOnBehalfOfSubID(String expectedOnBehalfOfSubID) {
		messages.assertFieldHasValue(info, actual, OnBehalfOfSubID.class, expectedOnBehalfOfSubID);
		return this;
	}

	public MessageHeaderAssert hasOnBehalfOfLocationID(String expectedOnBehalfOfLocationID) {
		messages.assertFieldHasValue(info, actual, OnBehalfOfLocationID.class, expectedOnBehalfOfLocationID);
		return this;
	}

	public MessageHeaderAssert hasDeliverToSubID(String expectedDeliverToSubID) {
		messages.assertFieldHasValue(info, actual, DeliverToSubID.class, expectedDeliverToSubID);
		return this;
	}

	public MessageHeaderAssert hasDeliverToLocationID(String expectedDeliverToLocationID) {
		messages.assertFieldHasValue(info, actual, DeliverToLocationID.class, expectedDeliverToLocationID);
		return this;
	}

	public MessageHeaderAssert hasPossDupFlag(boolean expectedPossDupFlag) {
		messages.assertFieldHasValue(info, actual, PossDupFlag.class, expectedPossDupFlag);
		return this;
	}

	public MessageHeaderAssert hasPossResend(boolean expectedPossResend) {
		messages.assertFieldHasValue(info, actual, PossResend.class, expectedPossResend);
		return this;
	}

	public MessageHeaderAssert hasSendingTime(LocalDateTime expectedSendingTime) {
		messages.assertFieldHasValue(info, actual, SendingTime.class, expectedSendingTime);
		return this;
	}

	public MessageHeaderAssert hasOrigSendingTime(LocalDateTime expectedOrigSendingTime) {
		messages.assertFieldHasValue(info, actual, OrigSendingTime.class, expectedOrigSendingTime);
		return this;
	}

	public MessageHeaderAssert hasXmlDataLen(int expectedXmlDataLen) {
		messages.assertFieldHasValue(info, actual, XmlDataLen.class, expectedXmlDataLen);
		return this;
	}

	public MessageHeaderAssert hasXmlData(String expectedXmlData) {
		messages.assertFieldHasValue(info, actual, XmlData.class, expectedXmlData);
		return this;
	}

	public MessageHeaderAssert hasMessageEncoding(String expectedMessageEncoding) {
		messages.assertFieldHasValue(info, actual, MessageEncoding.class, expectedMessageEncoding);
		return this;
	}

	public MessageHeaderAssert hasLastMsgSeqNumProcessed(int expectedLastMsgSeqNumProcessed) {
		messages.assertFieldHasValue(info, actual, LastMsgSeqNumProcessed.class, expectedLastMsgSeqNumProcessed);
		return this;
	}

	public MessageHeaderAssert hasOnBehalfOfSendingTime(LocalDateTime expectedOnBehalfOfSendingTime) {
		messages.assertFieldHasValue(info, actual, OnBehalfOfSendingTime.class, expectedOnBehalfOfSendingTime);
		return this;
	}

	public MessageHeaderAssert hasApplVerID(String expectedApplVerID) {
		messages.assertFieldHasValue(info, actual, ApplVerID.class, expectedApplVerID);
		return this;
	}

	public MessageHeaderAssert hasApplExtID(int expectedApplExtID) {
		messages.assertFieldHasValue(info, actual, ApplExtID.class, expectedApplExtID);
		return this;
	}

	public MessageHeaderAssert hasCstmApplVerID(String expectedCstmApplVerID) {
		messages.assertFieldHasValue(info, actual, CstmApplVerID.class, expectedCstmApplVerID);
		return this;
	}


	//    <group name="NoHops" required="N">
	//      <field name="HopCompID" required="N" />
	//      <field name="HopSendingTime" required="N" />
	//      <field name="HopRefID" required="N" />
	//    </group>

	//    <field name="" required="N"/>
	//    <field name="" required="N"/>
	//    <field name="" required="N"/>
	//    <component name="HopGrp" required="N"/>
}
