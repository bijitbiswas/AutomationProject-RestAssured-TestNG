package apiAutomation.framework.client;

import apiAutomation.framework.endpoints.JiraEndpoints;
import apiAutomation.framework.models.jiraModels.request.CreateIssueRequest;
import apiAutomation.framework.models.jiraModels.response.CreateIssueResponse;
import apiAutomation.utilities.BaseClient;
import apiAutomation.utilities.ConfigurationProfile;
import apiAutomation.utilities.RequestGenerator;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulates calls to the Jira REST API used in the test suite.
 */
public class JiraClient extends BaseClient {

    @Override
    protected ConfigurationProfile profile() {
        return ConfigurationProfile.JIRA;
    }

    public CreateIssueResponse createIssue(CreateIssueRequest requestBody) {
        RequestGenerator generator = request()
                .withBasePath(JiraEndpoints.BASE_PATH)
                .withHeaders(defaultHeaders())
                .withBody(requestBody)
                .postRequest(JiraEndpoints.CREATE_ISSUE);

        generator.verifyStatusCode(201);
        return generator.as(CreateIssueResponse.class);
    }

    public void deleteIssue(String issueId) {
        request()
                .withBasePath(JiraEndpoints.BASE_PATH)
                .withHeaders(defaultHeaders())
                .withPathParam("issueId", issueId)
                .deleteRequest(JiraEndpoints.ISSUE_BY_ID)
                .verifyStatusCode(204);
    }

    private Map<String, Object> defaultHeaders() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "Basic " + getJiraBasicAuthToken());
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        return headers;
    }

    public String getJiraBasicAuthToken() {
        String email = config.getProperty("jira.userEmail");
        String apiToken = config.getProperty("jira.apiToken");
        String concatenated = email + ":" + apiToken;
        return Base64.getEncoder().encodeToString(concatenated.getBytes(StandardCharsets.UTF_8));
    }
}

