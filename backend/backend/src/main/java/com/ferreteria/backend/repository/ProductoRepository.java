package com.ferreteria.backend.repository;

import com.ferreteria.backend.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>{
    /* Otras consultas */
}
