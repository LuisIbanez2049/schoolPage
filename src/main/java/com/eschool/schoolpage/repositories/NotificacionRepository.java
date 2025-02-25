package com.eschool.schoolpage.repositories;

import com.eschool.schoolpage.models.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
}
