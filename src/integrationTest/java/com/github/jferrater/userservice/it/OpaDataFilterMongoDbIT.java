package com.github.jferrater.userservice.it;

import com.github.jferrater.userservice.model.UserDto;
import org.junit.ClassRule;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author joffryferrater
 */
public class OpaDataFilterMongoDbIT {

    private static final String DOCKER_COMPOSE_YML = "./docker-compose.yml";
    private static final String OPA = "opa-server_1";
    private static final int OPA_PORT = 8181;
    private static final String MONGO_DB = "mongo-database_1";
    private static final int MONGO_DB_PORT = 27017;
    private static final String USER_SERVICE = "user-service_1";
    private static final int USER_SERVICE_PORT = 8082;
    private static final String USER_SERVICE_CONTEXT = "http://localhost:8082/user-service";

    private RestTemplate restTemplate;

    @ClassRule
    public static DockerComposeContainer environment = new DockerComposeContainer(new File(DOCKER_COMPOSE_YML))
            .withExposedService(MONGO_DB, MONGO_DB_PORT, Wait.forListeningPort())
            .withExposedService(OPA, OPA_PORT, Wait.forListeningPort())
            .withExposedService(USER_SERVICE, USER_SERVICE_PORT, Wait.forHttp("/user-service/ping").forStatusCode(200))
            .withLocalCompose(true);

    @BeforeAll
    public static void start() {
        environment.start();
    }

    @AfterAll
    public static void stop() {
        environment.stop();
    }

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
        request("/init", HttpMethod.POST);
    }

    @AfterEach
    void cleanUp() {
        request("/users", HttpMethod.DELETE);
    }

    @Test
    void alexShouldBeAbleToSeeUsersHeManagedInSOMA() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Basic YWxleDpwYXNzd29yZDEyMw=="); //The user is alex, the manager from SOMA Clinic
        httpHeaders.set("X-ORG-HEADER", "SOMA"); //The name of the clinic is in the X-ORG-HEADER
        UserDto[] results = get(httpHeaders);

        assertThat(results.length, is(4));
    }

    @Test
    void lenaShouldBeAbleToSeeUsersSheManagedInVETE() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Basic bGVuYTpwYXNzd29yZDEyMw=="); //The user is alex, the manager from SOMA Clinic
        httpHeaders.set("X-ORG-HEADER", "VETE"); //The name of the clinic is in the X-ORG-HEADER
        UserDto[] results = get(httpHeaders);

        assertThat(results.length, is(3));
    }

    @Test
    void userShouldBeAbleToSeeHerOwnInfo() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Basic d2VsbGE6cGFzc3dvcmQxMjM="); //The user is wella from VETE clinic
        httpHeaders.set("X-ORG-HEADER", "VETE"); //The name of the clinic is in the X-ORG-HEADER
        UserDto[] results = get(httpHeaders);

        assertThat(results.length, is(1));
    }

    @Test
    void shouldExpectEmptyIfUserTriesToAccessADifferentOrganization() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Basic YWxleDpwYXNzd29yZDEyMw=="); //The user is alex, the manager from SOMA Clinic
        httpHeaders.set("X-ORG-HEADER", "VETE"); //The name of the clinic is in the X-ORG-HEADER
        UserDto[] results = get(httpHeaders);

        assertThat(results.length, is(0));
    }

    private UserDto[] get(HttpHeaders httpHeaders) {
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<UserDto[]> responseEntity = restTemplate.exchange(USER_SERVICE_CONTEXT + "/users", HttpMethod.GET, httpEntity, UserDto[].class);
        assertThat(responseEntity, is(notNullValue()));
        assertThat(responseEntity.getStatusCodeValue(), is(200));
        return responseEntity.getBody();
    }


    private void request(String path, HttpMethod httpMethod) {
        ResponseEntity<Void> responseEntity = restTemplate.exchange(USER_SERVICE_CONTEXT + path, httpMethod, null, Void.class);
        assertThat(responseEntity.getStatusCodeValue(), is(200));
    }
}
