package com.ferreteria.backend;



import com.ferreteria.backend.model.Producto;
import com.ferreteria.backend.repository.ProductoRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//paquetes para la carga de datos de pruebas
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}


	//Carga de datos automatico para pruebas
	@Bean
	public CommandLineRunner init(ProductoRepository productoRepository) {
		return args -> {
			productoRepository.save(new Producto("Martillo", "Martillo clásico", 100.0, 10));
			productoRepository.save(new Producto("Destornillador", "Destornillador de estrella", 50.0, 25));
			productoRepository.save(new Producto("Clavos", "Caja de 100 clavos", 20.0, 100));
		};
	}
}
