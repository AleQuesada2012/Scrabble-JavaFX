package poo.scrabble;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Vector;


public class RummikubGameBoard {


    private String nombre1;
    private String nombre2;
    private String nombre3;
    private String nombre4;

    private Juego partida; // instancia que regula los tableros, jugadores, y cumplimiento de las reglas del juego
    private Jugador jugadorActual;


    private Ficha fichaActual; // apunta a una ficha temporal que se podria mover

    private Vector<Jugador> jugadores;

    private Vector<Jugador> jugadoresEnTurnoFinal;

    private int turnoActual; // guarda el numero de turnos jugados

    private int pilaActual; // guarda la cantidad de fichas restantes en la pila

    private boolean vieneDelSoporte;

    private int indiceSoporte; // guarda la posicion en el soporte que tiene la ficha actual

    private int primerJugadorIndex;
    private Button botonPrevio;

    private Button[][] botonesTablero;

    private int cantidadPreviaDeFichas;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private HBox topHBox;

    @FXML
    private Label currentPlayerLabel;

    @FXML
    private Label turnLabel;

    @FXML
    private Button validateTurnButton;

    @FXML
    private Button drawTileButton;

    @FXML
    private VBox mainVBox;

    @FXML
    private GridPane gameGrid;

    @FXML
    private GridPane soporteGridPane;

    /**
     * método que se invoca automáticamente cuando el "form" que controla esta clase se carga en una escena de JavaFX.
     * Se encarga de instanciar los objetos de JavaFX existentes en la interfaz para que puedan ser manipulados o interactuar
     * con ellos por medio de métodos vinculados a eventos de cada objeto.
     */
    public void initialize() {
        crearBotonesTablero(); // Call a method to create buttons
        crearBotonesSoporte();
        this.jugadores = new Vector<>();
        this.jugadoresEnTurnoFinal = new Vector<>();
    }

    /**
     * Se utiliza para crear los botones en tiempo de ejecución. El contenedor gridPane del soporte se declara vacío
     * previo a la ejecución de la aplicación, así que al inicializarse esta pantalla, se llama este método que instancia
     * todos los botones respectivos, les asigna un índice, un estilo, y un método de manejo de acceso.
     */
    private void crearBotonesTablero() {
        botonesTablero = new Button[15][15];
        int rows = 15;
        int columns = 15;
        int contieneFicha = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                botonesTablero[row][col] = new Button("Button " + row + "-" + col);
                // se almacena información importante dentro del botón, como su fila, columna, y si este tiene o no una ficha.
                botonesTablero[row][col].setUserData(new int[]{row, col, contieneFicha});
                botonesTablero[row][col].setText("");
                botonesTablero[row][col].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                // Define el método encargado de manejar el evento de haber dado click en el
                botonesTablero[row][col].setOnAction(e -> manejarClickFichaMatriz(e));

                gameGrid.add(botonesTablero[row][col], col, row); // Añade el botón al grid
                //GridPane.setRowIndex(button, row);
                //GridPane.setColumnIndex(button, col);
            }
        }
    }

    /**
     * Se utiliza para crear los botones en tiempo de ejecución. El contenedor gridPane del soporte se declara vacío
     * previo a la ejecución de la aplicación, así que al inicializarse esta pantalla, se llama este método que instancia
     * todos los botones respectivos, les asigna un índice, un estilo, y un método de manejo de acceso.
     */
    private void crearBotonesSoporte() {
        int filas = 3;
        int columnas = 15;
        int posSoporte = 0;
        int hayFichaDentro = 0;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                Button button = new Button("SoporteBtn " + i + '-' + columnas);
                button.setUserData(new int[] {i, j, posSoporte, hayFichaDentro}); // almacena la fila y columna igual que en el tablero
                button.setText("");
                button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                button.setOnAction(e -> manejarClickFichaSoporte(e));
                soporteGridPane.add(button, j, i);
                posSoporte ++;
            }
        }
    }

    /**
     * Método para controlar los clicks en los botones que representan el tablero. Se asigna durante tiempo de ejecución
     * a todos los botones del tablero, de modo que no se necesita crear un método único para cada botón.
     * @param event el click realizado por el jugador. Se utiliza para obtener la referencia al botón que fue presionado.
     */
    private void manejarClickFichaMatriz(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        int[] rowIndex = (int[]) clickedButton.getUserData();
        int row = rowIndex[0];
        int col = rowIndex[1];
        int tieneFicha = rowIndex[2];
        if(tieneFicha == 0) {
            System.out.println("entró a que el botón no tiene ficha");
            if (fichaActual != null && vieneDelSoporte) {
                System.out.println("entró a que la ficha no es nula y viene del soporte");
                // quiere decir que vamos a sacar una ficha del soporte y ponerla en el boton que se hizo click
                partida.getTemporalMesa().ingresarFicha(fichaActual, row, col, jugadorActual); // ya coloque la ficha
                fichaActual = null;
                indiceSoporte = 0;
                vieneDelSoporte = false;
                this.setSoporteInicial(jugadorActual);
                this.cargarTableroTemporal();
            }
            if (fichaActual != null && !vieneDelSoporte) {
                // entra cuando lo que se quiere hacer es mover una ficha del tablero a la que ya se le hizo click
                if (jugadorActual.puedoEmpezar() && botonPrevio != null) {

                    // restaura que el boton anterior ya no contenga que si tiene ficha.
                    int[] datosBoton = (int[]) botonPrevio.getUserData();
                    datosBoton[2] = 0; // ahora el boton previo no deberia tener ficha
                    botonPrevio.setUserData(datosBoton);

                    clickedButton.setStyle(botonPrevio.getStyle());
                    clickedButton.setText(botonPrevio.getText());// copia la apariencia del boton anterior
                    //
                    datosBoton = (int[]) clickedButton.getUserData();
                    datosBoton[2] = 1; //ahora el boton nuevo si tiene ficha.

                    int[] datosBtnPrevio = (int[]) botonPrevio.getUserData();
                    partida.getTemporalMesa().reacomodarFicha(datosBtnPrevio[0], datosBtnPrevio[1], datosBoton[0], datosBoton[1]);

                    botonPrevio.setStyle("");
                    botonPrevio.setText("");
                    //partida.getTemporalmesa().reacomodarFicha(coordenadasFicha[0], coordenadasFicha[1], row, col);
                    cargarTableroTemporal();
                    // despues de reubicar la ficha, se pone el valor de la ficha actual en nulo, y de las coordenadas en el valor de conveniencia
                    fichaActual = null;
                    botonPrevio = null;
                } else {
                    showErrorMessage("No puede manipular las fichas del tablero hasta que haga su primera jugada.");
                }
            }
        }
        else {
            if (fichaActual == null) {
                System.out.println("entra a que la ficha actual es nula y el boton si tiene ficha");
                // no hace falta validar si la seleccion es del soporte porque el metodo se llama solo si se presiona un boton en el tablero
                if (jugadorActual.puedoEmpezar() && botonPrevio == null) {
                    System.out.println("entra a cuando saca la ficha actual del boton cliqueado");
                    fichaActual = partida.getTemporalMesa().getFichaEnXY(row, col);
                    System.out.println("Ficha Actual: " + fichaActual.getNum() + fichaActual.getColor());
                    botonPrevio = clickedButton;
                }
                else {
                    showErrorMessage("No puede manipular fichas del tablero hasta hacer una jugada de 30 puntos.");
                }
            }
        }

        System.out.println("Button clicked at Row " + row + ", Column " + col);
    }

    /**
     * Método para controlar los clicks en los botones que representan el soporte. Se asigna durante tiempo de ejecución
     * a todos los botones del soporte, de modo que no se necesita crear un método único para cada botón.
     * @param event el click realizado por el jugador. Se utiliza para obtener la referencia al botón que fue presionado.
     */
    private void manejarClickFichaSoporte(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();


        int[] rowIndex = (int[]) clickedButton.getUserData();
        int row = rowIndex[0];
        int col = rowIndex[1];
        int indiceOriginal = rowIndex[2];
        int tieneFicha = rowIndex[3];

        if(fichaActual == null) {
            //System.out.println("entro a que ficha actual no es nula");
            if (tieneFicha == 1) {
                System.out.println("entró a que el botón clicado sí tiene ficha");
                this.fichaActual = jugadorActual.escogerficha(indiceOriginal);
                System.out.println("ficha Actual: " + fichaActual.getNum() + fichaActual.getColor());
                indiceSoporte = indiceOriginal;
                vieneDelSoporte = true;
                this.setSoporteInicial(jugadorActual);
                this.cargarTableroTemporal();
            }
        }
        else {
            //System.out.println("entra a que la ficha actual no era nula y va a cambiar la referencia");
            if (vieneDelSoporte && tieneFicha == 1) {
                // lo que quiere hacer aqui es escoger otra ficha del soporte
                this.fichaActual = jugadorActual.escogerficha(indiceOriginal);
                //System.out.println("ficha actual: " + fichaActual.getNum() + fichaActual.getColor());
            } else if (vieneDelSoporte && tieneFicha == 0) {
                this.fichaActual = null;
            }
        }


        System.out.println("Button clicked at Row " + row + ", Column " + col);
        System.out.println("La ficha en el soporte esta en el indice: " + indiceOriginal);
    }


    /**
     * Método llamado por la pantalla inicial cuando el usuario empieza el juego, utilizado para pasarle los nombres a
     * esta clase y que pueda crear los objetos de tipo Jugador.
     * @param input1 la cadena de entrada en el campo para el jugador 1
     * @param input2 la cadena de entrada en el campo para el jugador 2
     * @param input3 la cadena de entrada en el campo para el jugador 3
     * @param input4 la cadena de entrada en el campo para el jugador 4
     */
    public void initNombres(String input1, String input2, String input3, String input4) {

        setNombre1(input1);
        setNombre2(input2);
        setNombre3(input3);
        setNombre4(input4);

        startGame();
    }

    /**
     * Método que se encarga de iniciar el juego, tanto en la lógica como en la interfaz. Define el valor de variables
     * importantes e inicializa otras como la partida.
     */
    private void startGame() {
        this.jugadores.clear(); // para asegurarse que antes de jugar por primera vez no exista referencia a otros jugadores

        this.partida = getJuego();

        primerJugadorIndex = partida.determinarOrden();
        Jugador primerJugador = partida.getJugadores().get(primerJugadorIndex);
        this.jugadorActual = primerJugador;
        System.out.println(primerJugador); // para depurar y ver si se crea el jugador correctamente
        partida.agarrarfichas();

        boolean gameOver = false;
        turnoActual = 1;
        pilaActual = partida.getTablero().getFichas().getStackSize();
        fichaActual = null;
        //coordenadasFicha = new int[]{-1, -1};
        botonPrevio = null;
        currentPlayerLabel.setText("Jugando: " + primerJugador.getNombre());
        setSoporteInicial(primerJugador);
        cargarTableroTemporal();
        turnLabel.setText("Turno: " + turnoActual);
        drawTileButton.setText("Tomar ficha (" + pilaActual + ")");
    }

    /**
     * Método llamado por la pantalla de puntajes cuando el jugador escoge jugar otra partida. Reutiliza las instancias
     * de los jugadores ya que estos almacenan su "historial" de puntos.
     * @param jugadores un vector con los jugadores existentes. Se usa un vector ya que no es una cantidad exacta.
     */
    public void volverAJugar(Vector<Jugador> jugadores) {
        this.jugadores = jugadores;
        this.partida = new Juego(); // se reinicia
        for (Jugador jugador: jugadores) {
            jugador.reestablecerJugador();
            jugador.setFichasEnMano();
            this.partida.agregarjugador(jugador);
        }
        // se realiza el mismo proceso de inicialización para jugar una nueva partida
        primerJugadorIndex = partida.determinarOrden();
        Jugador primerJugador = partida.getJugadores().get(primerJugadorIndex);
        this.jugadorActual = primerJugador;
        System.out.println(primerJugador); // para depurar y ver si se crea el jugador correctamente
        partida.agarrarfichas();

        boolean gameOver = false;
        turnoActual = 1;
        pilaActual = partida.getTablero().getFichas().getStackSize();
        fichaActual = null;
        //coordenadasFicha = new int[]{-1, -1};
        botonPrevio = null;
        currentPlayerLabel.setText("Jugando: " + primerJugador.getNombre());
        setSoporteInicial(primerJugador);
        cargarTableroTemporal();
        turnLabel.setText("Turno: " + turnoActual);
        drawTileButton.setText("Tomar ficha (" + pilaActual + ")");
    }

    /**
     * Método aislado del flujo principal del juego para evitar hacerlo un bloque muy grande y perder legibilidad.
     * Se encarga de crear la instancia del juego a usar durante la partida, y se crean juegos nuevos para cada partida.
     */
    private Juego getJuego() {
        Juego partida = new Juego();
        Jugador j1 = new Jugador();
        j1.setNombre(nombre1);
        j1.setFichasEnMano();
        j1.crearVectorPuntos();
        partida.agregarjugador(j1);
        jugadores.add(j1);

        Jugador j2 = new Jugador();
        j2.setNombre(nombre2);
        j2.setFichasEnMano();
        j2.crearVectorPuntos();
        partida.agregarjugador(j2);
        jugadores.add(j2);

        if (!nombre3.isEmpty()) {
            Jugador j3 = new Jugador();
            j3.setNombre(nombre3);
            j3.setFichasEnMano();
            j3.crearVectorPuntos();
            partida.agregarjugador(j3);
            jugadores.add(j3);
        }

        if (!nombre4.isEmpty()) {
            Jugador j4 = new Jugador();
            j4.setNombre(nombre4);
            j4.setFichasEnMano();
            j4.crearVectorPuntos();
            partida.agregarjugador(j4);
            jugadores.add(j4);
        }
        return partida;
    }

    /**
     * Método para actualizar el estado .
     * Recorre la matriz de botones y les cambia la apariencia (color y texto) para que representen correctamente la
     * matriz existente en la lógica del juego.
     */
    private void setSoporteInicial(Jugador jugadorActual) {

        indiceSoporte = 0;
        System.out.println("Sus fichas: ");
        jugadorActual.printFichas();
        int sizeSoporte = jugadorActual.cantFichas();
        for (Node boton : soporteGridPane.getChildren()) {
            Button button = (Button) boton; // convierte el objeto de tipo nodo a un objeto de tipo boton
            if (indiceSoporte < sizeSoporte){// deja de recorrer el arreglo de fichas cuando llegue a la ultima
            Ficha fichaActual = jugadorActual.getFichasEnMano().getficha(indiceSoporte);
                // como el comodín tiene un 0, se hace una validación adicional para cambiar el texto a C
                if(fichaActual.getNum() == 0) {
                    button.setText("C");
                }
                else {
                    button.setText(Integer.toString(fichaActual.getNum()));
                }
                int[] infoAlmacenada = (int[]) button.getUserData();
                infoAlmacenada[3] = 1;
                button.setUserData(infoAlmacenada);


                // logica para cambiar el color del boton segun el valor de la ficha
                switch (fichaActual.getColor()) {
                    case "N" -> button.setStyle("-fx-background-color: rgba(128, 128, 128, 0.5); -fx-text-fill: black;");
                    case "A" -> button.setStyle("-fx-background-color: rgba(0, 0, 255, 0.5); -fx-text-fill: black;");
                    case "Y" -> button.setStyle("-fx-background-color: rgba(255, 255, 0, 0.5); -fx-text-fill: black;");
                    case "R" -> button.setStyle("-fx-background-color: rgba(255, 0, 0, 0.5); -fx-text-fill: black;");
                    case "comodin" -> button.setStyle("-fx-background-color: rgba(0, 200, 200, 0.5); -fx-text-fill: black");
                }
            }
            else {
                button.setText("");
                button.setStyle("");
                int[] infoAlmacenada = (int[]) button.getUserData();
                infoAlmacenada[3] = 0;
                button.setUserData(infoAlmacenada);
            }
            indiceSoporte++;
        }
    }

    /**
     * Método para cambiar el estado de la matriz del tablero al cargar el último estado del tablero temporal.
     * Recorre la matriz de botones y les cambia la apariencia (color y texto) para que representen correctamente la
     * matriz existente en la lógica del juego.
     */
    private void cargarTableroTemporal() {
        Mesa tableroTemporal = partida.getTemporalMesa(); // matriz de fichas
        System.out.println("Tablero actual");
        tableroTemporal.imprimirMesa();
        int filaMesa = 0;
        int columnaMesa = 0;
        Ficha fichaActual;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                Button botonActual = getButtonByRowColumnIndex(i, j, gameGrid);
                fichaActual = tableroTemporal.getMatrizFichas()[i][j];
                if (fichaActual != null) {
                    if(fichaActual.getNum() == 0) {
                        botonActual.setText("C");
                    }
                    else {
                        botonActual.setText(Integer.toString(fichaActual.getNum()));
                    }

                    // cambiar el valor del arreglo de user Data para que marque que si contiene ficha
                    int[] infoAlmacenada = (int[]) botonActual.getUserData();
                    infoAlmacenada[2] = 1;
                    botonActual.setUserData(infoAlmacenada);

                    // se cambia la apariencia del boton para que refleje la ficha que representa
                    switch (fichaActual.getColor()) {
                        case "N" -> botonActual.setStyle("-fx-background-color: rgba(128, 128, 128, 0.5); -fx-text-fill: black;");
                        case "A" -> botonActual.setStyle("-fx-background-color: rgba(0, 0, 255, 0.5); -fx-text-fill: black;");
                        case "Y" -> botonActual.setStyle("-fx-background-color: rgba(255, 255, 0, 0.5); -fx-text-fill: black;");
                        case "R" -> botonActual.setStyle("-fx-background-color: rgba(255, 0, 0, 0.5); -fx-text-fill: black;");
                        case "comodin" -> botonActual.setStyle("-fx-background-color: rgba(0, 200, 200, 0.5); -fx-text-fill: black");
                    }
                }
                else {
                    botonActual.setText("");
                    botonActual.setStyle("");
                    int[] infoAlmacenada = (int[]) botonActual.getUserData();
                    infoAlmacenada[2] = 0;
                    botonActual.setUserData(infoAlmacenada);
                }
            }
        }
    }

    /**
     * Método para cambiar el estado de la matriz del tablero al cargar el último estado del tablero válido.
     * Recorre la matriz de botones y les cambia la apariencia (color y texto) para que representen correctamente la
     * matriz existente en la lógica del juego.
     */
    private void cargarTableroValido() {
        Mesa tableroValido = partida.getTablero(); // matriz de fichas
        System.out.println("Tablero Valido:");
        tableroValido.imprimirMesa();
        int filaMesa = 0;
        int columnaMesa = 0;
        Ficha fichaActual;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                Button botonActual = getButtonByRowColumnIndex(i, j, gameGrid);
                fichaActual = tableroValido.getMatrizFichas()[i][j];
                if (fichaActual != null) {
                    if(fichaActual.getNum() == 0) {
                        botonActual.setText("C");
                    }
                    else {
                        botonActual.setText(Integer.toString(fichaActual.getNum()));
                    }

                    // cambiar el valor del arreglo de user Data para que marque que si contiene ficha
                    int[] infoAlmacenada = (int[]) botonActual.getUserData();
                    infoAlmacenada[2] = 1;
                    botonActual.setUserData(infoAlmacenada);

                    // se cambia la apariencia del boton para que refleje la ficha que representa
                    switch (fichaActual.getColor()) {
                        case "N" -> botonActual.setStyle("-fx-background-color: rgba(128, 128, 128, 0.5); -fx-text-fill: black;");
                        case "A" -> botonActual.setStyle("-fx-background-color: rgba(0, 0, 255, 0.5); -fx-text-fill: black;");
                        case "Y" -> botonActual.setStyle("-fx-background-color: rgba(255, 255, 0, 0.5); -fx-text-fill: black;");
                        case "R" -> botonActual.setStyle("-fx-background-color: rgba(255, 0, 0, 0.5); -fx-text-fill: black;");
                        case "comodin" -> botonActual.setStyle("-fx-background-color: rgba(0, 200, 200, 0.5); -fx-text-fill: black");
                    }
                }
                else {
                    botonActual.setText("");
                    botonActual.setStyle("");
                    int[] infoAlmacenada = (int[]) botonActual.getUserData();
                    infoAlmacenada[2] = 0;
                    botonActual.setUserData(infoAlmacenada);
                }
            }
        }
    }


    /**
     * Método para obtener un botón de un gridPane por medio de los índices i y j (fila y columna
     * @param row la posición en las filas del botón a obtener
     * @param column la posición en las columnas del botón a obtener
     * @param gridPane la referencia al objeto "gridPane" del que se quiere conseguir un botón
     * @return la referencia al botón en la posición deseada.
     */
    private Button getButtonByRowColumnIndex(int row, int column, GridPane gridPane) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column && node instanceof Button) {
                return (Button) node;
            }
        }
        return null; // Botón no encontrado
    }

    /**
     * Método para el manejo de clicks sobre el botón "Validar Turno". Es invocado automáticamente.
     */
    @FXML
    private void manejarBotonValidarTurno() {
        fichaActual = null;
        botonPrevio = null;

        if (partida.getTemporalMesa().valorDeJugada() && partida.getTemporalMesa().matrizValida()) {
            jugadorActual.setPuedoempezar(true);
        }
        if (!jugadorActual.puedoEmpezar()) {
            showErrorMessage("Su última jugada fue inválida. Intente de nuevo");
            partida.getTemporalMesa().restaurarFichas(jugadorActual);
            partida.getTemporalMesa().copiarMesa(partida.getTablero());
            setSoporteInicial(jugadorActual);
            cargarTableroTemporal();
        }
        else {
            // entra cuando se puede cambiar de turno porque supera los 30 puntos y es valido
            if (partida.getTemporalMesa().matrizValida() && cantidadPreviaDeFichas < partida.getTemporalMesa().getCantFichas()) {

                if (jugadorActual.getFichasEnMano().getCantfichas() == 0) {
                    partida.sumarPuntos();
                    terminarPartida();
                }

                partida.getTablero().copiarMesa(partida.getTemporalMesa());
                partida.getTablero().sonParteDe();

                cantidadPreviaDeFichas = partida.getTablero().getCantFichas();

                int indiceSigJugador = (primerJugadorIndex + 1) % partida.getJugadores().size();
                primerJugadorIndex = indiceSigJugador;
                jugadorActual = partida.getJugadores().get(primerJugadorIndex);

                turnoActual ++;
                turnLabel.setText("Turno: " + turnoActual);
                currentPlayerLabel.setText("Jugando: " + jugadorActual.getNombre());

                cargarTableroValido();
                setSoporteInicial(jugadorActual);
            }
            else {
                showErrorMessage("Debe agregar una jugada válida para pasar turno, o tomar ficha.");
                partida.getTemporalMesa().restaurarFichas(jugadorActual);
                partida.getTemporalMesa().copiarMesa(partida.getTablero());
                setSoporteInicial(jugadorActual);
                cargarTableroTemporal();
            }
        }
    }

    /**
     * Método para el manejo de clicks sobre el botón "Tomar Ficha". Es invocado automáticamente.
     */
    @FXML
    private void manejarBotonTomarFicha() {
        fichaActual = null;
        botonPrevio = null;
        if (partida.getTemporalMesa().matrizValida()) {
            Ficha tomada = partida.getTablero().agarrarpila();

            if (pilaActual > 0) { // si no es nulo es porque aun quedan fichas en la pila
                pilaActual--;
                drawTileButton.setText("Tomar ficha (" + pilaActual + ")");
                // en caso de que la persona coloca una jugada válida pero elige tomar ficha, se restauran
                partida.getTemporalMesa().restaurarFichas(jugadorActual);
                partida.getTemporalMesa().copiarMesa(partida.getTablero());

                jugadorActual.getFichasEnMano().ingresarficha(tomada);
                int indiceSigJugador = (primerJugadorIndex + 1) % partida.getJugadores().size();
                primerJugadorIndex = indiceSigJugador;
                jugadorActual = partida.getJugadores().get(primerJugadorIndex);
                cargarTableroValido();
                setSoporteInicial(jugadorActual);

                cantidadPreviaDeFichas = partida.getTablero().getCantFichas();

                turnoActual ++;
                turnLabel.setText("Turno: " + turnoActual);
                currentPlayerLabel.setText("Jugando: " + jugadorActual.getNombre());
            }
            else {
                drawTileButton.setText("Pasar turno");
                if (jugadoresEnTurnoFinal.size() == jugadores.size()) {
                    partida.sumarPuntosPilaEnCero();
                    terminarPartida();
                }
                else{
                    partida.getTemporalMesa().restaurarFichas(jugadorActual);
                    partida.getTemporalMesa().copiarMesa(partida.getTablero());

                    jugadoresEnTurnoFinal.add(jugadorActual);

                    int indiceSigJugador = (primerJugadorIndex + 1) % partida.getJugadores().size();
                    primerJugadorIndex = indiceSigJugador;
                    jugadorActual = partida.getJugadores().get(primerJugadorIndex);
                    cargarTableroValido();
                    setSoporteInicial(jugadorActual);

                    cantidadPreviaDeFichas = partida.getTablero().getCantFichas();

                    turnoActual ++;
                    turnLabel.setText("Turno: " + turnoActual);
                    currentPlayerLabel.setText("Jugando: " + jugadorActual.getNombre());
                }
            }
        }
        else {
            showErrorMessage("La disposición del tablero no era válida. Por favor reorganice sus fichas.");
            partida.getTemporalMesa().restaurarFichas(jugadorActual);
            partida.getTemporalMesa().copiarMesa(partida.getTablero());
            setSoporteInicial(jugadorActual);
            cargarTableroTemporal();

        }
    }

    /**
     * Método para cargar la ventana de puntajes cuando se termina la partida. No está vinculado a un botón directamente,
     * pero se invoca cuando alguno de los botones que cambian el estado de la pantalla es presionado y las condiciones
     * para terminar la partida se cumplen.
     */
    private void terminarPartida() {
        Stage currentStage = (Stage) gameGrid.getScene().getWindow();

        try {
            // Carga el archivo FXML para la pantalla de juego
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RummikubGanador.fxml"));
            Parent puntajesRoot = loader.load();
            Scene puntajesScene = new Scene(puntajesRoot);


            // Crea un nuevo escenario para la pantalla de juego
            Stage gameBoardStage = new Stage();
            gameBoardStage.setScene(puntajesScene);
            gameBoardStage.setTitle("Rummikub - Puntajes");

            // obtener la referencia a la clase de control para volver a la pantalla de juego
            RummikubGanador control = loader.getController();
            control.recibirJugadores(this.jugadores);
            // cierra la ventana de juego y abre la de puntajes
            currentStage.close();
            gameBoardStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método de configuración/seteo para modificar el atributo ligado al nombre del primer jugador.
     * @param nombre1 la cadena de texto ingresada por el usuario al lanzar la aplicación.
     */
    public void setNombre1(String nombre1) {
        this.nombre1 = nombre1;
    }

    /**
     * Método de configuración/seteo para modificar el atributo ligado al nombre del segundo jugador.
     * @param nombre2 la cadena de texto ingresada por el usuario al lanzar la aplicación.
     */
    public void setNombre2(String nombre2) {
        this.nombre2 = nombre2;
    }

    /**
     * Método de configuración/seteo para modificar el atributo ligado al nombre del tercer jugador.
     * @param nombre3 la cadena de texto ingresada por el usuario al lanzar la aplicación.
     */
    public void setNombre3(String nombre3) {
        this.nombre3 = nombre3;
    }

    /**
     * Método de configuración/seteo para modificar el atributo ligado al nombre del cuarto jugador.
     * @param nombre4 la cadena de texto ingresada por el usuario al lanzar la aplicación.
     */
    public void setNombre4(String nombre4) {
        this.nombre4 = nombre4;
    }

    /**
     * Método apartado para mostrar mensajes como alerta al usuario, con el objetivo de reutilizar el código y no repetir
     * el bloque de instrucciones cada vez que se desea imprimir un mensaje.
     * @param errorMessage la cadena de texto que se desea mostrar al usuario.
     */
    private void showErrorMessage(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);

        // Set the dialog modality to APPLICATION_MODAL
        alert.initModality(Modality.APPLICATION_MODAL);

        // Create an "OK" button to close the dialog
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(buttonTypeOk);

        // Show the dialog and wait for the user's response
        alert.showAndWait();
    }

}
