package lama;

import lama.logic.Player;
import lama.logic.Spiellogik;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class  Server extends Spiellogik implements ServerIF {
    private final ArrayList<ClientIF> clients;
    private final Map<ClientIF, Integer> idTable;
    private final int spielerAnzahl;
    private final int botAnzahl;




    public Server(int sa,int ba, int mode, int[] difficulty) throws RemoteException {
        super(sa,ba, mode,difficulty);
        this.spielerAnzahl = sa;
        this.botAnzahl=ba;
        clients = new ArrayList<>();
        idTable = new HashMap<>();
    }

    public Server(int sa, int ba, int mode, int rounds, int[] difficulty) throws RemoteException {
        super(sa,ba, mode, rounds,difficulty);
        this.spielerAnzahl = sa;
        this.botAnzahl = ba;
        clients = new ArrayList<>();
        idTable = new HashMap<>();
    }


    @Override
    public int registerPlayer(ClientIF c) throws RemoteException {
        clients.add(c);
        idTable.put(c,clients.size() - 1);
        System.out.println("Client added. ID: " + (clients.size() - 1));
        return clients.size()-1;
    }


    @Override
    public void updateScBoard() throws RemoteException{
        for (ClientIF client : clients) {
            client.getName();
            client.scU();
        }
    }


    @Override
    public void addChatPartner(ClientIF c) throws RemoteException{
        for(ClientIF cl : clients){
            if (c.getId()!=cl.getId()){
                cl.addChatpartner(c.getId());
                c.addChatpartner(cl.getId());
            }
        }
    }

    @Override
    public void upadePlayerGrid(ClientIF client) throws RemoteException {
        for (ClientIF c : clients) {
            if (c.getId()!=client.getId()){
               c.newClient(client.getId());
            }
        }
    }

    @Override
    public String setClientName(ClientIF c) throws  RemoteException{
        getSpielerListe().get(c.getId()).setName(c.getName());
        return c.getName();
    }

    @Override
    public void fade(int id) throws RemoteException {
        for (ClientIF c : clients){
            c.fade(id);
        }
    }

    @Override
    public void update(int id) throws RemoteException{
        for (ClientIF c : clients){
            c.updatePlayer(id, spielerListe.get(id).getValues());
            c.updateMitte(getAblage());
        }
    }

    @Override
    public boolean putCard(Integer c, int id) throws RemoteException{
        if (super.putCard(c, id)) {
            skip();
            return true;
        }
        return false;
    }

    @Override
    public Integer drawCard(int id) throws RemoteException{
         /*if (super.drawCard(id)==null) {
            System.out.println("null");
            return null;
        }
        else skip(); return super.drawCard(id);
        */

        Integer val = super.drawCard(id);
        if (val==null) return null;
        else skip(); return val;


    }

    @Override
    public boolean withdraw(int id) throws RemoteException{
        boolean w = super.withdraw(id);
        for(ClientIF cf : clients){
            cf.updatePlayer(id,spielerListe.get(id).getValues());
        }
        if (w) skip();
        return w;

    }

    @Override
    public void conversion() throws RemoteException {
        for (ClientIF c : clients){
            c.conversion();
        }
    }

    @Override
    public void chipReturn(int color) throws RemoteException {
        for (ClientIF c : clients){
            c.chipReturn(color);
        }
    }

    @Override
    public void newRound() throws RemoteException {
        super.newRound();
        if (clients != null){
            for (ClientIF c: clients){
                c.nextRound();
            }
        }
    }

    @Override
    public void chipRemove(int points) throws RemoteException {
        for (ClientIF c : clients){
            c.chipRemove(points);
        }
    }



    @Override
    public void remoteRundenEnde(boolean end) throws RemoteException{
        for (ClientIF c : clients){
            c.rundenEnde(end);
        }
    }



    @Override
    public void removePlayer(ClientIF c) {
       clients.remove(c);
    }

    @Override
    public void sendMessage(int clientID,String s ) throws RemoteException {
        for(ClientIF c : clients){
            if (c.getId()!=clientID){
                c.recieveMessage(s,clientID);
            }
        }
    }

    @Override
    public void sendPrivateMessage(Integer clientID,String s, Integer partnerID) {
        try{
            clients.get(partnerID).recievePrivateMessage(clientID,s,partnerID);
        }
        catch (Exception e ){
            e.printStackTrace();
        }
    }

    @Override
    public void spielEnde() throws RemoteException{
        for (ClientIF c : clients){
            c.spielEnde();
        }
    }

    @Override
    public void start() throws RemoteException{
        setSkipitall();
        setStart(true);
        for (ClientIF c : clients){
            c.started();
            for (int i = 0; i< spielerAnzahl+botAnzahl; i++){
                c.updatePlayer(i, spielerListe.get(i).getValues());
            }
        }
        int id = getCurrent();
        while(id <0 ){
            id++;
        }
        Player p = spielerListe.get(id);
        p.setActive(true);
        fade(id);
        if (p.getisBot()) {
            delayedAction(id);
        }
    }
}
