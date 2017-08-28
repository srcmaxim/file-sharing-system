package github.srcmaxim.filesharingsystem.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

public class MockSecurityContext implements SecurityContext {

    private static final long serialVersionUID = -1386535243513362694L;

    private Authentication authentication;

    public MockSecurityContext(Authentication authentication) {
        this.authentication = authentication;
    }

    @Override
    public Authentication getAuthentication() {
        return this.authentication;
    }

    @Override
    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

}
