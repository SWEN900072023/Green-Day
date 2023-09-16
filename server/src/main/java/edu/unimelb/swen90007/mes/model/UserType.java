package edu.unimelb.swen90007.mes.model;

import org.springframework.security.core.GrantedAuthority;

public class UserType implements GrantedAuthority {
    private final String authority;

    public UserType(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
