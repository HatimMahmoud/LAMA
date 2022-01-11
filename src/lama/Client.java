package lama;

import javafx.application.Platform;
import lama.logic.Ablagestapel;
import lama.logic.Deck;
import lama.logic.Player;
import lama.show.ClientSpielfeld;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Client extends UnicastRemoteObject implements ClientIF {
    private final int clientId;
    private final ServerIF server;
    private final ClientSpielfeld board;
    private final int spielerAnzahl;
    private final int botAnzahl;
    private String name;

    Client(ServerIF server, String name, ArrayList<ArrayList> allEnemyCards) throws RemoteException{
        this.server = server;
        this.name =name;
        clientId = server.registerPlayer(this);
        server.setClientName(this);
        spielerAnzahl = server.getSpielerAnzahl();
        botAnzahl = server.getBotAnzahl();
        board = new ClientSpielfeld(this, (spielerAnzahl+botAnzahl),allEnemyCards,name);
        server.updateScBoard();
        server.addChatPartner(this);
        server.upadePlayerGrid(this);
    }

    public ClientSpielfeld getBoard(){
        return board;
    }

    public Deck getDeck() throws RemoteException {
        return server.getDeck();
    }

    public Ablagestapel getAblage() throws RemoteException{
        return server.getAblage();
    }

    public ArrayList<Player> getSpieler() throws RemoteException {
        return server.getSpielerListe();
    }

    @Override
    public int getId() {
        return this.clientId;
    }

    @Override
    public void updatePlayer(int id, ArrayList<Integer> vals) {
        Platform.runLater(() -> {
            board.updatePlayer(((spielerAnzahl+botAnzahl)+id-clientId)%(spielerAnzahl+botAnzahl), vals);
            board.activateHand();
        });

    }

    @Override
    public void updateMitte(Ablagestapel a) {
        Platform.runLater(() -> board.getMitte().update(a));
    }


    @Override
    public void fade(int id) {
        Platform.runLater(() -> board.playerFade(((spielerAnzahl+botAnzahl)+id-clientId)%(spielerAnzahl+botAnzahl)));
    }

    @Override
    public void rundenEnde(boolean end) {
        Platform.runLater(() -> {
            try {
                board.rundenEnde(end);
            } catch ( RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void spielEnde() {
        Platform.runLater(() -> board.getMitte().end());
    }

    @Override
    public void nextRound() {
        Platform.runLater(() -> {
            try {
                board.newRound();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public void putCard(Integer c) throws RemoteException{
        server.putCard(c, clientId);
    }

    public void drawCard() throws RemoteException{
        server.drawCard(clientId);
    }


    public void withdraw() throws RemoteException{
        if (server.withdraw(clientId)) Platform.runLater(() -> {
                board.withdraw();
                System.out.println("Withdrawn " +clientId);
        });
    }

    public void start() throws RemoteException{
        server.start();
    }



    public void newRound() throws RemoteException {
        server.newRound();
    }

    public void conversion(){
        Platform.runLater(() -> {for (int i=0; i<10; i++) {
            board.getMitte().addChip(1);
        }
        board.getMitte().removeChips(10);
        });
    }

    public void chipReturn(int color){
        Platform.runLater(() ->board.getMitte().addChip(color));
    }

    public void chipRemove(int points){
        Platform.runLater(() ->board.getMitte().removeChips(points));
    }

    @Override
    public void started() {
        Platform.runLater(() -> board.start());
    }

    public void setName(String name){
        this.name = name;
    }

    @Override
    public void scU() {
        Platform.runLater(() -> {
            try {
                board.scoreboardUpdate();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void sendMessage(int clientID,String s) throws RemoteException {
        server.sendMessage(clientID,s);
    }

    @Override
    public void recieveMessage(String s,int senderID) {
        Platform.runLater(() -> board.recieveMessage(s,senderID));
    }

    @Override
    public void addChatpartner(int id) {
        Platform.runLater(() -> board.addChatParnet(id));
    }

    @Override
    public void recievePrivateMessage(Integer clientID,String s, Integer partnerID) {
        Platform.runLater(() -> board.recievePrivateMessage(clientID,s));
    }

    @Override
    public void newClient(int newclientId) {
        Platform.runLater(() -> {
            try {
                board.newPlayer(newclientId);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public String getName(){
        return this.name;
    }

    public void sendPrivateMessage(int clientID, String s, Integer partnerID) {
        try {
            server.sendPrivateMessage(clientID,s,partnerID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
