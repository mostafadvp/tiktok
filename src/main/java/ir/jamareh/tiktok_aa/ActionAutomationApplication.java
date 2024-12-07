package ir.jamareh.tiktok_aa;

import ir.jamareh.tiktok_aa.repositories.JobRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class ActionAutomationApplication {
    public static void main(String[] args) {
        SpringApplication.run(ActionAutomationApplication.class, args);
        log.info("ActionAutomationApplication started");
    }
}
