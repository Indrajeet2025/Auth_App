package com.example.auth.auth_app_backend.security;

import com.example.auth.auth_app_backend.entities.Provider;
import com.example.auth.auth_app_backend.entities.RefreshToken;
import com.example.auth.auth_app_backend.entities.User;
import com.example.auth.auth_app_backend.repositories.RefreshTokenRepository;
import com.example.auth.auth_app_backend.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2SuccessHandler.class);

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final CookieService cookieService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.auth.frontend.success-redirect}")
    private String successRedirect;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        logger.info("Successful OAuth2 Authentication");

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String registrationId = "unknown";
        if (authentication instanceof OAuth2AuthenticationToken token) {
            registrationId = token.getAuthorizedClientRegistrationId();
        }

        User user;

        switch (registrationId) {
            case "google" -> {
                String email = String.valueOf(oAuth2User.getAttributes().getOrDefault("email", ""));
                String name = String.valueOf(oAuth2User.getAttributes().getOrDefault("name", ""));
                String picture = String.valueOf(oAuth2User.getAttributes().getOrDefault("picture", ""));

                if (email == null || email.isBlank()) {
                    throw new IllegalStateException("Google OAuth2 did not provide an email.");
                }

                User newUser = User.builder()
                        .email(email)
                        .name(name)
                        .image(picture)
                        .enabled(true)
                        .provider(Provider.GOOGLE)
                        .build();

                user = userRepository.findByEmail(email)
                        .orElseGet(() -> userRepository.save(newUser));
            }

            case "github" -> {
                String name = String.valueOf(oAuth2User.getAttributes().getOrDefault("name", ""));
                String avatar = String.valueOf(oAuth2User.getAttributes().getOrDefault("avatar_url", ""));

                // GitHub email can be null unless you fetch it separately or request proper scope
                String email = (String) oAuth2User.getAttributes().get("email");
                if (email == null || email.isBlank()) {
                    // fallback (your choice) - better approach is to call GitHub email API
                    email = (name.isBlank() ? "user" : name.replaceAll("\\s+", "").toLowerCase()) + "@github.com";
                }

                User newUser = User.builder()
                        .provider(Provider.GITHUB)
                        .email(email)
                        .name(name)
                        .image(avatar)
                        .enabled(true)
                        .build();

                user = userRepository.findByEmail(email)
                        .orElseGet(() -> userRepository.save(newUser));
            }

            default -> throw new RuntimeException("Invalid provider: " + registrationId);
        }

        // Create refresh token record (jti stored server-side)
        String jti = UUID.randomUUID().toString();

        RefreshToken refreshTokenOb = RefreshToken.builder()
                .jti(jti)
                .user(user)
                .revoked(false)
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds()))
                .build();

        refreshTokenRepository.save(refreshTokenOb);

        // Generate tokens
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user, refreshTokenOb.getJti());

        // Attach refresh token cookie (HttpOnly cookie)
        cookieService.attachRefreshCookie(response, refreshToken, (int) jwtService.getRefreshTtlSeconds());

        // (Optional) If you want, you can also send the access token to frontend via:
        // 1) query param (not recommended), or
        // 2) another cookie, or
        // 3) frontend calls /auth/me or /auth/refresh after redirect.

        // ✅ Redirect to frontend success page (do NOT write to response after redirect)
        response.sendRedirect(successRedirect);
    }
}
