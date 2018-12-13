package de.htwBerlin.ai.kbe.storage;

import javax.ws.rs.NotAuthorizedException;
import java.io.IOException;
import java.util.List;

/**
 * Interface for user authentication
 * @author marcel
 *
 */
public interface ITokenCreator {


    boolean authenticate(String userID);



    boolean checkAuth(String auth) throws IOException;


    List<String> getTokenList();



}