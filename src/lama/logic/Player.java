package lama.logic;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {
    private final List<Integer> cardVals = new ArrayList<>();
    private int white = 0;
    private int black = 0;
    private boolean active = false;
    private boolean withdrawn;
    private boolean isBot = false;
    private boolean visibility;
    private String name;
    private int x,y,m,n;
    private int PointDif;

    public Player(){
        withdrawn=false;
    }

    public void play(Spiellogik logic) throws RemoteException {
    }


    public Bot replaceWithBot(int dif, int pos){
        return new Bot(this, dif, pos);
    }

    public void addCard(Integer c) {
        cardVals.add(c);
    }

    void removeCard(Integer c){
        cardVals.remove(c);
    }

    public boolean getVisibility(){
        return visibility;
    }

    public void setVisibility(boolean vis){
        visibility = vis;
    }

    public int getPoints(){
        return white+black*10;
    }

    public boolean update (int points){
        if (points > 0) {
            boolean convert = false;
            white = white + points % 10;
            if (white > 9) {
                black = black + 1;
                white = white - 10;
                convert = true;
            }
            black = black + points / 10;
            return convert;
        }
        else{
            if (points == -1&& white == 0){
                white = 9;
                black -= -1;
                System.out.println("white " + white + "  " + black);
                return true;
            }
            if (points == -10){
                black -= 1;
                System.out.println("black " + white + "  " + black);
                return false;
            }
            return false;
        }
    }

    int removeChip(){
        if (black>=1){
            black--;
            return 10;
        }
        else if (white>=1){
            white--;
            return 1;
        }
        return 0;
    }

    void setPointDif(int pointDif) {
        this.PointDif = pointDif;
    }

    public int getPointDif(){
        return this.PointDif;
    }

    public boolean getActive(){
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ArrayList<Integer> getValues(){
        return (ArrayList<Integer>) cardVals;
    }

    public boolean getWithdrawn(){
        return this.withdrawn;
    }

    public void setWithdrawn(boolean bool){
        this.withdrawn=bool;
    }

    public boolean getisBot(){
        return isBot;
    }

    public void setBot(boolean change){
        this.isBot = change;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setAll(int x, int y, int m , int n){
        this.x = x;
        this.y = y;
        this.m = m;
        this.n = n;
    }

    int getX(){
        return this.x;
    }

    int getY(){
        return this.y;
    }

    int getN(){
        return this.n;
    }

    public int getM(){
        return this.m;
    }


}
