//package com.filmsage.filmsage.models;
//
//import java.util.Collection;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.userdetails.UserDetails;
//
//public class UserWithRoles extends User implements UserDetails {
//    public UserWithRoles(User user) {
//        super(user);
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        String roles = ""; // hardcoded no roles -- can be changed
//        return AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}
