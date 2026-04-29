package QualidadeDeSoftware.ProjetoQA.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import QualidadeDeSoftware.ProjetoQA.service.LivroService;
import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {

    private final LivroService livroService;

    public PageController(LivroService livroService) {
        this.livroService = livroService;
    }

    @GetMapping("/")
    public String index(HttpSession session) {
        if (session.getAttribute("usuarioId") != null) {
            return "redirect:/livros";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        if (session.getAttribute("usuarioId") != null) {
            return "redirect:/livros";
        }
        return "login";
    }

    @GetMapping("/cadastro")
    public String cadastro(HttpSession session) {
        if (session.getAttribute("usuarioId") != null) {
            return "redirect:/livros";
        }
        return "cadastro";
    }

    @GetMapping("/livros")
    public String livros(HttpSession session, Model model) {
        String usuarioId = (String) session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuarioId", usuarioId);
        model.addAttribute("livros", livroService.listarPorUsuario(usuarioId));
        return "livros";
    }

    @GetMapping("/livros/novo")
    public String novoLivro(HttpSession session, Model model) {
        String usuarioId = (String) session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuarioId", usuarioId);
        return "livro-form";
    }

    @GetMapping("/livros/editar/{id}")
    public String editarLivro(@PathVariable String id, HttpSession session, Model model) {
        String usuarioId = (String) session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return "redirect:/login";
        }
        try {
            model.addAttribute("usuarioId", usuarioId);
            model.addAttribute("livro", livroService.buscarPorId(usuarioId, id));
            return "livro-editar";
        } catch (Exception e) {
            return "redirect:/livros";
        }
    }
}