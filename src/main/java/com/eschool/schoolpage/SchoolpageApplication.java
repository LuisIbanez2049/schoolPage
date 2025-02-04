package com.eschool.schoolpage;

import com.eschool.schoolpage.models.*;
import com.eschool.schoolpage.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication
public class SchoolpageApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolpageApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(UsuarioRepository usuarioRepository, MateriaRepository materiaRepository, ContenidoRepository contenidoRepository,
									  UsuarioMateriaRepository usuarioMateriaRepository, ComentarioRepository comentarioRepository,
									  RespuestaRepository respuestaRepository, ArchivoRepository archivoRepository) {
		return (args) -> {

			//-------------------------------------USUARIOS-------------------------------------------------
			Usuario luis = new Usuario("Luis", "Ibañez", "94706338", "luis@gmail.com", passwordEncoder.encode("1234"), Rol.ESTUDIANTE, "https://res.cloudinary.com/dhojn5eon/image/upload/v1736828893/luis_we1sms.png");
			Usuario tony = new Usuario("Tony", "Stark", "94706338", "tony@gmail.com", passwordEncoder.encode("1234"), Rol.ESTUDIANTE, "https://res.cloudinary.com/dhojn5eon/image/upload/v1736828893/tony_ogey5b.png");
			usuarioRepository.save(luis);
			usuarioRepository.save(tony);


			Usuario antonio = new Usuario("Antonio", "Rigan", "94706338", "antoniorigan@profesor.com", passwordEncoder.encode("1234"), Rol.PROFESOR, "https://res.cloudinary.com/dhojn5eon/image/upload/v1736828893/professor_c6m1fi.png");
			usuarioRepository.save(antonio);

			Usuario admin = new Usuario("admin", "admin", "94706332", "admin@admin.com", passwordEncoder.encode("1234"), Rol.ADMIN, "https://res.cloudinary.com/dhojn5eon/image/upload/v1736826159/people_14024658_uqxwhc.png");
			usuarioRepository.save(admin);
			//--------------------------------------------------------------------------------------

			//-------------------------------------Materias-------------------------------------------------
			Materia matematica = new Materia("Matematica", "Estudio del razonamiento lógico y analítico, abordando números, álgebra, geometría y más, para resolver problemas cotidianos y científicos.", "https://cdn-blog.superprof.com/blog_co/wp-content/uploads/2020/10/las-matematicas.jpeg", "#a1d9d9", "1234" );
			Materia quimica = new Materia("Quimica", "Ciencia que estudia la composición, estructura y transformación de la materia, aplicándose en la salud, industria, medio ambiente y tecnología.", "https://cards.algoreducation.com/_next/image?url=https%3A%2F%2Ffiles.algoreducation.com%2Fproduction-ts%2F__S3__85d3dd80-4e5e-4469-961c-a4b3a1814e27&w=3840&q=75", "#a2b38b", "1234");
			materiaRepository.save(matematica);
			materiaRepository.save(quimica);
			//--------------------------------------------------------------------------------------



			//-------------------------------------Agrego contenido a las materias-------------------------------------------------
			Contenido contenido1Matematica = new Contenido("Parabolas", LocalDateTime.now(), "Hablamos sobre como calcular las parabolas", "https://drive.google.com/file/d/1JZbeO-zW9rraskFvcyyWDiNMyBkoVlBz/view?usp=drive_link");
			Contenido contenido2Matematica = new Contenido("Polinomios", LocalDateTime.now(), "Calculo de polinomios de 1er a 3er grado", "https://drive.google.com/file/d/1JZbeO-zW9rraskFvcyyWDiNMyBkoVlBz/view?usp=drive_link");

			matematica.addContenido(contenido1Matematica);
			matematica.addContenido(contenido2Matematica);
			contenidoRepository.save(contenido1Matematica);
			contenidoRepository.save(contenido2Matematica);


			Contenido contenido1Quimica = new Contenido("Gases", LocalDateTime.now(), "Hablamos sobre los tipos de gases", "https://drive.google.com/file/d/1JZbeO-zW9rraskFvcyyWDiNMyBkoVlBz/view?usp=drive_link");
			Contenido contenido2Quimica = new Contenido("Estados", LocalDateTime.now(), "Hablamos sobre los distitos estados de la materia", "https://drive.google.com/file/d/1JZbeO-zW9rraskFvcyyWDiNMyBkoVlBz/view?usp=drive_link");

			quimica.addContenido(contenido1Quimica);
			quimica.addContenido(contenido2Quimica);
			contenidoRepository.save(contenido1Quimica);
			contenidoRepository.save(contenido2Quimica);
			//--------------------------------------------------------------------------------------




			//-------------------------------------Agrego archivos a las contenidos-------------------------------------------------
			Archivo archivo1PolinomiosMatematica = new Archivo("Polynomial calculus", "fa-brands fa-youtube", "https://www.youtube.com/watch?v=ueJtyB2Hg2I&ab_channel=Divertim%C3%A1ticas");
			Archivo archivo2PolinomiosMatematica = new Archivo("Homework", "fa-solid fa-image", "https://www.profesor10demates.com/wp-content/uploads/2020/04/Ecuaciones-de-tercer-grado-ejercicios-resueltos.png");
			contenido2Matematica.addArchivo(archivo1PolinomiosMatematica);
			contenido2Matematica.addArchivo(archivo2PolinomiosMatematica);
			archivoRepository.save(archivo1PolinomiosMatematica);
			archivoRepository.save(archivo2PolinomiosMatematica);


			Archivo archivo1ParabolasMatematica = new Archivo("PDF Parabola", "fa-solid fa-file-pdf", "https://www.webcolegios.com/file/4bc162.pdf");
			contenido1Matematica.addArchivo(archivo1ParabolasMatematica);
			archivoRepository.save(archivo1ParabolasMatematica);



			Archivo archivo1GasesQuimica = new Archivo("PDF Gases", "fa-solid fa-file-pdf", "https://sgcciencias.wordpress.com/wp-content/uploads/2019/07/7-bc3a1sico-los-gases-y-sus-propiedades-.pdf");
			Archivo archivo2GasesQuimica = new Archivo("Ideal gas calculation", "fa-brands fa-youtube", "https://www.youtube.com/watch?v=7uWK3GmeGzY&ab_channel=SusiProfe");
			Archivo archivo3GasesQuimica = new Archivo("Homework", "fa-solid fa-image", "https://website-assets.studocu.com/img/document_thumbnails/a28840d67c3e024d11cd5e47f4ab801e/thumb_1200_1553.png");
			contenido1Quimica.addArchivo(archivo1GasesQuimica);
			contenido1Quimica.addArchivo(archivo2GasesQuimica);
			contenido1Quimica.addArchivo(archivo3GasesQuimica);
			archivoRepository.save(archivo1GasesQuimica);
			archivoRepository.save(archivo2GasesQuimica);
			archivoRepository.save(archivo3GasesQuimica);
			//----------------------------------------------------------------------------------------------------------------------------------




			//-------------------------------------Agrego comentarios a los contenidos-------------------------------------------------
			Comentario comentario1MatematicaContenido1 = new Comentario("No me quedo claro el calculo de los parabolas...", LocalDateTime.now());
			contenido1Matematica.addComentario(comentario1MatematicaContenido1);
			comentario1MatematicaContenido1.setContenido(contenido1Matematica);
			luis.addComentario(comentario1MatematicaContenido1);
			comentario1MatematicaContenido1.setUsuario(luis);
			usuarioRepository.save(luis);
			comentarioRepository.save(comentario1MatematicaContenido1);


			Comentario comentario1MatematicaContenido2 = new Comentario("No me quedo claro el calculo de los polinomios...", LocalDateTime.now());
			contenido2Matematica.addComentario(comentario1MatematicaContenido2);
			comentario1MatematicaContenido2.setContenido(contenido2Matematica);
			luis.addComentario(comentario1MatematicaContenido2);
			comentario1MatematicaContenido2.setUsuario(luis);
			usuarioRepository.save(luis);
			comentarioRepository.save(comentario1MatematicaContenido2);


			Comentario comentario1QuimicaContenido1 = new Comentario("No me quedo claro el tema de los gases...", LocalDateTime.now());
			contenido1Quimica.addComentario(comentario1QuimicaContenido1);
			comentario1QuimicaContenido1.setContenido(contenido1Quimica);
			luis.addComentario(comentario1QuimicaContenido1);
			comentario1QuimicaContenido1.setUsuario(luis);
			usuarioRepository.save(luis);
			comentarioRepository.save(comentario1QuimicaContenido1);

			Comentario comentario1QuimicaContenido2 = new Comentario("No me quedo claro el tema de los ESTADOS...", LocalDateTime.now());
			contenido2Quimica.addComentario(comentario1QuimicaContenido2);
			comentario1QuimicaContenido2.setContenido(contenido2Quimica);
			tony.addComentario(comentario1QuimicaContenido2);
			comentario1QuimicaContenido2.setUsuario(tony);
			usuarioRepository.save(tony);
			comentarioRepository.save(comentario1QuimicaContenido2);
			//--------------------------------------------------------------------------------------


			//-------------------------------------Agrego respuestas a los comentarios-------------------------------------------------
			Respuesta respuesta1Comentario1QuimicaContenido1 = new Respuesta("Te explico, cuando tienes...", LocalDateTime.now());
			comentario1QuimicaContenido1.addRespuesta(respuesta1Comentario1QuimicaContenido1);
			respuesta1Comentario1QuimicaContenido1.setComentario(comentario1QuimicaContenido1);
			antonio.addRespuesta(respuesta1Comentario1QuimicaContenido1);
			respuesta1Comentario1QuimicaContenido1.setUsuario(antonio);
			respuesta1Comentario1QuimicaContenido1.setRespuestaPara("Luis Ibañez");
			usuarioRepository.save(antonio);
			respuestaRepository.save(respuesta1Comentario1QuimicaContenido1);


			Respuesta respuesta1Comentario1MatematicaContenido2 = new Respuesta("Para llegar a la respuesta debes...", LocalDateTime.now());
			comentario1MatematicaContenido2.addRespuesta(respuesta1Comentario1MatematicaContenido2);
			respuesta1Comentario1MatematicaContenido2.setComentario(comentario1MatematicaContenido2);
			tony.addRespuesta(respuesta1Comentario1MatematicaContenido2);
			respuesta1Comentario1MatematicaContenido2.setUsuario(tony);
			respuesta1Comentario1MatematicaContenido2.setRespuestaPara("Luis Ibañez");
			usuarioRepository.save(tony);
			respuestaRepository.save(respuesta1Comentario1MatematicaContenido2);


			Respuesta respuesta2Comentario1MatematicaContenido2 = new Respuesta("Ahora entiendo, gracias...", LocalDateTime.now());
			comentario1MatematicaContenido2.addRespuesta(respuesta2Comentario1MatematicaContenido2);
			respuesta2Comentario1MatematicaContenido2.setComentario(comentario1MatematicaContenido2);
			luis.addRespuesta(respuesta2Comentario1MatematicaContenido2);
			respuesta2Comentario1MatematicaContenido2.setUsuario(luis);
			respuesta2Comentario1MatematicaContenido2.setRespuestaPara("Tony Stark");
			usuarioRepository.save(luis);
			respuestaRepository.save(respuesta2Comentario1MatematicaContenido2);
			//--------------------------------------------------------------------------------------







			//-------------------------------------Asocio las materias con los usuarios-------------------------------------------------
			UsuarioMateria luisMateria1 = new UsuarioMateria(JornadaTurno.MORNING);
			luis.addUsuarioMateria(luisMateria1);
			matematica.addUsuarioMateria(luisMateria1);
			usuarioMateriaRepository.save(luisMateria1);

			UsuarioMateria luisMateria2 = new UsuarioMateria(JornadaTurno.EVENING);
			quimica.addUsuarioMateria(luisMateria2);
			luis.addUsuarioMateria(luisMateria2);
			usuarioMateriaRepository.save(luisMateria2);


			UsuarioMateria tonyMateria1 = new UsuarioMateria(JornadaTurno.MORNING);
			quimica.addUsuarioMateria(tonyMateria1);
			tony.addUsuarioMateria(tonyMateria1);
			usuarioMateriaRepository.save(tonyMateria1);

			UsuarioMateria tonyMateria2 = new UsuarioMateria(JornadaTurno.MORNING);
			matematica.addUsuarioMateria(tonyMateria2);
			tony.addUsuarioMateria(tonyMateria2);
			usuarioMateriaRepository.save(tonyMateria2);

			UsuarioMateria antonioPrefesor = new UsuarioMateria(JornadaTurno.MORNING);
			matematica.addUsuarioMateria(antonioPrefesor);
			antonio.addUsuarioMateria(antonioPrefesor);
			usuarioMateriaRepository.save(antonioPrefesor);
			//--------------------------------------------------------------------------------------


		};
	}

}
