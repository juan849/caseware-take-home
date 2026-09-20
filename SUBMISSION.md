# Submission Notes

## Assumptions Made

* An Engagement stores the `templateId` and the template version currently in use.
* The latest published template version can be retrieved without loading the full Engagement File.
* The system can obtain a diff directly between the engagement's current version and the latest published version.
* When multiple template versions have accumulated, the direct diff from the current version to the latest version represents the changes that need to be reviewed.
* The raw technical diff remains the source data and is transformed into a human-readable representation on the server.
* Applying or declining a template update is outside the scope of this exercise. The solution only initiates these actions from the client.
* `DECLINED` is treated as a traceable state; the exact reconsideration behavior would be confirmed with the product requirements.

## AI Usage

### Where AI helped

AI was used as a development and reasoning assistant throughout the exercise. It helped explore the domain model, architecture options, accumulated template versions, failure modes, testing scenarios, and the client/server contract.

It also helped review implementation details and identify opportunities to keep the solution within the exercise scope, particularly around avoiding full Engagement File loading and keeping the human-readable transformation on the server.

### Where I corrected, rewrote, or ignored AI output

I reviewed the proposed solutions against the exercise requirements and made the final architectural decisions.

In particular, I rejected unnecessary framework, persistence, and infrastructure code because the exercise asks for representative core logic rather than a complete production service. I also chose a direct current-version-to-latest-version diff for accumulated updates and kept the raw diff separate from the human-readable representation.

### How I would guide other engineers using AI on this system

AI should be used as an assistant for exploration, implementation support, test generation, and review, but engineers remain responsible for validating the output.

I would require engineers to provide the relevant domain constraints to the AI, verify generated code with tests and documentation, review architectural decisions themselves, and avoid accepting generated assumptions that are not supported by the product requirements.

### Where AI should not be trusted in this domain

AI should not be trusted to make accounting, audit, compliance, or regulatory interpretations on behalf of practitioners.

It should also not be trusted to determine whether a template change is professionally appropriate, invent business rules, make decisions about applying updates to customer Engagement Files, or process sensitive customer information without appropriate controls and human review.

## Approximate Time Spent

Approximately 3 hours 30 minutes.

## What I Would Do Next

* Connect the Angular client to the production API.
* Implement persistence and the actual Apply/Decline workflow.
* Add integration and contract tests between the client and server.
* Introduce an event-driven or read-model approach if the volume of engagements requires faster update visibility at scale.
* Expand the human-readable change transformation for additional template diff operations.
* Add production monitoring, tracing, and operational alerts around update computation and availability.
