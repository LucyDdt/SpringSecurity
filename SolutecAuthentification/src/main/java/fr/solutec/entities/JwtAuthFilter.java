package fr.solutec.entities;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import fr.solutec.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
	private final JwtUtils jwtUtils;
	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	
	
	 @Override
	    protected void doFilterInternal( //Filtre permettant de vérifier si le Token est valide
	            HttpServletRequest request,
	            HttpServletResponse response,
	            FilterChain filterChain) throws ServletException, IOException {
	        final String authHeader = request.getHeader(AUTHORIZATION);
	        final String username;
	        final String jwtToken;

	        if (authHeader == null || !authHeader.startsWith("Bearer")) {
	            filterChain.doFilter(request, response);
	            return ;
	        }
	        jwtToken = authHeader.substring(7);
	        username = jwtUtils.extractUsername(jwtToken);
	        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
	            if (jwtUtils.isTokenValid(jwtToken, userDetails)) {
	                UsernamePasswordAuthenticationToken authToken =
	                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                SecurityContextHolder.getContext().setAuthentication(authToken);
	            }
	        }
	        filterChain.doFilter(request, response);
	    }

}
