package com.larry.lallender.lallender.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

import static com.larry.lallender.lallender.service.LoginService.LOGIN_SESSION_KEY;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(Model model, HttpSession httpSession) {
        model.addAttribute("isSignIn",
                           httpSession.getAttribute(LOGIN_SESSION_KEY) != null);
        return "index";
    }

}
