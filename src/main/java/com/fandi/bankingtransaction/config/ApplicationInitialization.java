package com.fandi.bankingtransaction.config;

import com.fandi.bankingtransaction.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@Slf4j
public class ApplicationInitialization {

    @Autowired
    private UserService userService;

    @PostConstruct
    public void init() {

        log.info("Username : " + System.getenv("FIRST_USER_NAME"));
        log.info("Email : " + System.getenv("FIRST_USER_EMAIL"));
        log.info("Password : " + System.getenv("FIRST_USER_PASSWORD"));
        userService.createFirstAdmin();
    }
}
