package lama.logic;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServerLogik extends Remote {
    int getSpielerAnzahl() throws RemoteException;
    int getBotAnzahl() throws RemoteException;
    Ablagestapel getAblage() throws RemoteException;
    Deck getDeck() throws RemoteException;
    ArrayList<Player> getSpielerListe() throws RemoteException;
    void start() throws RemoteException;
    void newRound() throws RemoteException;
    Integer drawCard(int id) throws RemoteException;
    boolean putCard(Integer c, int id) throws RemoteException;
    boolean withdraw(int id) throws RemoteException;
}
