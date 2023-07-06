package ee.pocopay.demo.transaction;

import ee.pocopay.demo.account.AccountService;
import ee.pocopay.demo.transaction.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionListener {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionListener.class);

    private AccountService accountService;

    public TransactionListener(AccountService accountService) {
        this.accountService = accountService;
    }

    @RabbitListener(queues = "creditTransaction")
    public void creditTransaction(Transaction transaction) {
        LOG.info("Message read from myQueue : " + transaction);

        //accountService.updateCreditAccountBalance(transaction);
    }
}
