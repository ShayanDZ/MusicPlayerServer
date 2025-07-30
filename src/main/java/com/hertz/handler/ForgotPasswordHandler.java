package com.hertz.handler;

import com.hertz.model.ResetToken;
import com.hertz.model.User;
import com.hertz.repository.ResetTokenRepository;
import com.hertz.repository.UserRepository;
import com.hertz.utils.EmailUtils;
import com.hertz.utils.PasswordUtils;
import com.hertz.utils.TokenUtils;

import java.time.LocalDateTime;

public class ForgotPasswordHandler {
    private final UserRepository userRepository;
    private final ResetTokenRepository resetTokenRepository;

    public ForgotPasswordHandler(UserRepository userRepository, ResetTokenRepository resetTokenRepository) {
        this.userRepository = userRepository;
        this.resetTokenRepository = resetTokenRepository;
    }

    public String forgotPassword(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User with email not found");
        }

        // Generate a secure token and set expiration time
        String token = TokenUtils.generateSecureToken();
        ResetToken resetToken = new ResetToken(user.getUsername(), token, LocalDateTime.now().plusHours(1));

        // Save the reset token in the repository
        resetTokenRepository.saveResetToken(resetToken);

        // Send the reset link via email
        String resetLink = "http://localhost:8080/reset-password?token=" + token;
        EmailUtils.sendEmail(email, "Password Reset Request", "Click the link to reset your password: " + resetLink);

        return "Password reset link sent to your email.";
    }

    public String resetPassword(String token, String newPassword) {
        ResetToken resetToken = resetTokenRepository.findByToken(token);

        if (resetToken == null || resetToken.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invalid or expired token");
        }

        User user = userRepository.findByUsername(resetToken.getUsername());
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Hash and update the new password
        String hashedPassword = PasswordUtils.hashPassword(newPassword);
        user.setHashedPassword(hashedPassword);

        // Update the user in the repository
        userRepository.updateUser(user);

        // Remove the used reset token
        resetTokenRepository.deleteResetToken(token);

        return "Password reset successfully.";
    }
}