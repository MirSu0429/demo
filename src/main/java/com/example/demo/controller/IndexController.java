package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: supengfei
 * @Date: 2019/1/7 17:16
 * @Description:
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    public String index() {
        return "/view/index.html";
    }
}
