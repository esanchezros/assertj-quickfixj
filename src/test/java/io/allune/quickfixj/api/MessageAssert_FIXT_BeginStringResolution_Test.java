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

public class MessageAssert_FIXT_BeginStringResolution_Test {

    @Test
    public void headerShouldTriggerApplVerIdLookup_andFailIfMissing() throws InvalidMessage {
        // BeginString FIXT.1.1 but missing 1128 (ApplVerID)
        Message message = new Message();
        message.getHeader().setString(8, "FIXT.1.1");
        message.getHeader().setString(35, "0");

        // When/Then: calling header() will resolve begin string and require ApplVerID, producing a helpful failure
        assertThatThrownBy(() -> assertThat(message).header())
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("to have field with tag <1128>");
    }

    @Test
    public void beginStringShouldResolveToFix50sp2_whenApplVerIdProvided() throws InvalidMessage {
        // FIXT.1.1 + ApplVerID=9 (FIX50SP2) + Heartbeat
        Message message = new Message();
        message.getHeader().setString(8, "FIXT.1.1");
        message.getHeader().setString(35, "D");
        message.getHeader().setString(1128, "9"); // ApplVerID=FIX50SP2

        // When/Then: hasMsgTypeName should work using the resolved application dictionary
        assertThat(message).hasMsgTypeName("NewOrderSingle");
    }
}
