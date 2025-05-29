package com.ferreteria.backend.controller;

import com.ferreteria.backend.model.Producto;
import com.ferreteria.backend.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")  // Permite peticiones desde cualquier origen (útil para el frontend)

public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<Producto> listarProductos() {
        return productoService.listarProductos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable Long id) {
        Producto producto = productoService.obtenerProductoPorId(id)
                .orElseThrow(() -> new NoSuchElementException("Producto con ID " + id + " no encontrado"));
        return ResponseEntity.ok(producto);
    }


    @PostMapping
    public Producto crearProducto(@RequestBody Producto producto) {
        return productoService.guardarProducto(producto);
    }

    //Modificar producto con todos los campos
    @PutMapping("/{id}")
    public Producto actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        return productoService.actualizarProducto(id, producto);
    }
    //Modificar parcialmente algunos campos de un producto
    @PatchMapping("/{id}")
    public Producto actualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> campos) {
        return productoService.actualizarParcialmente(id, campos);
    }




    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }



}
