package com.javanatal.usuario.business;

import com.javanatal.usuario.business.conveter.UsuarioConveter;
import com.javanatal.usuario.business.dto.EnderecoDTO;
import com.javanatal.usuario.business.dto.TelefoneDTO;
import com.javanatal.usuario.business.dto.UsuarioDTO;
import com.javanatal.usuario.infrastructure.entity.Endereco;
import com.javanatal.usuario.infrastructure.entity.Telefone;
import com.javanatal.usuario.infrastructure.entity.Usuario;
import com.javanatal.usuario.infrastructure.exception.ConflictExcption;
import com.javanatal.usuario.infrastructure.exception.ResorceNotFoundException;
import com.javanatal.usuario.infrastructure.repository.EnderecoRepository;
import com.javanatal.usuario.infrastructure.repository.TelefoneRepository;
import com.javanatal.usuario.infrastructure.repository.UsuarioRepository;
import com.javanatal.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConveter usuarioConveter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;


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

        public UsuarioDTO buscarUsuarioPorEmail(String email){
            try {
                return usuarioConveter.paraUsusarioDTO(
                        usuarioRepository.findByEmail(email)
                                .orElseThrow(
                        () -> new ResorceNotFoundException("Email não encontrado" + email)
                                )
                );
            }catch (ResorceNotFoundException e){
                    throw new ResorceNotFoundException("Email não encontrado " + email);
            }
        }

        public void deletaUsuarioPorEmail(String email){
            usuarioRepository.deleteByEmail(email);
        }

            public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO dto){
               // Aqui busca o emial do usuario atraves do token (tirar a obrigatoriedade do email)
               String email = jwtUtil.extrairEmailToken(token.substring(7));

               //Criptografia de senha
               dto.setSenha(dto.getSenha() !=null ? passwordEncoder.encode(dto.getSenha()): null);

               //busca os dados do usuario no vanco de dados
               Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() ->
                       new ResorceNotFoundException("Email não localizado"));

                //mesclou os dados que recebemos na requisição DTO com os dados do banco de dados
                Usuario usuario = usuarioConveter.updateUsuario(dto, usuarioEntity);

                //Salvou os dados do usuario convertido e depois pegou o retorno e converteu para usuarioDTO
                return usuarioConveter.paraUsusarioDTO(usuarioRepository.save(usuario));

            }

            public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO enderecoDTO){

                Endereco entity = enderecoRepository.findById(idEndereco).orElseThrow(() ->
                        new ResorceNotFoundException("Id não encontrado" + idEndereco));

                Endereco endereco = usuarioConveter.updateEndereco(enderecoDTO, entity);

                return usuarioConveter.paraEnderecoDTO(enderecoRepository.save(endereco));
    }

    public TelefoneDTO atualizaTelefone(Long idTelefone, TelefoneDTO dto){

        Telefone entity = telefoneRepository.findById(idTelefone).orElseThrow(() ->
                new ResorceNotFoundException("Id não encontrado" + idTelefone));


        Telefone telefone = usuarioConveter.updateTelefone(dto, entity);

        return usuarioConveter.paraTelefoneDTO(telefoneRepository.save(telefone));

    }
}
