# API Automation Framework (Rest Assured + TestNG)

This repository provides a ready-to-use API test automation framework built with Rest Assured and TestNG. It balances readability, maintainability, and speed so teams can focus on modelling API scenarios rather than framework plumbing.

## Key Features
- **Layered design** separating configuration, clients, data, and assertions for clean test code.
- **Re-usable HTTP clients** that centralise authentication, headers, and request construction.
- **Configurable environments** via property files or environment variables for quick context switches.
- **Data factories & templates** to build dynamic payloads without duplication.
- **Pluggable utilities** for logging, reporting, and request/response enrichment.

## Prerequisites
- Java 11 or newer
- Maven 3.8+
- An IDE with TestNG support (IntelliJ IDEA recommended)
- Network access to the APIs under test

## Quick Start
1. **Clone the project**
   ```bash
   git clone <repository-url>
   cd AutomationProject-RestAssured-TestNG
   ```
2. **Install dependencies**
   ```bash
   mvn clean test -DskipTests
   ```
3. **Configure environments**
   - Update `src/test/resources/config/config.properties` with default values, or
   - Provide overrides via environment variables (preferred for secrets).
4. **Run all tests**
   ```bash
   mvn clean test
   ```

## Project Structure
- `src/test/java/apiAutomation/config` – configuration loaders + property management.
- `src/test/java/apiAutomation/testbase` – shared TestNG hooks and request specification builders.
- `src/test/java/apiAutomation/clients` – service-specific Rest Assured clients encapsulating endpoints.
- `src/test/java/apiAutomation/datafactory` – builders for dynamic payloads and DTO generators.
- `src/test/java/apiAutomation/utilities` – common helpers (request generator, JSON utilities, random data).
- `src/test/resources/config` – environment specific property files.
- `src/test/resources/data` – reusable payload templates.

## Configuration
- Create a new `.properties` file under `src/test/resources/config` as per API collections or envs.
- Mention the `.properties` location in `ConfigurationProfile` as enum values.
- Use `config.getProperty('<propertyName>)` in DataFactory classes by calling `ConfigurationManager.getInstance(ConfigurationProfile.JIRA);` 
- Use `config.getProperty('<propertyName>)` in Client classes by overriding the `profile()` method from BaseClient class.
- Default settings of `api.log.all` and `api.baseUri` is controlled from `defaultConfiguration.prperties`.

## Writing Your First API Test
1. **Create a client** in `clients/` (or reuse an existing one):
   - Define endpoint paths, authentication, and query parameters.
   - Return typed responses or `ValidatableResponse` for assertions.
2. **Author the test class** in `src/test/java/.../testcases` :
   - Inject the client.
   - Compose requests using builders or data factories.
   - Assert status codes, headers, and payload content with TestNG.
3. **Add test data** in the data factory or resources folder to keep test logic lean.

```java
@Test
public void shouldCreateTicket() {
    TicketRequest request = TicketDataFactory.defaultTicket();
    ValidatableResponse response = jiraClient.createTicket(request);
    response.statusCode(201)
            .body("key", notNullValue());
}
```

## Test Data & Utilities
- Store static payload templates under `src/test/resources/data` to reuse across suites.
- Use data factories for scenario-specific variations and edge cases.
- Utilities such as `RequestGenerator` and `TestDataLoader` standardise request creation and data access.

## Logging & Reporting
- Enable verbose logging by setting `api.log.all=true` in properties or `API_LOG_ALL=true` as an environment variable.
- Failures automatically output the request and response to aid debugging.
- Integrate with TestNG listeners or Allure for richer reports if required.

## Continuous Integration Tips
- Add `mvn clean test` to your CI pipeline with the correct environment profile.
- Externalise secrets via CI variables; never commit credentials.
- Run smoke tests on every pull request and schedule full regression suites nightly.

## Helpful Commands
- `mvn clean test -Dgroups=smoke` – run tagged smoke tests.
- `mvn surefire-report:report-only` – generate TestNG HTML reports.
- `mvn dependency:tree` – inspect library dependency graph.

You now have everything you need to start crafting reliable, maintainable API tests quickly. Happy testing!

