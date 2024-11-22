package ir.jamareh.tiktok_aa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ActionAutomationApplication {

    public static void main(String[] args) {
//        System.out.println(new BCryptPasswordEncoder().encode("admin")); //$2a$10$GBg5h4VMcLOg1kctK.D27.A5xxPZBXuUP.wic/2R/LngQfw3WLHF.
//        System.out.println(new BCryptPasswordEncoder().encode("123456")); //$2a$10$9ZxbyXzaofAjgYebyeIkuuqi1V0BL/FQNIUZTbfUkrYjgwovyfhAK
        SpringApplication.run(ActionAutomationApplication.class, args);
    }
}
