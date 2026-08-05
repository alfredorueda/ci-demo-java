# Demo Script

A condensed, copy-paste-ready run sheet for the live walkthrough — same
content as [docs/WALKTHROUGH.md](docs/WALKTHROUGH.md), formatted for
scanning quickly while presenting or following along. About 10 minutes
end to end.

## Before starting

- [ ] **Fork the repo, then clone your fork** — not this one. Cloning
      `alfredorueda/ci-demo-java` directly won't let you push branches or
      open pull requests, since you don't have write access to it:
      1. On GitHub, go to `https://github.com/alfredorueda/ci-demo-java`
         and click **Fork** (top-right).
      2. Clone *your* fork, replacing `<your-username>`:
         ```bash
         git clone https://github.com/<your-username>/ci-demo-java.git
         cd ci-demo-java
         ```
      Every command below runs inside your fork.
- [ ] On `main`, terminal open, the repository's **Actions** tab open in a
      browser tab.
- [ ] Confirm the baseline:
  ```bash
  git checkout main
  git pull
  ./mvnw clean test
  ```
  Expected: `Tests run: 15, Failures: 0, Errors: 0`.

## 1. Baseline (~1 min)

```bash
./mvnw clean test
```

`Tests run: 15, Failures: 0` in well under a second — no Docker, no
database, no network call. That's what makes the rest of this run fast.

## 2. Branch, then break the build on purpose (~2 min)

```bash
git checkout -b break-the-build
```

In `src/main/java/com/example/portfoliodemo/Portfolio.java`, inside
`buy(...)`, comment out:

```java
cash = cash.subtract(cost);
```

```bash
./mvnw clean test
```

Expected: `Tests run: 15, Failures: 2` —
`PortfolioTest.buyDeductsCashBalance` and
`PortfolioTest.sellIncreasesCashBalance`. One bug, two broken behaviors:
this is why a suite covers more than one scenario.

## 3. Push and open a pull request (~2 min)

```bash
git add -A
git commit -m "Introduce a bug on purpose for the CI demo"
git push -u origin break-the-build
gh pr create --fill
```

(No `gh`? Use the "Compare & pull request" banner GitHub shows on the
repository page after the push.)

## 4. Watch the check fail (~2 min)

- Refresh the PR page: the check goes yellow (queued/running) → red, in
  about 15–30 seconds (a JVM build takes a little longer to spin up than
  an interpreted one).
- **Details** on the failed check → same test output as local.
- **Merge** button: greyed out — *"Merging is blocked — Required statuses
  must pass before merging."* Applies to everyone, including repository
  admins — that's the branch protection rule on `main`.

## 5. Fix it and watch it go green (~2 min)

Uncomment the line:

```java
cash = cash.subtract(cost);
```

```bash
./mvnw clean test    # confirm 15 tests pass locally first
git add -A
git commit -m "Fix the cash balance bug"
git push
```

Refresh the PR: check turns green → **Merge** button active → merge
(`Squash and merge` is a fine default).

## 6. Recap (~1–2 min)

- This whole loop took minutes only because the suite is fast and
  infrastructure-free — a slow one trains people to stop waiting for it,
  which is how bugs like this reach `main` on real projects.
- Branch protection turns "please run the tests before merging" into "you
  cannot merge until they pass" — enforced, not requested.
- The entire pipeline is `.github/workflows/ci.yml` — versioned and
  reviewed like any other code.

## If something stalls

- Actions runs typically take 20–40s to go from "queued" to a result for a
  JVM project (downloading dependencies, starting the JVM) — that gap is
  normal, not a failure.
- If a local test result looks stale right after an edit (still red after
  a fix, or still green after breaking something), run `./mvnw clean test`
  instead of `./mvnw test` — Maven can occasionally reuse a compiled class
  from just before the edit. This doesn't happen on GitHub Actions, which
  always builds from a clean checkout.
- Branch protection can be turned off from **Settings → Branches** if
  `main` needs to be unblocked for any reason.

---

For every step explained in full, with diagrams, see
[docs/WALKTHROUGH.md](docs/WALKTHROUGH.md). To repeat this same exercise
on your own fork afterwards, see its
["Try it yourself"](docs/WALKTHROUGH.md#6-try-it-yourself) section.
