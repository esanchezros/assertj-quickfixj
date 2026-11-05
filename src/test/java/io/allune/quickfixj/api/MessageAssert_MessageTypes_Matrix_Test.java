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
import quickfix.InvalidMessage;
import quickfix.Message;

import static io.allune.quickfixj.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Representative matrix tests for message type assertions.
 */
public class MessageAssert_MessageTypes_Matrix_Test {

    @Test
    public void isHeartbeat_shouldPass_onHeartbeat() throws InvalidMessage {
        Message hb = new Message();
        hb.getHeader().setString(8, "FIX.4.2");
        hb.getHeader().setString(35, "0");
        assertThat(hb).isHeartbeat();
    }

    @Test
    public void isTestRequest_shouldFail_onHeartbeat_withDiagnostic() throws InvalidMessage {
        Message hb = new Message();
        hb.getHeader().setString(8, "FIX.4.2");
        hb.getHeader().setString(35, "0");
        assertThatThrownBy(() -> assertThat(hb).isTestRequest())
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("to be of type <\"1\">") // TEST_REQUEST = 1
                .hasMessageContaining("but was:\n <\"0\">"); // actual HEARTBEAT = 0
    }

    @Test
    public void isLogon_shouldPass_onLogon() throws InvalidMessage {
        Message logon = new Message();
        logon.getHeader().setString(8, "FIX.4.4");
        logon.getHeader().setString(35, "A");
        assertThat(logon).isLogon().isAdmin();
    }

    @Test
    public void isApp_shouldFail_onAdmin_withDiagnostic() throws InvalidMessage {
        Message logon = new Message();
        logon.getHeader().setString(8, "FIX.4.4");
        logon.getHeader().setString(35, "A");
        assertThatThrownBy(() -> assertThat(logon).isApp())
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("to be an Application message but was not");
    }
    @Test
    public void legacyQuickFixJAliasesShouldUseStableProtocolCodes() {
        assertMessageType("D").isNewOrderSingle();
        assertMessageType("E").isNewOrderList();
        assertMessageType("b").isMassQuoteAcknowledgement();
        assertMessageType("n").isXmlMessage();
    }

    private MessageAssert assertMessageType(String messageType) {
        Message message = new Message();
        message.getHeader().setString(8, "FIX.4.4");
        message.getHeader().setString(35, messageType);
        return assertThat(message);
    }

}
