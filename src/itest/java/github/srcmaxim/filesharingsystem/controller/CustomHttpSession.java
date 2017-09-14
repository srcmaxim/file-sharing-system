package github.srcmaxim.filesharingsystem.controller;

import github.srcmaxim.filesharingsystem.model.Authority;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.util.List;

public class CustomHttpSession extends MockHttpSession {

    public CustomHttpSession(String principal, String credentials, List<Authority> authorities) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(principal, credentials, authorities);
        setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                new MockSecurityContext(auth));
    }

}
