package ee.pocopay.demo.account;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql(scripts = {"/sql/cleanup.sql", "/sql/init.sql"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountResourceTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String ACCOUNT_NAME = "acc3";

    @Test
    public void getAllAccounts() throws JSONException {
        var response = restTemplate.getForEntity("/accounts", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        var dbCount = jdbcTemplate.queryForObject("select count(*) from account", Integer.class);

        JSONArray accounts = new JSONArray(response.getBody());

        assertEquals(dbCount, accounts.length());
    }

    @Test
    public void addAccountFail() throws JSONException {
        var json = new JSONObject();

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var response = restTemplate.exchange("/accounts", HttpMethod.POST, new HttpEntity<>(json.toString(), headers), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        var responseBody = new JSONObject(response.getBody());
        assertEquals("Name must be provided", responseBody.getString("message"));

        json.put("name", ACCOUNT_NAME);

        response = restTemplate.exchange("/accounts", HttpMethod.POST, new HttpEntity<>(json.toString(), headers), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        responseBody = new JSONObject(response.getBody());
        assertEquals("A positive balance must be provided", responseBody.getString("message"));
    }

    @Test
    public void addAccountSuccess() throws JSONException {
        var dbCount = jdbcTemplate.queryForObject("select count(*) from account where name = ?", Integer.class, ACCOUNT_NAME);
        assertEquals(0, dbCount);

        var json = new JSONObject();
        json.put("name", ACCOUNT_NAME);
        json.put("balance", 200.55);

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var response = restTemplate.exchange("/accounts", HttpMethod.POST, new HttpEntity<>(json.toString(), headers), String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        var responseBody = new JSONObject(response.getBody());
        assertEquals(ACCOUNT_NAME, responseBody.get("name"));

        dbCount = jdbcTemplate.queryForObject("select count(*) from account where name = ?", Integer.class, ACCOUNT_NAME);
        assertEquals(1, dbCount);
    }
}
