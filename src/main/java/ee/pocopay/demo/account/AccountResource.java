package ee.pocopay.demo.account;

import ee.pocopay.demo.account.model.Account;
import ee.pocopay.demo.transaction.TransactionService;
import ee.pocopay.demo.transaction.model.Transaction;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("accounts")
@RestController
public class AccountResource {

    private AccountService accountService;

    public AccountResource(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @PostMapping
    public Account addAccount(@RequestBody Account account) {
        return accountService.addAccount(account);
    }
}
