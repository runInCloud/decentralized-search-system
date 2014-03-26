package com.fly.house.authentication;

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

    private CookieManager cookieManager;

    private HttpStatusHandler httpHandler;

    @Value("${restUrl}")
    private String restUrl;

    private static Logger logger = LoggerFactory.getLogger(Authorization.class);

    public static final String AUTH_URL = "login?user={user}&password={pass}";

    @Autowired
    public Authorization(CookieManager cookieManager, RestTemplate restTemplate, HttpStatusHandler httpHandler) {
        this.cookieManager = cookieManager;
        this.restTemplate = restTemplate;
        this.httpHandler = httpHandler;
    }



    public void authentication(String login, String password) {
        logger.debug("Start authentication with login={} password={}", login, password);
        Account account = new Account(login, password);
        HttpEntity<Message<Account>> request = new HttpEntity<>(new Message<>(account));
        logger.debug("Call to server to authenticate user");
        String url = String.format("%s/%s", restUrl, AUTH_URL);
        ResponseEntity<Message<Account>> entity = restTemplate.exchange(url, POST, request, new Message<Account>(), login, password);
        httpHandler.handle(entity.getStatusCode());
        List<String> cookie = entity.getHeaders().get("Cookie");
        cookieManager.saveCookie(cookie);
    }

    public void logout() {
        logger.debug("logout from the service");
        cookieManager.removeCookies();
    }

    public boolean isAuthorized() {
        return cookieManager.isLoaded();
    }

}
