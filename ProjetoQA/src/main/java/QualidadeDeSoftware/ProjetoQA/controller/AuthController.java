package QualidadeDeSoftware.ProjetoQA.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import QualidadeDeSoftware.ProjetoQA.dto.LoginRequest;
import QualidadeDeSoftware.ProjetoQA.dto.UsuarioCreateRequest;
import QualidadeDeSoftware.ProjetoQA.dto.UsuarioResponse;
import QualidadeDeSoftware.ProjetoQA.service.UsuarioService;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String senha,
            HttpSession session) {
        try {
            LoginRequest request = new LoginRequest(email, senha);
            UsuarioResponse usuario = usuarioService.autenticar(request);
            session.setAttribute("usuarioId", usuario.id());
            session.setAttribute("usuarioEmail", usuario.email());
            return "redirect:/livros";
        } catch (Exception e) {
            return "redirect:/login?error";
        }
    }

    @PostMapping("/cadastro")
    public String cadastro(
            @RequestParam String nome,
            @RequestParam String email,
            @RequestParam String senha,
            HttpSession session) {
        try {
            UsuarioCreateRequest request = new UsuarioCreateRequest(nome, email, senha);
            UsuarioResponse usuario = usuarioService.cadastrar(request);
            session.setAttribute("usuarioId", usuario.id());
            session.setAttribute("usuarioEmail", usuario.email());
            return "redirect:/livros";
        } catch (Exception e) {
            return "redirect:/cadastro?error";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
