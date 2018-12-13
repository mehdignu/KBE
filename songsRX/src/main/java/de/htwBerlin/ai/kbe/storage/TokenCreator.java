package de.htwBerlin.ai.kbe.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htwBerlin.ai.kbe.pojo.Contact;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TokenCreator implements ITokenCreator{

    private static TokenCreator singleInstance = null;
    private static String token = null;
    private static List<String> tokenList;
    private static final Logger LOG = Logger.getLogger(InMemorySongsPersistence.class);

    private TokenCreator() {
        LOG.info("Constructing Token Generator");

        tokenList = new ArrayList<>();
        tokenList.add("toktok");
    }

    public synchronized static TokenCreator getInstance() {
        if (singleInstance == null) {
            singleInstance = new TokenCreator();
        }
        return singleInstance;
    }

    /**
     * authenticate a specific user
     * @param userID
     * @return
     */
    @Override
    public boolean authenticate(String userID) {
        List<Contact> contactsList = new ArrayList<>();
        try {

            String fileName = getClass().getClassLoader().getResource("contacts.json").getFile();

            ObjectMapper objectMapper = new ObjectMapper();
            try (InputStream is = new BufferedInputStream(new FileInputStream(fileName))) {
                contactsList = objectMapper.readValue(is, new TypeReference<List<Contact>>() {
                });
            }

        } catch (IOException e) {
            LOG.debug("Failed to generate token");
        }
        for (Contact c : contactsList) {
            if (c.getUserId().equals(userID)) {
                generateToken(userID);
                return true;
            }
        }
        return false;
    }




    /**
     * generate a token
     * @param userId
     * @return
     */
    private String generateToken(String userId) {
        MessageDigest md;
        try {

            md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest((userId + UUID.randomUUID().toString()).getBytes(StandardCharsets.UTF_8));

            token = Base64.getEncoder().encodeToString(hash);
            tokenList.add("toktok");//for test purposes

            tokenList.add(token);
            return token;

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * check if the user is authenticated
     * @param auth
     * @return
     * @throws IOException
     */
    @Override
    public boolean checkAuth(String auth) throws IOException {

        if (auth != null && !auth.equals("")) {
            List<String> tokenList = getTokenList();
            return tokenList.stream().anyMatch(t -> (t.equals(auth)));
        }
        return false;
    }


    public static void addTokenList(String token) {
        tokenList.add(token);
    }

    @Override
    public List<String> getTokenList() {
        return tokenList;
    }



    public static String getToken() {
        return token;
    }
}
