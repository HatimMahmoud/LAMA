package lama.show;


import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.ImagePattern;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;

/***
 * Verwaltung der Karten als Buttons
 */

public class Card extends Button implements Serializable, Comparable<Card> {

    private final int value;
    private BackgroundFill front;
    private Image frontSide;
    private Image frontSlave;
    private Image frontgray;
    private BackgroundFill back;
    private Image backSide;
    private Image backSlave;
    private Background bck;
    private final Tooltip tooltip;

    /***
     * Konstruktor
     * @param wert Wert der Karte, Lama = 7
     */
    public Card(int wert) {
        this.value = wert;
        GrayImage gi= new GrayImage();
        try {
            frontSide = new Image(new FileInputStream("Lama\\src\\lama\\Lama_Cards\\Card_" + wert + ".png"));
            frontSlave = new Image(new FileInputStream("Lama\\src\\lama\\Lama_Cards\\Card_" + wert + ".png"), 60, 80, false, false);
            this.front = new BackgroundFill(new ImagePattern(frontSide), CornerRadii.EMPTY, Insets.EMPTY);
            frontgray = gi.getGray(frontSide);
            backSide = new Image(new FileInputStream("Lama\\src\\lama\\Lama_Cards\\Card_back.png"));
            backSlave = new Image(new FileInputStream("Lama\\src\\lama\\Lama_Cards\\Card_back.png"), 60, 80, false, false);
            this.back = new BackgroundFill(new ImagePattern(backSide), CornerRadii.EMPTY, Insets.EMPTY);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bck = new Background(back);
        setBackground(bck);
        setPrefSize(60, 80);
        tooltip = new Tooltip();
        tooltip.getStyleClass().add("ttip");
        getStylesheets().add(getClass().getResource("gui.css").toExternalForm());
        tooltip.setShowDelay(new Duration(0.0));
        tooltip.setGraphic(new ImageView(backSide));
        setTooltip(tooltip);

    }
    /***
     * Methode um die Vorderseite der Karte anzuzeigen
     */

    void setFront() {
        bck = new Background(front);
        setBackground(bck);
        tooltip.setGraphic(new ImageView(frontSlave));
        setTooltip(tooltip);
    }


    void getGray() {
        setBackground(new Background(new BackgroundFill(new ImagePattern(frontgray), CornerRadii.EMPTY, Insets.EMPTY)));
        tooltip.setGraphic(new ImageView(frontgray));
        setTooltip(tooltip);
    }


    /***
     * Methode um die Rückseite der Karte anzuzeigen
     */
    void setBack() {
        bck = new Background(back);
        setBackground(bck);
        tooltip.setGraphic(new ImageView(backSlave));
        setTooltip(tooltip);

    }

    /***
     * Ausgabe des Kartenwertes, Lama = 7
     * @return Kartenwert
     */
    public int getValue() {
        return value;
    }

    /***
     * getValue als String statt int
     * @return Kartenwert als String
     */
    public String toString() {
        return Integer.toString(this.value);
    }

    @Override
    public int compareTo(Card o) {
        return Integer.compare(value, o.getValue());
    }

    public Image getImage(){
        return this.frontSlave;
    }
}
