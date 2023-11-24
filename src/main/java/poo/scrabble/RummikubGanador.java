package poo.scrabble;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Vector;

/**
 * Clase controladora para el form de la pantalla de ganadores. Es instanciada cuando un método dentro de la pantalla de juego
 * carga la pantalla de puntajes.
 */
public class RummikubGanador {

    @FXML
    private Label titleLabel;

    @FXML
    private TableView<FilaPuntajes> tableView;

    @FXML
    private TableColumn<FilaPuntajes, String> roundNumberColumn;

    @FXML
    private TableColumn<FilaPuntajes, String> player1Column;

    @FXML
    private TableColumn<FilaPuntajes, String> player2Column;

    @FXML
    private TableColumn<FilaPuntajes, String> player3Column;

    @FXML
    private TableColumn<FilaPuntajes, String> player4Column;

    private ObservableList<FilaPuntajes> data;


    @FXML
    private Label winnerLabel;

    @FXML
    private HBox buttonsHBox;

    @FXML
    private Button playAgainButton;

    @FXML
    private Button endGameButton;


    private Vector<Jugador> jugadores;

    @FXML
    Button botonJugarOtraVez;


    /**
     * Método que se invoca automáticamente cuando el "form" que controla esta clase se carga en una escena de JavaFX.
     * Se encarga de instanciar los objetos de JavaFX existentes en la interfaz para que puedan ser manipulados o interactuar
     * con ellos por medio de métodos vinculados a eventos de cada objeto.
     */
    @FXML
    public void initialize() {
        // aquí se instancian los objetos que pertenecen a la interfaz de la pantalla de puntajes
        data = FXCollections.observableArrayList();

        // Se configuran las "Cell Vallue Factory" para las columnas, lo que permite crear las filas en tiempo de ejecución.
        roundNumberColumn.setCellValueFactory(new PropertyValueFactory<>("numRonda"));
        player1Column.setCellValueFactory(new PropertyValueFactory<>("jugador1"));
        player2Column.setCellValueFactory(new PropertyValueFactory<>("jugador2"));
        player3Column.setCellValueFactory(new PropertyValueFactory<>("jugador3"));
        player4Column.setCellValueFactory(new PropertyValueFactory<>("jugador4"));
    }

    /**
     * Método invocado por el tablero de juego cuando la partida se termina, con el objetivo de pasar las referencias a
     * los jugadores que se crearon inicialmente.
     * @param jugadores el vector que contiene las referencias a los jugadores instanciados.
     */
    public void recibirJugadores(Vector<Jugador> jugadores) {
        this.jugadores = jugadores;

        int rondasJugadas = jugadores.get(0).getPuntos().size();

        player1Column.setText(jugadores.get(0).getNombre());
        player2Column.setText(jugadores.get(1).getNombre());

        if(jugadores.size() >= 3) {
            player3Column.setText(jugadores.get(2).getNombre());
        }
        if (jugadores.size() == 4) {
            player4Column.setText(jugadores.get(3).getNombre());
        }


        generarFilas(rondasJugadas);
        String nombreDelGanador = "";
        int puntajeMayor = -100000;
        for (Jugador jugador: jugadores) {
            if (jugador.getPuntosTotales() > puntajeMayor) {
                puntajeMayor = jugador.getPuntosTotales();
                nombreDelGanador = jugador.getNombre();
            }
        }
        winnerLabel.setText("¡El ganador es: " + nombreDelGanador + " con " + puntajeMayor + " puntos!");
    }

    /**
     * Método para crear las filas en la tabla
     * @param rondasJugadas
     */
    private void generarFilas(int rondasJugadas) {
        String numRonda;
        String puntaje1;
        String puntaje2;
        String puntaje3 = "-";
        String puntaje4 = "-";

        int cantJugadores = getJugadores().size();
        for (int i = 0; i < rondasJugadas; i++) {
            numRonda = Integer.toString(i + 1);
            puntaje1 = jugadorEnIndice(0).getPuntosEnRondaN(i);
            puntaje2 = jugadorEnIndice(1).getPuntosEnRondaN(i);
            if (cantJugadores >= 3) {
                puntaje3 = jugadorEnIndice(2).getPuntosEnRondaN(i);
            }
            if (cantJugadores == 4) {
                puntaje4 = jugadorEnIndice(3).getPuntosEnRondaN(i);
            }
            System.out.println("ronda: " + (i+1) + ", P1: " + puntaje1 + ", P2: " + puntaje2 + ", P3: " + puntaje3 + ", P4: " + puntaje4);
            data.add(new FilaPuntajes(numRonda, puntaje1, puntaje2, puntaje3, puntaje4));
        }
        tableView.setItems(data);
    }

    /**
     * Método para cerrar la pantalla de puntajes y volver a abrir la pantalla de juego
     * Se encarga de cargar una nueva partida donde se van a ir acumulando los puntajes dentro de cada jugador
     */
    @FXML
    public void jugarOtraVez() {
        Stage currentStage = (Stage) botonJugarOtraVez.getScene().getWindow();

        try {
            // Carga el archivo FXML para la pantalla de juego
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RummikubGameBoard.fxml"));
            Parent gameBoardRoot = loader.load();
            Scene gameBoardScene = new Scene(gameBoardRoot);


            // Crea un nuevo escenario para la pantalla de juego
            Stage gameBoardStage = new Stage();
            gameBoardStage.setScene(gameBoardScene);
            gameBoardStage.setTitle("Rummikub - Game Board");

            // obtener la referencia a la clase de control para volver a la pantalla de juego
            RummikubGameBoard control = loader.getController();
            control.volverAJugar(this.jugadores);
            // cierra la ventana actual y abre el tablero
            currentStage.close();
            gameBoardStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que se llama automáticamente cuando se presiona el botón de "Terminar". Contiene las instrucciones necesarias
     * para crear una pantalla de cierre, y si se selecciona la confirmación, se encarga de cerrar apropiadamente la aplicación.
     */
    public void terminarJuego() {
        // Se crea un diálogo de confirmación para evitar que el jugador cierre accidentalmente el programa.
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Saliendo del juego...");
        confirmationDialog.setHeaderText("Gracias por jugar!");
        confirmationDialog.setContentText("¿Está seguro que desea salir? se perderá el progreso de las partidas jugadas.");

        // Se agregan botones de confirmación y anulación del cierre del juego
        confirmationDialog.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        // Se muestra el diálogo y se espera la respuesta del usuario
        ButtonType userResponse = confirmationDialog.showAndWait().orElse(ButtonType.CANCEL);

        // Si el usuario da clic a OK, se cierra la aplicación
        if (userResponse == ButtonType.OK) {
            Platform.exit();
        }
    }

    /**
     * Método de acceso al contenedor de las referencias a los jugadores dentro de esta clase.
     * @return un Vector de tipo Jugador que contiene los jugadores (de 2 a 4) que participan en las partidas.
     */
    public Vector<Jugador> getJugadores() {
        return jugadores;
    }

    /**
     * Método de acceso específico para un jugador en un índice.
     * @param x entero de 0 a 3 (si el vector es de tamaño 4) que representa el índice al que se desea acceder
     * @return el jugador que se encontraba en esa posición del vector.
     */
    private Jugador jugadorEnIndice(int x) {
        return jugadores.get(x);
    }
}
