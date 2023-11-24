import java.util.ArrayList;
import java.util.Vector;
import java.util.Random;

/**
 * Clase para representar la totalidad de fichas del tablero con sus respectivos atributos y métodos.
 */
public class Pila{

    private Vector<Integer> totalDeLetras;
    private Random random;
    private char caracteres[];

    /**
     * Constructor de la clase pila el cual genera el vector
     * donde se almacenarán las fichas al igual que genera la pila.
     */
    public Pila() {
        this.totalDeLetras = new Vector<>(28);
        this.caracteres = new char[28];
        colocarCaracteres(); // se llama para que dentro de este arreglo existan las 27 letras que se van a utilizar
        random = new Random();
        iniciarcantidades();
    }

    public void iniciarcantidades(){
        totalDeLetras.set(0, 12);          // a
        totalDeLetras.set(1, 2);           // b
        totalDeLetras.set(2, 4);           // c
        totalDeLetras.set(3, 5);           // d
        totalDeLetras.set(4, 12);          // e
        totalDeLetras.set(5, 1);           // f
        totalDeLetras.set(6, 2);           // g
        totalDeLetras.set(7, 2);           // h
        totalDeLetras.set(8, 6);           // i
        totalDeLetras.set(9, 1);           // j
        totalDeLetras.set(10, 1);          // k
        totalDeLetras.set(11, 4);          // l
        totalDeLetras.set(12, 2);          // m
        totalDeLetras.set(13, 5);          // n
        totalDeLetras.set(14, 9);          // o
        totalDeLetras.set(15, 2);          // p
        totalDeLetras.set(16, 2);          // q
        totalDeLetras.set(17, 5);          // r
        totalDeLetras.set(18, 6);          // s
        totalDeLetras.set(19, 4);          // t
        totalDeLetras.set(20, 5);          // u
        totalDeLetras.set(21, 1);          // v
        totalDeLetras.set(22, 1);          // w
        totalDeLetras.set(23, 1);          // x
        totalDeLetras.set(24, 1);          // y
        totalDeLetras.set(25, 1);          // z
        totalDeLetras.set(26, 1);          // ñ
        totalDeLetras.set(27, 2);          // blank
    }

    public Vector<Integer> getTotaldeletras() {
        return totalDeLetras;
    }

    public void setTotaldeletras(Vector<Integer> nuevoTotal) {
        totalDeLetras = nuevoTotal;
    }

    private char obtenerLetra(int pos) {
        char letra = '\u0000';      // valor por convención para denotar un caracter nulo.
        if(totalDeLetras.get(pos) > 0) {
            totalDeLetras.set(pos, totalDeLetras.get(pos) - 1); // le resta 1 a la cantidad de letras existente
            letra = caracteres[pos];
        }
        return letra;
    }



    /**
     * Método para agarrar una ficha random de la pila.
     * @return la ficha que se agarró.
     */
    public Ficha agarrarficha() {
        int randomficha = random.nextInt(totalDeLetras.size());
        char letra = obtenerLetra(randomficha);

        if (letra=='\u0000')return null;

        Ficha ficha = new Ficha(letra);

        return ficha;
    }

    private void colocarCaracteres() {
        String letras = "abcdefghijklmnopqrstuvwxyzñ";
        for (int i = 0; i < 27; i++) {
            this.caracteres[i] = letras.charAt(i);
        }
    }
    
    /**
     * Método para obtener el tamaño de la pila.
     * @return la cantidad de fichas en la pila.
     */
    public int getStackSize(){
        return this.totalDeLetras.size();
    }

    /**
     * Método de acceso el cual obtiene la pila.
     * @return un vector de fichas que representa la pila.
     */
    public Vector<Integer> getStack(){
        return this.totalDeLetras;
    }

}