package com.github.bitsapling.sapling.controller.account;

import com.github.bitsapling.sapling.objects.ResponsePojo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/account")
public class AccountRegisterController {

    @Autowired

    private HttpServletRequest request;

    @GetMapping("/register")
    public ResponsePojo register() {

    }
    @Getter
    static class RegistrationClosedErrorPojo extends ResponsePojo {
        private final String message = "Sorry, this site is not accepting new registrations at this time";

        RegistrationClosedErrorPojo() {
            super(10005);
        }
    }
    @Getter
    static class AgeCheckErrorPojo extends ResponsePojo {
        private final String message = "You are under the age allowed to register on this site";

        AgeCheckErrorPojo() {
            super(10004);
        }
    }
    @Getter
    static class CaptchaErrorPojo extends ResponsePojo {
        private final String message = "Captcha Incorrect";

        CaptchaErrorPojo() {
            super(10003);
        }
    }
    @Getter
    static class EmailInUseErrorPojo extends ResponsePojo {
        private final String message = "Email is already in use";

        EmailInUseErrorPojo() {
            super(10002);
        }
    }

    @Getter
    static class UsernameInUseErrorPojo extends ResponsePojo {
        private final String message = "Username is already in use";

        UsernameInUseErrorPojo() {
            super(10001);
        }
    }
}
