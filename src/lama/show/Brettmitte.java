package lama.show;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lama.logic.Ablagestapel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/***
 * Verwaltung der Mitte des Spielfeldes. Anzeige von Deck, Ablagestapel und Chip-Bank
 */
public class Brettmitte extends Grid {
    private int white = 50;
    private int black = 20;
    private final Grid cards = new Grid();
    private WhiteChip w;
    private BlackChip b;
    private Card top;
    private Card but;


    /***
     * Konstruktor
     * @param ablage Anzuzeigender Ablagestapel
     */
    public Brettmitte(Ablagestapel ablage) {
        this.top = new Card(ablage.top());
        this.top.setBack();
        this.but = new Card(1);
        this.but.setBack();

        getStylesheets().add(getClass().getResource("gui.css").toExternalForm());

        setPrefSize(300, 80);
        createGrid(300, 80);

        cards.setPrefSize(190,80);
        cards.createGrid(190,80);
        cards.add(top,130,0, 60, 80);
        cards.add(but,0,0, 60, 80);

        Grid chips = new Grid();
        chips.setPrefSize(110,80);
        chips.createGrid(110,80);
        w = new WhiteChip();
        b = new BlackChip();
        w.setText(Integer.toString(white));
        b.setText(Integer.toString(black));
        w.getStyleClass().add("test");
        b.getStyleClass().add("test");
        chips.add(w,20,22, 35, 35);
        chips.add(b,70, 22, 35, 35);

        add(cards,0,0,190,80);
        add(chips,190,0,110,80);
    }

    //Konstruktor zu Testzwecken
    public Brettmitte() {

    }

    /***
     * Methode zum Update der Anzeige, falls sich etwas an Deck oder Ablagestapel ändert
     * @param a neuer Ablagestapel
     */
    public void update(Ablagestapel a) {
        top = new Card(a.top());
        this.top.setFront();
        cards.getChildren().clear();
        cards.add(top,130,0, 60, 80);
        cards.add(but, 0,0, 60, 80);
    }



    public void addChip(int color){
        if (color == 1){
            white++;
        }
        else if (color == 10){
            black++;
        }
    }

    public void removeChips(int points){
        white = white - points % 10;
        black = black - points / 10;
        w.setText(Integer.toString(white));
        b.setText(Integer.toString(black));
    }

    /***
     * Deck als Button um ziehen zu ermöglichen
     * @return Gibt den Button des Decks zurück
     */
    public Button getButton(){
        return but;
    }

    public void end() {
        getChildren().clear();
        ImageView worldsEnd = null;
        try {
            worldsEnd = new ImageView(new Image(new FileInputStream("Lama\\src\\lama\\WorldsEnd\\giphy.gif"),600,200,true,true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        add(worldsEnd,0,0,300,80);
    }

    public Card getTop(){
        return top;
    }

}
