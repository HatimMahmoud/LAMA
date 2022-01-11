package lama.logic;


import java.io.Serializable;
import java.util.Collections;
import java.util.Stack;

/***
 * Klasse zur Verwaltung des Decks als Stack
 */

public class Deck implements Serializable {

    private final Stack<Integer> deck;

    public Deck () {
        this.deck = new Stack<>();
    }

    void createDeck() {
        for (int i = 1 ; i < 8 ;i++) {
            for (int j = 0; j <8 ; j++) {
                this.deck.add(i);
            }
        }
        Collections.shuffle(deck);
    }

    /***
     * Debugmethode, gibt alle Karten im Deck als String aus
     * @return Kartenwerte in Reihenfolge im Deck
     */
    public String toString(){
        StringBuilder out = new StringBuilder();
        for (Integer i : this.deck){
            out.append("\n").append(i.toString());
        }
        return out.toString();
    }

    public boolean isEmpty(){
        return deck.isEmpty();
    }

    /***
     * Methode zum ziehen einer Karte. Entfernt die oberste Karte im Stack und gibt sie aus
     * @return Oberste Karte
     */
    public Integer getCard(){
        return deck.pop();
    }

    /***
     * Ungenutzte Methode zur Ausgabe der Anzahl verbleibender Karten im Deck
     * @return Deckgröße
     */
    public int getSize(){
        return deck.size();
    }
}
