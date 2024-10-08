package com.example.p1_backend.util;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.p1_backend.models.User;
import com.example.p1_backend.repositories.UserDao;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;

	private final UserDao userDao;

	@Autowired
	public JwtFilter(JwtUtil jwtUtil, UserDao userDao) {
		this.jwtUtil = jwtUtil;
		this.userDao = userDao;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// Ensures that Authorization Header is occupied
		String authHeader = request.getHeader("Authorization");
		String jwt = null;
		String name = null;

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			jwt = authHeader.substring(7);
			name = jwtUtil.extractName(jwt);
		}

		// Validation checks
		if (name != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			// Ensures that the user exists
			Optional<User> user = userDao.getByName(name);

			if (jwtUtil.validateToken(jwt) && user.isPresent()) {
				// Sets principal to user
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.get(),
						null, null);
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}

		filterChain.doFilter(request, response);
	}

}
