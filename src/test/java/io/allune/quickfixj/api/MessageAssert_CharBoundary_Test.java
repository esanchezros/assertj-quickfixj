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
import quickfix.field.Side;

import static io.allune.quickfixj.api.Assertions.assertThat;

/**
 * Verifies CHAR type retrieval differences across FIX versions.
 */
public class MessageAssert_CharBoundary_Test {

    @Test
    public void sideShouldBeCharacter_forFix42AndAbove() throws InvalidMessage {
        // FIX.4.2 message where CHAR should be retrieved as char
        Message message = new Message();
        message.getHeader().setString(8, "FIX.4.2");
        message.getHeader().setString(35, "D");
        message.setString(Side.FIELD, "1");
        assertThat(message).hasFieldValue(Side.FIELD, '1');
    }
}
