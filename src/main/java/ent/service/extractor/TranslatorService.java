package ent.service.extractor;

import org.springframework.stereotype.Service;

@Service
public class TranslatorService {

    /*public static void main(String[] args) throws Exception {
        PemObject privateKeyPem;

        try (PemReader reader = new PemReader(new FileReader("src/main/resources/private.pem"))) {
            privateKeyPem = reader.readPemObject();
        }

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyPem.getContent()));

        String serviceAccountId = "ajesnb4uu827diepfosj";
        String keyId = "ajeb0p3g8bbpa4fun181";

        Instant now = Instant.now();

        String encodedToken = Jwts.builder()
                .setHeaderParam("kid", keyId)
                .setIssuer(serviceAccountId)
                .setAudience("https://iam.api.cloud.yandex.net/iam/v1/tokens")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(1L, ChronoUnit.FOREVER)))
                .signWith(SignatureAlgorithm.PS256, privateKey).compact();

        System.out.println("encodedToken = " + encodedToken);
    }*/

    public String getTranslated(String text) {
        return text;
    }


}
