

import java.util.Vector;

/**
 * Clase que representa el tablero del juego.
 */
public class Mesa {
    private Ficha[][] matrizFichas;

    private Pila fichas;

    private int espaciolleno;

    /**
     * Constructor de la clase mesa el cual genera una pila de fichas y establece
     * la matriz que será el tablero.
     */
    public Mesa() {
        fichas = new Pila();
        matrizFichas = new Ficha[15][15];
    }

    /**
     * Segundo constructor de la clase mesa el cual solamente genera una matriz
     * esta será usada para la creación de la mesa temporal.
     * @param x utilizado para realizar una sobrecarga de constructores.
     */
    public Mesa(int x) {
        matrizFichas = new Ficha[15][15];
    }




    /**
     * Método para conocer la cantidad de fichas válidas dentro del tablero de 15x15.
     * @return un valor entero que refleja cuántas fichas existen en la mesa.
     */
    public int getCantFichas() {
        int contador = 0;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (this.getMatrizFichas()[i][j] != null) {
                    contador++;
                }
            }

        }
        return contador;
    }

    /**
     * Método el cual obtiene una ficha en una posición deseada de la matriz.
     * @param x fila de la matriz.
     * @param y columna de la matriz.
     * @return la ficha en la posicion deseada.
     */

    public Ficha getFichaEnXY(int x, int y) {
        return this.getMatrizFichas()[x][y];
    }

    /**
     * Método para poder ingresar una ficha del soporte del jugador en la matriz de la mesa.
     * @param ficha ficha que se desea colocar en la matriz.
     * @param x fila de la matriz donde se desea colocar.
     * @param y columna de la matriz donde se desea colocar.
     * @param jugador jugador el cual quiere realizar la jugada.
     */
    public void ingresarFicha(Ficha ficha, int x, int y, Jugador jugador) {
        if (this.getMatrizFichas()[x][y] == null) {
            this.getMatrizFichas()[x][y] = ficha;
            jugador.getFichasEnMano().usarficha(ficha);
        }
    }

    /**
     * Método el cual reacomoda una ficha de una posición de la matriz de la mesa a otra posición.
     * @param x fila de la matriz donde la ficha que se quiere mover esta.
     * @param y columna de la matriz donde la ficha que se quiere mover esta.
     * @param v fila de la matriz donde la ficha se desea mover.
     * @param j columna de la matriz donde la ficha se desea mover.
     */
    
    public void reacomodarFicha(int x, int y, int v, int j) {
        Ficha ficha = this.matrizFichas[x][y];
        this.matrizFichas[x][y] = null;

        if (this.matrizFichas[v][j] != null) {
            System.out.println("Esta posicion ya es ocupada.");
            return;
        }

        this.matrizFichas[v][j] = ficha;
    }


    /**
     * Método que verifica si el estado de la matriz con sus fichas es válido.
     * @return si las jugadas en la mesa son válidas retorna true falso de lo contrario.
     */
    public boolean matrizValida() {
        if (estaVacia()) return true;
        boolean esvalid = false;
        Vector<Ficha> fichas = new Vector<>();

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (matrizFichas[i][j] != null) {
                    fichas.add(matrizFichas[i][j]);
                }
                else if(!fichas.isEmpty()){
                    Jugada posibleJugada = new Jugada(fichas);
                    if (posibleJugada.serieValida() || posibleJugada.escaleraValida()) {
                        esvalid = true;
                    }
                    else {
                        return false;
                    }
                    fichas.clear();
                }
            }
            if(!fichas.isEmpty()){
                Jugada posibleJugada = new Jugada(fichas);
                if (posibleJugada.serieValida() || posibleJugada.escaleraValida()) {
                    esvalid = true;
                }
                else {
                    return false;
                }
                fichas.clear();
            }
        }

        return esvalid;
    }

    /**
     * Método que obtiene el valor de una jugada.
     * @return retorna true si la jugada tiene un valor mayor o igual que 30 falso de lo contrario.
     */
    public boolean valorDeJugada() {
        Vector<Ficha> fichas = new Vector<>();
        int cont = 0;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (matrizFichas[i][j] != null && !matrizFichas[i][j].isEsta()) {
                    fichas.add(matrizFichas[i][j]);
                }
                else if(!fichas.isEmpty()){
                    Jugada posibleJugada = new Jugada(fichas);
                    if (posibleJugada.serieValida() || posibleJugada.escaleraValida()) {
                        cont+=posibleJugada.valorJugada();
                    }
                    fichas.clear();
                }
            }
        }

        return cont >= 30;
    }

    /**
     * Método para conocer si la matriz pertenece a la mesa está vacía.
     * @return retorna falso si no esta vacía verdadero de lo contrario.
     */
    public boolean estaVacia(){
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){

                if(matrizFichas[i][j]!= null){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Método el cual establece las fichas de la mesa como parte de las mismas.
     */
    public void sonParteDe() {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if(this.matrizFichas[i][j]!=null){
                    this.matrizFichas[i][j].setEsta(true);
                }

            }
        }
    }

    /**
     * Método que restablece las fichas puestas en la mesa por el jugador a su soporte.
     * @param jugador jugador el cual se le restablecerán sus fichas.
     */
    public void restaurarFichas(Jugador jugador){
        for (int i=0;i<15;i++){
            for(int j=0;j<15;j++) {
                if (this.matrizFichas[i][j] != null) {
                    if (!this.matrizFichas[i][j].isEsta()) {
                        jugador.getFichasEnMano().ingresarficha(this.matrizFichas[i][j]);
                    }
                }
            }
        }
    }

    /**
     * Método el cual copia de los valores de una matriz perteneciente a una mesa.
     * @param Mesaoriginal mesa que se desea copiar.
     */
    public void copiarMesa(Mesa Mesaoriginal) {
        for (int fila = 0; fila < matrizFichas.length; fila++) {
            for (int col = 0; col < matrizFichas[fila].length; col++) {
                matrizFichas[fila][col] = Mesaoriginal.matrizFichas[fila][col];
            }
        }
        espaciolleno = Mesaoriginal.espaciolleno;
    }

    /**
     * Método para obtener una ficha de la pila.
     * @return retorna la ficha que se obtuvo de la pila.
     */
    public Ficha agarrarpila(){
        return  fichas.agarrarficha();
    }

    /**
     * Método el cual obtiene la pila de fichas de la mesa
     * @return retorna la pila de la mesa.
     */
    public Pila getFichas() {
        return fichas;
    }


    /**
     * Método el cual imprime en pantalla la matriz perteneciente a la mesa.
     *
     */
    public void imprimirMesa() {
        for (int fila = 0; fila < matrizFichas.length; fila++) {
            for (int columna = 0; columna < matrizFichas[fila].length; columna++) {
                Ficha ficha = matrizFichas[fila][columna];
                if (ficha != null) {
                    System.out.print(ficha.getNum() + " " + ficha.getColor() + "\t");
                } else {
                    System.out.print("-\t");
                }
            }
            System.out.println();
        }
    }

    /**
     * Método el cual obtiene la matriz de la mesa.
     * @return retorna la matriz de la mesa.
     */
    public Ficha[][] getMatrizFichas() {
        return matrizFichas;
    }
}