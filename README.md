# AssertJ QuickFIX/J

Fluent, semantic assertions for normal QuickFIX/J `Message` values. Use it to check FIX versions, message types, headers, trailers, and typed field values with useful failure diagnostics.

[![CI](https://github.com/esanchezros/assertj-quickfixj/actions/workflows/ci.yml/badge.svg)](https://github.com/esanchezros/assertj-quickfixj/actions/workflows/ci.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.allune/assertj-quickfixj.svg)](https://central.sonatype.com/artifact/io.allune/assertj-quickfixj)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)

## Why use it?

Plain QuickFIX/J tests often repeat tag lookups, conversions, and hand-written error messages. AssertJ QuickFIX/J provides:

- `Assertions.assertThat(Message)` as the fluent entry point.
- FIX 4.0 through 4.4 and FIXT 1.1 application-version assertions.
- Semantic message methods such as `isNewOrderSingle()` and `isExecutionReport()`.
- Header and trailer chaining with `header()`, `trailer()`, and `and()`.
- Dictionary-aware, typed `hasFieldValue(tag, value)` checks.
- SessionID, sequence, reject, Logon, and ExecutionReport field assertions.
- Repeating-group counts, entries, and nested groups.
- Assertion-scoped custom dictionaries for parallel test safety.
- Generic `hasMsgType(code)` and `hasMsgTypeName(name)` checks.
- One test artifact that lets the consumer select QuickFIX/J.

## Requirements and compatibility

| Library | Java | QuickFIX/J | AssertJ Core | Status |
| --- | --- | --- | --- | --- |
| 1.1.x (`main`) | 11+ | 2.3.2 and 3.0.2 | 3.24.2 minimum; 3.27.7 normal | In development; all combinations are release gates |
| 1.0.x | 11+ | built with 2.3.2 | broad 3.x range | Published maintenance line |

CI runs Java 11, 17, 21, and 25. See [the full compatibility policy](docs/compatibility.adoc).

## Installation

The current release is on Maven Central. Add it in test scope:

```xml
<dependency>
  <groupId>io.allune</groupId>
  <artifactId>assertj-quickfixj</artifactId>
  <version>1.0.0</version>
  <scope>test</scope>
</dependency>
```

```kotlin
testImplementation("io.allune:assertj-quickfixj:1.0.0")
```

QuickFIX/J is intentionally `provided`. Your application or test suite must supply `quickfixj-core` and the message module it uses. This library does not change the engine version selected by the application.

## Quick start

```java
import quickfix.Message;
import quickfix.field.BeginString;
import quickfix.field.MsgType;

import quickfix.field.OrdType;
import quickfix.field.Side;
import quickfix.field.Symbol;

import static io.allune.quickfixj.api.Assertions.assertThat;

Message message = new Message();
message.getHeader().setString(BeginString.FIELD, "FIX.4.4");
message.getHeader().setString(MsgType.FIELD, "D");
message.setString(Symbol.FIELD, "AAPL");
message.setChar(Side.FIELD, Side.BUY);
message.setChar(OrdType.FIELD, OrdType.LIMIT);

assertThat(message)
    .isVersion44()
    .isNewOrderSingle()
    .hasFieldValue(Symbol.FIELD, "AAPL")
    .hasFieldValue(Side.FIELD, Side.BUY)
    .hasFieldValue(OrdType.FIELD, OrdType.LIMIT);
```


## Headers and trailers

```java
assertThat(message)
    .header()
        .hasSenderCompID("CLIENT")
        .hasTargetCompID("BROKER")
    .and()
    .trailer()
        .hasChecksum("074")
    .and()
    .isNewOrderSingle();
```

## Session and sequence assertions

```java
SessionID sessionID = new SessionID("FIX.4.4", "CLIENT", "BROKER");

assertThat(message)
    .hasSessionID(sessionID)
    .header()
        .hasMsgSeqNum(42)
        .hasLastMsgSeqNumProcessed(41)
    .and();
```

The assertion compares the protocol fields in `SessionID`. It ignores the session qualifier because a qualifier is engine configuration and is not encoded in the FIX message. Sequence Reset and Reject messages can use `hasNewSeqNo`, `hasRefSeqNum`, `hasSessionRejectReason`, and `hasText`.

## Repeating groups

Group indexes are one-based, matching QuickFIX/J:

```java
assertThat(executionReport)
    .hasGroupCount(NoPartyIDs.FIELD, 2)
    .group(NoPartyIDs.FIELD, 1)
        .hasFieldValue(PartyID.FIELD, "CLIENT")
        .hasFieldValue(PartyRole.FIELD, 11)
        .group(NoPartySubIDs.FIELD, 1)
            .hasFieldValue(PartySubID.FIELD, "DESK-1")
        .and()
    .and()
    .isExecutionReport();
```

QuickFIX/J components are flattened into their containing `FieldMap`, so component fields use the normal `hasField`, `doesNotHaveField`, and `hasFieldValue` assertions. Nested repeating groups use the same `group` API.

## Message names and FIXT 1.1

```java
assertThat(message).hasMsgTypeName("NewOrderSingle");
```

For FIXT 1.1, the library resolves the application dictionary from header tag `1128` (`ApplVerID`). FIX 5.0, FIX 5.0 SP1, and FIX 5.0 SP2 are supported by `isVersion50()`, `isVersion50sp1()`, and `isVersion50sp2()`.

## Custom dictionaries

```java
DataDictionary dictionary = new DataDictionary("path/to/FIX44-custom.xml");
assertThat(message)
    .withDataDictionary("FIX.4.4", dictionary)
    .hasFieldValue(6000, "CustomValue");
```

For FIXT 1.1 application dictionaries, use the application version identifier:

```java
assertThat(message)
    .withApplicationDataDictionary(ApplVerID.FIX50SP2, dictionary)
    .isVersion50sp2()
    .hasFieldValue(6000, "CustomValue");
```

Both methods scope the dictionary to one assertion chain, including its header, trailer, and group assertions. Different assertion instances can safely use conflicting dictionaries in parallel.

### Migrating from the global dictionary API

`usingDataDictionary(beginString, dictionary)` is deprecated in 1.1. It retains its process-global behavior for compatibility. Replace it with `withDataDictionary`. Code that intentionally needs the old global side effect can continue to use the deprecated method during the 1.x line, but conflicting registrations remain unsafe in parallel tests.

## FixMock integration

FixMock exposes recorded traffic as QuickFIX/J `Message` values, so no adapter is required:

```java
Message received = fixMock.awaitMessage(newOrderSingle(), Duration.ofSeconds(5));

assertThat(received)
    .isVersion44()
    .isNewOrderSingle()
    .hasFieldValue(Symbol.FIELD, "AAPL");
```

Keep both libraries in test scope. The Spring Boot starter interoperability suite consumes their released artifacts and does not create a source dependency between repositories.

## Build locally

Requirements are JDK 11+ and Maven 3.8.6+. The canonical command is:

```shell
mvn --batch-mode verify
```

Verify QuickFIX/J 3.0.2 explicitly with:

```shell
mvn --batch-mode -Dquickfixj.version=3.0.2 verify
```

CI runs QuickFIX/J 2.3.2 and 3.0.2 with AssertJ Core 3.24.2 and 3.27.7 on Java 11, 17, 21, and 25. It also checks dependency convergence, coverage, public API compatibility with 1.0.0, and a separately built packaged consumer.

## Documentation

- [Documentation index](docs/README.adoc)
- [Compatibility](docs/compatibility.adoc)
- [Roadmap](ROADMAP.adoc)
- [Contributing](CONTRIBUTING.adoc)
- [Support policy](docs/support-policy.adoc)
- [Release process](docs/release-process.adoc)
- [Release checklist](docs/release-checklist.md)
- [Changelog](CHANGELOG.md)
- [Architecture decisions](docs/adr/README.adoc)
- [Security policy](SECURITY.md)

## Current limitations

- The deprecated `usingDataDictionary` method remains process-wide for 1.x compatibility. Use `withDataDictionary` for isolation.
- Exact failure-message text is not a stable API unless a release note says otherwise.

## License

Apache License 2.0.
