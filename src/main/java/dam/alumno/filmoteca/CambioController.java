package dam.alumno.filmoteca;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class CambioController implements Initializable {
    public Text txError;
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
    public Button btnCambio;
    public Button btnCancelar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txID.getStyleClass().add("titulo");
        btnCambio.getStyleClass().add("btn-verde");
        btnCancelar.getStyleClass().add("btn-rojo");
        tfYear.setTextFormatter(new javafx.scene.control.TextFormatter<>(
                change -> (change.getControlNewText().matches("\\d{0,4}")) ? change : null
        ));

    }

    public void onSliderMoved(MouseEvent actionEvent) {
        slRating.valueProperty().addListener((observable, oldValue, newValue) -> {
                    txRating.setText("Puntuación: " +  String.format("%.1f", newValue.doubleValue()));
                    slValue = newValue.doubleValue();
                }
        );

    }


    public void peliculaCambio(Pelicula pelicula, int index) {
        peliculaIndex = index;
        txID.getStyleClass().removeAll("titulo");
        txID.setText("ID: " + pelicula.getId());
        tfTitle.setText(pelicula.getTitle());
        tfYear.setText(String.valueOf(pelicula.getYear()));
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
            String error = "Debe introducir el titulo y el año de la película.";
            if(tfTitle.getText().isEmpty() || tfYear.getText().isEmpty()) {
                txError.setText(error);
                return;
            }else {
                idNuevo(pelicula);
                pelicula.setTitle(tfTitle.getText());
                pelicula.setYear(Integer.parseInt(tfYear.getText()));
                pelicula.setDescription(taDesc.getText());
                pelicula.setRating((float) slValue);
                peliculas.add(pelicula);
            }

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
