package github.srcmaxim.filesharingsystem.controller;

import github.srcmaxim.filesharingsystem.model.Authority;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CustomHttpSession extends MockHttpSession {

    public CustomHttpSession(String principal, String credentials, String... role) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                principal, credentials,
                Arrays.stream(role).map((String name) -> new Authority(name))
                        .collect(Collectors.toList()));
        setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                new MockSecurityContext(auth));
    }

}
