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
import quickfix.Group;
import quickfix.Message;
import quickfix.field.NoPartyIDs;
import quickfix.field.NoPartySubIDs;
import quickfix.field.PartyID;
import quickfix.field.PartyIDSource;
import quickfix.field.PartyRole;
import quickfix.field.PartySubID;
import quickfix.field.PartySubIDType;
import quickfix.field.Price;

import static io.allune.quickfixj.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MessageAssert_RepeatingGroups_Test {

	@Test
	public void shouldAssertGroupCountsEntriesAndNestedGroups() {
		Message message = executionReportWithParties();

		assertThat(message)
				.hasGroup(NoPartyIDs.FIELD)
				.hasGroupCount(NoPartyIDs.FIELD, 2)
				.group(NoPartyIDs.FIELD, 1)
					.hasFieldValue(PartyID.FIELD, "CLIENT")
					.hasFieldValue(PartyRole.FIELD, 11)
					.hasGroupCount(NoPartySubIDs.FIELD, 1)
					.group(NoPartySubIDs.FIELD, 1)
						.hasFieldValue(PartySubID.FIELD, "DESK-1")
						.hasFieldValue(PartySubIDType.FIELD, 5)
					.and()
				.and()
				.group(NoPartyIDs.FIELD, 2)
					.hasFieldValue(PartyID.FIELD, "BROKER")
				.and()
				.isExecutionReport();
	}

	@Test
	public void shouldReportExpectedAndActualGroupCount() {
		Message message = executionReportWithParties();

		assertThatThrownBy(() -> assertThat(message).hasGroupCount(NoPartyIDs.FIELD, 3))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("tag <453>")
				.hasMessageContaining("count <3>")
				.hasMessageContaining("but was <2>");
	}

	@Test
	public void shouldReportMissingGroupEntry() {
		Message message = executionReportWithParties();

		assertThatThrownBy(() -> assertThat(message).group(NoPartyIDs.FIELD, 3))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("tag <453> at index <3>")
				.hasMessageContaining("group count was <2>");
	}

	@Test
	public void shouldAssertFieldAbsenceWithDiagnostic() {
		Message message = executionReportWithParties();

		assertThat(message).doesNotHaveField(Price.FIELD);
		message.setDouble(Price.FIELD, 12.5);

		assertThatThrownBy(() -> assertThat(message).doesNotHaveField(Price.FIELD))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("not to have field with tag <44>")
				.hasMessageContaining("but it did");
	}

	@Test
	public void headerGroupAssertionShouldNotBeANoOp() {
		Message message = executionReportWithParties();

		assertThatThrownBy(() -> assertThat(message).header().hasGroup(627))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("repeating group with tag <627>");
	}

	private Message executionReportWithParties() {
		Message message = new Message();
		message.getHeader().setString(8, "FIX.4.4");
		message.getHeader().setString(35, "8");

		Group client = new Group(NoPartyIDs.FIELD, PartyID.FIELD);
		client.setString(PartyID.FIELD, "CLIENT");
		client.setChar(PartyIDSource.FIELD, 'D'); // Proprietary/custom code in the FIX protocol
		client.setInt(PartyRole.FIELD, 11);
		Group clientDesk = new Group(NoPartySubIDs.FIELD, PartySubID.FIELD);
		clientDesk.setString(PartySubID.FIELD, "DESK-1");
		clientDesk.setInt(PartySubIDType.FIELD, 5);
		client.addGroup(clientDesk);

		Group broker = new Group(NoPartyIDs.FIELD, PartyID.FIELD);
		broker.setString(PartyID.FIELD, "BROKER");
		broker.setChar(PartyIDSource.FIELD, PartyIDSource.BIC);
		broker.setInt(PartyRole.FIELD, 1);

		message.addGroup(client);
		message.addGroup(broker);
		return message;
	}
}
