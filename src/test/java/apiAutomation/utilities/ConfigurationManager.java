package apiAutomation.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

/**
 * Centralised configuration loader for the API test framework.
 * <p>
 * Resolution order for a configuration key is:
 * <ol>
 *     <li>Explicit JVM system properties (e.g. {@code -Djira.baseUri=https://...})</li>
 *     <li>Environment variables (e.g. {@code JIRA_BASEURI=https://...})</li>
 *     <li>Profile specific property files located under {@code src/test/resources/config}</li>
 * </ol>
 *
 * Secrets such as API tokens should be provided through environment variables
 * or system properties rather than committed to the repository.
 */
public final class ConfigurationManager {

    private static final Map<ConfigurationProfile, ConfigurationManager> INSTANCES =
            new EnumMap<>(ConfigurationProfile.class);

    private final ConfigurationProfile profile;
    private final Properties properties = new Properties();

   private ConfigurationManager(ConfigurationProfile profile) {
          this.profile = profile;
          loadResource(ConfigurationProfile.DEFAULT.getResourcePath());
          if (profile != ConfigurationProfile.DEFAULT) {
              loadResource(profile.getResourcePath());
          }
      }

    public static synchronized ConfigurationManager getInstance() {
        return getInstance(ConfigurationProfile.DEFAULT);
    }

    public static synchronized ConfigurationManager getInstance(ConfigurationProfile profile) {
        return INSTANCES.computeIfAbsent(
                Objects.requireNonNull(profile, "CONFIGURATION PROFILE IS NULL"),
                ConfigurationManager::new
        );
    }

    public boolean isLoggingEnabled() {
        return Boolean.parseBoolean(getPropertyAsOption("api.log.all").orElse("false"));
    }

    public String getProperty(String key) {
        return getPropertyAsOption(key)
                .orElseThrow(() -> new IllegalStateException(
                        "Missing value for key: " + key + " in : " + profile.getResourcePath()));
    }

    private Optional<String> getPropertyAsOption(String key) {
        Objects.requireNonNull(key, "key");

        String sysValue = System.getProperty(key);
        if (isPresent(sysValue)) {
            return Optional.of(sysValue.trim());
        }

        String envValue = System.getenv(toEnvKey(key));
        if (isPresent(envValue)) {
            return Optional.of(envValue.trim());
        }

        String fileValue = properties.getProperty(key);
        if (isPresent(fileValue)) {
            return Optional.of(fileValue.trim());
        }
        return Optional.empty();
    }

    private void loadResource(String resourcePath) {
        try (InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(resourcePath)) {

            if (inputStream == null) {
                throw new IllegalStateException("Unable to locate configuration file: " + resourcePath);
            }
            Properties loaded = new Properties();
            loaded.load(inputStream);
            properties.putAll(loaded);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to load framework configuration from " + resourcePath, ex);
        }
    }

    private String toEnvKey(String key) {
        return key.replace('.', '_').toUpperCase(Locale.ROOT);
    }

    private boolean isPresent(String value) {
        return value != null && !value.trim().isEmpty();
    }
}

