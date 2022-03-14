package com.filmsage.filmsage.services;

import com.filmsage.filmsage.models.User;
import com.filmsage.filmsage.models.auth.UserPrinciple;
import com.filmsage.filmsage.repositories.UserRepository;
import com.filmsage.filmsage.repositories.auth.RoleRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
@Transactional
public class UserDetailsLoader implements UserDetailsService {
    private final UserRepository userDao;
    private final RoleRepository roleDao;

    public UserDetailsLoader(UserRepository userDao, RoleRepository roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    // Just a heads up: this method is called "loadUserByUsername" but we are actually using
    // email instead. this method comes from UserDetailsService, so we can't change the name 😥
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDao.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(email);
        }
        return new UserPrinciple(user);
    }
}