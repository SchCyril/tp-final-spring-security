package com.example.demo.controller;

import com.example.demo.models.UserApp;
import com.example.demo.repositories.UserAppRepository;
import com.example.demo.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class LoginController {


    private final JwtService jwtService;
    private final UserAppRepository userAppRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public LoginController(JwtService jwtService, UserAppRepository userAppRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jwtService = jwtService;
        this.userAppRepository = userAppRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserApp userApp) throws Exception {
        Optional<UserApp> userAppOptional = userAppRepository.findByUsername(userApp.getUsername());
        if (userAppOptional.isPresent() && bCryptPasswordEncoder.matches(userApp.getPassword(), userAppOptional.get().getPassword())) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtService.createAuthenticationToken(userAppOptional.get()).toString())
                    .body("connected");
        }
        throw new Exception();
    }

    @PostMapping("/register")
    public void register(@RequestBody UserApp userApp) throws Exception {
        Optional<UserApp> userAppOptional = userAppRepository.findByUsername(userApp.getUsername());
        if (userAppOptional.isEmpty()) {
            userApp.setPassword(bCryptPasswordEncoder.encode(userApp.getPassword()));
            userAppRepository.save(userApp);
        } else {
            throw new Exception("Username already exists");
        }
    }
}
