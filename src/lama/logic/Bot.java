package lama.logic;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class Bot extends Player {
    private final int dif;
    private final int pos;

    Bot(int dif, int pos) {
        super();
        setBot(true);
        this.dif = dif;
        this.pos = pos;
    }

    public Bot(Player p, int dif, int pos) {
        super();
        for (Integer i : p.getValues()) {
            addCard(i);
        }
        setBot(true);
        this.dif = dif;
        this.pos = pos;
        setAll(p.getX(), p.getY(),p.getM(), p.getN());
        setActive(p.getActive());
        setWithdrawn(p.getWithdrawn());
        setVisibility(p.getVisibility());
        setName(p.getName() + " (Bot)");
        update(p.getPoints());
        setPointDif(p.getPointDif());
    }

    /***
     * Die drei Schwierigkeiten der Bots als Switch-Case.
     * @param logic Die Spiellogik, damit der Bot funktionen ausführen kann
     * @throws RemoteException Wird benötigt, da wir die auch aus dem Server aufrufen
     */
    @Override
    public void play(Spiellogik logic) throws RemoteException{
        Integer top = logic.getAblage().top();
        ArrayList<Integer> val = getValues();
        Deck d = logic.getDeck();
        switch (dif){
            case 1:
                //Easy Bot
                Random rand = new Random();
                int variable = rand.nextInt(100);
                if (variable> 0&& variable<20){
                    logic.withdraw(pos);
                    System.out.println("Ausgestiegen");
                    return;
                }
                else if(variable>=20&&variable<80){
                    if (val.contains(top)){
                        for (Integer c : val) {
                            if (c.equals(top)){
                                logic.putCard(c,pos);
                                System.out.println("Gleiche Karte");
                                return;
                            }
                        }
                    }
                    else if (val.contains(top+1)){
                        for (Integer c : val) {
                            if (c.equals(top+1)){
                                logic.putCard(c,pos);
                                System.out.println("Eins höher");
                                return;
                            }
                        }
                    }
                    else if (top == 7 && val.contains(1)){
                        for (Integer c : val) {
                            if (c.equals(1)) {
                                logic.putCard(c, pos);
                                System.out.println("1 falls Lama");
                                return;
                            }
                        }
                    }
                    else {
                        logic.drawCard(pos);
                        System.out.println("Karte gezogen");
                        return;
                    }

                }
                else if(variable>=80){
                    if (!d.isEmpty()&&logic.getCountWithdrawn()<(logic.getSpielerAnzahl()+logic.getBotAnzahl()-1)) {
                        logic.drawCard(pos);
                        System.out.println("Karte gezogen");
                        return;
                    }
                    else{
                        logic.withdraw(pos);
                        System.out.println("Ausgestiegen");
                        return;
                    }
                }

            case 2:
                //Medium Bot
                if (val.contains(top)){
                    for (Integer c : val) {
                        if (c.equals(top)){
                            logic.putCard(c,pos);
                            System.out.println("Gleiche Karte");
                            return;
                        }
                    }
                }
                else if (val.contains(top+1)){
                    for (Integer c : val) {
                        if (c.equals(top+1)){
                            logic.putCard(c,pos);
                            System.out.println("Eins höher");
                            return;
                        }
                    }
                }
                else if (top == 7 && val.contains(1)){
                    for (Integer c : val) {
                        if (c.equals(1)) {
                            logic.putCard(c, pos);
                            System.out.println("1 falls Lama");
                            return;
                        }
                    }
                }
                else if (!d.isEmpty()&&logic.getCountWithdrawn()<(logic.getSpielerAnzahl()+logic.getBotAnzahl()-1)){
                    logic.drawCard(pos);
                    System.out.println("Karte gezogen");
                    return;
                }
                else{
                    logic.withdraw(pos);
                    System.out.println("Ausgestiegen");
                    return;
                }
                System.out.println("Lost and found");
                return;

            case 3:
                if (playCard(top,val)){

                    int withdrawnPlayer = logic.getCountWithdrawn();
                    int allPlayer = logic.getBotAnzahl()+logic.getSpielerAnzahl();
                    //Check if there are more than 2 Player
                    if (allPlayer-withdrawnPlayer > 2) {
                        //Check if the you have more than 2 Cards
                        if (val.size() > 2) {
                            //Sort the List
                            Collections.sort(val);
                            //Check if you have a Lama
                            if (val.contains(7)){
                                //Play the Lama if you have one
                                if (possible(top,7)){
                                    logic.putCard(7,pos);
                                    System.out.println("Karte abgelegt");
                                    return;
                                }
                                //Otherwise try to play the highest card then the same or an 1 if it's a lama on top
                                else{
                                    if (val.contains(top+1)){
                                        logic.putCard(top+1,pos);
                                        System.out.println("Karte abgelegt");
                                        return;
                                    } else if (val.contains(top)) {
                                        logic.putCard(top,pos);
                                        System.out.println("Karte abgelegt");
                                        return;
                                    }
                                    else if(top == 7 && val.contains(1)){
                                        logic.putCard(1,pos);
                                        System.out.println("Karte abgelegt");
                                        return;
                                    }
                                }
                            }
                            else{
                                //If no Lama in your Hand try to put the highest then the same or an 1 if it's a Lama on top
                                if (val.contains(top+1)){
                                    logic.putCard(top+1,pos);
                                    System.out.println("Karte abgelegt");
                                    return;
                                } else if (val.contains(top)) {
                                    logic.putCard(top,pos);
                                    System.out.println("Karte abgelegt");
                                    return;
                                }
                                else if(top == 7 && val.contains(1)){
                                    logic.putCard(1,pos);
                                    System.out.println("Karte abgelegt");
                                    return;
                                }
                            }
                        }
                        //2 Cards or Less
                        else{
                            //Check if the two Cards are following each other
                            if (following(val)){
                                //Sort the Cards
                                Collections.sort(val);
                                //If the higher one is playable play it
                                if(val.size()>1) {
                                    if (possible(top, val.get(1))) {
                                        logic.putCard(val.get(1), pos);
                                        System.out.println("Karte abgelegt");
                                        return;
                                    }
                                    //If the higher one is nit playable put the lower
                                    else if (possible(top, val.get(0))) {
                                        logic.putCard(val.get(0), pos);
                                        System.out.println("Karte abgelegt");
                                        return;
                                    } else {
                                        System.out.println("Something went wrong");
                                    }
                                }
                                else{
                                    if(possible(top,val.get(0))){
                                        logic.putCard(val.get(0),pos);
                                        System.out.println("Karte abgelegt");
                                        return;
                                    }
                                }
                            }
                            //They are to follwing each other
                            else{
                                //Sort the Cards
                                Collections.sort(val);
                                //If the higher one is playable play it
                                if (possible(top,val.get(1))){
                                    logic.putCard(val.get(1),pos);
                                    System.out.println("Karte abgelegt");
                                    return;
                                }
                                //If the higher one is nit playable put the lower
                                else if(possible(top,val.get(0))){
                                    logic.putCard(val.get(0),pos);
                                    System.out.println("Karte abgelegt");
                                    return;
                                }
                                else{
                                    System.out.println("Something went wrong");
                                }
                            }

                        }
                    }
                    //There are only 2 Players left
                    else{
                        ArrayList<Player> p = logic.getSpielerListe();
                        //Get the position of the second player
                        int enemyPos = getSecond(logic);
                        //If the Enemy has less Cards than I
                        if (p.get(enemyPos).getValues().size()<val.size()){
                            //Sort the Values of your Hand
                            Collections.sort(val);
                            Collections.reverse(val);
                            //Play the highest possible Card
                            for (Integer integer : val) {
                                if (possible(top, integer)) {
                                    logic.putCard(integer, pos);
                                    System.out.println("Karte abgelegt");
                                    return;
                                }
                            }
                        }
                        else {
                            Collections.sort(val);
                            //Play the smalles possible Card
                            for (Integer integer : val) {
                                if (possible(top, integer)) {
                                    logic.putCard(integer, pos);
                                    System.out.println("Karte abgelegt");
                                    return;
                                }
                            }

                        }
                    }
                }
                else{
                    int allPlayer = logic.getBotAnzahl()+logic.getSpielerAnzahl();
                    //There are more than 2 Player
                    if(allPlayer-logic.getCountWithdrawn() > 2 ){
                        //Check if the Cards on your Hand are the same
                        if(theSame(val)){
                            //Withdraw if HandValue is smaller then 5
                            if(val.get(0)<=4){
                                logic.withdraw(pos);
                                System.out.println("Ausgestiegen");
                                return;
                            }
                            else{
                                //Check if the next Card was played more than 4 times
                                //If so then withdraw
                                HashMap<Integer,Integer> helper = new HashMap<>(logic.getPlayedCards());
                                if(val.get(0)!= 6) {
                                    if (helper.get((((val.get(0))) + 1) % 7) >= 5) {
                                        logic.withdraw(pos);
                                        System.out.println("Ausgestiegen");
                                        return;
                                    }
                                    else {
                                        logic.drawCard(pos);
                                        System.out.println("Karte gezogen");
                                        return;
                                    }
                                }
                                else if(val.get(0)==6){
                                    if (helper.get(7) >= 5) {
                                        logic.withdraw(pos);
                                        System.out.println("Ausgestiegen");
                                        return;
                                    }
                                    else {
                                        logic.drawCard(pos);
                                        System.out.println("Karte gezogen");
                                        return;
                                    }
                                }
                                //Draw a Card
                                else{
                                    if (!logic.getDeck().isEmpty()) {
                                        logic.drawCard(pos);
                                        System.out.println("Karte gezogen");
                                        return;
                                    }
                                    else{
                                        logic.withdraw(pos);
                                        System.out.println("Ausgestiegen");
                                    }
                                }
                            }
                        }
                        else {
                            //Withdraw if the Value is smaller than 5
                            if (value(val)< 5){
                                logic.withdraw(pos);
                                System.out.println("Ausgestiegen");
                                return;
                            }
                            else{
                                if(!logic.getDeck().isEmpty()) {
                                    logic.drawCard(pos);
                                    System.out.println("Karte gezogen");
                                    return;
                                }
                                else{
                                    logic.withdraw(pos);
                                    System.out.println("Ausgestiegen");
                                    return;
                                }
                            }
                        }
                    }
                    else{
                        if(allPlayer-logic.getCountWithdrawn()==1){
                            logic.withdraw(pos);
                            System.out.println("Ausgestiegen");
                            return;
                        }
                        else {
                            if (!logic.getDeck().isEmpty()) {
                                logic.drawCard(pos);
                                System.out.println("Karte gezogen");
                                return;
                            } else {
                                logic.withdraw(pos);
                                System.out.println("Ausgestiegen");
                                return;
                            }
                        }
                    }

                }

            default:
        }
    }


    /***
     * Gibt den zweiten Spieler der noch aktiv in der Runde ist zurück
     * @param logic Die Spiellogik, damit man auf der Spielerliste arbeiten kann
     * @return Gibt die Postition des Spielers aus
     */
    private int getSecond(Spiellogik logic) {
        for (int i = 0; i <logic.getSpielerListe().size() ; i++) {
            if(i!=pos) {
                if (!(logic.getSpielerListe().get(i).getWithdrawn())) {
                    return i;
                }
            }
        }
        return pos;
    }


    /***
     * Gibt den aktuellen Wert der Handkarten zurück
     * @param val Die Liste der Handkarten
     * @return Gibt den Wert aus
     */
    private Integer value(ArrayList<Integer> val) {
        int out = 0;
        for (int i = 1; i <7 ; i++) {
            if (val.contains(i)){
                out += i;
            }
        }
        if(val.contains(7)){
            out += 10;
        }
        return out;
    }

    /***
     * Schaut, ob alle karten auf der Hand die gleichen sind
     * @param val Die Liste der Handkarten
     * @return Gibt den Wahrheitswert aus
     */
    private boolean theSame(ArrayList<Integer> val) {
        for (int i = 0; i <val.size()-1 ; i++) {
            if(!(val.get(i).equals(val.get(i+1)))){
                return false;
            }
        }
        return true;
    }


    /***
     * Schaut, ob eine Karte ablegbar ist
     * @param top Die oberste Karte auf dem ablagestapel
     * @param integer Die Karte die man ablegen möchte
     * @return Gibt aus ob es geht oder nicht
     */
    private boolean possible(Integer top, Integer integer) {
        if (top.equals(integer)){
            return true;
        }
        if(top+1==integer){
            return true;
        }
        return top == 7 && integer == 1;
    }

    /***
     * Schaut ob zwei Karten aufeinanderfolende Karten sind
     * @param val Die Liste der Handkarten
     * @return Gibt aus ob zwei Karten aufeinanderfolgend sind
     */
    private boolean following(ArrayList<Integer> val) {
        Collections.sort(val);
        if(val.size() >1) {
            if (val.get(0) == val.get(1) + 1) {
                return true;
            }
            if (val.get(0) == 1 && val.get(1) == 7) {
                return true;
            }
        }
        return val.size() == 1;
    }

    /***
     * Schaut ob es eine Karte auf der Hand gibt, die spielbar ist
     * @param top Die oberste Karte
     * @param val Die Liste an Handkarten
     * @return Gibt aus ob es eine Spielbare Karte gibt
     */
    private boolean playCard(Integer top, ArrayList<Integer> val) {
        if (val.contains(top)){
            return true;
        }
        if(val.contains(top+1)){
            return true;
        }
        if (top == 7){
            return val.contains(1);
        }
        else return false;
    }
}
