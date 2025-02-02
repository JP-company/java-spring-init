package jjp.java.spring.init.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import jjp.java.spring.init.interfaces.interceptor.annotation.PublicApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("")
public class AccountController {

  @GetMapping
  @Operation(summary = "홈")
  @PublicApi
  public String home(Model model) {
    model.addAttribute("data", "hello!!");
    return "home";
  }

}
