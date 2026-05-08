package QualidadeDeSoftware.ProjetoQA.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import QualidadeDeSoftware.ProjetoQA.service.LivroService;
import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {

    private static final String USUARIO_ID = "usuarioId";
    private static final String REDIRECT_LIVROS = "redirect:/livros";
    private static final String REDIRECT_LOGIN = "redirect:/login";

    private final LivroService livroService;

    public PageController(LivroService livroService) {
        this.livroService = livroService;
    }

    @GetMapping("/")
    public String index(HttpSession session) {
        if (session.getAttribute(USUARIO_ID) != null) {
            return REDIRECT_LIVROS;
        }
        return REDIRECT_LOGIN;
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        if (session.getAttribute(USUARIO_ID) != null) {
            return REDIRECT_LIVROS;
        }
        return "login";
    }

    @GetMapping("/cadastro")
    public String cadastro(HttpSession session) {
        if (session.getAttribute(USUARIO_ID) != null) {
            return REDIRECT_LIVROS;
        }
        return "cadastro";
    }

    @GetMapping("/livros")
    public String livros(HttpSession session, Model model) {
        String usuarioId = (String) session.getAttribute(USUARIO_ID);
        if (usuarioId == null) {
            return REDIRECT_LOGIN;
        }
        model.addAttribute(USUARIO_ID, usuarioId);
        model.addAttribute("livros", livroService.listarPorUsuario(usuarioId));
        return "livros";
    }

    @GetMapping("/livros/novo")
    public String novoLivro(HttpSession session, Model model) {
        String usuarioId = (String) session.getAttribute(USUARIO_ID);
        if (usuarioId == null) {
            return REDIRECT_LOGIN;
        }
        model.addAttribute(USUARIO_ID, usuarioId);
        return "livro-form";
    }

    @GetMapping("/livros/editar/{id}")
    public String editarLivro(@PathVariable String id, HttpSession session, Model model) {
        String usuarioId = (String) session.getAttribute(USUARIO_ID);
        if (usuarioId == null) {
            return REDIRECT_LOGIN;
        }
        try {
            model.addAttribute(USUARIO_ID, usuarioId);
            model.addAttribute("livro", livroService.buscarPorId(usuarioId, id));
            return "livro-editar";
        } catch (Exception e) {
            return REDIRECT_LIVROS;
        }
    }
}