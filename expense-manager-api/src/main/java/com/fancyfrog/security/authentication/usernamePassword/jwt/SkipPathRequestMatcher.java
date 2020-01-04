package com.fancyfrog.security.authentication.usernamePassword.jwt;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Ujjwal Gupta on Jan,2020
 */

public class SkipPathRequestMatcher implements RequestMatcher {

    private OrRequestMatcher matcher;
    private RequestMatcher processingMatcher;

    public SkipPathRequestMatcher(List<String> pathToSkip, String processingPath){
        Assert.notNull(pathToSkip,"Public API URLs cannot be null");
        List<RequestMatcher> path = pathToSkip.stream().map(p -> new AntPathRequestMatcher(p)).collect(Collectors.toList());
        matcher = new OrRequestMatcher(path);
        processingMatcher = new AntPathRequestMatcher(processingPath);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        if(matcher.matches(request))
            return false;
        return processingMatcher.matches(request) ? true : false;
    }
}
