package com.javanatal.usuario.infrastructure.repository;

import com.javanauta.aprendendospring.infrastructore.entity.Telefone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelefoneRepository extends JpaRepository<Telefone, Long> {
}
