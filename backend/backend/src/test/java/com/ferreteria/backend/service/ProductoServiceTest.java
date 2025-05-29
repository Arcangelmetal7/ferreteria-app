package com.ferreteria.backend.service;

import com.ferreteria.backend.model.Producto;
import com.ferreteria.backend.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProductoServiceTest {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoRepository productoRepository;

    @BeforeEach
    void limpiarBaseDeDatos() {
        productoRepository.deleteAll();
    }

    @Test
    void testGuardarYRecuperarProducto() {
        // Arrange
        Producto producto = new Producto("Destornillador", "Punta plana", 150.0, 10);

        // Act
        Producto guardado = productoService.guardarProducto(producto);
        Optional<Producto> recuperado = productoService.obtenerProductoPorId(guardado.getId());

        // Assert
        assertTrue(recuperado.isPresent());
        Producto p = recuperado.get();
        assertEquals("Destornillador", p.getNombre());
        assertEquals("Punta plana", p.getDescripcion());
        assertEquals(150.0, p.getPrecio());
        assertEquals(10, p.getStock());
    }

    @Test
    void testEliminarProducto() {
        // Arrange
        Producto producto = new Producto("Taladro", "Con percutor", 1200.0, 5);
        Producto guardado = productoService.guardarProducto(producto);
        Long id = guardado.getId();

        // Aseguramos que existe antes de eliminar
        assertTrue(productoService.obtenerProductoPorId(id).isPresent());

        // Act
        productoService.eliminarProducto(id);

        // Assert
        assertFalse(productoService.obtenerProductoPorId(id).isPresent(), "El producto debería haber sido eliminado");
    }

    @Test
    void testActualizarProducto() {
        // Arrange: Guardar un producto original
        Producto original = new Producto("Llave inglesa", "Ajustable", 500.0, 20);
        Producto guardado = productoService.guardarProducto(original);
        Long id = guardado.getId();

        // Act: Crear un nuevo objeto con los cambios
        Producto actualizado = new Producto("Llave inglesa grande", "Para tuercas grandes", 750.0, 15);
        Producto resultado = productoService.actualizarProducto(id, actualizado);

        // Assert: Verificar que se haya actualizado correctamente
        assertEquals(id, resultado.getId());  // el ID debe mantenerse
        assertEquals("Llave inglesa grande", resultado.getNombre());
        assertEquals("Para tuercas grandes", resultado.getDescripcion());
        assertEquals(750.0, resultado.getPrecio());
        assertEquals(15, resultado.getStock());
    }

}
