package com.TrollMarket.TrollMarket.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request ->
                        request.requestMatchers("/resources/**","/error/403").permitAll()
                                .requestMatchers("/registerSeller/**","/registerBuyer/**", "/login").anonymous()
                                .requestMatchers("/home").hasAnyAuthority("Buyer","Seller","Admin")
                                .requestMatchers("/admin","/addAdmin","/categories","/shipment","history").hasAuthority("Admin")
                                .requestMatchers("/merchandise/**").hasAuthority("Seller")
                                .requestMatchers("/profile").hasAnyAuthority("Seller","Buyer")
                                .requestMatchers("/shop","/carts/**").hasAuthority("Buyer")
                                .anyRequest().authenticated()
                ).formLogin(login->login //login postnya kemanadimana dan kemana
                        .loginPage("/login")
                        .loginProcessingUrl("/authenticating")
                        .failureUrl("/login?error=true"))
                .logout(logout ->logout
                        .logoutUrl("/logout"));//
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
