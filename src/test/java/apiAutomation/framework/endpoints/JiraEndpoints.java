package apiAutomation.framework.endpoints;

public final class JiraEndpoints {

    public static final String CREATE_ISSUE = "/issue";

    private JiraEndpoints() {
        // Utility class
    }

    public static String issueById(String issueId) {
        return CREATE_ISSUE + "/" + issueId;
    }
}

