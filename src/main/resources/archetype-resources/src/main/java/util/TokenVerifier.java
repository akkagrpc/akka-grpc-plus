#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.SneakyThrows;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class TokenVerifier {

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String accessTokenFromKeyCloak = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI0c1Q2VG1PWHIyWjhiTnJuaHZsRkV1TlEwWTh5eG4weDBsTHN5UEl4REo4In0.eyJleHAiOjE2NDQ4MjYxNTQsImlhdCI6MTY0NDgyNTg1NCwianRpIjoiYjdiZmJhYWYtNzVjMC00MjI2LWI2NWUtOGZhOTY0MzM5YTE1IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2F1dGgvcmVhbG1zL0lYTWFzdGVyIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6ImIwOWU3NWUyLTM5YjEtNDIzYS1iMjdkLWM5NzBmMmUxNmMyNCIsInR5cCI6IkJlYXJlciIsImF6cCI6Iml4bGFnb20iLCJzZXNzaW9uX3N0YXRlIjoiYTM5MDhjNWQtZmEwYy00M2JjLWE4ODktZGY0YzViMGYxNzExIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vbG9jYWxob3N0OjkwMDAvKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1peG1hc3RlciIsIml4dXNlciIsIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJzaWQiOiJhMzkwOGM1ZC1mYTBjLTQzYmMtYTg4OS1kZjRjNWIwZjE3MTEiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsInByZWZlcnJlZF91c2VybmFtZSI6InRlc3R1c2VyIn0.LHCA5fNGsXEpoGHBN8zQSXhYAcBoSHQuL1DJ_yTV5rtsUhIrPeNeKJ59XWY6VXadHXbg5ySpaPIoJ1BPx00m6QxcP5QNo8VGyMMLMyH3_2aFicX_8nKd1kBYUt331UL-JJBF19WTAtcA6ZcRHgKQhQ3zwcbftWusqZ2fl_cJYmIAL5I4DCdVVY5oeN1v_2UHGFGsDMC55LBlaybDv1zP-f4mHav2Kn0VS2qcMAUqWV1v_3abKeG1uRhZ6fvgTsvLlg5NNNJrikqich6H9YysU_cajeeOjEOx0hVfA-77Y6bOYxmcDrUF4_T1wBg1bP9H2hY4R4z18d8QFsx4FQFUew";
        Jws<Claims> claims = parseJwt(accessTokenFromKeyCloak);
        System.out.println(claims.toString());
    }

    @SneakyThrows
    public static Jws<Claims> parseJwt(String jwtString) {
        PublicKey publicKey = getPublicKey();

        Jws<Claims> jwt = Jwts.parser()
                .setSigningKey(publicKey)
                .parseClaimsJws(jwtString);

        return jwt;
    }

    private static PublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String rsaPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoWGdoQYz5V+T0DHJlJXzSxJoPnil+j1diyotbzhoRy5ruvemeIGplJNfjcYeIPXUhjZGkCeSeI1pDelwuM/GouzvASAxn0aOnQJCgv/sf8a4YJBB+Rogi4qCtgOrq3xGWx1pWqLbxOtzfO+X9CRFbpq3gqHk2cXktL1CxtJWQ3tmNNX+3cxCJqyBUX5je2MYeXzEiUnS+Tb1DJgi6iKPz2Bf3VNtSjckkTvHA39VhhOb/M5F5+nKeYvyYN1B/QU/ZFl+0CG4NYc95zUzXuI13/k+iPgG1TNY919HvZrnU7l/y53ikmTLG5kuesdvWVG9UAUjABXYjKBEWn7IdrGckQIDAQAB";
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(rsaPublicKey));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey publicKey = kf.generatePublic(keySpec);
        return publicKey;
    }
}
