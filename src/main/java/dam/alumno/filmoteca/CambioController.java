package dam.alumno.filmoteca;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.util.Comparator;

public class CambioController {
    Pelicula pelicula = new Pelicula();
    int peliculaIndex;
    public TextField tfPoster;
    private final DatosFilmoteca datosFilmoteca = DatosFilmoteca.getInstancia();
    private final ObservableList<Pelicula> peliculas = datosFilmoteca.getListaPeliculas();
    public Text txID;
    public Text txTitle;
    public TextField tfTitle;
    public Text txYear;
    public TextField tfYear;
    public Text txDesc;
    public TextArea taDesc;
    public Text txRating;
    public Slider slRating;
    private double slValue;
    public ImageView ivPortada;
    public Button btnCambio;
    public Button btnCancelar;

    public void onSliderMoved(MouseEvent actionEvent) {
        slRating.valueProperty().addListener((observable, oldValue, newValue) -> {
                    txRating.setText("Puntuación: " +  String.format("%.1f", newValue.doubleValue()));
                    slValue = newValue.doubleValue();
                }
        );

    }

    public void peliculaCambio(Pelicula pelicula, int index) {
        peliculaIndex = index;
        txID.setText("ID: " + pelicula.getId());
        tfTitle.setText(pelicula.getTitle());
        tfYear.setTextFormatter(new javafx.scene.control.TextFormatter<>(
                new IntegerStringConverter(),
                pelicula.getYear(),
                change -> (change.getControlNewText().matches("\\d{0,4}")) ? change : null
        ));
        taDesc.setText(pelicula.getDescription());
        txRating.setText(String.valueOf(pelicula.getRating()));
        slRating.setValue(pelicula.getRating());
        tfPoster.setText(pelicula.getPoster());
    }

    public void accionCambio(String accion) {
        btnCambio.setText(accion);
    }

    public void onCambioClick(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        if(button.getText().equals("Añadir Película")){
            idNuevo(pelicula);
            pelicula.setTitle(tfTitle.getText());
            pelicula.setYear(Integer.parseInt(tfYear.getText()));
            pelicula.setDescription(taDesc.getText());
            pelicula.setRating((float) slValue);
            peliculas.add(pelicula);
        }else {
            pelicula = peliculas.get(peliculaIndex);
            pelicula.setTitle(tfTitle.getText());
            pelicula.setYear(Integer.parseInt(tfYear.getText()));
            pelicula.setDescription(taDesc.getText());
            pelicula.setRating((float) slRating.getValue());
            pelicula.setPoster(tfPoster.getText());
        }

        Stage stage = (Stage)((Node)(actionEvent.getSource())).getScene().getWindow();
        stage.close();

    }

    public void onCancelarClick(ActionEvent actionEvent) {

        Stage stage = (Stage)((Node)(actionEvent.getSource())).getScene().getWindow();
        stage.close();
    }

    public void idNuevo(Pelicula pelicula) {
        int idLibre = 1;

        peliculas.sort(Comparator.comparingInt(Pelicula::getId));

        for (Pelicula p : peliculas) {
            if (p.getId() != idLibre) {
                pelicula.setId(idLibre);
                return;
            }
            idLibre++;
        }
        pelicula.setId(idLibre);

    }


    
}
