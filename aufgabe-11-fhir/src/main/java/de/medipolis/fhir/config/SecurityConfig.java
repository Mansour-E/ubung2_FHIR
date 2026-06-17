package de.medipolis.fhir.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;

/**
 * Security Konfiguration fuer FHIR Endpunkte.
 *
 * In Produktion wuerde man OAuth2/SMART on FHIR nutzen.
 * Fuer diese Uebung: BasicAuth mit zwei Rollen.
 *
 * Zwei Rollen:
 *   FHIR_READER  → darf nur GET
 *   FHIR_WRITER  → darf GET und POST
 *
 * Credentials:
 *   fhir-reader / reader-pass-2026
 *   fhir-writer / writer-pass-2026
 *
 * Testen mit curl:
 *   curl -u fhir-reader:reader-pass-2026 http://localhost:8089/fhir/MedicationRequest
 *   curl -u fhir-writer:writer-pass-2026 -X POST http://localhost:8089/fhir/MedicationRequest
 *   curl http://localhost:8089/fhir/MedicationRequest  → 401 Unauthorized
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/fhir/**")
                    .hasAnyRole("FHIR_READER", "FHIR_WRITER")
                .requestMatchers(HttpMethod.POST, "/fhir/**")
                    .hasRole("FHIR_WRITER")
                .anyRequest().authenticated()
            )
            .httpBasic(basic -> {});

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        return new InMemoryUserDetailsManager(
            User.builder()
                .username("fhir-reader")
                .password(encoder.encode("reader-pass-2026"))
                .roles("FHIR_READER")
                .build(),
            User.builder()
                .username("fhir-writer")
                .password(encoder.encode("writer-pass-2026"))
                .roles("FHIR_WRITER")
                .build()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
