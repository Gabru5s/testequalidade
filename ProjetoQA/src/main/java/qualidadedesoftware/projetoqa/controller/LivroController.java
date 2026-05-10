package qualidadedesoftware.projetoqa.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import qualidadedesoftware.projetoqa.dto.LivroCreateRequest;
import qualidadedesoftware.projetoqa.dto.LivroResponse;
import qualidadedesoftware.projetoqa.dto.LivroUpdateRequest;
import qualidadedesoftware.projetoqa.service.LivroService;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @PostMapping
    public LivroResponse criar(jakarta.servlet.http.HttpSession session,
                               @RequestHeader(value = "X-Usuario-Id", required = false) String usuarioIdHeader,
                               @Valid @RequestBody LivroCreateRequest request) {
        String usuarioId = resolveUsuarioId(session, usuarioIdHeader);
        return livroService.criar(usuarioId, request);
    }

    @GetMapping
    public List<LivroResponse> listar(jakarta.servlet.http.HttpSession session,
                                      @RequestHeader(value = "X-Usuario-Id", required = false) String usuarioIdHeader) {
        String usuarioId = resolveUsuarioId(session, usuarioIdHeader);
        return livroService.listarPorUsuario(usuarioId);
    }

    @GetMapping("/{id}")
    public LivroResponse buscar(jakarta.servlet.http.HttpSession session,
                                @RequestHeader(value = "X-Usuario-Id", required = false) String usuarioIdHeader,
                                @PathVariable String id) {
        String usuarioId = resolveUsuarioId(session, usuarioIdHeader);
        return livroService.buscarPorId(usuarioId, id);
    }

    @PutMapping("/{id}")
    public LivroResponse atualizar(jakarta.servlet.http.HttpSession session,
                                   @RequestHeader(value = "X-Usuario-Id", required = false) String usuarioIdHeader,
                                   @PathVariable String id,
                                   @Valid @RequestBody LivroUpdateRequest request) {
        String usuarioId = resolveUsuarioId(session, usuarioIdHeader);
        return livroService.atualizar(usuarioId, id, request);
    }

    @DeleteMapping("/{id}")
    public void remover(jakarta.servlet.http.HttpSession session,
                        @RequestHeader(value = "X-Usuario-Id", required = false) String usuarioIdHeader,
                        @PathVariable String id) {
        String usuarioId = resolveUsuarioId(session, usuarioIdHeader);
        livroService.remover(usuarioId, id);
    }
    
    private String resolveUsuarioId(jakarta.servlet.http.HttpSession session, String header) {
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getName() != null) {
            return auth.getName();
        }
        String usuarioId = (String) session.getAttribute("usuarioId");
        if (usuarioId != null) {
            return usuarioId;
        }
        if (header != null) {
            return header;
        }
        throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Usuário não autenticado");
    }
}