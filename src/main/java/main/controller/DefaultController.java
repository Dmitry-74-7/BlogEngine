package main.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class DefaultController implements ErrorController {

  @RequestMapping("/*")
  public String index() {
    return "index";
  }

  @Override
  public String getErrorPath() {
    return "index";
  }

}
