package ee.pocopay.demo.transaction;

import ee.pocopay.demo.account.AccountRepository;
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

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql(scripts = {"/sql/cleanup.sql", "/sql/init.sql"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionResourceTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AccountRepository accountRepository;

    private static final String DEBIT_ACCOUNT_NAME = "acc1";
    private static final String CREDIT_ACCOUNT_NAME = "acc2";

    @Test
    public void getAllTransactions() throws JSONException {
        var response = restTemplate.getForEntity("/transactions", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        var dbCount = jdbcTemplate.queryForObject("select count(*) from transaction", Integer.class);

        JSONArray transactions = new JSONArray(response.getBody());

        assertEquals(dbCount, transactions.length());
    }

    @Test
    public void addTransactionSuccess() throws JSONException {
        var debitAccount = accountRepository.findFirstByName(DEBIT_ACCOUNT_NAME);
        var creditAccount = accountRepository.findFirstByName(CREDIT_ACCOUNT_NAME);

        var debitAccountBalance = debitAccount.getBalance();
        var creditAccountBalance = creditAccount.getBalance();
        var accountBalanceSum = debitAccountBalance.add(creditAccountBalance);

        var amount = BigDecimal.valueOf(27.35);
        var json = new JSONObject();
        json.put("debitAccountId", debitAccount.getId());
        json.put("creditAccountId", creditAccount.getId());
        json.put("amount", amount);

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var response = restTemplate.exchange("/transactions", HttpMethod.POST, new HttpEntity<>(json.toString(), headers), String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        debitAccount = accountRepository.findFirstByName(DEBIT_ACCOUNT_NAME);
        creditAccount = accountRepository.findFirstByName(CREDIT_ACCOUNT_NAME);

        assertEquals(debitAccountBalance.add(amount), debitAccount.getBalance());
        assertEquals(creditAccountBalance.subtract(amount), creditAccount.getBalance());
        assertEquals(accountBalanceSum, debitAccount.getBalance().add(creditAccount.getBalance()));
    }
}
