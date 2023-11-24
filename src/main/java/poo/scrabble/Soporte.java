package poo.scrabble;

import java.util.Vector;


/**
 * Clase destinada a ser la representación de la "mano" o "soporte" del jugador. Se utiliza para saber cuáles fichas le
 * pertenecen a cada jugador.
 */
public class Soporte {

    private  Vector<Ficha>fichas;

    private  int cantFichas;

    /**
     * Constructor por defecto de la clase Soporte la cual crea una mano vacía de el jugador.
     */
    public Soporte() {
        this.fichas = new Vector<>();
    }

    /**
     * Constructor inicializado el cual crea la mano de el jugador con las fichas dadas.
     * @param fichas1 un Vector de fichas que representa la mano de un jugador.
     */
    public Soporte(Vector<Ficha>fichas1){
        this.fichas = fichas1;
    }

    /**
     * Método de acceso a las fichas del jugador. Se utiliza para obtener el vector que las almacena.
     * @return un vector con objetos de tipo Ficha que corresponden al arreglo de fichas que le pertenecen al jugador.
     */
    public Vector<Ficha> getFichas(){
        return fichas;
    }

    /**
     * Se utiliza para acceder a una ficha en una posición determinada del vector en el soporte.
     * @param x índice entre 0 y N-1, donde N es el tamaño del soporte, en el que se encuentra la ficha.
     * @return la referencia a la ficha que se encontraba en el índice x.
     */
    public Ficha getficha(int x){
        return fichas.get(x);
    }

    /**
     * Método para acceder al atributo de cantidad de fichas.
     * @return un valor entero que representa la cantidad de fichas en la mano del jugador.
     */
    public int getCantfichas() {
        return cantFichas;
    }

    /**
     * Método para sobreescribir el valor del atributo relacionado a la cantidad de fichas del jugador.
     * @param cantfichas un entero mayor o igual que 0 siendo este la cantidad actual de fichas en la mano.
     */
    public void setCantfichas(int cantfichas) {
        this.cantFichas = cantfichas;
    }

    /**
     * Método usado para dar la suma de las fichas de un jugador al final de la partida.
     * @return un int con el valor de la suma de las fichas del jugador.
     */

    /**
     * Método destinado a ingresar una ficha dentro de la mano de un jugador.
     * @param ficha una Ficha la cual será ingresada a la mano de el jugador.
     */
    public void ingresarficha(Ficha ficha){
        fichas.add(ficha);
        cantFichas++;
    }

    /**
     * se utiliza para denotar que una ficha dentro de la mano del jugador fue utilizada en una jugada válida. Se encarga
     * de modificar tanto el vector de fichas como la cantidad de fichas respectivas.
     * @param ficha la referencia a la ficha que se utilizó en la jugada.
     */
    public void usarficha(Ficha ficha){
        fichas.remove(ficha);
        cantFichas--;
    }

    /**
     * Método para actualizar las fichas del soporte a un jugador.
     * @param newTiles vector de fichas que se desea establecer como el soporte.
     */
    public void setFichas(Vector<Ficha> newTiles) {
        this.fichas.clear(); // Clear the current tiles
        this.fichas.addAll(newTiles); // Add the copied tiles
    }

    /**
     * Método para copiar las fichas de un soporte deseado.
     * @param source soporte el cual se desea copiar sus fichas.
     */
    public void copiarsoporte(Soporte source) {
        fichas.clear();
        for (Ficha ficha : source.getFichas()) {
            fichas.add(ficha);
        }
    }
}