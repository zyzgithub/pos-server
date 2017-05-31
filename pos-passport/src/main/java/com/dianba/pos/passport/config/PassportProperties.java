package com.dianba.pos.passport.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PassportProperties {

    @Value("${pos.passport.url}")
    private String baseUrl;

    /**登录**/
    private String login = "passport/loginPassport/";

    public String getLogin() {
        return baseUrl+login;
    }

    private String register="passport/registerPassport/";

    public String getRegister() {
        return baseUrl+register;
    }
}
