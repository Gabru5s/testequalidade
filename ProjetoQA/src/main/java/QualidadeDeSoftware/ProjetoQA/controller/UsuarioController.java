package QualidadeDeSoftware.ProjetoQA.controller;

import org.springframework.web.bind.annotation.RestController;

import QualidadeDeSoftware.ProjetoQA.service.UsuarioService;

@RestController
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
}