package ee.pocopay.demo.transaction.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {
    @Id
    private Long id;
    private Long debitAccountId;
    private Long creditAccountId;
    private BigDecimal amount;
    private String createdSessionId;
}
