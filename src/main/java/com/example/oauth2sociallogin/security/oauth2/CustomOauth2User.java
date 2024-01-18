package com.example.oauth2sociallogin.security.oauth2;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOauth2User implements OAuth2User {
    private final OAuth2User oAuth2User;

    public CustomOauth2User(OAuth2User oAuth2User) {
        this.oAuth2User = oAuth2User;
    }

    public Map<String, Object> getAttributes() {
        return this.oAuth2User.getAttributes();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.oAuth2User.getAuthorities();
    }

    public String getName() {
        return (String)this.oAuth2User.getAttribute("name");
    }

    public String getEmail() {
        return (String)this.oAuth2User.getAttribute("email");
    }
}
