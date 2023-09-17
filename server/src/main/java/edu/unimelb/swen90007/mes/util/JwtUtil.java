package edu.unimelb.swen90007.mes.util;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import edu.unimelb.swen90007.mes.constants.Constant;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class JwtUtil {
    private static JwtUtil instance;
    private JwtDecoder decoder;
    private JwtEncoder encoder;

    private static final Logger logger = LogManager.getLogger(JwtUtil.class);

    private JwtUtil() {
        try {
            RSAPublicKey publicKey = RSAKeyReader.readPublicKey();
            RSAPrivateKey privateKey = RSAKeyReader.readPrivateKey();
            decoder = NimbusJwtDecoder.withPublicKey(publicKey).build();
            JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
            JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
            encoder = new NimbusJwtEncoder(jwks);
        } catch (Exception e) {
            logger.error("Error reading key pairs: " + e.getMessage());
        }
    }

    public static JwtUtil getInstance() {
        if (instance == null) {
            instance = new JwtUtil();
        }
        return instance;
    }

    public JwtDecoder getDecoder() {
        return decoder;
    }

    public JwtEncoder getEncoder() {
        return encoder;
    }

    public Integer getUserId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String jwtToken = authHeader.split(" ")[1];
        return ((Long) decoder.decode(jwtToken).getClaim(Constant.JWT_USER_ID_CLAIM)).intValue();
    }
}
