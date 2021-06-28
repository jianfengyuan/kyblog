package com.kyblog.front.Config;

import com.kyblog.front.Filter.JwtLoginFilter;
import com.kyblog.front.Filter.JwtPreAuthFilter;
import com.kyblog.front.Handler.KyblogAccessDeniedServletHandler;
import com.kyblog.front.Handler.KyblogAuthenticationSuccessHandler;
import com.kyblog.front.Handler.KyblogLogoutSuccessHandler;
import com.kyblog.front.Service.KyblogUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Autowired
    private KyblogAuthenticationProvider authenticationProvider;
    @Autowired
    private KyblogUserDetailsService userService;
    @Autowired
    private KyblogLogoutSuccessHandler logoutSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
                .formLogin().loginProcessingUrl("/user/login")
                .successHandler(new KyblogAuthenticationSuccessHandler("/"));
//                .and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                http
//                .antMatcher("/**")
                        .authorizeRequests()
                .antMatchers("/login","/").permitAll()
                .antMatchers("/admin/**").hasAnyRole("admin")
//                .permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new KyblogAccessDeniedServletHandler("/logout"))
                .and()
                .logout().invalidateHttpSession(true).deleteCookies("JSESSIONID").logoutSuccessHandler(logoutSuccessHandler)
        ;
//        http.addFilterBefore(new JwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAt(new JwtLoginFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
        //        http.addFilter(new JwtPreAuthFilter(authenticationManager()));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/**/*.js", "/lang/*.json", "/**/*.css", "/**/*.js", "/**/*.map", "/**/*.html",
                "/**/*.png");
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
