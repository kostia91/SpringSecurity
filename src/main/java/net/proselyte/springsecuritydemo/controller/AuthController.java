package net.proselyte.springsecuritydemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller//контроллер наверное это то, что мы можем взвать, а в этом классе распределяют
@RequestMapping("/auth")//типа большая папка
public class AuthController {

    @GetMapping("/login")//типа папка большой подпапки
    public String getLoginPage() {
        return "login";
    }//непонимаю, почему мы не добавляем html

    @GetMapping("/success")//типа папка большой подпапки
    public String getSuccessPage() {
        return "success";
    }
}
