# AssertJ-QuickFIX/J

Fluent, AssertJ-style assertions for QuickFIX/J Message objects. This library lets you write readable, chainable tests for FIX messages, including message type and FIX version validation, header and trailer assertions, and precise field value checks based on the QuickFIX/J data dictionaries.

- Project home: https://github.com/esanchezros/assertj-quickfixj
- License: Apache 2.0
- Java: 11, 17, 21
- QuickFIX/J: 2.3.2 (API compatible with 2.x)
- AssertJ: 3.x (tested with recent versions)


## Why this library?
Testing FIX messages with plain QuickFIX/J often leads to brittle code (manual tag lookups, casting by field type, poor error text). AssertJ-QuickFIX/J provides:

- Fluent entry point Assertions.assertThat(Message)
- FIX version checks (isVersion40(), … isVersion50sp2())
- Message type checks (isLogon(), isNewOrderSingle(), …) and generic hasMsgType(..)/hasMsgTypeName(..)
- Header and trailer assertions with a chainable API: header() … and() … trailer()
- Field presence and value checks that are type-aware via the data dictionary
- Clear, actionable failure messages that show the actual message and tags


## Installation
The artifact is published to Maven Central.

Maven:

```xml
<dependency>
  <groupId>io.allune</groupId>
  <artifactId>assertj-quickfixj</artifactId>
  <version>1.0.0</version>
  <scope>test</scope>
</dependency>
```

Gradle (Kotlin DSL):

```gradle
testImplementation("io.allune:assertj-quickfixj:1.0.0")
```

Notes:
- quickfixj-core and quickfixj-messages-* are expected to be provided by your project/test runtime. This library does not bundle QuickFIX/J classes.
- If you use a BOM or dependency constraints for AssertJ, ensure a compatible 3.x is present.


## Quick start
Create a QuickFIX/J Message and assert fields and types in one chain.

```java
// Given
test Message message = new Message(
    "8=FIX.4.0\u00019=122\u000135=D\u000134=215\u000149=CLIENT12\u000152=20100225-19:41:57.316\u000138=1000\u000156=B\u00011=Marcel\u000111=13346\u000121=1\u000140=2\u000144=5\u000154=1\u000155=GBP/USD\u000159=0\u000160=20100225-19:39:52.020\u000110=074\u0001");

// When/Then
Assertions.assertThat(message)
    .isVersion40()
    .isNewOrderSingle()
    .header()
        .hasField(quickfix.field.BeginString.FIELD)
        .hasField(quickfix.field.BodyLength.FIELD)
        .hasField(quickfix.field.MsgType.FIELD)
        .hasField(quickfix.field.MsgSeqNum.FIELD)
        .hasField(quickfix.field.SenderCompID.FIELD)
        .hasField(quickfix.field.SendingTime.FIELD)
        .hasField(quickfix.field.TargetCompID.FIELD)
    .and()
    .trailer()
        .hasField(quickfix.field.CheckSum.FIELD)
    .and()
        .hasFieldValue(quickfix.field.Account.FIELD, "Marcel")
        .hasFieldValue(quickfix.field.ClOrdID.FIELD, "13346")
        .hasFieldValue(quickfix.field.Side.FIELD, "1")
        .hasFieldValue(quickfix.field.Symbol.FIELD, "GBP/USD")
        .hasFieldValue(quickfix.field.OrdType.FIELD, "2");
```

## Core concepts and API
- Entry point: io.allune.quickfixj.api.Assertions.assertThat(Message)
- Message version:
  - isVersion40(), isVersion41(), isVersion42(), isVersion43(), isVersion44()
  - isVersion50(), isVersion50sp1(), isVersion50sp2() for FIXT.1.1 + ApplVerID
- Message type:
  - Dozens of convenience methods: isLogon(), isHeartbeat(), isExecutionReport(), isNewOrderSingle(), …
  - Generic checks: hasMsgType(String), hasMsgTypeName(String)
- Header/trailer:
  - header() … and() … trailer() to dive into header/trailer, then return to the root assertion
  - Examples: header().hasBodyLength(254).hasMsgSeqNum(3).and().trailer().hasChecksum("074")
- Field assertions:
  - hasField(int tag), hasFields(int... tags)
  - hasFieldValue(int tag, Object value)
  - Values are read type-safely via QuickFIX/J’s DataDictionary for the message BeginString


## Message type by name
If you prefer to assert by message name instead of type code, use hasMsgTypeName(..). The lookup uses the session/application data dictionaries for the message’s version.

```java
Assertions.assertThat(message)
    .hasMsgTypeName("NewOrderSingle");
```

## FIXT.1.1 and ApplVerID
For FIXT.1.1 sessions (BeginString FIXT.1.1), the effective application BeginString is derived from ApplVerID using QuickFIX/J’s MessageUtils.toBeginString mapping. This library handles that internally, so field type and message name lookups behave as if you were on FIX.5.0, FIX.5.0SP1, or FIX.5.0SP2.

If a message lacks the required ApplVerID, assertions that need the effective version (e.g., hasMsgTypeName, some field type reads) will fail with a clear error indicating the missing header field.


## Custom Data Dictionaries (optional)
You can register custom data dictionaries when asserting messages, for example if you have user-defined fields or variants.

```java
DataDictionary dd = new DataDictionary("path/to/FIX40.xml");
Assertions.assertThat(message)
    .usingDataDictionary("FIX.4.0", dd)
    .hasFieldValue(6000, "CustomValue");
```

Notes:
- The registration applies process-wide during the test run; prefer to register in a setup method and restore state between tests if needed.
- For FIXT flows you may also register application dictionaries per ApplVerID via Dictionaries (advanced use).


## Failure messages (examples)
- Missing field:
  Expecting Message:
   <...>
  to have field with tag <8>
  but did not.

- Wrong message type:
  Expecting Message:
   <...>
  to be of type <D>
  but was:
   <8>

- Wrong field value:
  Expecting field with tag <1> in Message:
   <...>
  to have value:
   <Marcel>
  but was:
   <John>


## Compatibility
- Java: 11, 17, 21
- QuickFIX/J: tested with 2.3.2; should work with other 2.x
- AssertJ: 3.x (the project’s pom uses a version range; pin a modern 3.x if you manage via BOM)

If you work with older dictionaries or custom ones, ensure they are compatible with your QuickFIX/J version.


## Build and test locally
- Requirements: JDK 11+ and Maven 3.8+
- Run tests:
  mvn -DskipTests=false test

The repository includes GitHub Actions workflows that run the test suite on Java 11, 17 and 21 for PRs and pushes to main.


## FAQ
- Does this library parse strings into Message? No, parsing is QuickFIX/J’s responsibility. You can build Message instances from strings or via the QuickFIX/J message classes. This library only asserts over Message/FieldMap objects.
- Does it support repeating groups/components? Not yet. You can still assert presence/values using tags; higher-level group assertions may come in later versions.
- Is the state thread-safe? Data dictionary overrides are process-global in this version. Avoid registering different dictionaries concurrently from parallel tests unless you isolate execution.


## Contributing
Issues and PRs are welcome. Please:
- Discuss larger changes in an issue first (especially new assertions or dictionary behavior)
- Add tests for new behavior
- Keep the fluent API consistent with AssertJ style


## License
Apache License 2.0. See LICENSE in the repository.
