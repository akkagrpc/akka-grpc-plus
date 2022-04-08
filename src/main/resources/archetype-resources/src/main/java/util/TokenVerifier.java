#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.SneakyThrows;

import akka.actor.typed.ActorSystem;
import ${package}.dagger.ActorSystemModule;
import com.typesafe.config.Config;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class TokenVerifier {

    @SneakyThrows
    public static Jws<Claims> parseJwt(String jwtString) {
        PublicKey publicKey = getPublicKey();

        Jws<Claims> jwt = Jwts.parser()
                .setSigningKey(publicKey)
                .parseClaimsJws(jwtString);

        return jwt;
    }

    private static PublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        ActorSystemModule actorSystemModule = new ActorSystemModule();
        ActorSystem<?> system = actorSystemModule.provideActorSystem();
        Config config = system.settings().config();
        String rsaPublicKey = config.getString("jwt.rsa.public-key");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(rsaPublicKey));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey publicKey = kf.generatePublic(keySpec);
        return publicKey;
    }
}
