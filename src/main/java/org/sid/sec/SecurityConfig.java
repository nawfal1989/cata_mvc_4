package org.sid.sec;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.*;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private DataSource dataSource;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// Où sont les utilisateurs qui sont authentifiés 
		//auth.inMemoryAuthentication().withUser("admin").password("{noop}1234").roles("USER", "ADMIN");
		
		//auth.inMemoryAuthentication().withUser("user").password("{noop}1234").roles("USER");
		
		// Definir au spring comment il va chercher les users et les roles
		auth.jdbcAuthentication()
		.dataSource(dataSource)
		.usersByUsernameQuery("select login as principal, pass as credentials, active from users where login=?")
		.authoritiesByUsernameQuery("select login as principal, role as role from users_roles where login=?")
		.passwordEncoder(new Md4PasswordEncoder())
		.rolePrefix("ROLE_");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Utiliser http pour définir les règles de securité
		// l'application passe dabors par le formulaire d'authentification
		http.formLogin().loginPage("/login");
		
		// desactiver le token qui permet d eliminer l'attaque csrf
		//http.csrf().disable();
		
		// dire tel url que tel requete doit avoir ce role pour avoir cette ressource
		//http.authorizeRequests().antMatchers("/index").hasRole("USER");
		http.authorizeRequests().antMatchers("/user/*").hasRole("USER");
		
		//http.authorizeRequests().antMatchers("/form","/save","/edit","/delete").hasRole("ADMIN");
		http.authorizeRequests().antMatchers("/admin/*").hasRole("ADMIN");
		
		// Personnaliser les erreurs des droits d'accès
		http.exceptionHandling().accessDeniedPage("/403");
	}
}
