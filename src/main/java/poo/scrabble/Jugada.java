

import java.util.Vector;

/**
 * Clase utilizada para crear las jugadas con las fichas del tablero con sus respectivos métodos y atributos.
 */

public class Jugada {
    private Vector<Ficha> jugada;
    private Diccionario diccionario;


    public Jugada() {
        jugada = new Vector<>();
        // diccionario = new Diccionario(); ESTO ES LO QUE NO HAY QUE HACER PORQUE CARGA A MEMORIA EL ARCHIVO CADA VEZ QUE SE CREA UNA JUGADA (sea valida o no)
    }

    public  Jugada(Vector<Ficha>lista){
        jugada = lista;
    }

    public Jugada (Diccionario diccionario1) {
        this.diccionario = diccionario1;
    }

    /**
     * Método para verificar que la secuencia de fichas del tipo escalera sea válido.
     * @return un booleano que representa el valor de verdad de la escalera.
     */
    public boolean jugadavalida(){
        StringBuilder wordBuilder = new StringBuilder();

        for (Ficha ficha : jugada) {
            // Assuming Ficha has a method to get the letter
            char letter = ficha.getLetra();
            wordBuilder.append(letter);
        }

        return wordBuilder.toString();

    }


    /**
     * Método para calcular el valor de una jugada según los puntos de las fichas.
     * @return un valor entero que representa los puntos de la jugada calculada.
     */
    public int valorJugada(){
        int cont = 0;
        if(this.escaleraValida() || this.serieValida()){
            for(int i=0;i<this.jugada.size();i++){
                Ficha ficha = this.jugada.get(i);
                cont+=ficha.getNum();

            }
        }
        return cont;
    }

    /**
     * Método el cual obtiene la ficha en una posición deseada de la jugada.
     * @param x recibe el índice de la ficha en la jugada que se desea obtener.
     * @return retorna la ficha deseada.
     */
    public Ficha getFichaPos(int x){
        return this.jugada.get(x);

    }
}