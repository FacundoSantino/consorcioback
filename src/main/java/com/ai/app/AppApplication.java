package com.ai.app;

import com.ai.app.controller.*;
import com.ai.app.negocio.*;
import org.junit.Test;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class AppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

	//TODO revisar el dominio entregado e implementar cosas de las notas

	//--------------------------------------Edificio--------------------------------------

	//C
/*
	@Bean CommandLineRunner crearEdificio(EdificioController controller){
		return args -> {
			String nombre="Torre SQL";
			String direccion="Av. Puerto 1433";
			Edificio edificio=controller.crearEdificio(nombre,direccion);

			System.out.println("------------------");
			System.out.println("Edificio creado");
			System.out.println("Nombre: "+edificio.getNombre());
			System.out.println("Dirección: "+edificio.getDireccion());
			System.out.println("------------------");
		};
	}


 */
/*

	@Bean CommandLineRunner guardarEdificio(EdificioController controller){
		return args -> {
			String nombre="Torre SQL";
			String direccion="Av. Puerto 1433";
			Edificio edificio=controller.crearEdificio(nombre,direccion);

			controller.guardar(edificio);

			System.out.println("Nombre del edificio guardado: "+controller.buscarEdificio(nombre,direccion).getNombre());


		};
	}



 */

	//R
/*
	@Bean CommandLineRunner leerEdificio(EdificioController controller){
		return args -> {

			Edificio edificio=controller.leerEdificio(1);

			System.out.println("------------------");
			System.out.println("Edificio leido");
			System.out.println("Nombre: "+edificio.getNombre());
			System.out.println("Dirección: "+edificio.getDireccion());
			System.out.println("------------------");

			List<Edificio> edificios=controller.leerEdificios();

			System.out.println(" ");

			for(Edificio e: edificios){
				System.out.println("------------------");
				System.out.println("Edificio leido");
				System.out.println("Nombre: "+e.getNombre());
				System.out.println("Dirección: "+e.getDireccion());
			}

		};
	}


 */

	//U
/*
	@Bean
	CommandLineRunner actualizarEdificio(EdificioController controller){
		return args -> {
			Edificio buscado=controller.buscarEdificio("Torre SQL", "Av. Puerto 1433");

			buscado.setDireccion("Puerto 1433");

			controller.update(buscado);

			System.out.println("Direccion nueva: "+controller.leerEdificio(buscado.getCodigo()).getDireccion());

		};
	}


 */

	//D
/*
	@Bean
	CommandLineRunner borrarEdificio(EdificioController controller){
		return args -> {
			controller.borrar(1);

			Edificio buscado=controller.buscarEdificio("The Link Towers","Arrayanes 1230");

			controller.borrar(buscado);


			//TODO chequear que tan optimo es
		};
	}



 */
	//--------------------------------------Unidad--------------------------------------

	//C

/*
	@Bean
	CommandLineRunner crearUnidad (UnidadController controller,EdificioController edi) {
		return args -> {
			Edificio edificio = edi.crearEdificio("Edificio TEST", "Main St.");

			Unidad creada = controller.crearUnidad("3", "2", edificio);

			edi.agregarUnidad(edificio,creada);

			edi.guardar(edificio);

		};
	}


 */

	//R

/*
	@Bean
	CommandLineRunner leerUnidadPorId(UnidadController uni){
		return args ->{
			uni.leerUnidad(1);
		};
	}


 */
/*
	@Bean
	CommandLineRunner buscarUnidad(UnidadController uni,EdificioController edi){
		return args -> {
			Edificio buscado=edi.buscarEdificio("Edificio TEST", "Main St.");

			Unidad buscada=uni.buscarUnidad("2","3",buscado.getCodigo());

			System.out.println("Unidad buscada: ");
			System.out.println("Edificio: "+buscado.getNombre());
			System.out.println("Piso: "+buscada.getPiso());
			System.out.println("Número: "+buscada.getNumero());


		};
	}


 */


	//U

/*
	@Bean
	CommandLineRunner actualizarUnidad(UnidadController controller){
		return args -> {
			Unidad actualizable=controller.buscarUnidad("1","1",3);

			actualizable.setNumero("2");

			controller.actualizar(actualizable);

			System.out.println("Unidad actualizada");

			Unidad actualizada=controller.leerUnidad(actualizable.getIdentificador());

			System.out.println("Numero nuevo: "+actualizada.getNumero());

		};
	}


 */
	//D

/*
	 @Bean
	CommandLineRunner borrarUnidad(UnidadController uni){
		 return args -> {
			 uni.borrarPorId(299);

			 Unidad aBorrar=uni.leerUnidad(360);

			 uni.borrar(aBorrar);

		 };
	 }


 */

	//--------------------------------------Persona--------------------------------------

	//C
/*
	@Bean
	CommandLineRunner crearPersona(PersonaController per){
		return args -> {
			Persona juan=per.crearPersona("DNI123456","Juan Perez");
			System.out.println("---PERSONA---");
			System.out.println("Nombre: "+juan.getNombre());
			System.out.println("Documento: "+juan.getDocumento());
			System.out.println("-------------");
		};
	}


 */
/*
	@Bean
	CommandLineRunner guardarPersona(PersonaController per,UnidadController uni){
		return args -> {
			Persona pedro=per.crearPersona("DNI1234567","Pedro Perez");
			Unidad u=uni.leerUnidad(212);
			uni.agregarInquilino(pedro,u);

			boolean estaPedro=false;

			List<Persona> buscar=uni.leerUnidad(212).getInquilinos();

			for(Persona p:buscar){
				if(p.getDocumento().equals(pedro.getDocumento())&&p.getNombre().equals(pedro.getNombre())){
					estaPedro=true;
					break;
				}
			}

			if(estaPedro){
				System.out.println("Pedro ahora reside en la unidad 212");
			}
			else{
				System.out.println("Algo salio mal");
			}

		};
	}


 */

	//R

/*
	@Bean
	CommandLineRunner testPersona(PersonaController personaControlador){
		return args -> {
			Iterable<Persona> personas= personaControlador.leerPersonas();
			for(Persona persona: personas){
				System.out.println("---PERSONA---");
				System.out.println("Nombre: "+persona.getNombre());
				System.out.println("Documento: "+persona.getDocumento());
				System.out.println("-------------");
			}
		};
	}



 */
	//U


/*
	@Bean
	CommandLineRunner actualizarPersona(PersonaController per){
		return args -> {
			Persona p=per.leerPersona("DNI1234567");

			p.setNombre("Nombre actualizado");

			per.actualizar(p);

			Persona actualizada=per.leerPersona("DNI1234567");

			System.out.println("---PERSONA ACTUALIZADA---");
			System.out.println("Nombre: "+actualizada.getNombre());
			System.out.println("Documento: "+actualizada.getDocumento());
			System.out.println("-------------");
		};
	}



 */
	//D


	//No tiene sentido hacer delete de una persona fuera del contexto de
	//la necesidad de borrarla ya que no es inquilino o duenio


	//--------------------------------------Reclamo--------------------------------------

	//C
/*
	@Bean
	CommandLineRunner crearReclamo(ReclamoController cont,EdificioController e, UnidadController u, ImagenController i,PersonaController p){
		return args -> {

			Edificio edificio=e.leerEdificio(3);
			Unidad unidad=u.leerUnidad(232);
			Persona reclamante=p.leerPersona("DNI30732736");


			Reclamo reclamo=cont.crearReclamo(reclamante,edificio,"Techo","Hay humedad",unidad);

			Imagen imagen=i.crearImagen("imagen.com","PNG",reclamo);

			reclamo.agregarImagen(imagen);

			cont.guardarReclamo(reclamo);
		};
	}


	//R
 */

/*
	@Bean
	CommandLineRunner leerReclamo(ReclamoController r){
		return args -> {
			Reclamo reclamo=r.leerReclamo(1);

			System.out.println("RECLAMO");
			System.out.println("");
			System.out.println("Reclamante: "+reclamo.getReclamante().getNombre());
			System.out.println("Lugar: "+reclamo.getUbicacion());
			System.out.println("Descripcion: "+reclamo.getDescripcion());
			System.out.println("Edificio: "+reclamo.getEdificio().getNombre());
			System.out.println("Unidad: "+reclamo.getUnidad().getNumero());
			List<Imagen> imagenes=reclamo.getImagenes();

			for(Imagen i:imagenes){
				System.out.println("Path de la imagen: "+i.getPath());
			}
		};
	}
*/

	//U

	/*
	@Bean
	CommandLineRunner actualizarReclamo(ReclamoController controller){
		return args -> {

			Reclamo modificable=controller.leerReclamo(1);

			modificable.setDescripcion("Hay mucha humedad");

			controller.actualizar(modificable);

			Reclamo modificado=controller.leerReclamo(1);

			System.out.println("Nueva desripcion: "+modificado.getDescripcion());
		};
	}
 */
	//D
/*
	@Bean
	CommandLineRunner borrarReclamo(ReclamoController controller){
		return args -> {
			controller.borrar(1);
			Reclamo borrado=controller.leerReclamo(1);
			if(borrado==null){
				System.out.println("Se borró exitosamente");
			}
			else{
				System.out.println("Algo fallo");
			}
			Reclamo procesado=controller.leerReclamo(1);
			System.out.println("");
		};
	}


 */
	//--------------------------------------Imagen--------------------------------------

	//C
/*
	@Bean
	CommandLineRunner crearImagen(ReclamoController r,EdificioController e, UnidadController u, ImagenController i,PersonaController p){
		return args -> {
			Edificio edificio=e.leerEdificio(3);
			Unidad unidad=u.leerUnidad(232);
			Persona reclamante=p.leerPersona("DNI30732736");


			Reclamo reclamo=r.crearReclamo(reclamante,edificio,"Techo","Hay humedad",unidad);

			Imagen imagen=i.crearImagen("imagen.com","PNG",reclamo);
			Imagen imagen2=i.crearImagen("imagen2.com","JPEG",reclamo);
			Imagen imagen3=i.crearImagen("imagen3.com","WEBP",reclamo);


			reclamo.agregarImagen(imagen);
			reclamo.agregarImagen(imagen2);
			reclamo.agregarImagen(imagen3);

			r.guardarReclamo(reclamo);
		};
	}
 */
	//R
/*
	@Bean
	CommandLineRunner leerImagen(ImagenController i){
		return args -> {
			Imagen leida=i.leerImagen(2);

			System.out.println("IMAGEN");
			System.out.println("Numero: "+leida.getNumero());
			System.out.println("Path: "+leida.getPath());
			System.out.println("Tipo: "+leida.getTipo());

		};
	}
 */

	//U
/*
	@Bean
	CommandLineRunner actualizarImagen(ImagenController i){
		return args ->{
			Imagen actualizable=i.leerImagen(2);

			actualizable.setPath("nuevolink.com");

			i.actualizar(actualizable);

			Imagen leida=i.leerImagen(2);

			System.out.println("IMAGEN");
			System.out.println("Numero: "+leida.getNumero());
			System.out.println("Path: "+leida.getPath());
			System.out.println("Tipo: "+leida.getTipo());

		};
	}
*/
	//D
/*
	@Bean
	CommandLineRunner borrarImagen(ImagenController i){
		return args -> {
			i.borrar(i.leerImagen(2));

			if(i.leerImagen(2)==null){
				System.out.println("El borrado fue exitoso");
			}
			else{
				System.out.println("Algo salio mal");
			}
		};
	}
 */

}
