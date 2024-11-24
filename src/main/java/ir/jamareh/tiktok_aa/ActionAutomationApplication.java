package ir.jamareh.tiktok_aa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ActionAutomationApplication {
    private static final Logger logger = LoggerFactory.getLogger(ActionAutomationApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ActionAutomationApplication.class, args);
        logger.info("ActionAutomationApplication started");
    }
}
