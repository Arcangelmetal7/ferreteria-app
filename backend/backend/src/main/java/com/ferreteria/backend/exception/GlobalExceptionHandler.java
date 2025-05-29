package com.ferreteria.backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
//paquetes para errores 404 no encontrado el recurso
import java.util.NoSuchElementException;
import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> manejarIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        ApiError error = new ApiError(
                400,
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.badRequest().body(error);
    }

/*    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> manejarRuntime(RuntimeException ex, HttpServletRequest request) {
        ApiError error = new ApiError(
                400,
                "Bad Request",
                ex.getMessage(),
               request.getRequestURI()
        );
        return ResponseEntity.badRequest().body(error);
   }*/

    //control de errores 404
    @ExceptionHandler({NoSuchElementException.class, EntityNotFoundException.class})
    public ResponseEntity<ApiError> manejarNotFound(RuntimeException ex, HttpServletRequest request) {
        ApiError error = new ApiError(
                404,
                "Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(404).body(error);
    }

    //control de errores 500 (evita que lleguen errores feos al cliente)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> manejarExcepcionGeneral(Exception ex, HttpServletRequest request) {
        ApiError error = new ApiError(
                500,
                "Internal Server Error",
                "Ha ocurrido un error inesperado.",
                request.getRequestURI()
        );
        ex.printStackTrace(); // Imprime la pila, acordarme de borrar esta linea despues del desarrollo
        return ResponseEntity.status(500).body(error);
    }


}
