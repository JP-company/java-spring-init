package jjp.java.spring.init.interfaces.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("")
public class AccountController {

  @GetMapping("")
  public String emailPage() {
    return "email";
  }

  @GetMapping("/home")
  public String home() {
    return "home";
  }
}
