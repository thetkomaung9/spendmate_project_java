package app.service;

import app.dao.UserDAO;
import app.model.User;

/**
 * Service class for user authentication operations.
 * Provides business logic for login and signup.
 */
public class UserService {

    private final UserDAO userDAO;
    private User currentUser;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
        this.currentUser = null;
    }

    /**
     * Attempt to log in a user.
     * 
     * @param username Username
     * @param password Password
     * @return LoginResult with success status and message
     */
    public LoginResult login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            return new LoginResult(false, "Username is required");
        }
        if (password == null || password.isEmpty()) {
            return new LoginResult(false, "Password is required");
        }

        User user = userDAO.authenticate(username.trim(), password);
        
        if (user != null) {
            this.currentUser = user;
            return new LoginResult(true, "Login successful! Welcome, " + user.getUsername());
        } else {
            return new LoginResult(false, "Invalid username or password");
        }
    }

    /**
     * Register a new user.
     * 
     * @param username Username
     * @param password Password
     * @param confirmPassword Password confirmation
     * @param email Email address
     * @return SignupResult with success status and message
     */
    public SignupResult signup(String username, String password, String confirmPassword, String email) {
        // Validate username
        if (username == null || username.trim().isEmpty()) {
            return new SignupResult(false, "Username is required");
        }
        if (username.trim().length() < 3) {
            return new SignupResult(false, "Username must be at least 3 characters");
        }
        if (username.trim().length() > 20) {
            return new SignupResult(false, "Username must be less than 20 characters");
        }
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            return new SignupResult(false, "Username can only contain letters, numbers, and underscores");
        }

        // Validate password
        if (password == null || password.isEmpty()) {
            return new SignupResult(false, "Password is required");
        }
        if (password.length() < 6) {
            return new SignupResult(false, "Password must be at least 6 characters");
        }
        if (!password.equals(confirmPassword)) {
            return new SignupResult(false, "Passwords do not match");
        }

        // Validate email
        if (email == null || email.trim().isEmpty()) {
            return new SignupResult(false, "Email is required");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return new SignupResult(false, "Please enter a valid email address");
        }

        // Check if username exists
        if (userDAO.usernameExists(username.trim())) {
            return new SignupResult(false, "Username already taken");
        }

        // Check if email exists
        if (userDAO.emailExists(email.trim())) {
            return new SignupResult(false, "Email already registered");
        }

        // Create user
        User newUser = new User(username.trim(), password, email.trim());
        boolean success = userDAO.registerUser(newUser);

        if (success) {
            return new SignupResult(true, "Account created successfully! Please log in.");
        } else {
            return new SignupResult(false, "Failed to create account. Please try again.");
        }
    }

    /**
     * Log out the current user.
     */
    public void logout() {
        this.currentUser = null;
    }

    /**
     * Get the currently logged-in user.
     * 
     * @return Current user or null if not logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Check if a user is currently logged in.
     * 
     * @return true if logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Result class for login operations.
     */
    public static class LoginResult {
        private final boolean success;
        private final String message;

        public LoginResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }

    /**
     * Result class for signup operations.
     */
    public static class SignupResult {
        private final boolean success;
        private final String message;

        public SignupResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }
}
