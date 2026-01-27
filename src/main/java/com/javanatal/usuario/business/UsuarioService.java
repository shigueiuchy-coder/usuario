package com.javanatal.usuario.business;

import com.javanatal.usuario.business.conveter.UsuarioConveter;
import com.javanatal.usuario.business.dto.UsuarioDTO;
import com.javanatal.usuario.infrastructure.entity.Usuario;
import com.javanatal.usuario.infrastructure.exception.ConflictExcption;
import com.javanatal.usuario.infrastructure.exception.ResorceNotFoundException;
import com.javanatal.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConveter usuarioConveter;
    private final PasswordEncoder passwordEncoder;


    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){
        emialExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConveter.paraUsusario(usuarioDTO);
        return usuarioConveter.paraUsusarioDTO(
                usuarioRepository.save(usuario)
                );

    }

    public void emialExiste(String email){
        try{
            boolean existe =  verificaEmailExistente(email);
            if(existe){
                throw new ConflictExcption("Email já cadastrado" + email);
            }

        }catch (ConflictExcption e){
            throw new ConflictExcption("Email já cadastrado" + e.getCause());
        }
    }
        public boolean verificaEmailExistente (String email){
            return usuarioRepository.existsByEmail(email);
        }
        public Usuario buscarUsuarioPorEmail(String email){
            return usuarioRepository.findByEmail(email).orElseThrow(
                    () -> new ResorceNotFoundException("Email não encontrado" + email ));
        }
        public void deletaUsuarioPorEmail(String email){
            usuarioRepository.deleteByEmail(email);
        }
}
