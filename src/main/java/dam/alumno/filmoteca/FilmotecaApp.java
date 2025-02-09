package dam.alumno.filmoteca;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.io.IOException;
import java.util.List;

public class FilmotecaApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FilmotecaApp.class.getResource("filmoteca-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 720, 740);
        stage.setTitle("Filmoteca German");
        stage.setMinWidth(720);
        stage.setMinHeight(740);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {launch();}

    public void init() {
        System.out.println("Cargando datos desde fichero datos/peliculas.json");
        DatosFilmoteca datosFilmoteca = DatosFilmoteca.getInstancia();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<Pelicula> lista = objectMapper.readValue(new File("datos/peliculas.json"),
                                    objectMapper.getTypeFactory().constructCollectionType(List.class, Pelicula.class));

            datosFilmoteca.getListaPeliculas().setAll(lista);
        } catch (IOException e){
            System.out.println("ERROR al cargar los datos. La aplicación no puede iniciarse");
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println(datosFilmoteca.getListaPeliculas());
    }

    public void stop() {
        ObservableList<Pelicula> listaPeliculas = DatosFilmoteca.getInstancia().getListaPeliculas();
        System.out.println(listaPeliculas);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writeValue(new File("datos/peliculas2.json"),listaPeliculas);
        }catch (IOException e) {
            System.out.println("ERROR no se ha podido guardar los datos de la aplicación");
            e.printStackTrace();
        }

    }
}



