package com.userauth.infrastructure.web;

import com.userauth.application.dto.forgot.ForgotPasswordRequestDTO;
import com.userauth.application.dto.forgot.GenericResponseDTO;
import com.userauth.application.dto.forgot.ResetPasswordRequestDTO;
import com.userauth.domain.service.PasswordResetService;
import com.userauth.exceptions.InvalidTokenException;
import com.userauth.exceptions.TokenAlreadyUsedException;
import com.userauth.exceptions.TokenExpiredException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "PasswordReset", description = "Password Reset API")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/forgot")
    @Operation(summary = "Request password reset")
    public ResponseEntity<GenericResponseDTO> forgotPassword(@RequestBody ForgotPasswordRequestDTO request) {
        passwordResetService.requestPasswordReset(request.getEmail());
        return ResponseEntity.ok(
                new GenericResponseDTO("If the email exists in our system, you will receive a link to reset your password.")
        );
    }

    @GetMapping("/reset-validate")
    @Operation(summary = "Validate reset token")
    public ResponseEntity<GenericResponseDTO> validateResetToken(@RequestParam String token) {
        try {
            passwordResetService.validateToken(token);
            return ResponseEntity.ok(new GenericResponseDTO("Valid token"));
        } catch (InvalidTokenException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new GenericResponseDTO("Invalid token"));
        } catch (TokenAlreadyUsedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new GenericResponseDTO("This token has already been used"));
        } catch (TokenExpiredException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new GenericResponseDTO("The token has expired"));
        }
    }

    @PostMapping("/reset-pass")
    @Operation(summary = "Reset password")
    public ResponseEntity<GenericResponseDTO> resetPassword(
            @RequestParam String token,
            @RequestBody ResetPasswordRequestDTO request) {
        try {
            passwordResetService.resetPassword(token, request.getNewPassword());
            return ResponseEntity.ok(
                    new GenericResponseDTO("Password successfully updated")
            );
        } catch (InvalidTokenException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new GenericResponseDTO("Invalid token"));
        } catch (TokenAlreadyUsedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new GenericResponseDTO("This token has already been used"));
        } catch (TokenExpiredException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new GenericResponseDTO("The token has expired"));
        }
    }
}
