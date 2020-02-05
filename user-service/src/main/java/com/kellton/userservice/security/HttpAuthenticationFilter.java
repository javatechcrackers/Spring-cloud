package com.kellton.userservice.security;

import java.io.IOException;
import java.lang.reflect.Method;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import com.kellton.userservice.exception.UnauthorizedUserException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Provider
//@Component
public class HttpAuthenticationFilter implements ContainerRequestFilter {

	@Context
	private ResourceInfo resourceInfo;

	@Context
	HttpHeaders headers;

	@Context
	private HttpServletRequest httpServletRequest;
	public static final String JWT_PREFIX = "Bearer ";

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		Method method = resourceInfo.getResourceMethod();

		Optional<AuthFree> authAnnotation = Optional.ofNullable(AnnotationUtils.findAnnotation(method, AuthFree.class));

		if (!authAnnotation.isPresent()) {

			String jwtToken = resolveToken(requestContext);

			if (StringUtils.isBlank(jwtToken)) {

				try {
					throw new UnauthorizedUserException("Unauthorized user.");
				} catch (UnauthorizedUserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			JwtToken token = new JwtToken(jwtToken);

			Optional<Claims> claims = getClaims(token);

			if (!claims.isPresent()) {
				try {
					throw new UnauthorizedUserException("Unauthorized user.");
				} catch (UnauthorizedUserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			boolean isClaimsValid = isClaimsValid(claims.get());

			if (!isClaimsValid) {
				try {
					throw new UnauthorizedUserException("Unauthorized user.");
				} catch (UnauthorizedUserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
			}

		} else {
		}

	}

	public Optional<Claims> getClaims(JwtToken token) {

		Claims claims = null;
		try {
			String publicKeyString = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAiwNFPEzvm42eNhNIS9ZBEvV/X4xgugDCR509WIeL7nT+fTMXJGmcqDpzawpL5RAwmtnx48yXj4SPf+Fy6adjNzfjPWXN8fvlo9gnMaMXS3gec+cGw1c5n8MlQ/+g5DZ6oiCUQd28PfAruq2ctXeTSTgscY+l24PvVfjAQ4cpmB+Y7t6RpePFwvE88nCVbz2LFqk/RPAAeZhz2boUEoHkTt9Um5w05mWwgfc2VW78C4e++Rdwg4qBZmEwFOWmaaO5yaTV1EIiWw2S+2G1tviPeqwvhUo3fK42zLWoqwTl9X++80dmnMD5QXA4OZIWyU0bDLnhh2qky6tGKZNqTq+BBGdHtmTNmPN8jML7mDzXfYE2Q22KL6S/eBapAzidvzWaoS2CDT9lNqHRYy2c7V8RviuJMHVmEDrG8/tBB32M6mw+tknyCW5tpEHU1aXpoF4W6sViI7F5dOhsmzSqSFNzxpHRCZvsHc6liiaDt22RpYXBYUJU4KYzKt1WkAz8NtWXVW33cCCOifciqCnFCn2kOO7R11GfHYfPX6A6ZLyKzl8QTut+UXr442akv5vmz0hw3F0oV4RRB07ezUc4YN7MNsEMfGgGglppwcBY30XAraDwAEVxe93//QFk/PY5A4XksZk/hCeoVtkUg+Swod7K3nW9qA1DRS2gcQHm0rUkHTkCAwEAAQ==";
			byte[] publicKeyBytes = getByteArrayFromString(publicKeyString);
			PublicKey publicKey = getPublicKey(publicKeyBytes);
			claims = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token.getValue()).getBody();
		} catch (JwtException e) {
		} catch (Exception exception) {
		}

		return Optional.ofNullable(claims);

	}

	public static PublicKey getPublicKey(byte[] key) throws NoSuchAlgorithmException, InvalidKeySpecException {
		X509EncodedKeySpec spec = new X509EncodedKeySpec(key);
		// PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(key);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePublic(spec);
	}

	public static byte[] getByteArrayFromString(String key) {
		byte[] keyBytes = Base64.decodeBase64(key);
		return keyBytes;
	}

	private String resolveToken(ContainerRequestContext containerRequestContext) {

		String authorizationHeader = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

		if (StringUtils.isBlank(authorizationHeader)) {

			Map<String, Cookie> cookies = containerRequestContext.getCookies();

			if (cookies.containsKey(HttpHeaders.AUTHORIZATION)) {
				Cookie cookie = cookies.get(HttpHeaders.AUTHORIZATION);
				authorizationHeader = cookie.getValue();

			}

		}

		String jwt = null;
		if (StringUtils.isNotBlank(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {

			jwt = authorizationHeader.replace(JWT_PREFIX, "");
		}
		return jwt;
	}

	public boolean isClaimsValid(Claims claims) {

		boolean claimsValid = false;

		final long nowMillis = System.currentTimeMillis();
		final Date now = new Date(nowMillis);
		claimsValid = true;

		return claimsValid;
	}

}
