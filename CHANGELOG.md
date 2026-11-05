# Changelog

Notable changes follow semantic versioning intent.

## [Unreleased] - proposed 1.1.0

### Added

- Source and test compatibility gates for QuickFIX/J 2.3.2 and 3.0.2.
- Java 11, 17, and 21 CI matrix.
- Packaged-artifact consumer verification.
- Dependency convergence, JaCoCo baseline, and japicmp public API checks.
- Compatibility, support, contribution, architecture decision, and release documentation.

### Changed

- Replaced QuickFIX/J message-type aliases removed in 3.0.x with stable FIX protocol codes while preserving public assertion methods.
- Restored FIXT dictionary routing and regression tests from pull request 9, which had been merged to the historical `master` branch instead of `main`.
- Pinned AssertJ Core to 3.27.7 instead of resolving a wide version range.
- Advanced the development version to `1.1.0-SNAPSHOT`.
- Made `mvn --batch-mode verify` the canonical build command.

### Fixed

- JaCoCo execution data is now retained and coverage is enforced at the current 45 percent line baseline.
- Test fixtures no longer compile against QuickFIX/J APIs removed in 3.0.x.

### Compatibility

- Minimum Java remains 11.
- QuickFIX/J 2.3.2 and 3.0.2 are release gates.
- QuickFIX/J remains a provided dependency selected by the consumer.
- No intentional public API removals.

## [1.0.0]

Initial Maven Central release of the fluent message, version, type, header, trailer, and field assertions.
