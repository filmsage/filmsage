package com.filmsage.filmsage.models;

import com.filmsage.filmsage.models.auth.Privilege;
import com.filmsage.filmsage.models.auth.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserPrinciple implements UserDetails {

    private User user;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private boolean accountNonLocked;
    private Collection<GrantedAuthority> authorities;

//    public UserPrinciple(User user, boolean enabled, boolean accountNonExpired,
//                         boolean credentialsNonExpired, boolean accountNonLocked,
//                         Collection<GrantedAuthority> authorities) {
//        this.user = user;
//        this.enabled = enabled;
//        this.accountNonExpired = accountNonExpired;
//        this.credentialsNonExpired = credentialsNonExpired;
//        this.accountNonLocked = accountNonLocked;
//        this.authorities = authorities;
//    }

    public UserPrinciple(User user) {
        this.user = user;
        this.enabled = user.isEnabled();
        this.accountNonExpired = true;
        this.credentialsNonExpired = true;
        this.accountNonLocked = true;
        this.authorities = getGrantedAuthorities(getPrivileges(user.getRoles()));;
    }

//    @Override
//    public Collection<GrantedAuthority> getAuthorities() {
//        return AuthorityUtils.commaSeparatedStringToAuthorityList(
//                user.getRoles().stream()
//                        .map(Role::toString)
//                        .collect(Collectors.joining(","))
//        );
//    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public User getUser() {
        return user;
    }

    //    private Collection<GrantedAuthority> getAuthorities(Collection<Role> roles) {
//        return getGrantedAuthorities(getPrivileges(roles));
//    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }

    private List<String> getPrivileges(Collection<Role> roles) {

        List<String> privileges = new ArrayList<>();
        List<Privilege> collection = new ArrayList<>();
        for (Role role : roles) {
            privileges.add(role.getName());
            collection.addAll(role.getPrivileges());
        }
        for (Privilege item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }
}
