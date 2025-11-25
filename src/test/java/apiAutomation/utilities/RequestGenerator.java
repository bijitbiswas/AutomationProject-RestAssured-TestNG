package apiAutomation.utilities;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Fluent wrapper around RestAssured's {@link RequestSpecification} that applies
 * framework configuration defaults (base URI, logging, content type etc.) and
 * provides chainable helpers for common request customisation.
 */
public class RequestGenerator {

    private static final ConfigurationManager config = ConfigurationManager.getInstance();
    private static final boolean defaultIsLoggingEnabled = config.isLoggingEnabled();
    private static final String defaultBaseURI = config.getBaseUri();
    private RequestSpecification requestSpec;
    private ValidatableResponse validatableResponse;

    private RequestGenerator(RequestSpecification baseSpec) {
        this.requestSpec = RestAssured.given()
                .spec(baseSpec);
    }

    /**
     * Generates a request with the framework base URI from configuration.
     */
    public static RequestGenerator generateRequest() {
        return generateRequest(defaultBaseURI);
    }

    /**
     * Generates a request for the supplied base URI.
     */
    public static RequestGenerator generateRequest(String baseUri) {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setRelaxedHTTPSValidation()
                .setContentType(ContentType.JSON);

        if (defaultIsLoggingEnabled) {
            builder.addFilter(new RequestLoggingFilter(LogDetail.ALL));
            builder.addFilter(new ResponseLoggingFilter(LogDetail.ALL));
        }
        return new RequestGenerator(builder.build());
    }

    public RequestGenerator withBasePath(String basePath) {
        requestSpec = requestSpec.basePath(basePath);
        return this;
    }

    public RequestGenerator withHeaders(Map<String, Object> headers) {
        requestSpec = requestSpec.headers(new HashMap<>(headers));
        return this;
    }

    public RequestGenerator withHeader(String key, Object value) {
        requestSpec = requestSpec.header(key, value);
        return this;
    }

    public RequestGenerator withContentType(ContentType contentType) {
        requestSpec = requestSpec.contentType(contentType);
        return this;
    }

    public RequestGenerator withQueryParam(String key, Object value) {
        requestSpec = requestSpec.queryParam(key, value);
        return this;
    }

    public RequestGenerator withQueryParams(Map<String, ?> params) {
        requestSpec = requestSpec.queryParams(params);
        return this;
    }

    public RequestGenerator withPathParam(String key, Object value) {
        requestSpec = requestSpec.pathParam(key, value);
        return this;
    }

    public RequestGenerator withBody(String body) {
        requestSpec = requestSpec.body(body);
        return this;
    }

    public RequestGenerator withBody(Object body) {
        requestSpec = requestSpec.body(body);
        return this;
    }

    public RequestGenerator withFormParam(String key, Object value) {
        requestSpec = requestSpec.formParam(key, value);
        return this;
    }

    public RequestGenerator withFormParams(Map<String, ?> params) {
        requestSpec = requestSpec.formParams(params);
        return this;
    }

    public RequestGenerator withJsonBodyFromResource(String resourcePath) {
        InputStream stream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(resourcePath);
        if (stream == null) {
            throw new IllegalArgumentException("Unable to find resource: " + resourcePath);
        }
        requestSpec = requestSpec.body(stream);
        return this;
    }

    public RequestGenerator getRequest(String resourcePath) {
        return getRequest(resourcePath, defaultIsLoggingEnabled);
    }

    public RequestGenerator getRequest(String resourcePath, boolean logResponse) {
        execute(resourcePath, Method.GET, logResponse);
        return this;
    }

    public RequestGenerator postRequest(String resourcePath) {
        return postRequest(resourcePath, defaultIsLoggingEnabled);
    }

    public RequestGenerator postRequest(String resourcePath, boolean logResponse) {
        execute(resourcePath, Method.POST, logResponse);
        return this;
    }

    public RequestGenerator deleteRequest(String resourcePath) {
        return deleteRequest(resourcePath, defaultIsLoggingEnabled);
    }

    public RequestGenerator deleteRequest(String resourcePath, boolean logResponse) {
        execute(resourcePath, Method.DELETE, logResponse);
        return this;
    }

    public RequestGenerator putRequest(String resourcePath) {
        return putRequest(resourcePath, defaultIsLoggingEnabled);
    }

    public RequestGenerator putRequest(String resourcePath, boolean logResponse) {
        execute(resourcePath, Method.PUT, logResponse);
        return this;
    }

    public RequestGenerator patchRequest(String resourcePath) {
        return patchRequest(resourcePath, defaultIsLoggingEnabled);
    }

    public RequestGenerator patchRequest(String resourcePath, boolean logResponse) {
        execute(resourcePath, Method.PATCH, logResponse);
        return this;
    }

    public RequestGenerator verifyStatusCode(int statusCode) {
        validatableResponse = validatableResponse
                .log().ifValidationFails()
                .assertThat()
                .statusCode(statusCode);
        return this;
    }

    public Response getResponse() {
        return validatableResponse.extract().response();
    }

    public ValidatableResponse getValidatableResponse() {
        return validatableResponse;
    }

    public JsonPath getJsonPath() {
        return getResponse().jsonPath();
    }

    public <T> T as(Class<T> clazz) {
        return getResponse().as(clazz);
    }

    private void execute(String resourcePath, Method method, boolean logResponse) {
        Response response = switch (method) {
            case GET -> requestSpec.when().get(resourcePath);
            case POST -> requestSpec.when().post(resourcePath);
            case PUT -> requestSpec.when().put(resourcePath);
            case PATCH -> requestSpec.when().patch(resourcePath);
            case DELETE -> requestSpec.when().delete(resourcePath);
        };
        validatableResponse = response.then();
        if (logResponse) {
            validatableResponse.log().all();
        }
    }

    private enum Method {
        GET, POST, PUT, PATCH, DELETE
    }
}
