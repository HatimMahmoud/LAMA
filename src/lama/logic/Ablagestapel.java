package lama.logic;

import java.io.Serializable;
import java.util.Stack;

public class Ablagestapel implements Serializable {

    private final Stack<Integer> ablagestapel;


    public Ablagestapel(){
        this.ablagestapel = new Stack<>();
    }

    /***
     * Methode, welche eine Karte oben auf den Ablagestapel legt
     * @param i Abzulegene Karte
     */
    void ablegen(Integer i){
        this.ablagestapel.push(i);
    }

    /***
     * Methode zur Ausgabe der obersten Karte
     * @return Oberste Karte des Ablagestapels
     */
    public Integer top(){
        if (ablagestapel==null){return null;}
        else return this.ablagestapel.peek();
    }
}
