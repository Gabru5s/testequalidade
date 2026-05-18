package qualidadedesoftware.projetoqa.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

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
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        /*
         * CSRF via HttpSession (padrão do Spring Security).
         *
         * O token fica na sessão do servidor — sem cookie httpOnly=false,
         * sem hotspot no Sonar.
         *
         * O Thymeleaf injeta _csrf em formulários com th:action automaticamente.
         *
         * Para chamadas fetch/AJAX, o token é exposto via meta tags no HTML:
         *   <meta name="_csrf" th:content="${_csrf.token}"/>
         *   <meta name="_csrf_header" th:content="${_csrf.headerName}"/>
         * e lido pelo JS via document.querySelector('meta[name="_csrf"]').content
         */
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();

        http
                .csrf(csrf -> csrf
                        .csrfTokenRequestHandler(requestHandler)
                )

                .addFilterBefore(
                        sessionAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class
                )

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/",
                                "/login",
                                "/cadastro",
                                "/logout",
                                 "/api/cep/**"
                        ).permitAll()

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

                        .requestMatchers(
                                "/livros/**",
                                "/api/**"
                        ).authenticated()

                        .anyRequest().authenticated()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .clearAuthentication(true)
                        .permitAll()
                )

                .sessionManagement(session ->
                        session.maximumSessions(1)
                );

        return http.build();
    }
}