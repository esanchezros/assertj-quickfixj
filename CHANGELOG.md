# Changelog

Notable changes follow semantic versioning intent.

## [Unreleased]

### Added

- Added `hasDecimalFieldValue(tag, BigDecimal)` for exact `PRICE`, `AMT`, `QTY`, `FLOAT`, `PRICEOFFSET`, and `PERCENTAGE` assertions across messages, headers, trailers, and repeating groups. Decimal scale is ignored during numeric comparison.
- Exact-decimal conversion failures now report the tag, dictionary field name and type, raw value, expected value, and conversion problem.

### Compatibility

- Existing `hasFieldValue(tag, value)` decimal handling remains `Double`-based and behaviorally unchanged.

## [1.1.0] - 2026-11-17

### Added

- Source and test compatibility gates for QuickFIX/J 2.3.2 and 3.0.2.
- Java 11, 17, 21, and 25 CI matrix.
- Packaged-artifact consumer verification.
- Dependency convergence, JaCoCo baseline, and japicmp public API checks.
- Compatibility, support, contribution, architecture decision, and release documentation.
- Assertion-scoped session and FIXT application dictionaries.
- SessionID, field-absence, repeating-group, nested-group, and protocol-field assertions.

### Changed

- Replaced QuickFIX/J message-type aliases removed in 3.0.x with stable FIX protocol codes while preserving public assertion methods.
- Restored FIXT dictionary routing and regression tests from pull request 9, which had been merged to the historical `master` branch instead of `main`.
- Pinned AssertJ Core to 3.27.7 instead of resolving a wide version range.
- Declared AssertJ Core 3.24.2 as the tested minimum while retaining 3.27.7 as the normal version.
- Made `mvn --batch-mode verify` the canonical build command.

### Fixed

- JaCoCo execution data is now retained and coverage is enforced at the current 45 percent line baseline.
- Test fixtures no longer compile against QuickFIX/J APIs removed in 3.0.x.
- Test fixtures no longer require Lombok, so annotation processing cannot block the Java 25 release gate.
- `MessageHeaderAssert.hasGroup` now performs the assertion instead of returning success without inspection.
- Custom dictionaries configured through the new scoped API no longer contaminate parallel assertions.
- Normal verification no longer resolves the release-only Central publishing extension in every CI matrix job.

### Compatibility

- Minimum Java remains 11.
- QuickFIX/J 2.3.2 and 3.0.2 are release gates.
- QuickFIX/J remains a provided dependency selected by the consumer.
- No intentional public API removals.

## [1.0.0]

Initial Maven Central release of the fluent message, version, type, header, trailer, and field assertions.
