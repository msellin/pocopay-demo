package ee.pocopay.demo.transaction;

import ee.pocopay.demo.transaction.model.Transaction;
import org.springframework.data.repository.ListCrudRepository;

public interface TransactionRepository extends ListCrudRepository<Transaction, Long> {
}
