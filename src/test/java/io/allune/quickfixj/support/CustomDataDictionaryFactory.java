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
package io.allune.quickfixj.support;

import quickfix.ConfigError;
import quickfix.DataDictionary;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public final class CustomDataDictionaryFactory {

	private CustomDataDictionaryFactory() {
	}

	public static DataDictionary dictionaryWithCustomFieldType(String fieldType) throws ConfigError {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<fix type=\"FIX\" major=\"4\" minor=\"4\">"
				+ "<header>"
				+ "<field name=\"BeginString\" required=\"Y\"/>"
				+ "<field name=\"BodyLength\" required=\"Y\"/>"
				+ "<field name=\"MsgType\" required=\"Y\"/>"
				+ "</header>"
				+ "<messages><message name=\"CustomMessage\" msgtype=\"Z\" msgcat=\"app\">"
				+ "<field name=\"CustomField\" required=\"N\"/>"
				+ "</message></messages>"
				+ "<trailer><field name=\"CheckSum\" required=\"Y\"/></trailer>"
				+ "<components/>"
				+ "<fields>"
				+ "<field number=\"8\" name=\"BeginString\" type=\"STRING\"/>"
				+ "<field number=\"9\" name=\"BodyLength\" type=\"LENGTH\"/>"
				+ "<field number=\"35\" name=\"MsgType\" type=\"STRING\"/>"
				+ "<field number=\"10\" name=\"CheckSum\" type=\"STRING\"/>"
				+ "<field number=\"6000\" name=\"CustomField\" type=\"" + fieldType + "\"/>"
				+ "</fields>"
				+ "</fix>";
		return new DataDictionary(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
	}
}
