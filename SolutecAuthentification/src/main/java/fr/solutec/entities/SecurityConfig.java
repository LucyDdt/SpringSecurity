package fr.solutec.entities;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import fr.solutec.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtAuthFilter jwtAuthFilter;
		@Autowired
		private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Bean
    AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsServiceImpl);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
	@Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
	
	
	  @Bean
	  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	        	.csrf().disable()
	        	/*.cors().disable()*/ //Pour paramétrer le crossOrigin
	        	/*.formLogin((form) -> form
		                .permitAll()
		            )*/
	            .authorizeHttpRequests((requests) -> requests
	            	.requestMatchers("/coucou").hasAuthority("ROLE_ADMIN")
	            	.requestMatchers("/registration").permitAll()
	            	.requestMatchers("/authenticate").permitAll()
	            	.requestMatchers("/addRole/**").permitAll()
	            	.anyRequest().authenticated()
	            )
	            .sessionManagement((session) -> session 
	            		.sessionCreationPolicy(SessionCreationPolicy.STATELESS) //Pour créer une session avec une authentification par Token
	            )
	            .authenticationProvider(authenticationProvider())
	            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) //Filtre avec Token
	            .logout((logout) -> logout.permitAll()
	            );

	        return http.build();
	    }
	  
    @Bean
    PasswordEncoder passwordEncoder() //Encodage de mot de passe en Bcrypt
    {
        return new BCryptPasswordEncoder();
    }
    


}
