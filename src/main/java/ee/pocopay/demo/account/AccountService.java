package ee.pocopay.demo.account;

import ee.pocopay.demo.account.model.Account;
import ee.pocopay.demo.config.MdcUtil;
import ee.pocopay.demo.config.PocopayException;
import ee.pocopay.demo.transaction.model.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class AccountService {

    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account addAccount(Account account) {
        account.validate();
        account.setCreatedSessionId(MdcUtil.getSessionId());
        account.setModifiedSessionId(MdcUtil.getSessionId());
        return accountRepository.save(account);
    }

    public void checkAccountBalances(Transaction transaction) {
        if(transaction.getDebitAccountId() == null || transaction.getCreditAccountId() == null) {
            throw new PocopayException("Both debit and credit account ids must be provided");
        }

        var creditAccount = accountRepository.findById(transaction.getCreditAccountId()).orElse(null);
        if(creditAccount == null) {
            throw new PocopayException("Credit account not found", HttpStatus.NOT_FOUND);
        }
        if(creditAccount.getBalance().compareTo(transaction.getAmount()) < 0) {
            throw new PocopayException("Credit account balance is too low");
        }

        var debitAccount = accountRepository.findById(transaction.getDebitAccountId()).orElse(null);
        if(debitAccount == null) {
            throw new PocopayException("Debit account not found", HttpStatus.NOT_FOUND);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateAccountBalances(Transaction transaction) {
        var debitAccount = accountRepository.findById(transaction.getDebitAccountId()).orElse(null);
        var creditAccount = accountRepository.findById(transaction.getCreditAccountId()).orElse(null);

        if(debitAccount != null && creditAccount != null) {
            var sessionId = MdcUtil.getSessionId();
            debitAccount.setBalance(debitAccount.getBalance().add(transaction.getAmount()));
            debitAccount.setModifiedSessionId(sessionId);
            accountRepository.save(debitAccount);

            creditAccount.setBalance(creditAccount.getBalance().subtract(transaction.getAmount()));
            creditAccount.setModifiedSessionId(sessionId);
            accountRepository.save(creditAccount);
        }
    }
}
