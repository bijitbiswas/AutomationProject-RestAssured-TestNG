package apiAutomation.utilities;

/**
 * Distinguishes configuration sets for different API domains.
 */
public enum ConfigurationProfile {
    DEFAULT("config/defaultConfiguration.properties"),
    JIRA("config/jiraConfiguration.properties"),
    OAUTH("config/oAuthConfiguration.properties");

    private final String resourcePath;

    ConfigurationProfile(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public String getResourcePath() {
        return resourcePath;
    }
}


