package ee.pocopay.demo.account;

import ee.pocopay.demo.account.model.Account;
import ee.pocopay.demo.transaction.model.Transaction;
import org.springframework.data.repository.ListCrudRepository;

public interface AccountRepository extends ListCrudRepository<Account, Long> {
}
