package com.librasja.libras_api.service;

import com.librasja.libras_api.dto.InterpreterRegistrationDto;
import com.librasja.libras_api.dto.JwtAuthResponseDto;
import com.librasja.libras_api.dto.LoginRequestDto;
import com.librasja.libras_api.dto.RequesterRegistrationDto;
import com.librasja.libras_api.entity.InterpreterProfile;
import com.librasja.libras_api.entity.Role;
import com.librasja.libras_api.entity.User;
import com.librasja.libras_api.repository.InterpreterProfileRepository;
import com.librasja.libras_api.repository.UserRepository;
import com.librasja.libras_api.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final InterpreterProfileRepository interpreterProfileRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public JwtAuthResponseDto registerRequester(RequesterRegistrationDto dto) {
        log.info("Registrando usuário surdo (REQUESTER): {}", dto.getNome());
        User user = createUser(dto.getNome(), dto.getEmail(), dto.getPassword(), Role.REQUESTER);
        return buildAuthResponse(user);
    }

    @Transactional
    public JwtAuthResponseDto registerInterpreter(InterpreterRegistrationDto dto) {
        log.info("Registrando intérprete: {}", dto.getNome());
        User user = createUser(dto.getNome(), dto.getEmail(), dto.getPassword(), Role.INTERPRETER);

        InterpreterProfile profile = InterpreterProfile.builder()
                .interpreter(user)
                .especialidades(dto.getEspecialidades())
                .descricaoCurta(dto.getDescricaoCurta())
                .disponivel(dto.getDisponivel())
                .build();

        interpreterProfileRepository.save(profile);
        log.info("Perfil de intérprete criado para userId={}", user.getId());

        return buildAuthResponse(user);
    }

    public JwtAuthResponseDto login(LoginRequestDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()
                )
        );

        String token = jwtTokenProvider.generateToken(authentication);

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return buildAuthResponse(user, token);
    }

    private User createUser(String nome, String email, String password, Role role) {
        if (userRepository.existsByUsername(nome)) {
            throw new IllegalArgumentException("Nome de usuário já está em uso");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email já está em uso");
        }

        User user = User.builder()
                .username(nome)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(role)
                .active(1)
                .build();

        return userRepository.save(user);
    }

    private JwtAuthResponseDto buildAuthResponse(User user) {
        String token = jwtTokenProvider.generateTokenFromUsername(user.getUsername());
        return buildAuthResponse(user, token);
    }

    private JwtAuthResponseDto buildAuthResponse(User user, String token) {
        return new JwtAuthResponseDto(token, user.getId(), user.getUsername(), user.getRole().toString());
    }
}
