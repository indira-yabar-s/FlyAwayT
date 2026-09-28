package com.example.flyawayt.auth.domain;

import com.example.flyawayt.auth.components.JwtService;
import com.example.flyawayt.auth.dto.LoginRequestDto;
import com.example.flyawayt.auth.dto.TokenResponse;
import com.example.flyawayt.exceptions.ConflictException;
import com.example.flyawayt.exceptions.ResourceNotFoundException;
import com.example.flyawayt.user.domain.Role;
import com.example.flyawayt.user.domain.User;
import com.example.flyawayt.user.dto.RegisterRequestDto;
import com.example.flyawayt.user.dto.UserResponseDto;
import com.example.flyawayt.user.infrastructure.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
                       AuthenticationManager authenticationManager, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.modelMapper = modelMapper;
    }

    public UserResponseDto register(RegisterRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Ya existe un usuario con el email: " + request.getEmail());
        }

        User user = modelMapper.map(request, User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);

        return modelMapper.map(user, UserResponseDto.class);
    }

    public TokenResponse login(LoginRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("No existe un usuario con el email: " + request.getEmail()));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        return new TokenResponse(jwtService.generateToken(user));
    }
}
