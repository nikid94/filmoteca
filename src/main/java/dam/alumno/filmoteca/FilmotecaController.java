package dam.alumno.filmoteca;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class FilmotecaController implements Initializable {
    private final DatosFilmoteca datosFilmoteca = DatosFilmoteca.getInstancia();
    public Text txDescargar;
    private String url;
    private ObservableList<Pelicula> peliculas;
    public ButtonBar btnBar;
    public Button btnAñadir;
    public Button btnModificar;
    public Button btnBorrar;
    public Button btnCerrar;
    public Text txRating;
    public ImageView ivPortada;
    Image portada;
    public Text txID;
    @FXML
    public Text txTitulo;
    @FXML
    public TableView<Pelicula> tvPeliculas;
    @FXML
    public TableColumn<Pelicula, Integer> colID = new TableColumn<>("ID");
    @FXML
    public TableColumn<Pelicula, String> colTitle = new TableColumn<>("Title");
    public Text txTitle;
    public Text txYear;
    public Text txDesc;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        peliculas = datosFilmoteca.getListaPeliculas();
        txTitulo.setText("Filmoteca");
        txTitulo.getStyleClass().add("titulo");
        txDescargar.getStyleClass().add("tx-azul");
        btnBorrar.getStyleClass().add("btn-rojo");
        btnCerrar.getStyleClass().add("btn-rojo");
        btnAñadir.getStyleClass().add("btn-verde");
        btnModificar.getStyleClass().add("btn-verde");

        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        tvPeliculas.setItems(peliculas);

        tvPeliculas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txID.setText("ID: " + newValue.getId());
                txTitle.setText("Título: \n" + newValue.getTitle());
                txYear.setText("Año: \n" + newValue.getYear());
                txDesc.setText("Descripción: \n" + newValue.getDescription());
                txRating.setText("Puntuación: \n" + String.format("%.1f", newValue.getRating()));
                url = newValue.getPoster();

                if(newValue.getPoster() != null) {
                    portada = new Image(newValue.getPoster());
                    ivPortada.setImage(portada);
                }else{
                    ivPortada.setImage(null);
                }

                btnModificar.setDisable(false);
                btnBorrar.setDisable(false);
            }else {
                txID.setText("ID:");
                txTitle.setText("Título:");
                txYear.setText("Año:");
                txDesc.setText("Descripción:");
                txRating.setText("Puntuación:");
                btnModificar.setDisable(true);
                btnBorrar.setDisable(true);
            }
        });
    }

    public void onAnadirClick(ActionEvent actionEvent) throws IOException {
        String accion = btnAñadir.getText();
        nuevaVentana("cambio-view.fxml", accion);
    }

    public void onModificarClick(ActionEvent actionEvent) throws IOException {
        String accion = btnModificar.getText();
        nuevaVentana("cambio-view.fxml", accion);
    }

    public void onBorrarClick(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmacion");
        alert.setHeaderText(null);
        alert.setContentText("¿Seguro que quieres borrar la película?");
        ButtonType btnSi = new ButtonType("Sí");
        ButtonType btnNo = new ButtonType("No");
        alert.getButtonTypes().setAll(btnSi, btnNo);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == btnSi) {
                peliculas.remove(tvPeliculas.getSelectionModel().getSelectedIndex());
            } else if (result.get() == btnNo) {
                alert.close();
            }
        }
    }

    public void onCerrarClick(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmacion");
        alert.setHeaderText(null);
        alert.setContentText("¿Seguro que quieres cerrar la aplicación?");
        ButtonType btnSi = new ButtonType("Sí");
        ButtonType btnNo = new ButtonType("No");
        alert.getButtonTypes().setAll(btnSi, btnNo);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == btnSi) {
                System.exit(0);
            } else if (result.get() == btnNo) {
                alert.close();
            }
        }
    }

    public void nuevaVentana(String view, String accion) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(view));
        Scene scene = new Scene(fxmlLoader.load()
                , 500, 600);
        Stage sCambio = new Stage();
        sCambio.setTitle(accion);
        sCambio.setMinWidth(500);
        sCambio.setMinHeight(600);
        sCambio.setScene(scene);
        CambioController controller = fxmlLoader.getController();
        controller.accionCambio(accion);
        if(accion.equals(btnModificar.getText())) {
            controller.peliculaCambio(tvPeliculas.getSelectionModel().getSelectedItem(), tvPeliculas.getSelectionModel().getSelectedIndex());
        }

        sCambio.showAndWait();

    }

    public void onDescargarClick(MouseEvent mouseEvent) {
        if(url!=null) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}