package poo.scrabble;

import java.util.Vector;

/**
 * Clase que maneja la creación de un una partida o un Juego.
 */
public class Juego {

    private Mesa tablero;

    private Mesa temporalMesa;

    private Vector<Jugador> jugadores;

    /**
     * Constructor de la clase Juego la cual crea un nuevo tablero, una mesa temporal y los jugadores.
     */
    public Juego() {
        tablero = new Mesa();
        temporalMesa = new Mesa(1);
        jugadores = new Vector<>();
    }

    /**
     * Método de cambio para el atributo "jugadores".
     * @param jugadores un Vector que representa a los jugadores de la partida.
     */
    public void setJugadores(Vector<Jugador> jugadores) {
        this.jugadores = jugadores;
    }


    /**
     * Método para agregar jugadores al vector para guardar sus nombres.
     * @param jugador un Jugador que representa a una persona nueva jugando.
     */
    public void agregarjugador(Jugador jugador){
        jugadores.add(jugador);
    }


    /**
     * Método el cual es usado para determinar el orden en el cual va a iniciar la partida.
     * @return un int con el índice de el jugador el cual tiene la ficha más grande.
     */
    public int determinarOrden() {
        int maxficha = -1;
        int indicedeprimerjugador = 0;

        for (int i = 0; i < this.jugadores.size(); i++) {
            Jugador jugador = this.jugadores.get(i);
            jugador.agregarFicha(tablero.agarrarpila());
            for (Ficha ficha : jugador.getFichasEnMano().getFichas()) {
                if (ficha.getNum() > maxficha) {
                    maxficha= ficha.getNum();
                    indicedeprimerjugador= i;
                }
            }
        }

        return indicedeprimerjugador;
    }

    /**
     * Método el cual es usado para sumar los puntos de los jugadores y meterlos en sus vectores respectivos, cuando la partida se acaba porque un jugador se quedo sin fichas.
     */
    public void sumarPuntos(){
        int cont = 0;
        Jugador ganador = null;
        for(Jugador jugador : getJugadores()){
            if(jugador.getFichasEnMano().getCantfichas()==0){
                jugador.setGanador(true);
                ganador = jugador;

            }
            else {
                int puntos = jugador.getFichasEnMano().getsumadefichas();
                jugador.setPuntos(puntos * -1); // le coloca en negativo los puntos al jugador
                jugador.setPuntosTotales(jugador.getPuntosTotales()  + (puntos * -1));
                cont += puntos;

            }
        }
        if(ganador!=null){
            ganador.setPuntos(cont);
            ganador.setPuntosTotales(ganador.getPuntosTotales() + cont);
        }
    }

    /**
     * Método el cual es usado para sumar los puntos de los jugadores y meterlos en sus vectores respectivos, cuando la pila se encuentra vacia y nadie puede jugar más.
     */
    public void sumarPuntosPilaEnCero() {
        Jugador ganador = null;
        int sumaMenorDePuntos = 1000000; //Un número arbitrario grande para sacar el menor
        for (Jugador jugador : getJugadores()) {
            if (jugador.getFichasEnMano().getsumadefichas() < sumaMenorDePuntos) {
                ganador = jugador;
                sumaMenorDePuntos = jugador.getFichasEnMano().getsumadefichas();
            }
        }
        ganador.setGanador(true);

        // se vuelven a recorrer, esta vez ya sabiendo el que tenía la cantidad menor de puntos
        for (Jugador jugador : getJugadores()) {
            int puntos = jugador.getFichasEnMano().getsumadefichas();
            if (!jugador.isGanador()) {
                puntos *= -1;
            }
            jugador.setPuntos(puntos);
            jugador.setPuntosTotales(jugador.getPuntosTotales() + puntos);
        }
    }

    /**
     * Método el cual es usado para agarrar las fichas de los jugadores para el soporte.
     */
    public void agarrarfichas(){
        for(Jugador jugador :  jugadores) {
            for (int i = 0; i < 13; i++) {
                jugador.agregarFicha((tablero.agarrarpila()));
            }
        }

    }

    /**
     * Método que realiza una copia de el soporte de el jugador seleccionado.
     * @param jugador un Jugador que representa a la persona jugando en el momento.
     * @return un Vector de la copia de las fichas de el jugador.
     */
    public Vector<Ficha> copiarFichasEnMano(Jugador jugador) {
        Vector<Ficha> copiedTiles = new Vector<>();

        for (Ficha ficha : jugador.getFichasEnMano().getFichas()) {
            Ficha copiedFicha = new Ficha(ficha.getNum(), ficha.getColor());
            copiedTiles.add(copiedFicha);
        }

        return copiedTiles;
    }

    /**
     * Metódo que se encarga de eliminar una ficha de una posición de la matriz.
     * @param x un int que representa el eje x de la matriz.
     * @param y un int que representa el eje y de la matriz.
     */
    public void retirarFicha(int x, int y){
        Ficha temp = tablero.getMatrizFichas()[x][y];
        tablero.getMatrizFichas()[x][y] = null;

    }

    /**
     * Método de acceso a el atributo "temporalMesa".
     * @return la Mesa temporal que se lleva junto con la mesa original.
     */
    public Mesa getTemporalMesa() {
        return temporalMesa;
    }

    /**
     * Método que actualiza el tablero guardado, si es válido.
     */
    public void actualizarMesa(){
        if(temporalMesa.matrizValida()){
            tablero.copiarMesa(temporalMesa);
        }
    }

    /**
     * Método usado para terminar el turno de un jugador y actualiza la mesa.
     */
    public void terminarTurno(){
        actualizarMesa();

    }

    /**
     * Método de acceso a el atributo "tablero".
     * @return la Mesa actual de el juego.
     */
    public Mesa getTablero() {
        return tablero;
    }

    /**
     * Método de acceso a el atributo "jugadores".
     * @return el Vector que contiene los jugadores de la partida.
     */
    public Vector<Jugador> getJugadores() {
        return this.jugadores;
    }

}