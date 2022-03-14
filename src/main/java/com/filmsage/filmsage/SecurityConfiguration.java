package com.filmsage.filmsage;

import com.filmsage.filmsage.services.UserDetailsLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserDetailsLoader usersLoader;

    public SecurityConfiguration(UserDetailsLoader usersLoader) {
        this.usersLoader = usersLoader;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(usersLoader) // How to find users by their username
                .passwordEncoder(passwordEncoder()) // How to encode and verify passwords
        ;
    }

    /*
    ** Small note for the following method:
    from here on out we are fully integrating user authentication and protecting our
    content Create Update Delete access. You will have to make sure to be logged
    in when testing your pages if you are doing manual testing.
    I would recommend writing some junit tests
    to auto login for your work so that you can automate the process.
    ** Also, for the antMatchers paths:
    We are using Regular Expressions to ensure proper matching for the routes we are securing.
    I would encourage everyone to read a little about regular expressions and going
    to https://regex101.com/ to play around with them to understand why these ugly little
    strings have to exist. For reference:

    * IMDB ids: ^\w{2}\d+$ (ie "tt0106145", "tt1209299")
    * our content ids: ^/d+$ (ie "14", "1", "159")

    I will try and keep this list up to date as new
    matches need to be added or if/when we
    change our ID generation system. Let me know if
    you think you need one, I will happily write you one :)
    -- James
    */

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                /* Login configuration */
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/") // user's home page, it can be any URL
                .permitAll() // Anyone can go to the login page
                /* Logout configuration */
                .and()
                .logout()
                .logoutSuccessUrl("/login?logout") // append a query string value
                /* Pages that can be viewed without having to log in */
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/",
                        "/movies/{imdb:^\\w{2}\\d+$}",
                        "/movies/{imdb:^\\w{2}\\d+$}/reviews",
                        "/movies/{imdb:^\\w{2}\\d+$}/reviews/{id:^\\d+$}"
                        ) // anyone can read the site's contents
                .permitAll()
                /* Pages that require authentication */
                .and()
                .authorizeRequests()
                .antMatchers(
                       "/movies/{imdb:^\\w{2}\\d+$}/reviews/create",
                        "/movies/{imdb:^\\w{2}\\d+$}/reviews/delete",
                        "/movies/{imdb:^\\w{2}\\d+$}/reviews/edit"
                )
                .authenticated()
        ;
    }
}