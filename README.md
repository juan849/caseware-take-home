# Caseware Take-Home Exercise

Implementation of the Caseware template update review exercise.

## Contents

* `DESIGN.md` — architecture, API contract, implementation approach, testing strategy, observability, and tradeoffs.
* `SUBMISSION.md` — assumptions, AI usage, time spent, and next steps.
* `java/` — Java domain model and core pending-update logic with representative tests.
* `caseware-client/` — Angular client displaying pending template updates and human-readable changes.

## Running the Java tests

From the `java` directory:

```bash
mvn test
```

## Running the Angular client

From the `caseware-client/` directory:

```bash
cd caseware-client
npm install
ng serve
```

Then open the local URL shown by Angular in the terminal.

## Scope

The implementation focuses on detecting pending template updates, handling accumulated template versions, and presenting human-readable changes to the user.

Actual template application and persistence of Apply/Decline actions are outside the scope of this exercise.
