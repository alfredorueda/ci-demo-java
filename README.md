# ci-demo-java

A tiny, dependency-free stock portfolio domain model in Java, used as a live
example in a Continuous Integration training session. No web framework, no
database, no external services — just business rules and the tests that pin
them down, wired to a GitHub Actions pipeline that blocks merging into `main`
when a test fails.

This is the Java sibling of [ci-demo-python](https://github.com/alfredorueda/ci-demo-python) — same domain, same tests, same pipeline, same walkthrough, ported to Java 21 and JUnit 5.

## What's here

```
src/main/java/com/example/portfoliodemo/
  Money.java                     Money value object (immutable, rounds to 2 decimals)
  Portfolio.java                 Portfolio: deposit / buy / sell shares, FIFO lots
  DomainException.java           Base class for domain errors
  InsufficientFundsException.java
  InsufficientSharesException.java
src/test/java/com/example/portfoliodemo/
  MoneyTest.java
  PortfolioTest.java
.github/workflows/ci.yml         Runs the full test suite on every push and PR
```

## Running it locally

No local Maven installation needed — the Maven Wrapper downloads the right
version automatically:

```bash
./mvnw clean test        # Windows: mvnw.cmd clean test
```

All 15 tests run in well under a second — no Docker, no network, no
database. Only a JDK (21 or newer) is required.

## The point of this repo

`main` is protected: a pull request cannot be merged while the
**Run domain tests** check is failing. Try it yourself:

1. Create a branch.
2. Break something in `src/main/java/com/example/portfoliodemo/Portfolio.java`
   (or "fix" a test to expect the wrong thing).
3. Push the branch and open a pull request against `main`.
4. Watch the check turn red — the **Merge** button is disabled.
5. Fix the code, push again — the check turns green and the PR becomes
   mergeable.

- [DEMO_SCRIPT.md](DEMO_SCRIPT.md) — condensed, copy-paste-ready run sheet
  for the live walkthrough above.
- [docs/WALKTHROUGH.md](docs/WALKTHROUGH.md) — the same walkthrough fully
  explained, with diagrams, plus a follow-up exercise to repeat the whole
  thing on your own fork afterwards.

## License

MIT — see [LICENSE](LICENSE).
