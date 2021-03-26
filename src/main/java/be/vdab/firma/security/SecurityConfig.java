package be.vdab.firma.security;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;
@EnableWebSecurity
class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String MANAGER = "manager";
    private final DataSource dataSource;

    SecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery
                ("select emailAdres as username, paswoord as password , true as enabled " + "from werknemers where emailadres= ?")
        .authoritiesByUsernameQuery ("select ?,'gebruiker'");
             ; //  .authoritiesByUsernameQuery("select emailadres as username  from werknemers");
               // .authoritiesByUsernameQuery("select gebruikers.naam as username, rollen.naam as authorities "+
               //         "from gebruikers inner join gebruikersrollen on gebruikers.id= gebruikersrollen.gebruikerId "+
               //         "inner join rollen on rollen.id =gebruikersrollen.rolId where gebruikers.naam=?");
    }

    @Override
    public void configure(WebSecurity web){
        web.ignoring()
                .mvcMatchers("/images/**")
                .mvcMatchers("/css/**")
                .mvcMatchers("/js/**");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.formLogin(login->login.loginPage("/"));
        http.authorizeRequests(requests -> requests
                .mvcMatchers("/").permitAll()
                .mvcMatchers("/**").authenticated());
        http.logout(logout -> logout.logoutSuccessUrl("/"));
    }


}

