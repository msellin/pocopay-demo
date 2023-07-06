package ee.pocopay.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/ping")
@RestController
public class PingResource {

    @GetMapping
    public String pong() {
        return "pong";
    }
}
