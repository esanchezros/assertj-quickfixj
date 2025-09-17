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
package io.allune.quickfixj.internal;

import quickfix.DataDictionary;
import quickfix.DefaultDataDictionaryProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Eduardo Sanchez-Ros
 */
public class Dictionaries {

	private static final Dictionaries INSTANCE = new Dictionaries();

	private static final DefaultDataDictionaryProvider dataDictionaryProvider = new DefaultDataDictionaryProvider();

	// Local overrides to allow dynamic registration from tests/clients without relying on provider add* APIs
	private final Map<String, DataDictionary> sessionOverrides = new ConcurrentHashMap<>();
	private final Map<String, DataDictionary> applicationOverrides = new ConcurrentHashMap<>();

	private Dictionaries() {
		//
	}

	public static Dictionaries instance() {
		return INSTANCE;
	}

	public DataDictionary getSessionDataDictionary(String beginString) {
		DataDictionary dd = sessionOverrides.get(beginString);
		return dd != null ? dd : dataDictionaryProvider.getSessionDataDictionary(beginString);
	}

	public void addDataDictionary(String beginString, DataDictionary dataDictionary) {
		sessionOverrides.put(beginString, dataDictionary);
	}

	public DataDictionary getApplicationDataDictionary(String applVerId) {
		DataDictionary dd = applicationOverrides.get(applVerId);
		return dd != null ? dd : dataDictionaryProvider.getApplicationDataDictionary(new quickfix.field.ApplVerID(applVerId));
	}

	public void addApplicationDataDictionary(String applVerId, DataDictionary dataDictionary) {
		applicationOverrides.put(applVerId, dataDictionary);
	}
}
