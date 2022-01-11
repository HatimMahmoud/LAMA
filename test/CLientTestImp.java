import lama.ClientIF;
import lama.logic.Ablagestapel;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class CLientTestImp implements ClientIF {
    public boolean scuCalled = false;
    public boolean updatePlayerCalled = false;
    public boolean updateMitteCalled = false;
    public boolean updateFadeCalled = false;
    public boolean rundenEndeCalled = false;
    public boolean spielEndeCalled = false;
    public String name;
    public int id;
    public boolean chipReturnCalled;
    public boolean conversionCalled;
    public boolean chipRemoveCalled;
    public boolean startedCalled;
    public boolean nextRoundCalled;


    @Override
    public int getId() {
        return id;
    }

    @Override
    public void updatePlayer(int id, ArrayList<Integer> vals) {
        updatePlayerCalled = true;
    }

    @Override
    public void updateMitte(Ablagestapel a) {
        updateMitteCalled = true;
    }

    @Override
    public void fade(int id)  {
        updateFadeCalled = true;
    }

    public void setId(int id){
        this.id = id;
    }

    @Override
    public void rundenEnde(boolean end) {
        rundenEndeCalled = true;
    }

    @Override
    public void spielEnde()  {
        spielEndeCalled = true;
    }

    @Override
    public void conversion()  {
        conversionCalled = true;
    }

    @Override
    public void chipReturn(int color) {
        chipReturnCalled = true;
    }

    @Override
    public void chipRemove(int points) {
        chipRemoveCalled = true;
    }

    @Override
    public void started() {
        startedCalled = true;
    }

    @Override
    public void nextRound() {
        nextRoundCalled = true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void scU() {
        scuCalled = true;
    }

    @Override
    public void sendMessage(int clientID, String s) throws RemoteException {

    }

    @Override
    public void recieveMessage(String s, int clientID) throws RemoteException {

    }

    @Override
    public void addChatpartner(int id) throws RemoteException {

    }

    @Override
    public void recievePrivateMessage(Integer clientID, String s, Integer partnerID) throws RemoteException {

    }

    @Override
    public void newClient(int clientID) {

    }
}
