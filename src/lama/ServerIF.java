package lama;

import lama.logic.*;
import lama.show.Card;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServerIF extends ServerLogik {
    int registerPlayer(ClientIF c) throws RemoteException;

    String setClientName(ClientIF c) throws RemoteException;

    void updateScBoard() throws RemoteException;

    void removePlayer(ClientIF c) throws RemoteException;

    void sendMessage(int clientID,String s) throws RemoteException;

    void sendPrivateMessage(Integer clientID,String s, Integer partnerID) throws RemoteException;

    void addChatPartner(ClientIF c) throws RemoteException;

    void upadePlayerGrid(ClientIF client) throws RemoteException;
}
