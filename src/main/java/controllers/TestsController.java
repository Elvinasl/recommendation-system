package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import services.TestService;

@RestController
@RequestMapping("/test")
public class TestsController {

    private TestService testService;

    @Autowired
    public TestsController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping
    public String test() {
        return testService.test();
    }
}
