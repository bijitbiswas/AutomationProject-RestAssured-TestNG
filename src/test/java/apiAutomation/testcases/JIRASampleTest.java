package apiAutomation.testcases;

import apiAutomation.framework.client.JiraClient;
import apiAutomation.framework.dataFactory.JiraDataFactory;
import apiAutomation.framework.models.jiraModels.request.CreateIssueRequest;
import apiAutomation.framework.models.jiraModels.response.CreateIssueResponse;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Demonstrates a readable end-to-end Jira issue workflow using the framework
 * abstractions. Create and delete operations are encapsulated within the
 * {@link JiraClient}, keeping the test focused on behaviour.
 */
public class JIRASampleTest {

    private JiraClient jiraClient;

    @Test
    public void shouldCreateAndDeleteIssue() {
        jiraClient = new JiraClient();

        CreateIssueRequest requestBody = JiraDataFactory.createIssueRequestBody();

        CreateIssueResponse createResponse = jiraClient.createIssue(requestBody);
        Assert.assertNotNull(createResponse.getId(), "Issue id missing in response");
        Assert.assertTrue(createResponse.getKey() != null && !createResponse.getKey().isBlank(),
                "Issue key missing in response");

        System.out.println("Created Jira issue with key: " + createResponse.getKey());

        jiraClient.deleteIssue(createResponse.getId());
    }
}
