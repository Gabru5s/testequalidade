package qualidadedesoftware.projetoqa.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SessionAuthenticationFilter sessionAuthenticationFilter() {
        return new SessionAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {

        http

            // DESABILITA CSRF
            .csrf(csrf -> csrf.disable())

            // FILTRO DE SESSÃO
            .addFilterBefore(
                sessionAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class
            )

            // ROTAS
            .authorizeHttpRequests(auth -> auth

                // =========================
                // ROTAS PÚBLICAS
                // =========================
                .requestMatchers(
                    "/",
                    "/login",
                    "/cadastro",
                    "/logout"
                ).permitAll()

                // =========================
                // ARQUIVOS ESTÁTICOS
                // =========================
                .requestMatchers(

                    "/styles.css",
                    "/script.js",

                    "/cadastro_styles.css",
                    "/cadastro_script.js",

                    "/login_styles.css",
                    "/login_script.js",

                    "/livros_styles.css",
                    "/livros_script.js",

                    "/livro-form_styles.css",
                    "/livro-form_script.js",

                    "/livro-editar_styles.css",
                    "/livro-editar_script.js",

                    "/images/**",
                    "/favicon.ico"
                ).permitAll()

                // =========================
                // ROTAS AUTENTICADAS
                // =========================
                .requestMatchers(
                    "/livros/**",
                    "/api/**"
                ).authenticated()

                // QUALQUER OUTRA ROTA
                .anyRequest().authenticated()
            )

            // =========================
            // LOGOUT
            // =========================
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true)
                .permitAll()
            )

            // =========================
            // SESSÃO
            // =========================
            .sessionManagement(session ->
                session.maximumSessions(1)
            );

        return http.build();
    }
}