package QualidadeDeSoftware.ProjetoQA.controller;

import QualidadeDeSoftware.ProjetoQA.dto.LoginRequest;
import QualidadeDeSoftware.ProjetoQA.dto.UsuarioCreateRequest;
import QualidadeDeSoftware.ProjetoQA.dto.UsuarioResponse;
import QualidadeDeSoftware.ProjetoQA.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/cadastro")
    public UsuarioResponse cadastrar(@Valid @RequestBody UsuarioCreateRequest request) {
        return usuarioService.cadastrar(request);
    }

    @PostMapping("/login")
    public UsuarioResponse login(@Valid @RequestBody LoginRequest request, HttpSession session) {
        UsuarioResponse usuario = usuarioService.autenticar(request);
        session.setAttribute("usuarioId", usuario.id());
        session.setAttribute("usuarioEmail", usuario.email());
        return usuario;
    }

    @PostMapping("/logout")
    public void logout(HttpSession session) {
        session.invalidate();
    }
}