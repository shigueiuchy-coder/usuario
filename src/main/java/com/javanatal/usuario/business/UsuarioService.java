package com.javanatal.usuario.business;

import com.javanatal.usuario.business.conveter.UsuarioConveter;
import com.javanatal.usuario.business.dto.UsuarioDTO;
import com.javanatal.usuario.infrastructure.entity.Usuario;
import com.javanatal.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConveter usuarioConveter;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){
        Usuario usuario = usuarioConveter.paraUsusario(UsuarioDTO);
        return usuarioConveter.paraUsusarioDTO(
                usuarioRepository.save(usuario);
                );

    }
}
