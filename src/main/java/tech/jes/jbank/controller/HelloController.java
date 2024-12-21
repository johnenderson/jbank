package tech.jes.jbank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping
    public ResponseEntity<String> ok() {
        System.out.println("inside controller");
        return ResponseEntity.ok().build();
    }

}
