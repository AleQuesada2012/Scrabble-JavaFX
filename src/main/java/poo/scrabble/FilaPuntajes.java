package poo.scrabble;

/**
 * Clase destinada a la lógica detrás de la fila de puntajes y el manejo de los jugadores.
 */
public class FilaPuntajes {
    private String numRonda;
    private String jugador1;
    private String jugador2;
    private String jugador3;
    private String jugador4;

    /**
     *
     * @param roundNumber un String que es el número de ronda por la que va la partida.
     * @param player1 un String que representa el nombre del primer jugador.
     * @param player2 un String que representa el nombre del segundo jugador.
     * @param player3 un String que representa el nombre del tercer jugador.
     * @param player4 un String que representa el nombre del cuarto jugador.
     */
    public FilaPuntajes(String roundNumber, String player1, String player2, String player3, String player4) {
        setNumRonda(roundNumber);
        setJugador1(player1);
        setJugador2(player2);
        setJugador3(player3);
        setJugador4(player4);
    }

    /**
     * Método de acceso al atributo "numRonda".
     * @return un String que representa la ronda actual por la que va la partida.
     */
    public String getNumRonda() {
        return numRonda;
    }

    /**
     * Método de cambio para el atributo "numRonda".
     * @param numRonda un String que representa la ronda por la que va la partida.
     */
    public void setNumRonda(String numRonda) {
        this.numRonda = numRonda;
    }

    /**
     * Método de acceso a el atributo "jugador1".
     * @return un String que representa el nombre de el primer jugador.
     */
    public String getJugador1() {
        return jugador1;
    }

    /**
     * Método de cambio para el atributo "jugador1".
     * @param jugador1 un String que representa el nombre de el segundo jugador.
     */
    public void setJugador1(String jugador1) {
        this.jugador1 = jugador1;
    }

    /**
     * Método de acceso para el atributo "jugador2".
     * @return un String que representa el nombre de el segundo jugador.
     */
    public String getJugador2() {
        return jugador2;
    }

    /**
     * Método de cambio para el atributo "jugador2".
     * @param jugador2 un String que representa el nombre de el segundo jugador.
     */
    public void setJugador2(String jugador2) {
        this.jugador2 = jugador2;
    }

    /**
     * Método de acceso para el atributo "jugador3".
     * @return un String que representa el nombre de el tercer jugador.
     */
    public String getJugador3() {
        return jugador3;
    }

    /**
     * Método de cambio para el atributo "jugador3".
     * @param jugador3 un String que representa el nombre de el tercer jugador.
     */
    public void setJugador3(String jugador3) {
        this.jugador3 = jugador3;
    }

    /**
     * Método de acceso para el atributo "jugador4".
     * @return un String que representa el nombre de el cuarto jugador.
     */
    public String getJugador4() {
        return jugador4;
    }

    /**
     * Método de cambio para el atributo "jugador4".
     * @param jugador4 un String que representa el nombre de el cuarto jugador.
     */
    public void setJugador4(String jugador4) {
        this.jugador4 = jugador4;
    }
}