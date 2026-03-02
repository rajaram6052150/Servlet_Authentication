package util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;

public class JwtUtil {

    private static final String SECRET = System.getenv("jwt_secret");
    private static final long EXPIRATION_MS = 1000 * 60 * 60;

    public static String generateToken(String email) {

        return JWT.create().withSubject(email).withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .sign(Algorithm.HMAC256(SECRET));
    }
}

