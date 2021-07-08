package com.larry.lallender.lallender.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.larry.lallender.lallender.service.LoginService.LOGIN_SESSION_KEY;

@Controller
public class IndexController {

    @GetMapping("/test")
    public String test(Model model, HttpSession httpSession) {
        final Map<String, Object> props = new HashMap<>();
        props.put("title", "타이틀입니당");
        props.put("calendar", "sample@gmail.com");
        props.put("period", "언제부터 언제까지");
        props.put("attendees", List.of("test@email.com", "test2@email.com", "test3@email.com"));
        props.put("acceptUrl", "http://localhost:8080");
        props.put("rejectUrl", "http://localhost:8080");
        model.addAllAttributes(props);
        return "engagement-email";
    }

    @GetMapping("/")
    public String index(Model model, HttpSession httpSession) {
        model.addAttribute("isSignIn",
                           httpSession.getAttribute(LOGIN_SESSION_KEY) != null);
        return "index";
    }

}
