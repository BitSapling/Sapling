package org.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyTestController {
    public MyTestController() {
        System.out.println("oh! created!");
    }

    @GetMapping("/mytest")
    public String test() {
        return "Yes!";
    }
}
