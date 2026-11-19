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
package io.allune.quickfixj.consumer;

import org.junit.Test;
import quickfix.Group;
import quickfix.Message;
import quickfix.SessionID;
import quickfix.field.NoPartyIDs;
import quickfix.field.PartyID;
import quickfix.field.Price;
import quickfix.field.Symbol;

import java.math.BigDecimal;

import static io.allune.quickfixj.api.Assertions.assertThat;

public class PackagedArtifactConsumerTest {

    @Test
    public void shouldAssertAQuickFixJMessageThroughThePackagedArtifact() {
        Message message = new Message();
        message.getHeader().setString(8, "FIX.4.4");
        message.getHeader().setString(35, "D");
        message.getHeader().setString(49, "CLIENT");
        message.getHeader().setString(56, "BROKER");
        message.setString(Symbol.FIELD, "AAPL");
        message.setString(Price.FIELD, "12345678901234567890.123456789");
        Group party = new Group(NoPartyIDs.FIELD, PartyID.FIELD);
        party.setString(PartyID.FIELD, "CLIENT");
        message.addGroup(party);

        assertThat(message)
                .isVersion44()
                .isNewOrderSingle()
                .hasSessionID(new SessionID("FIX.4.4", "CLIENT", "BROKER"))
                .hasFieldValue(Symbol.FIELD, "AAPL")
                .hasDecimalFieldValue(Price.FIELD, new BigDecimal("12345678901234567890.123456789"))
                .hasGroupCount(NoPartyIDs.FIELD, 1)
                .group(NoPartyIDs.FIELD, 1)
                    .hasFieldValue(PartyID.FIELD, "CLIENT")
                .and()
                .isNewOrderSingle();
    }
}
