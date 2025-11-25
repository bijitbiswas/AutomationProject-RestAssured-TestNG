package apiAutomation.framework.client;

import apiAutomation.utilities.BaseClient;
import apiAutomation.utilities.ConfigurationProfile;
import io.restassured.http.ContentType;

/**
 * Encapsulates OAuth API interactions.
 */
public class OAuthClient extends BaseClient {

    @Override
    protected ConfigurationProfile profile() {
        return ConfigurationProfile.OAUTH;
    }

    public String requestAccessToken() {
        return request()
                .withContentType(ContentType.URLENC)
                .withFormParam("client_id", config.getProperty("oauth.clientId"))
                .withFormParam("client_secret", config.getProperty("oauth.clientSecret"))
                .withFormParam("grant_type", "client_credentials")
                .withFormParam("scope", config.getProperty("oauth.scope"))
                .postRequest("/oauthapi/oauth2/resourceOwner/token")
                .verifyStatusCode(200)
                .getJsonPath()
                .getString("access_token");
    }

    public void verifyProtectedResourceAccessDenied(String accessToken) {
        request()
                .withQueryParam("access_token", accessToken)
                .getRequest("/oauthapi/getCourseDetails")
                .verifyStatusCode(401);
    }
}


