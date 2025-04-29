package com.userauth.infrastructure.web;

import com.userauth.application.dto.activation.ActivationRequestDTO;
import com.userauth.application.dto.activation.ActivationResponseDTO;
import com.userauth.domain.service.ActivationService;
import com.userauth.exceptions.InvalidTokenException;
import com.userauth.exceptions.TokenAlreadyUsedException;
import com.userauth.exceptions.TokenExpiredException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Activation", description = "User Activation API")
public class ActivationController {

    private final ActivationService activationService;

    @GetMapping("/activate")
    public ResponseEntity<ActivationResponseDTO> validateToken(@RequestParam String token) {
        try {
            // Decode token in case it contains special characters
            String decodedToken = URLDecoder.decode(token, StandardCharsets.UTF_8.name());
            System.out.println("Received token: " + decodedToken); // Debug log

            try {
                activationService.validateToken(decodedToken);
            } catch (TokenAlreadyUsedException e) {
                throw new RuntimeException(e);
            } catch (TokenExpiredException e) {
                throw new RuntimeException(e);
            }

            return ResponseEntity.ok(new ActivationResponseDTO("Valid token. Please set your password."));
        } catch (Exception e) {
            throw new InvalidTokenException("Error processing token: " + e.getMessage());
        }
    }

    @PostMapping("/activate-pass")
    @Operation(summary = "Activate user account with password")
    public ResponseEntity<ActivationResponseDTO> activateAccount(
            @RequestParam String token,
            @RequestBody ActivationRequestDTO request) {

        try {
            activationService.activateUser(token, request.getPassword());
        } catch (TokenExpiredException e) {
            throw new RuntimeException(e);
        } catch (TokenAlreadyUsedException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(new ActivationResponseDTO("Account successfully activated. You can now log in."));
    }
}
