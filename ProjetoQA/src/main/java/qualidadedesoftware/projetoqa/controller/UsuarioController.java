package qualidadedesoftware.projetoqa.controller;

import org.springframework.web.bind.annotation.RestController;

import qualidadedesoftware.projetoqa.service.UsuarioService;


@RestController
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
}