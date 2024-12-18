package com.eschool.schoolpage.repositories;

import com.eschool.schoolpage.models.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, Long> {
    Materia findTopByOrderByIdDesc();
}
