package com.example.springbatchapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    // Pour crypter les mots de passe
    // @Bean
    // public PasswordEncoder passwordEncoder(){
    //     return new BCryptPasswordEncoder();
    // }

    @Bean
    public NoOpPasswordEncoder passwordEncoder(){
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

    // Cree l'authentification a partir des  information recupere dans UserDetailImpl
    // Autorithies
    // Utiliser l'algorithme d'encryptage
    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    // On definit l'acces a notre application
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // demande a toutes les requetes de s'authentifier
        http.cors().and().authorizeRequests().anyRequest().fullyAuthenticated();
        // permet a la fonctionalite d'authentifier l'utilisateur lors de la requete Http
        http.httpBasic();
        http.csrf().disable();
    }

    // WhiteList URLs
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui",
                "/swagger-resources/**", "/configuration/**", "/swagger-ui.html"
                , "/webjars/**", "/csrf", "/");
    }
}
