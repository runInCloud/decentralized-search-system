package com.fly.house.authentication;

import com.fly.house.authentication.exception.CookieIOException;
import com.fly.house.authentication.exception.CookieNotFoundException;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.Files.newInputStream;

/**
 * Created by dimon on 1/31/14.
 */
@Component
public class CookieManager {

    private Path cookieDirPath;
    private HttpHeaders cookieHeader = new HttpHeaders();
    private static Logger logger = LoggerFactory.getLogger(CookieManager.class);

    public HttpHeaders getCookieHeader() {
        if (!cookieHeader.isEmpty()) {
            return cookieHeader;
        }
        Path cookiePath = getCookiePath();
        List<String> cookieValues = loadCookie(cookiePath);
        cookieHeader.put("Cookie", cookieValues);
        return cookieHeader;
    }

    public void saveCookie(List<String> cookie) {
        String fileName = generateFileName(cookie);
        Path path = cookieDirPath.resolve(fileName);
        try (OutputStream is = Files.newOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(is)) {
            oos.writeObject(cookie);
            cookieHeader.put("Cookie", cookie);
        } catch (IOException e) {
            logger.warn("Cannot save cookie on the disk", e);
            throw new CookieIOException("Cannot save cookie on the disk", e);
        }
    }

    public boolean isLoaded() {
        return cookieHeader != null && !cookieHeader.isEmpty();
    }

    public void removeCookies() {
        logger.debug("Removing cookie from the file system");
        cookieHeader = null;
        Path cookiePath = getCookiePath();
        cookiePath.toFile().delete();
    }

    private Path getCookiePath() {
        File file = cookieDirPath.toFile();
        String[] cookieNames = file.list();
        if (cookieNames.length == 1) {
            throw new CookieNotFoundException("Cookie not found. Maybe you not authorized");
        }
        String cookieName = cookieNames[0];
        return cookieDirPath.resolve(cookieName);
    }

    private List<String> loadCookie(Path cookiePath) {
        logger.debug("Loading cookies...");
        try (ObjectInputStream oos = new ObjectInputStream(newInputStream(cookiePath))) {
            return (List<String>) oos.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.warn("Cannot save cookie on the disk", e);
            throw new CookieIOException("Cannot save cookie on the disk", e);
        }
    }

    private String generateFileName(List<String> cookie) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : cookie) {
            stringBuilder.append(s);
        }
        return DigestUtils.md5Hex(stringBuilder.toString());
    }

}
