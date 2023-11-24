package poo.scrabble;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase de control para la pantalla inicial de la aplicación. En esta clase se declaran atributos privados que corresponden
 * a objetos de JavaFX dentro del "form", por ello existen sus etiquetas @FXML.
 */
public class RummikubController {
    public Button startButton;
    @FXML
    private RadioButton player2RadioButton;

    @FXML
    private RadioButton player3RadioButton;

    @FXML
    private RadioButton player4RadioButton;

    @FXML
    private TextField player1TextField;

    @FXML
    private TextField player2TextField;

    @FXML
    private TextField player3TextField;

    @FXML
    private TextField player4TextField;

    @FXML
    private ToggleGroup jugadoresToggleGroup;
    private int selectedPlayerCount = 2; // Inicializado con este valor por defecto.

    /**
     * método que se invoca automáticamente cuando el "form" que controla esta clase se carga en una escena de JavaFX.
     * Se encarga de instanciar los objetos de JavaFX existentes en la interfaz para que puedan ser manipulados o interactuar
     * con ellos por medio de métodos vinculados a eventos de cada objeto.
     */
    @FXML
    public void initialize() {
        // configura el ToggleGroup y su estado inicial dentro del método que se llama automáticamente al cargar la pantalla
        player2RadioButton.setToggleGroup(jugadoresToggleGroup);
        player3RadioButton.setToggleGroup(jugadoresToggleGroup);
        player4RadioButton.setToggleGroup(jugadoresToggleGroup);

        // Por defecto, selecciona 2 jugadores y oculta los otros 2 TextFields
        player2RadioButton.setSelected(true);
        player3TextField.setVisible(false);
        player4TextField.setVisible(false);
    }

    /**
     * Método que se encarga de realizar cambios en la pantalla cuando se interactúa con un RadioButton.
     * @param event la activación de la interacción de usuario por medio de hacer clic en el botón.
     */
    @FXML
    public void manejarSeleccionRadioButton(ActionEvent event) {
        RadioButton selectedRadioButton = (RadioButton) event.getSource();
        selectedPlayerCount = Integer.parseInt(selectedRadioButton.getText().split(" ")[0]);

        // Muestra u oculta los campos de entrada de texto dependiendo de la cantidad seleccionada de jugadores.
        player3TextField.setVisible(selectedPlayerCount >= 3);
        player4TextField.setVisible(selectedPlayerCount == 4);
    }

    /**
     * Método para cargar la ventana de juego cuando se da click al botón de "Empezar". Se encuentra vinculado al botón
     * por medio de la propiedad "onAction" de JavaFX, por lo que al dar click el método llama automáticamente a este
     * método.
     */
    @FXML
    private void startGame() {
        // Now you can access the selected player count from here
        System.out.println("cantidad seleccionada: " + selectedPlayerCount);

        // You can access the player names in the text fields here
        String player1Name = player1TextField.getText();
        String player2Name = player2TextField.getText();
        String player3Name = player3TextField.getText();
        String player4Name = player4TextField.getText();

        // Se obtiene la referencia a la ventana actual (stage)
        Stage currentStage = (Stage) startButton.getScene().getWindow();

        try {
            // Se carga el archivo FXML para el tablero de juego
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RummikubGameBoard.fxml"));
            Parent gameBoardRoot = loader.load();
            Scene gameBoardScene = new Scene(gameBoardRoot);


            // Se crea una nueva Stage (ventana) para la pantalla de juego
            Stage gameBoardStage = new Stage();
            gameBoardStage.setScene(gameBoardScene);
            gameBoardStage.setTitle("Rummikub - Pantalla de Juego");

            // Se obtiene la referencia a la clase de control para la siguiente pantalla
            RummikubGameBoard control = loader.getController();
            control.initNombres(player1Name, player2Name, player3Name, player4Name); // pasa los parámetros de los nombres

            // Cierra la ventana actual y abre el tablero
            currentStage.close();
            gameBoardStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
