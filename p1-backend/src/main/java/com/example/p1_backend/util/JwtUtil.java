package com.example.p1_backend.util;

import com.example.p1_backend.models.User;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {

	// Expiration time (24 hrs) for Token
	private static final long EXPIRE_TIME = 1000 * 60 * 60 * 24;

	// A randomly generated key specific to the application
	private final SecretKey KEY = Jwts.SIG.HS256.key().build();

	// Verify given JWT token
	public boolean validateToken(String token) {
		try {
			Jwts.parser().verifyWith(KEY).build().parseSignedClaims(token);
			return true;
		}
		catch (ExpiredJwtException e) {
			log.error("Token has expired");
		}
		catch (IllegalArgumentException e) {
			log.error("Token is null, empty, or only whitespace");
		}
		catch (MalformedJwtException e) {
			log.error("JWT token is invalid");
		}

		return false;
	}

	// Generate JWT token
	public String generateToken(User user) {
		return Jwts.builder()
			// Stores userId in the subject of the token
			.subject(String.format("%s", user.getUserId()))
			// Encodes name and roles in the claims of the token
			.claim("name", user.getName())
			.claim("role", user.getRoles())
			.issuer("project1team")
			.issuedAt(new Date())
			.expiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
			.signWith(KEY)
			.compact();
	}

	// Extract userId from JWT token
	public int extractUserId(String token) {
		return Integer
			.parseInt(Jwts.parser().verifyWith(KEY).build().parseSignedClaims(token).getPayload().getSubject());
	}

	// Extract name from JWT token
	public String extractName(String token) {
		return Jwts.parser().verifyWith(KEY).build().parseSignedClaims(token).getPayload().get("name", String.class);
	}

	// Extract roles from JWT token
	public ArrayList extractRoles(String token) {
		return Jwts.parser().verifyWith(KEY).build().parseSignedClaims(token).getPayload().get("role", ArrayList.class);
	}

}
