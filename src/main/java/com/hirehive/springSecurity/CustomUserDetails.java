package com.hirehive.springSecurity;

import com.hirehive.constants.RoleType;
import com.hirehive.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private User user;
    public CustomUserDetails(User user) {
        this.user = user;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        RoleType role1 = this.user.getRole();
        String roleConvertedToString = role1.toString();
        SimpleGrantedAuthority roleConvertedToSimpleGrantedAuthorityType = new SimpleGrantedAuthority(roleConvertedToString);
        authorityList.add(roleConvertedToSimpleGrantedAuthorityType);
        return authorityList;
    }
    public Long getUserId(){
        return this.user.getId();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {return user.getEmail();}

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
