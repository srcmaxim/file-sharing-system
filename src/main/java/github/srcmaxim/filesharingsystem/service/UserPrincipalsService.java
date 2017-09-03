package github.srcmaxim.filesharingsystem.service;

import github.srcmaxim.filesharingsystem.model.User;
import github.srcmaxim.filesharingsystem.model.UserPrincipals;
import github.srcmaxim.filesharingsystem.repository.UserRepository;
import github.srcmaxim.filesharingsystem.annotation.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Loggable
public class UserPrincipalsService implements UserDetailsService {

    UserRepository repository;

    @Autowired
    public UserPrincipalsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public UserPrincipals loadUserByUsername(String username) {
        User user = repository.findByLogin(username);
        if (user == null) {
            throw new UsernameNotFoundException("error.user.login.not-found");
        }
        return new UserPrincipals(user);
    }

}
