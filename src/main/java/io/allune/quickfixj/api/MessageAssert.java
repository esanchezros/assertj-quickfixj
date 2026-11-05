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

import quickfix.DataDictionary;
import quickfix.FixVersions;
import quickfix.Message;
import quickfix.field.BodyLength;

import static io.allune.quickfixj.error.ShouldBeAdminMessage.shouldBeAdminMessage;
import static io.allune.quickfixj.error.ShouldBeAppMessage.shouldBeAppMessage;
import static io.allune.quickfixj.error.ShouldBeEmptyMessage.shouldBeEmptyMessage;
import static io.allune.quickfixj.error.ShouldBeEqual.shouldBeEqual;
import static io.allune.quickfixj.error.ShouldHaveHeader.shouldHaveHeader;
import static io.allune.quickfixj.error.ShouldHaveTrailer.shouldHaveTrailer;
import static org.assertj.core.util.Preconditions.checkArgument;
import static quickfix.FixVersions.BEGINSTRING_FIX40;
import static quickfix.FixVersions.BEGINSTRING_FIX41;
import static quickfix.FixVersions.BEGINSTRING_FIX42;
import static quickfix.FixVersions.BEGINSTRING_FIX43;
import static quickfix.FixVersions.BEGINSTRING_FIX44;
import static quickfix.FixVersions.BEGINSTRING_FIXT11;
import static quickfix.field.ApplVerID.FIX50;
import static quickfix.field.ApplVerID.FIX50SP1;
import static quickfix.field.ApplVerID.FIX50SP2;
import static quickfix.field.MsgType.ADVERTISEMENT;
import static quickfix.field.MsgType.ALLOCATION_INSTRUCTION;
import static quickfix.field.MsgType.ALLOCATION_INSTRUCTION_ACK;
import static quickfix.field.MsgType.BID_REQUEST;
import static quickfix.field.MsgType.BID_RESPONSE;
import static quickfix.field.MsgType.BUSINESS_MESSAGE_REJECT;
import static quickfix.field.MsgType.CROSS_ORDER_CANCEL_REPLACE_REQUEST;
import static quickfix.field.MsgType.CROSS_ORDER_CANCEL_REQUEST;
import static quickfix.field.MsgType.DERIVATIVE_SECURITY_LIST;
import static quickfix.field.MsgType.DERIVATIVE_SECURITY_LIST_REQUEST;
import static quickfix.field.MsgType.DONT_KNOW_TRADE;
import static quickfix.field.MsgType.EMAIL;
import static quickfix.field.MsgType.EXECUTION_REPORT;
import static quickfix.field.MsgType.HEARTBEAT;
import static quickfix.field.MsgType.INDICATION_OF_INTEREST;
import static quickfix.field.MsgType.LIST_CANCEL_REQUEST;
import static quickfix.field.MsgType.LIST_EXECUTE;
import static quickfix.field.MsgType.LIST_STATUS;
import static quickfix.field.MsgType.LIST_STATUS_REQUEST;
import static quickfix.field.MsgType.LIST_STRIKE_PRICE;
import static quickfix.field.MsgType.LOGON;
import static quickfix.field.MsgType.LOGOUT;
import static quickfix.field.MsgType.MARKET_DATA_INCREMENTAL_REFRESH;
import static quickfix.field.MsgType.MARKET_DATA_REQUEST;
import static quickfix.field.MsgType.MARKET_DATA_REQUEST_REJECT;
import static quickfix.field.MsgType.MARKET_DATA_SNAPSHOT_FULL_REFRESH;
import static quickfix.field.MsgType.MASS_QUOTE;
import static quickfix.field.MsgType.MULTILEG_ORDER_CANCEL_REPLACE;
import static quickfix.field.MsgType.NEWS;
import static quickfix.field.MsgType.NEW_ORDER_CROSS;
import static quickfix.field.MsgType.NEW_ORDER_MULTILEG;
import static quickfix.field.MsgType.ORDER_CANCEL_REJECT;
import static quickfix.field.MsgType.ORDER_CANCEL_REPLACE_REQUEST;
import static quickfix.field.MsgType.ORDER_CANCEL_REQUEST;
import static quickfix.field.MsgType.ORDER_MASS_CANCEL_REPORT;
import static quickfix.field.MsgType.ORDER_MASS_CANCEL_REQUEST;
import static quickfix.field.MsgType.ORDER_STATUS_REQUEST;
import static quickfix.field.MsgType.QUOTE;
import static quickfix.field.MsgType.QUOTE_CANCEL;
import static quickfix.field.MsgType.QUOTE_REQUEST;
import static quickfix.field.MsgType.QUOTE_STATUS_REQUEST;
import static quickfix.field.MsgType.REGISTRATION_INSTRUCTIONS;
import static quickfix.field.MsgType.REGISTRATION_INSTRUCTIONS_RESPONSE;
import static quickfix.field.MsgType.REJECT;
import static quickfix.field.MsgType.RESEND_REQUEST;
import static quickfix.field.MsgType.SECURITY_DEFINITION;
import static quickfix.field.MsgType.SECURITY_DEFINITION_REQUEST;
import static quickfix.field.MsgType.SECURITY_LIST;
import static quickfix.field.MsgType.SECURITY_LIST_REQUEST;
import static quickfix.field.MsgType.SECURITY_STATUS;
import static quickfix.field.MsgType.SECURITY_STATUS_REQUEST;
import static quickfix.field.MsgType.SECURITY_TYPES;
import static quickfix.field.MsgType.SECURITY_TYPE_REQUEST;
import static quickfix.field.MsgType.SEQUENCE_RESET;
import static quickfix.field.MsgType.SETTLEMENT_INSTRUCTIONS;
import static quickfix.field.MsgType.TEST_REQUEST;
import static quickfix.field.MsgType.TRADE_CAPTURE_REPORT_REQUEST;
import static quickfix.field.MsgType.TRADING_SESSION_STATUS;
import static quickfix.field.MsgType.TRADING_SESSION_STATUS_REQUEST;

/**
 * Assertions for QuickFix/J {@link Message}.
 *
 * @author Eduardo Sanchez-Ros
 * @author Simon Lewis
 */
public class MessageAssert extends AbstractFieldMapAssert<MessageAssert, Message> {
	// These FIX protocol values are stable across QuickFIX/J 2.3.x and 3.0.x.
	private static final String MASS_QUOTE_ACKNOWLEDGEMENT = "b";
	private static final String ORDER_LIST = "E";
	private static final String ORDER_SINGLE = "D";
	private static final String XML_MESSAGE = "n";


	private String beginString;

	/**
	 * Creates a new {@link MessageAssert}.
	 *
	 * @param message the {@link Message} to verify.
	 */
	public MessageAssert(Message message) {
		super(message, MessageAssert.class);
		// Defer beginString resolution until first needed to avoid asserting during construction
		this.beginString = null;
	}

	@Override
	public String getBeginString() {
		if (beginString == null) {
			beginString = messages.determineBeginString(info, actual);
		}
		return beginString;
	}

	protected Message getActual() {
		return actual;
	}

	/**
	 * Verifies that the actual {@link Message}'s FIX version number is {@link FixVersions#BEGINSTRING_FIX40}.
	 * Example:
	 * <pre>
	 * 	<code class='java'>
	 * 		Message message = new Message(&quot;8=FIX.4.0\u00019=61\u000135=A\u000134=1\u000149=BANZAI\u000152=20200408-06:49:07\u000156=EXEC\u000198=0\u0001108=30\u000110=015\u0001&quot;);
	 * 		assertThat(message).isVersion40();
	 * 	</code>
	 * </pre>
	 *
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual {@code Message} is {@code null}.
	 * @throws AssertionError if the actual {@code Message}'s FIX version number is not {@link FixVersions#BEGINSTRING_FIX40}.
	 */
	public MessageAssert isVersion40() {
		versions.assertMessageIsVersion(info, actual, BEGINSTRING_FIX40);
		return this;
	}

	/**
	 * Verifies that the actual {@link Message}'s FIX version number is {@link FixVersions#BEGINSTRING_FIX41}.
	 * Example:
	 * <pre>
	 * 	<code class='java'>
	 * 		Message message = new Message(&quot;8=FIX.4.1\u00019=61\u000135=A\u000134=1\u000149=BANZAI\u000152=20200408-06:49:07\u000156=EXEC\u000198=0\u0001108=30\u000110=018\u0001&quot;);
	 * 		assertThat(message).isVersion41();
	 * 	</code>
	 * </pre>
	 *
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual {@code Message} is {@code null}.
	 * @throws AssertionError if the actual {@code Message}'s FIX version number is not {@link FixVersions#BEGINSTRING_FIX41}.
	 */
	public MessageAssert isVersion41() {
		versions.assertMessageIsVersion(info, actual, BEGINSTRING_FIX41);
		return this;
	}

	/**
	 * Verifies that the actual {@link Message}'s FIX version number is {@link FixVersions#BEGINSTRING_FIX42}.
	 * Example:
	 * <pre>
	 * 	<code class='java'>
	 * 		Message message = new Message(&quot;8=FIX.4.2\u00019=61\u000135=A\u000134=1\u000149=BANZAI\u000152=20200408-06:49:07\u000156=EXEC\u000198=0\u0001108=30\u000110=017\u0001&quot;);
	 * 		assertThat(message).isVersion42();
	 * 	</code>
	 * </pre>
	 *
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual {@code Message} is {@code null}.
	 * @throws AssertionError if the actual {@code Message}'s FIX version number is not {@link FixVersions#BEGINSTRING_FIX42}.
	 */
	public MessageAssert isVersion42() {
		versions.assertMessageIsVersion(info, actual, BEGINSTRING_FIX42);
		return this;
	}

	/**
	 * Verifies that the actual {@link Message}'s FIX version number is {@link FixVersions#BEGINSTRING_FIX43}.
	 * Example:
	 * <pre>
	 * 	<code class='java'>
	 * 		Message message = new Message(&quot;8=FIX.4.3\u00019=61\u000135=A\u000134=1\u000149=BANZAI\u000152=20200408-06:49:07\u000156=EXEC\u000198=0\u0001108=30\u000110=018\u0001&quot;);
	 * 		assertThat(message).isVersion43();
	 * 	</code>
	 * </pre>
	 *
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual {@code Message} is {@code null}.
	 * @throws AssertionError if the actual {@code Message}'s FIX version number is not {@link FixVersions#BEGINSTRING_FIX43}.
	 */
	public MessageAssert isVersion43() {
		versions.assertMessageIsVersion(info, actual, BEGINSTRING_FIX43);
		return this;
	}

	/**
	 * Verifies that the actual {@link Message}'s FIX version number is {@link FixVersions#BEGINSTRING_FIX44}.
	 * Example:
	 * <pre>
	 * 	<code class='java'>
	 * 		Message message = new Message(&quot;8=FIX.4.4\u00019=61\u000135=A\u000134=1\u000149=BANZAI\u000152=20200408-06:49:07\u000156=EXEC\u000198=0\u0001108=30\u000110=019\u0001&quot;);
	 * 		assertThat(message).isVersion44();
	 * 	</code>
	 * </pre>
	 *
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual {@code Message} is {@code null}.
	 * @throws AssertionError if the actual {@code Message}'s FIX version number is not {@link FixVersions#BEGINSTRING_FIX44}.
	 */
	public MessageAssert isVersion44() {
		versions.assertMessageIsVersion(info, actual, BEGINSTRING_FIX44);
		return this;
	}

	/**
	 * Verifies that the actual {@link Message}'s FIX version number is {@link FixVersions#FIX50}.
	 * Example:
	 * <pre>
	 * 	<code class='java'>
	 * 		Message message = new Message(&quot;8=FIXT.1.1\u00019=74\u000135=2\u000149=BuySide\u000156=SellSide\u000134=4\u000152=20190605-17:09:11.495\u00011128=7\u00017=2\u000116=0\u000110=189\u0001&quot;);
	 * 		assertThat(message).isVersion50();
	 * 	</code>
	 * </pre>
	 *
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual {@code Message} is {@code null}.
	 * @throws AssertionError if the actual {@code Message}'s FIX version number is not {@link FixVersions#FIX50}.
	 */
	public MessageAssert isVersion50() {
		versions.assertMessageIsVersion(info, actual, BEGINSTRING_FIXT11, FIX50);
		return this;
	}

	/**
	 * Verifies that the actual {@link Message}'s FIX version number is {@link FixVersions#FIX50SP1}.
	 * Example:
	 * <pre>
	 * 	<code class='java'>
	 * 		Message message = new Message(&quot;8=FIXT.1.1\u00019=74\u000135=2\u000149=BuySide\u000156=SellSide\u000134=4\u000152=20190605-17:09:11.495\u00011128=8\u00017=2\u000116=0\u000110=190\u0001&quot;);
	 * 		assertThat(message).isVersion50sp1();
	 * 	</code>
	 * </pre>
	 *
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual {@code Message} is {@code null}.
	 * @throws AssertionError if the actual {@code Message}'s FIX version number is not {@link FixVersions#FIX50SP1}.
	 */
	public MessageAssert isVersion50sp1() {
		versions.assertMessageIsVersion(info, actual, BEGINSTRING_FIXT11, FIX50SP1);
		return this;
	}

	/**
	 * Verifies that the actual {@link Message}'s FIX version number is {@link FixVersions#FIX50SP2}.
	 * Example:
	 * <pre>
	 * 	<code class='java'>
	 * 		Message message = new Message(&quot;8=FIXT.1.1\u00019=74\u000135=2\u000149=BuySide\u000156=SellSide\u000134=4\u000152=20190605-17:09:11.495\u00011128=9\u00017=2\u000116=0\u000110=191\u0001&quot;);
	 * 		assertThat(message).isVersion50sp2();
	 * 	</code>
	 * </pre>
	 *
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual {@code Message} is {@code null}.
	 * @throws AssertionError if the actual {@code Message}'s FIX version number is not {@link FixVersions#FIX50SP2}.
	 */
	public MessageAssert isVersion50sp2() {
		versions.assertMessageIsVersion(info, actual, BEGINSTRING_FIXT11, FIX50SP2);
		return this;
	}

	@Override
	public MessageAssert isEqualTo(Object expected) {
		throw new UnsupportedOperationException("");
	}

	/**
	 * @return a {@link MessageHeaderAssert} assertion object.
	 */
	public MessageHeaderAssert header() {
		isNotNull();
		if (actual.getHeader() == null)
			throw failures.failure(info, shouldHaveHeader(actual));
		return new MessageHeaderAssert(actual.getHeader(), this, getBeginString());
	}

	/**
	 * @return a {@link MessageTrailerAssert} assertion object.
	 */
	public MessageTrailerAssert trailer() {
		isNotNull();
		if (actual.getTrailer() == null)
			throw failures.failure(info, shouldHaveTrailer(actual));
		return new MessageTrailerAssert(actual.getTrailer(), this, getBeginString());
	}

	/**
	 * Verifies that the actual {@link Message} is an admin message
	 *
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isAdmin() {
		isNotNull();
		if (!actual.isAdmin())
			throw failures.failure(info, shouldBeAdminMessage(actual));
		return this;
	}

	/**
	 * Verifies that the actual {@link Message} is an application message
	 *
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isApp() {
		isNotNull();
		if (!actual.isApp())
			throw failures.failure(info, shouldBeAppMessage(actual));
		return this;
	}

	/**
	 * Verifies that the actual {@link Message} is empty
	 *
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isEmpty() {
		isNotNull();
		if (!actual.isEmpty())
			throw failures.failure(info, shouldBeEmptyMessage(actual));
		return this;
	}

	/**
	 * Verifies that the actual {@link Message} is a Heartbeat
	 *
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isHeartbeat() {
		messages.assertMessageIsOfType(info, actual, HEARTBEAT);
		return this;
	}

	/**
	 * Verifies that the actual {@link Message} is a Test Request
	 *
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isTestRequest() {
		messages.assertMessageIsOfType(info, actual, TEST_REQUEST);
		return this;
	}

	/**
	 * Verifies that the actual {@link Message} is a Resend Request
	 *
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isResendRequest() {
		messages.assertMessageIsOfType(info, actual, RESEND_REQUEST);
		return this;
	}

	/**
	 * Verifies that the actual {@link Message} is a Reject
	 *
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isReject() {
		messages.assertMessageIsOfType(info, actual, REJECT);
		return this;
	}

	/**
	 * Verifies that the actual {@link Message} is a Sequence Reset
	 *
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isSequenceReset() {
		messages.assertMessageIsOfType(info, actual, SEQUENCE_RESET);
		return this;
	}

	/**
	 * Verifies that the actual {@link Message} is a Logout
	 *
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isLogout() {
		messages.assertMessageIsOfType(info, actual, LOGOUT);
		return this;
	}

	/**
	 * Verifies that the actual {@link Message} is an Indication of Interest
	 *
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isIndicationOfInterest() {
		messages.assertMessageIsOfType(info, actual, INDICATION_OF_INTEREST);
		return this;
	}

	/**
	 * Verifies that the actual {@link Message} is an Advertisement
	 *
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isAdvertisement() {
		messages.assertMessageIsOfType(info, actual, ADVERTISEMENT);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isExecutionReport() {
		messages.assertMessageIsOfType(info, actual, EXECUTION_REPORT);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isOrderCancelReject() {
		messages.assertMessageIsOfType(info, actual, ORDER_CANCEL_REJECT);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isLogon() {
		messages.assertMessageIsOfType(info, actual, LOGON);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isNews() {
		messages.assertMessageIsOfType(info, actual, NEWS);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isEmail() {
		messages.assertMessageIsOfType(info, actual, EMAIL);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isNewOrderSingle() {
		messages.assertMessageIsOfType(info, actual, ORDER_SINGLE);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isNewOrderList() {
		messages.assertMessageIsOfType(info, actual, ORDER_LIST);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isOrderCancelRequest() {
		messages.assertMessageIsOfType(info, actual, ORDER_CANCEL_REQUEST);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isOrderCancelReplaceRequest() {
		messages.assertMessageIsOfType(info, actual, ORDER_CANCEL_REPLACE_REQUEST);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isOrderStatusRequest() {
		messages.assertMessageIsOfType(info, actual, ORDER_STATUS_REQUEST);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isAllocationInstruction() {
		messages.assertMessageIsOfType(info, actual, ALLOCATION_INSTRUCTION);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isListCancelRequest() {
		messages.assertMessageIsOfType(info, actual, LIST_CANCEL_REQUEST);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isListExecute() {
		messages.assertMessageIsOfType(info, actual, LIST_EXECUTE);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isListStatusRequest() {
		messages.assertMessageIsOfType(info, actual, LIST_STATUS_REQUEST);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isListStatus() {
		messages.assertMessageIsOfType(info, actual, LIST_STATUS);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isAllocationInstructionAck() {
		messages.assertMessageIsOfType(info, actual, ALLOCATION_INSTRUCTION_ACK);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isDontKnowTrade() {
		messages.assertMessageIsOfType(info, actual, DONT_KNOW_TRADE);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isQuoteRequest() {
		messages.assertMessageIsOfType(info, actual, QUOTE_REQUEST);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isQuote() {
		messages.assertMessageIsOfType(info, actual, QUOTE);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isSettlementInstructions() {
		messages.assertMessageIsOfType(info, actual, SETTLEMENT_INSTRUCTIONS);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isMarketDataRequest() {
		messages.assertMessageIsOfType(info, actual, MARKET_DATA_REQUEST);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isMarketDataSnapshotFullRefresh() {
		messages.assertMessageIsOfType(info, actual, MARKET_DATA_SNAPSHOT_FULL_REFRESH);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isMarketDataIncrementalRefresh() {
		messages.assertMessageIsOfType(info, actual, MARKET_DATA_INCREMENTAL_REFRESH);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isMarketDataRequestReject() {
		messages.assertMessageIsOfType(info, actual, MARKET_DATA_REQUEST_REJECT);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isQuoteCancel() {
		messages.assertMessageIsOfType(info, actual, QUOTE_CANCEL);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isQuoteStatusRequest() {
		messages.assertMessageIsOfType(info, actual, QUOTE_STATUS_REQUEST);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isMassQuoteAcknowledgement() {
		messages.assertMessageIsOfType(info, actual, MASS_QUOTE_ACKNOWLEDGEMENT);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isSecurityDefinitionRequest() {
		messages.assertMessageIsOfType(info, actual, SECURITY_DEFINITION_REQUEST);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isSecurityDefinition() {
		messages.assertMessageIsOfType(info, actual, SECURITY_DEFINITION);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isSecurityStatusRequest() {
		messages.assertMessageIsOfType(info, actual, SECURITY_STATUS_REQUEST);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isSecurityStatus() {
		messages.assertMessageIsOfType(info, actual, SECURITY_STATUS);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isTradingSessionStatusRequest() {
		messages.assertMessageIsOfType(info, actual, TRADING_SESSION_STATUS_REQUEST);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isTradingSessionStatus() {
		messages.assertMessageIsOfType(info, actual, TRADING_SESSION_STATUS);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isMassQuote() {
		messages.assertMessageIsOfType(info, actual, MASS_QUOTE);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isBusinessMessageReject() {
		messages.assertMessageIsOfType(info, actual, BUSINESS_MESSAGE_REJECT);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isBidRequest() {
		messages.assertMessageIsOfType(info, actual, BID_REQUEST);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isBidResponse() {
		messages.assertMessageIsOfType(info, actual, BID_RESPONSE);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isListStrikePrice() {
		messages.assertMessageIsOfType(info, actual, LIST_STRIKE_PRICE);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isXmlMessage() {
		messages.assertMessageIsOfType(info, actual, XML_MESSAGE);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isRegistrationInstructions() {
		messages.assertMessageIsOfType(info, actual, REGISTRATION_INSTRUCTIONS);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isRegistrationInstructionsResponse() {
		messages.assertMessageIsOfType(info, actual, REGISTRATION_INSTRUCTIONS_RESPONSE);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isOrderMassCancelRequest() {
		messages.assertMessageIsOfType(info, actual, ORDER_MASS_CANCEL_REQUEST);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isOrderMassCancelReport() {
		messages.assertMessageIsOfType(info, actual, ORDER_MASS_CANCEL_REPORT);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isNewOrderCross() {
		messages.assertMessageIsOfType(info, actual, NEW_ORDER_CROSS);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isCrossOrderCancelReplaceRequest() {
		messages.assertMessageIsOfType(info, actual, CROSS_ORDER_CANCEL_REPLACE_REQUEST);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isCrossOrderCancelRequest() {
		messages.assertMessageIsOfType(info, actual, CROSS_ORDER_CANCEL_REQUEST);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isSecurityTypeRequest() {
		messages.assertMessageIsOfType(info, actual, SECURITY_TYPE_REQUEST);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isSecurityTypes() {
		messages.assertMessageIsOfType(info, actual, SECURITY_TYPES);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isSecurityListRequest() {
		messages.assertMessageIsOfType(info, actual, SECURITY_LIST_REQUEST);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isSecurityList() {
		messages.assertMessageIsOfType(info, actual, SECURITY_LIST);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isDerivativeSecurityListRequest() {
		messages.assertMessageIsOfType(info, actual, DERIVATIVE_SECURITY_LIST_REQUEST);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isDerivativeSecurityList() {
		messages.assertMessageIsOfType(info, actual, DERIVATIVE_SECURITY_LIST);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isNewOrderMultileg() {
		messages.assertMessageIsOfType(info, actual, NEW_ORDER_MULTILEG);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isMultilegOrderCancelReplace() {
		messages.assertMessageIsOfType(info, actual, MULTILEG_ORDER_CANCEL_REPLACE);
		return this;
	}

	/**
	 * @return {@code this} assertion object.
	 */
	public MessageAssert isTradeCaptureReportRequest() {
		messages.assertMessageIsOfType(info, actual, TRADE_CAPTURE_REPORT_REQUEST);
		return this;
	}


	/**
	 * @param expectedMsgType
	 * @return {@code this} assertion object.
	 */
	public MessageAssert hasMsgType(String expectedMsgType) {
		messages.assertMessageIsOfType(info, actual, expectedMsgType);
		return this;
	}


	/**
	 * @param expectedMsgTypeName
	 * @return {@code this} assertion object.
	 */
	public MessageAssert hasMsgTypeName(String expectedMsgTypeName) {
		messages.assertMessageIsOfTypeName(info, actual, getBeginString(), expectedMsgTypeName);
		return this;
	}

	/**
	 * @param expectedBodyLength
	 * @return {@code this} assertion object.
	 */
	public MessageAssert hasBodyLength(int expectedBodyLength) {
		isNotNull();

		if (actual.bodyLength() != expectedBodyLength)
			throw failures.failure(info, shouldBeEqual(actual, BodyLength.class, BodyLength.FIELD, actual.bodyLength(), expectedBodyLength));
		return this;
	}

	/**
	 * @param beginString
	 * @param dataDictionary
	 * @return {@code this} assertion object.
	 */
	public MessageAssert usingDataDictionary(String beginString, DataDictionary dataDictionary) {
		checkArgument(dataDictionary != null, "'dataDictionary' must not be null");

		dictionaries.addDataDictionary(beginString, dataDictionary);
		return this;
	}
}
