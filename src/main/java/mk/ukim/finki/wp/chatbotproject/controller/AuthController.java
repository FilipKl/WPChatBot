package mk.ukim.finki.wp.chatbotproject.controller;

import mk.ukim.finki.wp.chatbotproject.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for authentication-related HTTP requests.
 * Handles user registration and login page display.
 * Spring Security handles the authentication logic for POST /login.
 */
@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Display the login page.
     * GET /login
     *
     * @param error optional error parameter from Spring Security on failed login
     * @param model the model to pass data to the view
     * @return the login view name
     */
    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        return "login";
    }

    /**
     * Display the registration page.
     * GET /register
     *
     * @return the register view name
     */
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    /**
     * Handle user registration.
     * POST /register
     * Form parameters: username, password, confirmPassword
     *
     * Validates that:
     * - Username is not already taken
     * - Passwords match
     * - Passwords are not empty
     *
     * On success, redirects to login page.
     * On error, redirects back to register page with error message.
     *
     * @param username the desired username
     * @param password the password
     * @param confirmPassword the password confirmation
     * @param model the model to pass data to the view
     * @return redirect to login on success, or back to register on error
     */
    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model) {

        try {
            if (username == null || username.trim().isEmpty()) {
                model.addAttribute("error", "Username cannot be empty");
                return "register";
            }

            if (password == null || password.trim().isEmpty()) {
                model.addAttribute("error", "Password cannot be empty");
                return "register";
            }

            if (!password.equals(confirmPassword)) {
                model.addAttribute("error", "Passwords do not match");
                return "register";
            }

            userService.registerUser(username, password);

            return "redirect:/login";

        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "register";
        }
    }
}

