# Design

## 1. High-Level Architecture

```mermaid
flowchart TD
    A[Engagement] --> B[PendingUpdateService]
    C[TemplateRepository] --> B
    B --> D[TemplateDiffProvider]
    D --> E[TemplateChange]
    E --> F[HumanReadableChange]
    F --> G[PendingUpdate]
    G --> H[API]
    H --> I[Angular Client]
    I --> J[Review Update]
    I --> K[Apply / Decline]

The solution separates update detection, diff transformation, and client presentation.

An Engagement stores the `templateId` and the template version currently used by the engagement. The latest published version is retrieved through the template repository. The Pending Update Service compares the current version with the latest version and, when a newer version exists, requests a direct diff between those versions.

The raw technical diff is transformed on the server into a human-readable change summary. The Angular client consumes this representation and is responsible only for displaying the update state and allowing the user to initiate Apply or Decline.

The full Engagement File is not loaded to detect pending updates because loading it can take approximately one minute. Update detection should therefore rely on the lightweight engagement metadata and template version information.

### Client / Server Contract

A conceptual endpoint for retrieving the update state is:

`GET /engagements/{engagementId}/template-update`

Example response:

```json
{
  "engagementId": "ENG-001",
  "engagementName": "Review 2026",
  "templateId": "REVIEW-CA",
  "currentVersion": 6,
  "latestVersion": 8,
  "status": "PENDING",
  "changes": [
    {
      "type": "MODIFIED",
      "message": "The template display name was updated."
    },
    {
      "type": "ADDED",
      "message": "A new inquiry question was added."
    },
    {
      "type": "MODIFIED",
      "message": "Tolerance changed from 0.15 to 0.10."
    },
    {
      "type": "REMOVED",
      "message": "Help text was removed from a question."
    },
    {
      "type": "ADDED",
      "message": "A going-concern checklist was added."
    }
  ]
}
```

The update status can be `UP_TO_DATE`, `PENDING`, `COMPUTING`, `UNAVAILABLE`, or `DECLINED`. `COMPUTING` and `UNAVAILABLE` must not be interpreted by the client as `UP_TO_DATE`.

Apply and Decline can be represented by separate actions such as:

`POST /engagements/{engagementId}/template-update/apply`

`POST /engagements/{engagementId}/template-update/decline`

The actual application of template content is outside the scope of this exercise.

### Human-Readable Change Summary

The raw template diff is transformed on the server into a human-readable representation. This keeps JSON diff semantics and domain interpretation out of the Angular client and provides a stable client-facing contract.

The raw diff remains the source representation; the human-readable summary is a separate projection rather than an overwrite of the original diff.

For accumulated updates, the implementation requests a direct diff from the engagement's current version to the latest published version. This provides the complete set of pending changes without requiring the client or service to process each intermediate version.

## 2. Implementation Plan

1. Model the engagement, template versions, raw changes, update status, and human-readable changes.
2. Retrieve the latest published template version and compare it with the engagement's current version.
3. Request a direct diff from the current version to the latest version when an update exists.
4. Transform the raw diff into human-readable changes on the server.
5. Expose the resulting update state through the client/server contract.
6. Display the state and change summary in Angular and provide Apply/Decline actions.
7. Add focused tests and production observability around update detection and diff processing.

## 3. Testing Strategy

The most important scenarios are:

* An engagement already using the latest version returns `UP_TO_DATE`.
* An engagement using an older version returns `PENDING` with a human-readable summary.
* An engagement several versions behind requests a direct diff from its current version to the latest version.
* `COMPUTING` and `UNAVAILABLE` states are represented without incorrectly reporting the engagement as up to date.
* Invalid version relationships, such as a latest version lower than the current version, are treated as an inconsistency.

The representative implementation includes focused Java tests for the up-to-date case, a pending update, and accumulated versions.

## 4. Evaluation & Observability

The system should provide metrics for the number of engagements in each update state, diff computation latency, diff failures, and the time between template publication and update visibility.

Structured logs should include identifiers such as `engagementId`, `templateId`, `currentVersion`, `latestVersion`, and the resulting status. Logs should avoid including sensitive Engagement File content.

For production, traces or correlation IDs can be used to follow update detection and diff processing across services.

## 5. Failure Modes & Tradeoffs

The main constraint is the slow Engagement File load. The design avoids loading the full file merely to determine whether a template update exists.

Diff generation may be temporarily unavailable or still being computed. These conditions are represented explicitly as `COMPUTING` or `UNAVAILABLE` so the client does not confuse missing information with an up-to-date engagement.

A direct current-to-latest diff is preferred over chaining every intermediate diff because the exercise provides the ability to compare two template versions and the requirement is to present accumulated pending changes. If exact version-by-version history becomes a product requirement, intermediate diffs would need to be retained and processed.

`DECLINED` is treated as a traceable user decision. The exact behavior for reconsidering a declined version should be confirmed with the product requirements.

A lightweight read model or event-driven refresh could be introduced later if update visibility needs to scale further, but it is not required for this representative implementation.
