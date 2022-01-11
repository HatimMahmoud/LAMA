package lama.show;


import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import lama.logic.Player;

import java.util.ArrayList;
import java.util.Collections;

/***
 * Klasse zur Verwaltung der Spieler als Pane
 */
public class PlayerGrid extends Grid{
    private final Grid cardHand = new Grid();
    private final Grid chips = new Grid();
    private final WhiteChip w;
    private final BlackChip b;
    private final ArrayList<Card> cards = new ArrayList<>();
    private ArrayList<Integer> vals = new ArrayList<>();
    private boolean sorted = false;
    private boolean sorteddesc = false;
    private boolean gegner = true;
    private final int degree;
    private final Withdraw withdraw;
    private Player p;
    private final String name;
    private int x,y,m,n;
    private int m1,n1;
    private final ArrayList<ArrayList> allEnemyCards;
    private Label nameL;



    PlayerGrid(int deg, Player p, String name, ArrayList<ArrayList> allEnemyCards) {
        getStylesheets().add(getClass().getResource("gui.css").toExternalForm());
        this.p = p;
        this.degree = deg;
        this.name = name;
        setCards(p.getValues());
        p.setName(name);

        w = new WhiteChip();
        b = new BlackChip();
        w.setText(Integer.toString(p.getPoints()%10));
        b.setText(Integer.toString(p.getPoints()/10));
        withdraw = new Withdraw();

        this.allEnemyCards = allEnemyCards;
        placement(deg);
        drawHand();
    }

    private void placement(int degree) {
        if (degree==0) {
            setPrefSize(670, 110);
            createGrid(670, 110);
            cardHand.setPrefSize(480, 80);
            cardHand.createGrid(480, 80);
            add(cardHand, 0, 0, 480, 110);
            chips.setPrefSize(100, 110);
            chips.createGrid(100, 110);
            add(chips, 480, 0, 100, 100);
            chips.add(w, 0, 22, 45, 35);
            chips.add(b, 50, 22, 45, 35);
            add(withdraw,590,0,80,80);
            w.getStyleClass().add("test");
            b.getStyleClass().add("test");
        }
        if (degree==90) {

            setPrefSize(150, 580);
            createGrid(150, 580);
            cardHand.setPrefSize(150, 480);
            cardHand.createGrid(150, 480);
            add(cardHand, 0, 0, 150, 480);
            chips.setPrefSize(150, 100);
            chips.createGrid(150, 100);
            add(chips, 0, 480, 150, 100);
            chips.add(w, 22, 0, 35, 40);
            chips.add(b, 22, 50, 35, 40);
            w.getStyleClass().add("test");
            b.getStyleClass().add("test");
            if (name != null){
                nameL = new Label(name);
                nameL.setRotate(90);
                nameL.setStyle("-fx-font-weight: bold");
                add(nameL,60,480,200,200);
            }
        }
        if (degree==270) {

            setPrefSize(150, 580);
            createGrid(150, 580);
            cardHand.setPrefSize(150, 480);
            cardHand.createGrid(150, 480);
            add(cardHand, 0, 100, 150, 480);
            chips.setPrefSize(150, 100);
            chips.createGrid(150, 100);
            add(chips, 80, 20, 100, 100);
            chips.add(w, 0, 0, 70, 42);
            chips.add(b, 0, 50, 70, 42);
            w.getStyleClass().add("test");
            b.getStyleClass().add("test");
            if (name != null){
                nameL = new Label(name);
                nameL.setRotate(-90);
                nameL.setStyle("-fx-font-weight: bold");
                add(nameL,0,50,200,30);
            }
        }
        if (degree == 180) {
            setPrefSize(580, 150);
            createGrid(580, 150);
            cardHand.setPrefSize(480, 150);
            cardHand.createGrid(480, 150);
            add(cardHand, 100, 0, 480, 150);
            chips.setPrefSize(100, 150);
            chips.createGrid(100, 150);
            add(chips, 0, 0, 100, 150);
            chips.add(w, 0, 22, 40, 35);
            chips.add(b, 50, 22, 40, 35);
            w.getStyleClass().add("test");
            b.getStyleClass().add("test");
            if (name != null){
                nameL = new Label(name);
                nameL.setStyle("-fx-font-weight: bold");
                add(nameL,0,0,200,200);
            }

        }
        drawHand();

    }


    /*
    setBackground(new Background(new BackgroundFill(Paint.valueOf("red"),null, null)));
    cardHand.setBackground(new Background(new BackgroundFill(Paint.valueOf("red"),null, null)));
    chips.setBackground(new Background(new BackgroundFill(Paint.valueOf("red"),null, null)));
    */

    public void update (){
        w.setText(Integer.toString(p.getPoints()%10));
        b.setText(Integer.toString(p.getPoints()/10));
    }

    void drawHand() {
        getStylesheets().add(getClass().getResource("gui.css").toExternalForm());
        cardHand.getChildren().clear();
        if(degree == 0) {
            if (cards.size() != 0) {
                int distance = 400 / cards.size();
                int i = 0;
                if(this.sorted) {
                    Collections.sort(cards);
                }
                else if (this.sorteddesc) {
                    Collections.sort(cards);
                    Collections.reverse(cards);
                }

                for (Card card : cards) {
                    if (p.getVisibility()) {
                        card.setFront();
                    } else {
                        card.setBack();
                    }
                    cardHand.add(card, 30+distance * i, 0, 80, 80);
                    i++;
                }

            }
        }
        int limit = 7;
        Button label;
        if(degree == 180) {
            if (cards.size() != 0) {
                if (gegner && cards.size() > limit) {
                    label = new Button(Integer.toString(cards.size()));
                    label.getStyleClass().add("invisButton");
                    label.setFont(new Font("Comic Sans",18));
                    if(!p.getWithdrawn()){
                        ArrayList<ArrayList> helper = allEnemyCards.get(1);
                        ArrayList<Background>actual = helper.get(0);
                        cardHand.setBackground(actual.get(7));
                    }
                    else{
                        ArrayList<ArrayList> helper = allEnemyCards.get(1);
                        ArrayList<Background>actual = helper.get(1);
                        cardHand.setBackground(actual.get(7));
                    }
                    cardHand.add(label, 50, 80, 34, 34);
                } else {
                    if (!p.getWithdrawn()){
                        ArrayList<ArrayList> helper = allEnemyCards.get(1);
                        ArrayList<Background>actual = helper.get(0);
                        cardHand.setBackground(actual.get(cards.size()-1));
                    }
                    else{
                        ArrayList<ArrayList> helper = allEnemyCards.get(1);
                        ArrayList<Background>actual = helper.get(1);
                        cardHand.setBackground(actual.get(cards.size()-1));
                    }

                }
            }
            else {
                ArrayList<Background> helper = allEnemyCards.get(3);
                cardHand.setBackground(helper.get(0));
            }
        }
        if(degree==90){
            if (cards.size() != 0) {
                if (gegner && cards.size() > limit) {
                    int distance = 400 / limit;
                    label = new Button(Integer.toString(cards.size()));
                    label.getStyleClass().add("invisButton");
                    label.setFont(new Font("Comic Sans",18));
                    if(!p.getWithdrawn()){
                        ArrayList<ArrayList> helper = allEnemyCards.get(0);
                        ArrayList<Background>actual = helper.get(0);
                        cardHand.setBackground(actual.get(7));
                    }
                    else{
                        ArrayList<ArrayList> helper = allEnemyCards.get(0);
                        ArrayList<Background>actual = helper.get(1);
                        cardHand.setBackground(actual.get(7));
                    }
                    cardHand.add(label, 100, 400, 34, 34);
                } else {
                    if (!p.getWithdrawn()){
                        ArrayList<ArrayList> helper = allEnemyCards.get(0);
                        ArrayList<Background>actual = helper.get(0);
                        cardHand.setBackground(actual.get(cards.size()-1));
                    }
                    else{
                        ArrayList<ArrayList> helper = allEnemyCards.get(0);
                        ArrayList<Background>actual = helper.get(1);
                        cardHand.setBackground(actual.get(cards.size()-1));
                    }
                }
            }
            else{
                ArrayList<Background> helper = allEnemyCards.get(3);
                cardHand.setBackground(helper.get(0));
            }
        }
        if(degree == 270){
            if (cards.size() != 0) {
                if (gegner && cards.size() > limit) {
                    label = new Button(Integer.toString(cards.size()));
                    label.getStyleClass().add("invisButton");
                    label.setFont(new Font("Comic Sans",18));
                    if(!p.getWithdrawn()){
                        ArrayList<ArrayList> helper = allEnemyCards.get(2);
                        ArrayList<Background>actual = helper.get(0);
                        cardHand.setBackground(actual.get(7));
                    }
                    else{
                        ArrayList<ArrayList> helper = allEnemyCards.get(2);
                        ArrayList<Background>actual = helper.get(1);
                        cardHand.setBackground(actual.get(7));
                    }
                    cardHand.add(label, 0, 45, 34, 34);
                } else {
                    if (!p.getWithdrawn()){
                        ArrayList<ArrayList> helper = allEnemyCards.get(2);
                        ArrayList<Background>actual = helper.get(0);
                        cardHand.setBackground(actual.get(cards.size()-1));
                    }
                    else{
                        ArrayList<ArrayList> helper = allEnemyCards.get(2);
                        ArrayList<Background>actual = helper.get(1);
                        cardHand.setBackground(actual.get(cards.size()-1));
                    }
                }
            }
            else{
                ArrayList<Background> helper = allEnemyCards.get(3);
                cardHand.setBackground(helper.get(0));
            }
        }
    }

    public void fade() {
        Fade fd = new Fade(this);
        fd.fadeTo();
        fd.fadeFrom();
    }

    void sortCards() {
        Collections.sort(cards);
        this.sorted=true;
        this.sorteddesc = false;
        drawHand();
    }

    void sortCardsDesc() {
        Collections.sort(cards);
        Collections.reverse(cards);
        this.sorteddesc=true;
        this.sorted = false;
        drawHand();
    }

    ArrayList<Card> getCards() {
        return cards;
    }

    void setCards(ArrayList<Integer> vals) {
        cards.clear();
        this.vals = vals;
        for (Integer val : vals) {
            cards.add(new Card(val));
        }
    }

    public Player getP(){return p;}

    public void setP(Player p) {
        this.p = p;
        setCards(p.getValues());
    }

    boolean getVisibility(){
        return p.getVisibility();
    }

    void setVisibility(boolean vis){
        p.setVisibility(vis);
    }

    void setPlayer(){
        gegner = false;
    }

    ArrayList<Integer> getVals() {
        return vals;
    }

    public Withdraw getWithdraw(){
        return this.withdraw;
    }

    void setAll(int x, int y, int m, int n){
        this.x = x;
        this.y = y;
        this.m = m;
        this.n = n;
    }

    void setRemove(int m1, int n1){
        this.m1=m1;
        this.n1=n1;
    }


    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public int getN(){
        return this.n;
    }

    public int getM(){
        return this.m;
    }

    public int getM1(){return  this.m1;}

    public int getN1(){return  this.n1;}

    public void setLabel(String name) {
        nameL = new Label(name);
        nameL.setStyle("-fx-font-weight: bold");
        if (degree == 90){
            nameL.setRotate(90);
            add(nameL,60,480,200,200);
        }
        else if (degree == 180){
            add(nameL,0,0,200,200);
        }
        else if (degree ==270) {
            nameL.setRotate(-90);
            add(nameL,0,50,200,30);
        }
    }

    public Grid getCardHand(){
        return this.cardHand;
    }
}
