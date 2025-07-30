package com.hertz.handler;

import com.hertz.model.ResetCode;
import com.hertz.model.User;
import com.hertz.repository.ResetCodeRepository;
import com.hertz.repository.UserRepository;
import com.hertz.utils.EmailUtils;

import java.time.LocalDateTime;
import java.util.Random;

public class ForgotPasswordHandler {
    private final UserRepository userRepository;
    private final ResetCodeRepository resetCodeRepository;

    public ForgotPasswordHandler(UserRepository userRepository, ResetCodeRepository resetCodeRepository) {
        this.userRepository = userRepository;
        this.resetCodeRepository = resetCodeRepository;
    }

    public String forgotPassword(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User with email not found");
        }

        // Generate a 6-digit code
        String code = String.format("%06d", new Random().nextInt(1000000));
        ResetCode resetCode = new ResetCode(email, code, LocalDateTime.now().plusMinutes(10));

        // Save the reset code in the repository
        resetCodeRepository.saveResetCode(resetCode);

        // Send the code via email
        EmailUtils.sendEmail(email, "Password Reset Code", "Your reset code is: " + code);

        return "Reset code sent to your email.";
    }

    public boolean verifyCode(String email, String code) {
        ResetCode resetCode = resetCodeRepository.findByEmail(email);

        if (resetCode == null || resetCode.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invalid or expired code");
        }

        if (!resetCode.getCode().equals(code)) {
            throw new IllegalArgumentException("Incorrect code");
        }

        // Code is valid, remove it from the repository
        resetCodeRepository.deleteResetCode(email);
        return true;
    }
}