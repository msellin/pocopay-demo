package ee.pocopay.demo.config;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PocopayException extends RuntimeException {

    private HttpStatus status = HttpStatus.BAD_REQUEST;

    public PocopayException(String message) {
        super(message);
    }
    public PocopayException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
