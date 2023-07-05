package ee.pocopay.demo.config;

import lombok.experimental.UtilityClass;
import org.slf4j.MDC;

import java.util.UUID;

@UtilityClass
public class MdcUtil {

    private static final String SESSION_ID = "sessionId";

    public static void setSessionId() {
        MDC.put(SESSION_ID, UUID.randomUUID().toString());
    }

    public static String getSessionId() {
        return MDC.get(SESSION_ID);
    }
}
