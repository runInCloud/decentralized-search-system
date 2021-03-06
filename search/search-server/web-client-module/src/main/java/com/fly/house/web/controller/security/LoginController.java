package com.fly.house.web.controller.security;

import com.fly.house.core.dto.AccountDto;
import com.fly.house.model.Account;
import com.fly.house.service.converter.AccountConverter;
import com.fly.house.service.converter.ConverterFactory;
import com.fly.house.service.registration.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Objects;

import static com.fly.house.service.converter.ConverterFactory.getAccountConverter;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by dimon on 4/19/14.
 */
@RestController
@RequestMapping("/rest")
public class LoginController {

    @Autowired
    private AccountService accountService;

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login", params = {"user", "password"}, method = POST)
    public ResponseEntity<AccountDto> login(HttpServletRequest request,
                                        @RequestParam String user,
                                        @RequestParam String password) throws ServletException {
        try {
            request.login(user, password);
            logger.debug("User {} logged in successfully", user);
            Account account = accountService.findAccountByName(user);
            AccountDto responseBody = getAccountConverter().convert(account);
            return new ResponseEntity<>(responseBody, OK);
        } catch (ServletException e) {
            logger.warn("Exception occurred: ", e);
            return new ResponseEntity<>(UNAUTHORIZED);
        }
    }

    @ResponseStatus(OK)
    @RequestMapping(value = "/logout", method = GET)
    public void logout(HttpServletRequest request, Principal principal) throws ServletException {
        try {
            if (Objects.nonNull(principal)) {
                String userName = principal.getName();
                logger.debug("User {} logged out successfully", userName);
                request.logout();
            }
        } catch (ServletException e) {
            logger.warn("Exception occurred: ", e);
        }
    }


}
