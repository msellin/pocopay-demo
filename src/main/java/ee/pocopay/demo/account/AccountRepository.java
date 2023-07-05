package ee.pocopay.demo.account;

import ee.pocopay.demo.account.model.Account;
import org.springframework.data.repository.ListCrudRepository;

public interface AccountRepository extends ListCrudRepository<Account, Long> {

    Account findFirstByName(String name);
}
