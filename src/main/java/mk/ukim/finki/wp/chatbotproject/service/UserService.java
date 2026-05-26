package mk.ukim.finki.wp.chatbotproject.service;

import mk.ukim.finki.wp.chatbotproject.models.User;

/**
 * Service interface for User operations.
 */
public interface UserService {

    /**
     * Register a new user with username and password.
     * The password is automatically BCrypt encoded.
     *
     * @param username the desired username
     * @param password the plain text password
     * @return the created User entity
     * @throws IllegalArgumentException if username is already taken
     */
    User registerUser(String username, String password);

    /**
     * Find a user by username.
     *
     * @param username the username to search for
     * @return the User entity
     * @throws IllegalArgumentException if user not found
     */
    User getUserByUsername(String username);

    /**
     * Check if a username already exists in the database.
     *
     * @param username the username to check
     * @return true if the username exists, false otherwise
     */
    boolean usernameExists(String username);
}

