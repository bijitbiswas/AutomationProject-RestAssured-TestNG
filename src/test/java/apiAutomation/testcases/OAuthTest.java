package apiAutomation.testcases;

import apiAutomation.framework.client.OAuthClient;
import org.testng.Assert;
import org.testng.annotations.Test;

public class OAuthTest {

    private OAuthClient oauthClient;
    private String accessToken;

    @Test
    public void shouldRetrieveAccessToken() {
        oauthClient = new OAuthClient();

        accessToken = oauthClient.requestAccessToken();
        Assert.assertNotNull(accessToken, "Access token should be returned");
    }

    @Test(dependsOnMethods = "shouldRetrieveAccessToken")
    public void shouldHandleProtectedResourceAccess() {

        oauthClient.verifyProtectedResourceAccessDenied(accessToken);
    }
}
