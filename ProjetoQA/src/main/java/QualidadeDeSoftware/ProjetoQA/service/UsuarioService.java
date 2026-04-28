package QualidadeDeSoftware.ProjetoQA.service;

import QualidadeDeSoftware.ProjetoQA.dto.LoginRequest;
import QualidadeDeSoftware.ProjetoQA.dto.UsuarioCreateRequest;
import QualidadeDeSoftware.ProjetoQA.dto.UsuarioResponse;

public interface UsuarioService {
    UsuarioResponse cadastrar(UsuarioCreateRequest request);
    UsuarioResponse autenticar(LoginRequest request);
    UsuarioResponse buscarPorEmail(String email);
}