package lama.logic;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class Scoreboard {

    private ArrayList<Player> playerList;
    private final PriorityQueue<Paar> scorList;

    public Scoreboard(ArrayList<Player> playerList){
        this.playerList = playerList;
        scorList = new PriorityQueue<>();
        for (Player p : playerList) {
            Paar newP = new Paar(p.getName(), p.getPoints());
            scorList.add(newP);
        }
    }

    public void update(){
        scorList.clear();
        for (Player p : playerList) {
            Paar newP = new Paar(p.getName(), p.getPoints());
            scorList.add(newP);
        }
    }

    public PriorityQueue<Paar> getScoreList(){
        return this.scorList;
    }

    public void setPlayerList(ArrayList<Player> playerList){
        this.playerList = playerList;
    }
}
