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
import quickfix.FixVersions;
import quickfix.field.ApplVerID;

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
		if (dd != null) {
			return dd;
		}
		// For FIXT.1.1 application flows, the beginString may be mapped to FIX.5.0* by MessageUtils.toBeginString(ApplVerID)
		// In those cases, DefaultDataDictionaryProvider expects the application dictionary, not a session one.
		if (FixVersions.FIX50SP2.equals(beginString)) {
			return getApplicationDataDictionary(ApplVerID.FIX50SP2); // ApplVerID.FIX50SP2
		}
		if (FixVersions.FIX50SP1.equals(beginString)) {
			return getApplicationDataDictionary(ApplVerID.FIX50SP1); // ApplVerID.FIX50SP1
		}
		if (FixVersions.FIX50.equals(beginString)) {
			return getApplicationDataDictionary(ApplVerID.FIX50); // ApplVerID.FIX50
		}
		return dataDictionaryProvider.getSessionDataDictionary(beginString);
	}

	public void addDataDictionary(String beginString, DataDictionary dataDictionary) {
		sessionOverrides.put(beginString, dataDictionary);
	}

	public DataDictionary getApplicationDataDictionary(String applVerId) {
		DataDictionary dd = applicationOverrides.get(applVerId);
		return dd != null ? dd : dataDictionaryProvider.getApplicationDataDictionary(new ApplVerID(applVerId));
	}

	public void addApplicationDataDictionary(String applVerId, DataDictionary dataDictionary) {
		applicationOverrides.put(applVerId, dataDictionary);
	}
}
