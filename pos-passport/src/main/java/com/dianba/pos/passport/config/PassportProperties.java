package com.dianba.pos.passport.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PassportProperties {

    @Value("${pos.passport.url}")
    private String baseUrl;

    private String login = "passport/loginPassport/";

    public String getLogin() {
        return baseUrl+login;
    }
}
