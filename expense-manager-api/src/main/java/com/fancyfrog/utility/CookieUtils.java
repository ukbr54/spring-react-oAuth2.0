package com.fancyfrog.utility;

import org.springframework.util.SerializationUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by Ujjwal Gupta on Dec,2019
 */
public class CookieUtils {

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name){
        Cookie[] cookies = request.getCookies();
        Optional<Cookie> cookie = Arrays.stream(cookies).filter(c -> c.getName().equals(name)).findAny();
        return cookie;
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge){
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name){
        Cookie[] cookies = request.getCookies();
        if(Objects.nonNull(cookies) && cookies.length > 0){
            Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(name)).forEach(cookie -> {
                cookie.setPath("/");
                cookie.setValue("");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            });
        }
    }

    public static String serialize(Object object){
        Base64.Encoder encoder = Base64.getUrlEncoder();
        String value = encoder.encodeToString(SerializationUtils.serialize(object));
        return value;
    }

    public static <T> T deserialize(Cookie cookie, Class<T> clazz){
        Base64.Decoder decoder = Base64.getUrlDecoder();
        Object deserializeValue = SerializationUtils.deserialize(decoder.decode(cookie.getValue()));
        return clazz.cast(deserializeValue);
    }
}
