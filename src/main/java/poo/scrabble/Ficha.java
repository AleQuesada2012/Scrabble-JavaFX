package poo.scrabble;

/**
 * La clase ficha que crea la lógica destinada a las características de la ficha.
 */
public class Ficha {

    private char letra;

    private boolean esta;

    /**
     * Método de acceso para el atributo "está".
     * @return un boolean de si la ficha se encuentra en la mesa o no.
     */
    public boolean isEsta() {
        return esta;
    }

    /**
     *Metodo de cambio para el atributo "está".
     * @param esta un boolean que dice si la ficha se encuentra en la mesa o no.
     */
    public void setEsta(boolean esta) {
        this.esta = esta;
    }

    /**
     * Constructor de la clase Ficha que crea una ficha con la letra dada.
     * @param letra un char que representa que letra va a ser la nueva ficha.
     */
    public Ficha(char letra){
        setLetra(letra);

    }

    /**
     * Método de acceso para el atributo letra.
     * @return un char que representa la letra que esta en esa ficha.
     */
    public char getLetra() {
        return letra;
    }

    /**
     * Método de cambio para el atributo letra.
     * @param letra un char que representa la letra que va a ser la ficha.
     */
    public void setLetra(char letra) {
        this.letra = letra;
    }
}