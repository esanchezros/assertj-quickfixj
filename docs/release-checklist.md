# AssertJ QuickFIX/J 1.1.0 release checklist

Nothing in this checklist authorizes publication. The maintainer must explicitly approve deploy, Central publication, tag creation, push, and GitHub release publication.

## Release gates

- [ ] Milestone issues are closed or explicitly deferred.
- [ ] Root POM, `consumer-pom.xml`, README examples, and changelog use the intended release version.
- [ ] `mvn --batch-mode verify` passes from a clean checkout with QuickFIX/J 2.3.2.
- [ ] The same command passes with `-Dquickfixj.version=3.0.2`.
- [ ] CI passes Java 11, 17, and 21 for both QuickFIX/J versions.
- [ ] The packaged consumer passes for both QuickFIX/J versions.
- [ ] Dependency convergence passes.
- [ ] JaCoCo line coverage remains at or above 45 percent.
- [ ] japicmp reports no binary or source incompatibility with 1.0.0.
- [ ] `mvn --batch-mode -Prelease -Dgpg.skip=true clean verify` produces valid main, source, and Javadoc JARs.
- [ ] POM metadata, licenses, checksums, signatures, and Maven coordinates are reviewed.
- [ ] README, compatibility table, changelog, and migration notes are final.
- [ ] FixMock consumes the candidate successfully with QuickFIX/J 2.3.2 and 3.0.2.
- [ ] The starter compatibility workflow accepts the candidate version.

## Approval gates

- [ ] Maintainer explicitly approves signed deployment.
- [ ] Maintainer inspects the validated Central deployment.
- [ ] Maintainer explicitly approves Central publication.
- [ ] Maven Central artifacts resolve from a fresh local repository.
- [ ] Maintainer explicitly approves the release tag and push.
- [ ] Maintainer explicitly approves the GitHub release.

## After publication

- [ ] Verify POM, JAR, source JAR, and Javadoc JAR from Maven Central.
- [ ] Create and push the approved signed or annotated `1.1.0` tag.
- [ ] Publish approved GitHub release notes.
- [ ] Update README examples if required.
- [ ] Advance to the next development version.
- [ ] Update FixMock and starter test dependencies through separate pull requests.
