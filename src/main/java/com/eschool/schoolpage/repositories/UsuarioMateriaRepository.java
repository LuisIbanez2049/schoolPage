package com.eschool.schoolpage.repositories;

import com.eschool.schoolpage.models.UsuarioMateria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioMateriaRepository extends JpaRepository<UsuarioMateria, Long> {
}
