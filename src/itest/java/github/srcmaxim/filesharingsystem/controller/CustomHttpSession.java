package github.srcmaxim.filesharingsystem.controller;

import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import static java.util.Arrays.asList;

public class CustomHttpSession extends MockHttpSession {

    public CustomHttpSession(String principal, String credentials, String role) {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        principal, credentials, asList((GrantedAuthority) () -> role));
        setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                new MockSecurityContext(auth));
    }

}
