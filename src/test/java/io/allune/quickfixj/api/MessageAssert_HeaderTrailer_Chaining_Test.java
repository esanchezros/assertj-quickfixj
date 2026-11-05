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
import quickfix.field.Account;
import quickfix.field.BeginString;
import quickfix.field.BodyLength;
import quickfix.field.CheckSum;
import quickfix.field.MsgType;

import static io.allune.quickfixj.api.Assertions.assertThat;

/**
 * Validates header()…and()…trailer() chaining and that we can continue with body assertions.
 */
public class MessageAssert_HeaderTrailer_Chaining_Test {

    @Test
    public void shouldChainHeaderAndTrailerAndContinueWithBody() {
        Message message = new Message();
        message.getHeader().setString(BeginString.FIELD, "FIX.4.0");
        message.getHeader().setString(MsgType.FIELD, "D");
        message.getHeader().setInt(BodyLength.FIELD, 123);
        message.getTrailer().setString(CheckSum.FIELD, "000");
        message.setString(Account.FIELD, "ACC");

        assertThat(message)
                .header()
                    .hasField(BeginString.FIELD)
                    .hasField(MsgType.FIELD)
                    .hasField(BodyLength.FIELD)
                .and()
                .trailer()
                    .hasField(CheckSum.FIELD)
                .and()
                .hasField(Account.FIELD)
                .hasFieldValue(Account.FIELD, "ACC");
    }
}
