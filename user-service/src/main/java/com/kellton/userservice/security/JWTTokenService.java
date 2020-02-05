package com.kellton.userservice.security;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JWTTokenService {

	private static final String PRIVATE_KEY = "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";
	public static final String JWT_PREFIX = "Bearer ";

	public String createToken(String userId) throws SQLException, Exception {

		String tokenUniqueId = UUID.randomUUID().toString();

		return createJWT(tokenUniqueId, userId);
	}

	private String createJWT(String id, String userId) throws SQLException, Exception {

		final long nowMillis = System.currentTimeMillis();
		final Date now = new Date(nowMillis);

		byte[] privateKeyBytes = getByteArrayFromString(PRIVATE_KEY);
		PrivateKey privateKey = getPrivateKey(privateKeyBytes);
		final Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("userId", userId);

		// Let's set the JWT Claims
		final JwtBuilder builder = Jwts.builder().setClaims(claims).setId(id).setIssuer("kellton").setIssuedAt(now)
				.setSubject(userId) // username
				.setAudience(userId) // username
				.signWith(SignatureAlgorithm.RS512, privateKey);

		String expiryFromDB = "36000";

		long expMillis = nowMillis + Long.parseLong(expiryFromDB);
		Date exp = new Date(expMillis);
		builder.setExpiration(exp);

		// Builds the JWT and serializes it to a compact, URL-safe string
		JwtToken jwtToken = new JwtToken(JWT_PREFIX + builder.compact());

		return jwtToken.getValue();
	}

	public static byte[] getByteArrayFromString(String key) {
		byte[] keyBytes = Base64.decodeBase64(key);
		return keyBytes;
	}

	public static PrivateKey getPrivateKey(byte[] key) throws NoSuchAlgorithmException, InvalidKeySpecException {
		// X509EncodedKeySpec spec = new X509EncodedKeySpec(key);
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(key);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePrivate(spec);
	}

}
