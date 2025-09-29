package com.worksta.backend.api.v1;

import com.worksta.backend.api.v1.dto.AuthenticationRequest;
import com.worksta.backend.api.v1.dto.AuthenticationResponse;
import com.worksta.backend.api.v1.dto.RegistrationRequest;
import com.worksta.backend.session.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthAPIController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final InMemoryUserDetailsManager userStore;
    private final PasswordEncoder encoder;

    public AuthAPIController(AuthenticationManager authenticationManager,
                             JwtService jwtService,
                             InMemoryUserDetailsManager userStore,
                             PasswordEncoder encoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userStore = userStore;
        this.encoder = encoder;
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest req) {
        Authentication auth = new UsernamePasswordAuthenticationToken(req.username(), req.password());
        Authentication result = authenticationManager.authenticate(auth); // throws if bad creds
        String role = result.getAuthorities().iterator().next().getAuthority(); // ROLE_WORKER / ROLE_BUSINESS
        String token = jwtService.generateToken(result.getName(), role);
        return new AuthenticationResponse(token);
    }

    /**
     * Stateless logout: client just drops the token. We answer 200 OK.
     */
    @PostMapping("/register")
    public void register(@RequestBody RegistrationRequest req) {
        // must pick exactly one role
        if (req.isWorker() == req.isBusiness()) {
            throw new IllegalArgumentException("Choose exactly one role: WORKER or BUSINESS");
        }
        if (userStore.userExists(req.username())) {
            throw new IllegalArgumentException("Username already taken");
        }
        String role = req.isWorker() ? "WORKER" : "BUSINESS";
        UserDetails user = User.withUsername(req.username())
                .password(encoder.encode(req.password()))
                .roles(role)
                .build();
        userStore.createUser(user);
    }

    @PostMapping("/logout")
    public void logout() {
        // stateless – client drops JWT
    }
}
