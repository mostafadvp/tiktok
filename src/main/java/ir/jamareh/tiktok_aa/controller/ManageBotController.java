package ir.jamareh.tiktok_aa.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bot")
public class ManageBotController {


    @GetMapping("/hello")
    public String seyHello(HttpServletRequest http) {
        return "Hello World! "+http.getSession().getId();
    }
}
