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
package io.allune.quickfixj.api.newordersingle;

import quickfix.fix42.NewOrderSingle;

/**
 * @author Eduardo Sanchez-Ros
 */
public class NewOrderSingle42Assert extends AbstractNewOrderSingle<NewOrderSingle42Assert, NewOrderSingle> {

	public NewOrderSingle42Assert(NewOrderSingle actual) {
		super(NewOrderSingle42Assert.class, actual);
	}

	// From data dictionary
	//    <message name="NewOrderSingle" msgtype="D" msgcat="app">
	//      <field name="ClOrdID" required="Y"/>
	//      <field name="ClientID" required="N"/>
	//      <field name="ExecBroker" required="N"/>
	//      <field name="Account" required="N"/>
	//      <group name="NoAllocs" required="N">
	//        <field name="AllocAccount" required="N"/>
	//        <field name="AllocShares" required="N"/>
	//      </group>
	//      <field name="SettlmntTyp" required="N"/>
	//      <field name="FutSettDate" required="N"/>
	//      <field name="HandlInst" required="Y"/>
	//      <field name="ExecInst" required="N"/>
	//      <field name="MinQty" required="N"/>
	//      <field name="MaxFloor" required="N"/>
	//      <field name="ExDestination" required="N"/>
	//      <group name="NoTradingSessions" required="N">
	//        <field name="TradingSessionID" required="N"/>
	//      </group>
	//      <field name="ProcessCode" required="N"/>
	//      <field name="Symbol" required="Y"/>
	//      <field name="SymbolSfx" required="N"/>
	//      <field name="SecurityID" required="N"/>
	//      <field name="IDSource" required="N"/>
	//      <field name="SecurityType" required="N"/>
	//      <field name="MaturityMonthYear" required="N"/>
	//      <field name="MaturityDay" required="N"/>
	//      <field name="PutOrCall" required="N"/>
	//      <field name="StrikePrice" required="N"/>
	//      <field name="OptAttribute" required="N"/>
	//      <field name="ContractMultiplier" required="N"/>
	//      <field name="CouponRate" required="N"/>
	//      <field name="SecurityExchange" required="N"/>
	//      <field name="Issuer" required="N"/>
	//      <field name="EncodedIssuerLen" required="N"/>
	//      <field name="EncodedIssuer" required="N"/>
	//      <field name="SecurityDesc" required="N"/>
	//      <field name="EncodedSecurityDescLen" required="N"/>
	//      <field name="EncodedSecurityDesc" required="N"/>
	//      <field name="PrevClosePx" required="N"/>
	//      <field name="Side" required="Y"/>
	//      <field name="LocateReqd" required="N"/>
	//      <field name="TransactTime" required="Y"/>
	//      <field name="OrderQty" required="N"/>
	//      <field name="CashOrderQty" required="N"/>
	//      <field name="OrdType" required="Y"/>
	//      <field name="Price" required="N"/>
	//      <field name="StopPx" required="N"/>
	//      <field name="Currency" required="N"/>
	//      <field name="ComplianceID" required="N"/>
	//      <field name="SolicitedFlag" required="N"/>
	//      <field name="IOIID" required="N"/>
	//      <field name="QuoteID" required="N"/>
	//      <field name="TimeInForce" required="N"/>
	//      <field name="EffectiveTime" required="N"/>
	//      <field name="ExpireDate" required="N"/>
	//      <field name="ExpireTime" required="N"/>
	//      <field name="GTBookingInst" required="N"/>
	//      <field name="Commission" required="N"/>
	//      <field name="CommType" required="N"/>
	//      <field name="Rule80A" required="N"/>
	//      <field name="ForexReq" required="N"/>
	//      <field name="SettlCurrency" required="N"/>
	//      <field name="Text" required="N"/>
	//      <field name="EncodedTextLen" required="N"/>
	//      <field name="EncodedText" required="N"/>
	//      <field name="FutSettDate2" required="N"/>
	//      <field name="OrderQty2" required="N"/>
	//      <field name="OpenClose" required="N"/>
	//      <field name="CoveredOrUncovered" required="N"/>
	//      <field name="CustomerOrFirm" required="N"/>
	//      <field name="MaxShow" required="N"/>
	//      <field name="PegDifference" required="N"/>
	//      <field name="DiscretionInst" required="N"/>
	//      <field name="DiscretionOffset" required="N"/>
	//      <field name="ClearingFirm" required="N"/>
	//      <field name="ClearingAccount" required="N"/>
	//    </message>

}
