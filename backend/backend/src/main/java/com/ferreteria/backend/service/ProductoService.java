package com.ferreteria.backend.service;

import com.ferreteria.backend.model.Producto;
import com.ferreteria.backend.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
//paquetes para actualizar parcialmente un producto
import java.lang.reflect.Field;
import org.springframework.util.ReflectionUtils;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id);
    }

    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto actualizarProducto(Long id, Producto productoActualizado) {
        return productoRepository.findById(id).map(producto -> {
            producto.setNombre(productoActualizado.getNombre());
            producto.setDescripcion(productoActualizado.getDescripcion());
            producto.setPrecio(productoActualizado.getPrecio());
            producto.setStock(productoActualizado.getStock());
            return productoRepository.save(producto);
        }).orElseThrow(() -> new NoSuchElementException("Producto no encontrado con id " + id));
    }

    //Hace validaciones por si trata de mandar atributos que no tiene la entidad y tambien
    //si manda valores que no son del mismo tipo de dato del atributo.
    public Producto actualizarParcialmente(Long id, Map<String, Object> campos) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Producto no encontrado"));

        for (Map.Entry<String, Object> entrada : campos.entrySet()) {
            String clave = entrada.getKey();
            Object valor = entrada.getValue();

            Field campo = ReflectionUtils.findField(Producto.class, clave);
            if (campo == null) {
                throw new IllegalArgumentException("Campo '" + clave + "' no existe en Producto.");
            }

            campo.setAccessible(true);

            Class<?> tipoEsperado = campo.getType();
            if (valor != null && !tipoEsperado.isInstance(valor)) {
                // Hacer conversión explícita si es necesario
                try {
                    Object valorConvertido = convertirValor(valor, tipoEsperado);
                    ReflectionUtils.setField(campo, producto, valorConvertido);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Tipo inválido para el campo '" + clave + "'");
                }
            } else {
                ReflectionUtils.setField(campo, producto, valor);
            }
        }

        return productoRepository.save(producto);
    }

    //parsea a string si recibe numero en un atributo que es de tipo string
    private Object convertirValor(Object valor, Class<?> tipoEsperado) {
        if (tipoEsperado == Double.class || tipoEsperado == double.class) {
            return Double.valueOf(valor.toString());
        } else if (tipoEsperado == Integer.class || tipoEsperado == int.class) {
            return Integer.valueOf(valor.toString());
        } else if (tipoEsperado == Long.class || tipoEsperado == long.class) {
            return Long.valueOf(valor.toString());
        } else if (tipoEsperado == String.class) {
            return valor.toString();
        } else {
            throw new IllegalArgumentException("No se puede convertir el tipo para " + tipoEsperado.getSimpleName());
        }
    }


    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new NoSuchElementException("Producto con ID " + id + " no encontrado");
        }
        productoRepository.deleteById(id);
    }

}
