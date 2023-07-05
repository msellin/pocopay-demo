package ee.pocopay.demo.account;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

//@Sql("/sql/init.sql")
//@Sql(scripts = "/sql/cleanup.sql", executionPhase = AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountResourceTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void getAllAccounts() throws JSONException {
        var response = restTemplate.getForEntity("/accounts", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        var dbCount = jdbcTemplate.queryForObject("select count(*) from account", Integer.class);

        JSONArray accounts = new JSONArray(response.getBody());

        assertEquals(dbCount, accounts.length());
    }

    @Test
    @Disabled
    public void addAccount() throws JSONException {
        var json = new JSONObject();

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var response = restTemplate.exchange("/accounts", HttpMethod.POST, new HttpEntity<>(json.toString(), headers), String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*
        var dbCount = jdbcTemplate.queryForObject("select count(*) from account", Integer.class);

        JSONArray accounts = new JSONArray(response.getBody());

        assertEquals(dbCount, accounts.length());

         */
    }
}
