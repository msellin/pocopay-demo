package ee.pocopay.demo.transaction;

import ee.pocopay.demo.transaction.model.Transaction;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("transactions")
@RestController
public class TransactionResource {

    private TransactionService transactionService;

    public TransactionResource(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        return transactionService.createTransaction(transaction);
    }
}
