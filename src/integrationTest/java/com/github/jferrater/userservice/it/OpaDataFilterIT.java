package com.github.jferrater.userservice.it;

import com.github.jferrater.userservice.model.UserDto;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
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
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integrationTest")
public class OpaDataFilterIT {

    private static final String DOCKER_COMPOSE_YML = "src/integrationTest/resources/docker-compose.yml";
    private static final String OPA = "opa-server_1";
    private static final int OPA_PORT = 8181;
    private static final String POLICY_ENDPOINT = "/v1/policies";
    private static final String MONGO_DB = "mongo-database_1";
    private static final int MONGO_DB_PORT = 27017;

    @ClassRule
    public static DockerComposeContainer environment = new DockerComposeContainer(new File(DOCKER_COMPOSE_YML))
            .withExposedService(MONGO_DB, MONGO_DB_PORT, Wait.forListeningPort())
            .withExposedService(OPA, OPA_PORT, Wait.forHttp(POLICY_ENDPOINT)
                    .forStatusCode(200))
            .withLocalCompose(true);

    @BeforeAll
    public static void start() {
        environment.start();
    }

    @AfterAll
    public static void stop() {
        environment.stop();
    }

    @Test
    void userCanViewTheirOwnInfo() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Basic Ym9iOnBhc3N3b3Jk"); //The user is bob
        httpHeaders.set("X-ORG-HEADER", "SOMA"); //The name of the clinic is in the X-ORG-HEADER
        UserDto[] results = get(httpHeaders);
        assertThat(results.length, is(1));

        UserDto result = results[0];

        assertThat(result, is(notNullValue()));
    }

    private static UserDto[] get(HttpHeaders httpHeaders) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<UserDto[]> responseEntity = restTemplate.exchange("http://localhost:8082/users", HttpMethod.GET, httpEntity, UserDto[].class);
        assertThat(responseEntity, is(notNullValue()));
        assertThat(responseEntity.getStatusCodeValue(), is(200));
        return responseEntity.getBody();
    }
}
