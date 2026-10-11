package apiAutomation.utilities;

import java.util.Locale;

public abstract class BaseClient {

    protected final ConfigurationManager config;

    protected BaseClient() {
        this.config = ConfigurationManager.getInstance(profile());
    }

    protected ConfigurationProfile profile() {
        return ConfigurationProfile.DEFAULT;
    }

    /**
     * Creates a {@link RequestGenerator} pre-configured with the profile-specific base URI.
     */
    protected RequestGenerator request() {
        String baseURIKey = profile().name().toLowerCase(Locale.ROOT) + ".baseUri";
        String baseURI = config.getProperty(baseURIKey);
        return RequestGenerator.generateRequest(baseURI);
    }

}

