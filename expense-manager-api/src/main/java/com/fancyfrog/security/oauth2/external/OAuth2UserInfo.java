package com.fancyfrog.security.oauth2.external;

import java.util.Map;

/**
 * Created by Ujjwal Gupta on Dec,2019
 */
public abstract class OAuth2UserInfo {

    public Map<String,Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String,Object> getAttributes(){
        return attributes;
    }

    public abstract String getId();

    public abstract  String getName();

    public abstract String getEmail();

    public abstract String getImageUrl();
}
