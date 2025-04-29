package com.userauth.infrastructure.web;

import com.userauth.application.dto.login.LoginRequestDTO;
import com.userauth.application.dto.login.LoginResponseDTO;
import com.userauth.application.dto.register.RegisterRequestDTO;
import com.userauth.application.dto.register.RegisterResponseDTO;
import com.userauth.domain.model.User;
import com.userauth.domain.ports.EmailServicePort;
import com.userauth.domain.ports.UserServicePort;
import com.userauth.infrastructure.services.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication API")
public class AuthController {

    private final UserServicePort userServicePort;
    private final EmailServicePort emailServicePort;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO request) {
        // 1. Verificar si el usuario ya existe
        User existingUser = userServicePort.findByEmail(request.getEmail());
        if (existingUser != null) {
            RegisterResponseDTO response = new RegisterResponseDTO();
            response.setMessage("Email already in use");
            response.setEmail(request.getEmail());
            return ResponseEntity.badRequest().body(response);
        }

        // 2. Registrar el usuario
        User user = userServicePort.registerUser(request.getFullName(), request.getEmail());

        // 3. Generar token de activación
        String activationToken = UUID.randomUUID().toString();

        // 4. Enviar email de activación
        emailServicePort.sendActivationEmail(user.getEmail(), activationToken);

        // 5. Preparar respuesta
        RegisterResponseDTO response = new RegisterResponseDTO();
        response.setMessage("Registration successful. Please check your email to activate your account.");
        response.setEmail(user.getEmail());

        return ResponseEntity.ok(response);
    }
    @PostMapping("/login")
    @Operation(summary = "Authenticate user and get JWT token")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userServicePort.findByEmail(request.getEmail());
        String jwt = jwtService.generateToken(user);

        return ResponseEntity.ok(new LoginResponseDTO(
                jwt,
                user.getEmail(),
                user.getFullName()
        ));
    }
}