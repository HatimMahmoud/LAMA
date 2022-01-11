package lama;

import javafx.scene.control.Label;
import lama.logic.Ablagestapel;
import lama.logic.Deck;
import lama.logic.LeftMessage;
import lama.show.Card;

import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ClientIF extends Remote {
    int getId() throws RemoteException;
    void updatePlayer(int id, ArrayList<Integer> vals) throws RemoteException;
    void updateMitte(Ablagestapel a) throws RemoteException;
    void fade(int id) throws RemoteException;
    void rundenEnde(boolean end) throws RemoteException;
    void spielEnde() throws RemoteException;
    void conversion() throws RemoteException;
    void chipReturn(int color) throws RemoteException;
    void chipRemove(int points) throws RemoteException;
    void started() throws RemoteException;
    void nextRound() throws RemoteException;
    String getName() throws RemoteException;
    void setName(String name) throws  RemoteException;
    void scU() throws RemoteException;
    void sendMessage(int clientID,String s) throws RemoteException;
    void recieveMessage(String s,int clientID) throws RemoteException;
    void addChatpartner(int id) throws RemoteException;
    void recievePrivateMessage(Integer clientID,String s, Integer partnerID) throws RemoteException;
    void newClient(int clientID) throws  RemoteException;
}
