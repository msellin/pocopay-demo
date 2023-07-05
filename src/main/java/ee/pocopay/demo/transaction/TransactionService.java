package ee.pocopay.demo.transaction;

import ee.pocopay.demo.account.AccountService;
import ee.pocopay.demo.config.MdcUtil;
import ee.pocopay.demo.transaction.model.Transaction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class TransactionService {

    private TransactionRepository transactionRepository;

    private AccountService accountService;

    public TransactionService(TransactionRepository transactionRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Transactional
    public Transaction createTransaction(Transaction transaction) {
        accountService.checkAccountBalances(transaction);

        transaction.setCreatedSessionId(MdcUtil.getSessionId());
        var newTransaction = transactionRepository.save(transaction);

        accountService.updateAccountBalances(transaction);

        return newTransaction;
    }
}
