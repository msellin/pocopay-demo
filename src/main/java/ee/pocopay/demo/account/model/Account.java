package ee.pocopay.demo.account.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ee.pocopay.demo.config.PocopayException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {
    @Id
    private Long id;
    private String name;
    private BigDecimal balance;
    private String createdSessionId;
    private String modifiedSessionId;

    public void validate() {
        if (name == null || name.isEmpty()) {
            throw new PocopayException("Name must be provided");
        }
        if (balance == null || balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new PocopayException("A positive balance must be provided");
        }
    }
}
