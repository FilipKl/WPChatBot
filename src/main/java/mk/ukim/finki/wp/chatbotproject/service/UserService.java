package mk.ukim.finki.wp.chatbotproject.service;

import mk.ukim.finki.wp.chatbotproject.models.User;

public interface UserService {

    User registerUser(String username, String password);

    User getUserByUsername(String username);

    boolean usernameExists(String username);
}

