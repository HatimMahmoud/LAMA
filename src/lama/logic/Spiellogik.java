package lama.logic;

import javafx.concurrent.Task;
import lama.show.PlayerGrid;
import lama.show.Spielfeld;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Spiellogik extends UnicastRemoteObject implements ServerLogik {
    private final int spielerAnzahl;
    private int botAnzahl = 0;
    private Spielfeld board;
    protected ArrayList<Player> spielerListe = new ArrayList<>();
    private Deck d;
    private Ablagestapel a;
    private boolean start;
    private int round;
    private boolean ended=false;
    private boolean emptyHand = false;
    private int countWithdrawn = 0;
    private final boolean isBot;
    private final int mode;
    private int maxRounds, rounds = 0;
    private boolean skipitall = true;
    private final int[] dif;
    private final HashMap<Integer,Integer> playedCards= new HashMap<>();


    public Spiellogik(int sa, boolean isBot, Spielfeld board, int mode,int[] dif) throws RemoteException{
        this.board = board;
        this.spielerAnzahl = sa;
        this.isBot = isBot;
        this.mode = mode;
        this.dif = dif;
        init();
    }

    public Spiellogik(int sa, boolean isBot, Spielfeld board, int mode, int rounds,int[] dif) throws RemoteException{
        this.board = board;
        this.spielerAnzahl = sa;
        this.isBot = isBot;
        this.mode = mode;
        this.maxRounds = rounds;
        this.dif = dif;
        init();
    }

    //Server Konstruktoren
    public Spiellogik(int sa,int ba, int mode,int[] dif) throws RemoteException {
        this.spielerAnzahl = sa;
        this.botAnzahl = ba;
        this.isBot = false;
        this.mode = mode;
        this.dif = dif;
        init();
    }

    public Spiellogik(int sa, int ba ,int mode, int rounds,int[] dif ) throws RemoteException {
        this.spielerAnzahl = sa;
        this.botAnzahl = ba;
        this.isBot = false;
        this.mode = mode;
        this.maxRounds = rounds;
        this.dif = dif;
        init();
    }

    private void init() throws RemoteException {
        Random rand = new Random();
        start = false;
        round = rand.nextInt(spielerAnzahl+botAnzahl);
        if(!isBot){
            for (int i=0; i<spielerAnzahl; i++){
                spielerListe.add(new Player());
                System.out.println("Spieler added: " + i );
            }
            for (int i = 0; i <botAnzahl ; i++) {
                spielerListe.add(new Bot(dif[i],spielerAnzahl+i));
                spielerListe.get(spielerAnzahl+i).setName("Bot " + i);
                System.out.println("Bot added: "+i);
            }
        }
        else {
            spielerListe.add(new Player());
            for (int i=1; i<spielerAnzahl; i++){
                spielerListe.add(new Bot(dif[i-1],i));
                System.out.println("Bot added: "+i);
            }
        }
        newRound();
    }

    public void newRound() throws RemoteException {
        d = new Deck();
        a = new Ablagestapel();
        d.createDeck();
        playedCards.clear();
        for (int j = 1; j<7; j++) {
            for (Player player : spielerListe) {
                player.addCard(d.getCard());
            }
        }
        a.ablegen(d.getCard());
    }

    public void start() throws RemoteException{
        skipitall= false;
        start = true;
        for (Player pl : spielerListe){
            pl.setVisibility(true);
        }
        spielerListe.get(0).setVisibility(true);
        for (int i = 0; i<(spielerAnzahl+botAnzahl); i++){
            update(i);
        }
        int id = round%(spielerAnzahl+botAnzahl);
        Player p = spielerListe.get(id);
        p.setActive(true);
        fade(id);
        if (p.getisBot()) {
            delayedAction(id);
        }
    }

    public Integer drawCard(int id) throws RemoteException{
        Player spieler = spielerListe.get(id);
        if (start&&!(d.isEmpty())&&countWithdrawn<(spielerAnzahl+botAnzahl-1)&&spieler.getActive()){
            Integer c = d.getCard();
            spieler.addCard(c);
            update(id);
            spieler.setActive(false);
            round++;
            return c;
        }
        return null;
    }

    public boolean putCard(Integer c, int id) throws RemoteException{
        Player spieler = spielerListe.get(id);
        if (start && spieler.getActive() && spieler.getValues().contains(c)){
            int av = a.top();
            if (c==av || c == (av+1) || (av==7 && c== 1)){
                a.ablegen(c);
                spieler.removeCard(c);
                if (playedCards.containsKey(c)){
                    playedCards.replace(c, playedCards.get(c)+1);
                }
                else{
                    playedCards.put(c,1);
                }
                if (spieler.getValues().size()==0){
                    emptyHand=true;
                    start = false;
                }
                update(id);
                spieler.setActive(false);
                round++;
                return true;
            }
            else return false;
        }
        else return false;
    }

    public boolean withdraw(int id) throws RemoteException{
        Player p = spielerListe.get(id);
        if (start&& p.getActive()) {
            p.setWithdrawn(true);
            p.setActive(false);
            p.setVisibility(false);
            update(id);
            countWithdrawn++;
            if (countWithdrawn == (spielerAnzahl+botAnzahl)) {
                start = false;
            }
            round++;
            return true;
        }
        else return false;
    }

    public void skip() throws RemoteException {
        if (skipitall){return;}
        if (!start){
            skipitall = true;
            round--;
            if (round < 0){
                round++;
            }
            rundenEnde();
            return;
        }
        int id = round %(spielerAnzahl+botAnzahl);
        Player p = spielerListe.get(id);
        if (p.getWithdrawn()){
            round++;
            skip();
        }
        else {
            p.setActive(true);
            fade(id);
            if (p.getisBot()){
                delayedAction(id);
            }
        }
    }

    private void rundenEnde() throws RemoteException {
        int color = 0;
        if (emptyHand) {
            color = spielerListe.get(round % (spielerAnzahl + botAnzahl)).removeChip();
            spielerListe.get(round % (spielerAnzahl + botAnzahl)).setPointDif(color*-1);
            chipReturn(color);
            if (board != null) {
            PlayerGrid pG = board.getPlayer().get(round % (spielerAnzahl + botAnzahl));
                board.remove_animation(pG.getM1(), pG.getN1(), color);
            }
        }
        emptyHand = false;
        countWithdrawn = 0;
        round = round % (spielerAnzahl + botAnzahl);
        for (int i = 0; i <spielerListe.size() ; i++) {
            Player p = spielerListe.get(i);
            int points = 0;
            ArrayList<Integer> vals = p.getValues();
            if (!vals.isEmpty()) {
                for (int j = 1; j < 7; j++) {
                    if (vals.contains(j)) {
                        points = points + j;
                    }
                }
                if (vals.contains(7)) {
                    points = points + 10;
                }
            }
            if (this.board != null) {
                PlayerGrid pG = board.getPlayer().get(i);
                if (points >= 11 && points % 10 != 0) {
                    board.animation(pG.getX(), pG.getY(), pG.getM(), pG.getN(), 2, points);
                } else if (points % 10 == 0 && points != 0) {
                    board.animation(pG.getX(), pG.getY(), pG.getM(), pG.getN(), 1, points);
                } else if (points > 0 && points < 10) {
                    board.animation(pG.getX(), pG.getY(), pG.getM(), pG.getN(), 0, points);
                }
            }
            else {
                if (points == 0){
                points = color*-1;
                }
                p.setPointDif(points);
            }
            p.getValues().clear();
            p.setWithdrawn(false);
            p.setVisibility(false);
            if(p.update(points)){
                conversion();
            }
            chipRemove(points);
            if (mode==0 && p.getPoints()>=40){
                ended = true;
            }
        }
        rounds++;
        if (mode==2 && rounds==maxRounds){
            ended = true;
        }
        remoteRundenEnde(ended);
        if (ended){
            spielEnde();
        }
    }

    public void remoteRundenEnde(boolean end) throws RemoteException {
        board.rundenEnde(end);
    }

    public void spielEnde() throws RemoteException{
        board.getMitte().end();
    }

    public void fade(int id) throws RemoteException {
        board.playerFade(id);
    }

    public void update(int id) throws RemoteException {
        board.getMitte().update(a);
        board.activateHand(id);
        board.updatePlayer(id);

    }

    protected void delayedAction(int id){
        Task<Void> t = new Task<>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(1250);
                return null;
            }
        };
        t.setOnSucceeded(workerStateEvent -> {
            try {
                if(spielerListe.get(id).getisBot()){
                    spielerListe.get(id).play(this);
                    skip();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        new Thread(t).start();
    }

    public void conversion() throws RemoteException {
        for (int i=0; i<10; i++) {
            board.getMitte().addChip(1);
        }
        board.getMitte().removeChips(10);
    }

    public void chipReturn(int color) throws RemoteException {
        board.getMitte().addChip(color);
    }

    public void chipRemove(int points) throws RemoteException {
        board.getMitte().removeChips(points);
    }

    protected void setSkipitall(){this.skipitall = false;}

    public void setStart(boolean start) {
        this.start = start;
    }

    public int getSpielerAnzahl() {
        return spielerAnzahl;
    }

    public int getBotAnzahl(){return this.botAnzahl;}

    int getCountWithdrawn(){
        return this.countWithdrawn;
    }

    public ArrayList<Player> getSpielerListe(){
        return spielerListe;
    }

    HashMap<Integer, Integer> getPlayedCards() {
        return playedCards;
    }

    public Deck getDeck(){
        return d;
    }

    public Ablagestapel getAblage() {
        return a;
    }

    public int getCurrent() {
        return round % (spielerAnzahl+botAnzahl);
    }

    public boolean getStart() {
        return start;
    }


}
