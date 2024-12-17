package ma.ensate.gestetudiants.controller;

import lombok.AllArgsConstructor;
import ma.ensate.gestetudiants.dto.auth.LoginRequestDTO;
import ma.ensate.gestetudiants.dto.auth.LoginResponseDTO;
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
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            // Authenticate the user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new RuntimeException("username et password incorrect", e);
        }

        // If authentication is successful, generate JWT
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        String role = userDetails.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
        String access_token = jwtUtil.generateToken(userDetails.getUsername(), role);

        return new LoginResponseDTO(access_token);
    }
}