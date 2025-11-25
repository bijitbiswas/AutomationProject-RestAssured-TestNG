# API Automation Framework (Rest Assured + TestNG)

This project is a reference implementation of a maintainable API testing framework
using Rest Assured and TestNG. The goal is to keep tests highly readable while
centralising configuration, data management, and HTTP client logic.

## Project Layout

- `src/test/java/apiAutomation/config` – environment configuration loader.
- `src/test/java/apiAutomation/testbase` – base test class with common setup.
- `src/test/java/apiAutomation/clients` – service specific API clients.
- `src/test/java/apiAutomation/datafactory` – reusable test data builders.
- `src/test/java/apiAutomation/utilities` – shared helpers (request builder, random data, JSON loader).
- `src/test/resources/config` – environment-specific settings.
- `src/test/resources/data` – structured test data templates.

## Getting Started

1. **Install dependencies**

   ```bash
   mvn clean test -DskipTests
   ```

2. **Configure environment variables or override properties**

   Create/Update `src/test/resources/config/config.properties` or set the
   following environment variables (preferred for secrets):

   - `JIRA_BASEURI`
   - `JIRA_BASEPATH`
   - `JIRA_USEREMAIL`
   - `JIRA_APITOKEN`
   - `JIRA_BASICAUTHTOKEN` (optional – overrides email/token combination)
   - `OAUTH_BASEURI`
   - `OAUTH_CLIENTID`
   - `OAUTH_CLIENTSECRET`

   Any values missing at runtime will raise a clear error message.

3. **Execute tests**

   ```bash
   mvn clean test
   ```

## Extending the Framework

- Add new API clients under `clients/` to encapsulate endpoint logic.
- Reuse `RequestGenerator` to build readable flows with sensible defaults.
- Store complex payload templates under `src/test/resources/data` and load them
  via `TestDataLoader`.
- Create data factories for scenario-specific payload variations.

## Logging

Set `api.log.all=true` in `config.properties` (or `API_LOG_ALL=true` env var) to
enable request/response logging for every call. Regardless of this flag, full
payloads are logged automatically when an assertion fails.

