package qualidadedesoftware.projetoqa.service;

import qualidadedesoftware.projetoqa.dto.LoginRequest;
import qualidadedesoftware.projetoqa.dto.UsuarioCreateRequest;
import qualidadedesoftware.projetoqa.dto.UsuarioResponse;

public interface UsuarioService {
    UsuarioResponse cadastrar(UsuarioCreateRequest request);
    UsuarioResponse autenticar(LoginRequest request);
    UsuarioResponse buscarPorEmail(String email);
}