package com.TrollMarket.TrollMarket.models;

import com.TrollMarket.TrollMarket.dtos.auth.SelectListRoleDto;
import com.TrollMarket.TrollMarket.services.AccountService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Builder
@Getter
public class AccountDetail implements UserDetails {
    private Account account;
    private AccountService accountService;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String username = account.getUsername();

        List<SelectListRoleDto> roles = accountService.getRoleDropdown(username);

        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());



        return authorities;

    }

    @Override
    public String getPassword() {
        return this.account.getPassword();
    }

    @Override
    public String getUsername() {
        return this.account.getUsername();
    }

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
