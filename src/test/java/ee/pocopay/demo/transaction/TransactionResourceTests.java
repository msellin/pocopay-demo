package ee.pocopay.demo.transaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@Sql("/sql/init.sql")
//@Sql(scripts = "/sql/cleanup.sql", executionPhase = AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionResourceTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void getAllTransactions() throws JSONException {
        var response = restTemplate.getForEntity("/transactions", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        var dbCount = jdbcTemplate.queryForObject("select count(*) from transaction", Integer.class);

        JSONArray transactions = new JSONArray(response.getBody());

        assertEquals(dbCount, transactions.length());
    }
}
