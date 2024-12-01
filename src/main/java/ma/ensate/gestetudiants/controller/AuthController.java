package ma.ensate.gestetudiants.controller;

import lombok.AllArgsConstructor;
import ma.ensate.gestetudiants.dto.AuthRequest;
import ma.ensate.gestetudiants.dto.AuthResponse;
import ma.ensate.gestetudiants.service.impl.CustomUserDetailsService;
import ma.ensate.gestetudiants.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest authRequest) {
        try {
            // Authenticate the user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Incorrect username or password", e);
        }

        // If authentication is successful, generate JWT
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        String role = userDetails.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
        String jwt = jwtUtil.generateToken(userDetails.getUsername(), role);

        return new AuthResponse(jwt);
    }
}