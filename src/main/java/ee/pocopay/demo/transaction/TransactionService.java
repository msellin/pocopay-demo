package ee.pocopay.demo.transaction;

import ee.pocopay.demo.account.AccountService;
import ee.pocopay.demo.config.MdcUtil;
import ee.pocopay.demo.transaction.model.Transaction;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class TransactionService {

    private TransactionRepository transactionRepository;

    private AccountService accountService;

    private RabbitTemplate rabbitTemplate;

    public TransactionService(TransactionRepository transactionRepository, AccountService accountService, RabbitTemplate rabbitTemplate) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Transactional
    public Transaction createTransaction(Transaction transaction) {
        accountService.checkAccountBalances(transaction);

        transaction.setCreatedSessionId(MdcUtil.getSessionId());
        var newTransaction = transactionRepository.save(transaction);

        accountService.updateDebitAccountBalance(transaction);
        accountService.updateCreditAccountBalance(transaction);
        rabbitTemplate.convertAndSend("creditTransaction", transaction);

        return newTransaction;
    }
}
