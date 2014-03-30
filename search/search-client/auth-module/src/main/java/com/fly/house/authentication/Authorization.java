package com.fly.house.authentication;

import com.fly.house.rest.CookieService;
import com.fly.house.rest.HttpHandler;
import com.fly.house.rest.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.http.HttpMethod.POST;

/**
 * Created by dimon on 1/26/14.
 */
@Service
public class Authorization {

    private RestTemplate restTemplate;

    private CookieService cookieService;

    private HttpHandler httpHandler;

    @Value("${authUrl}")
    private String authUrl;

    private static Logger logger = LoggerFactory.getLogger(Authorization.class);

    @Autowired
    public Authorization(CookieService cookieService, RestTemplate restTemplate, HttpHandler httpHandler) {
        this.cookieService = cookieService;
        this.restTemplate = restTemplate;
        this.httpHandler = httpHandler;
    }


    public void authentication(String login, String password) {
        logger.debug("Start authentication with login={} password={}", login, password);
        Account account = new Account(login, password);
        HttpEntity<Message<Account>> request = new HttpEntity<>(new Message<>(account));
        logger.debug("Call to server to authenticate user");
        ResponseEntity<Message<Account>> entity = restTemplate.exchange(authUrl, POST, request, new Message<Account>(), login, password);
        httpHandler.handle(entity.getStatusCode());
        List<String> cookie = entity.getHeaders().get("Cookie");
        cookieService.saveCookie(cookie);
    }

    public void logout() {
        //todo kill session in the server
        logger.debug("logout from the service");
        cookieService.removeCookies();
    }

    public boolean isAuthorized() {
        return cookieService.isLoaded();
    }

}