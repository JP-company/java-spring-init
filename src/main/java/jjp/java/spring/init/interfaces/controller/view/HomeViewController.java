package jjp.java.spring.init.interfaces.controller.view;

import jjp.java.spring.init.interfaces.interceptor.annotation.PublicApi;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class HomeViewController {

  @PublicApi
  @GetMapping("")
  public String auth() {
    return "pages/auth";
  }

  @GetMapping("/home")
  public String home() {
    return "pages/home";
  }
}
