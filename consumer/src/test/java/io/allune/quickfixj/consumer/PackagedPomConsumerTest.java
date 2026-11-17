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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PackagedPomConsumerTest {

	@Test
	public void packagedPomShouldNotSelectAQuickFixJRuntimeVersion() throws Exception {
		String localRepository = System.getProperty("consumer.localRepository");
		String version = System.getProperty("consumer.assertjQuickfixjVersion");
		Path pom = Paths.get(localRepository, "io", "allune", "assertj-quickfixj", version,
				"assertj-quickfixj-" + version + ".pom");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		Document document = factory.newDocumentBuilder().parse(pom.toFile());

		Map<String, String> scopes = dependencyScopes(document);
		assertEquals("provided", scopes.get("org.quickfixj:quickfixj-core"));
		assertEquals("provided", scopes.get("org.quickfixj:quickfixj-messages-all"));
		assertEquals("compile", scopes.get("org.assertj:assertj-core"));
	}

	private Map<String, String> dependencyScopes(Document document) {
		Map<String, String> scopes = new HashMap<>();
		NodeList dependencies = document.getElementsByTagName("dependency");
		for (int index = 0; index < dependencies.getLength(); index++) {
			Element dependency = (Element) dependencies.item(index);
			String groupId = text(dependency, "groupId");
			String artifactId = text(dependency, "artifactId");
			String scope = text(dependency, "scope");
			scopes.put(groupId + ":" + artifactId, scope.isEmpty() ? "compile" : scope);
		}
		return scopes;
	}

	private String text(Element parent, String tagName) {
		NodeList nodes = parent.getElementsByTagName(tagName);
		return nodes.getLength() == 0 ? "" : nodes.item(0).getTextContent().trim();
	}
}
