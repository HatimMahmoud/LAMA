import lama.Client;
import lama.ClientIF;
import lama.Server;
import lama.logic.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ServerTest {
    Server server;
    CLientTestImp client1;
    CLientTestImp client2;

    @BeforeEach
    void first() throws RemoteException {
        int[] dif = {2};
        server = new Server(2,0,1,2,dif);
        client1 = new CLientTestImp();
        client1.setId(0);
        client2 = new CLientTestImp();
        client2.setId(1);
        server.registerPlayer(client1);
        server.registerPlayer(client2);
    }



    @org.junit.jupiter.api.Test
    void registerPlayer() throws RemoteException {
        int[] dif = {2};
        Server testServer = new Server(2,0,1,dif);
        assertEquals(0,testServer.registerPlayer(new CLientTestImp()));
        assertEquals(1,testServer.registerPlayer(new CLientTestImp()));
    }

    @org.junit.jupiter.api.Test
    void updateScBoard() throws RemoteException {
        server.updateScBoard();
        assertTrue(client1.scuCalled);
        assertTrue(client2.scuCalled);
    }

    @org.junit.jupiter.api.Test
    void setClientName() throws RemoteException {
        client1.setName("Test1");
        client2.setName("Test2");
        server.setClientName(client1);
        server.setClientName(client2);
        assertEquals("Test1", server.getSpielerListe().get(0).getName());
        assertEquals("Test2", server.getSpielerListe().get(1).getName());
    }

    @org.junit.jupiter.api.Test
    void update() throws RemoteException {
        server.update(0);
        assertTrue(client1.updatePlayerCalled);
        assertTrue(client1.updateMitteCalled);
        assertTrue(client2.updateMitteCalled);
        assertTrue(client2.updatePlayerCalled);
    }

    @org.junit.jupiter.api.Test
    void start() throws RemoteException {
        server.start();
        assertTrue(client1.startedCalled);
        assertTrue(client2.startedCalled);
    }

    @org.junit.jupiter.api.Test
    void putCard() throws RemoteException {
        server.start();
        Integer c = server.getAblage().top();
        if (c<7 && server.getSpielerListe().get(server.getCurrent()).getValues().contains(c+1)){
            server.putCard(c+1,server.getCurrent());
            assertEquals(c+1,server.getAblage().top());
        }
        if(c==7 && server.getSpielerListe().get(server.getCurrent()).getValues().contains(1)) {
            server.putCard(1,server.getCurrent());
            assertEquals(1, server.getAblage().top());
        }
    }

    @org.junit.jupiter.api.Test
    void drawCard() throws RemoteException {
        int i = server.getDeck().getSize();
        server.drawCard(0);
        server.drawCard(1);
        assertEquals(i,server.getDeck().getSize());
        server.start();
        Integer c = server.drawCard(0);
        int id = 0;
        if (c == null) {
            c = server.drawCard(1);
            id = 1;
        }
        assertEquals(i-1, server.getDeck().getSize());
        assertEquals(7, server.getSpielerListe().get(id).getValues().size());
        assertTrue(server.getSpielerListe().get(id).getValues().contains(c));
    }

    @org.junit.jupiter.api.Test
    void withdraw() throws RemoteException {
        assertFalse(server.withdraw(0));
        assertFalse(server.withdraw(1));
        server.start();
        assertTrue(server.withdraw(0) || server.withdraw(1));
    }

    @org.junit.jupiter.api.Test
    void conversion() throws RemoteException {
        server.conversion();
        assertTrue(client1.conversionCalled);
        assertTrue(client2.conversionCalled);
    }

    @org.junit.jupiter.api.Test
    void chipReturn() throws RemoteException {
        server.chipReturn(1);
        assertTrue(client1.chipReturnCalled);
        assertTrue(client2.chipReturnCalled);
    }

    @org.junit.jupiter.api.Test
    void newRound() throws RemoteException {
        server.start();
        if (server.withdraw(0)) {
            server.withdraw(1);
        }
        else {
            server.withdraw(1);
            server.withdraw(0);
        }
        assertTrue(client1.rundenEndeCalled);
        assertTrue(client2.rundenEndeCalled);
        server.newRound();
        assertTrue(client1.nextRoundCalled);
        assertTrue(client2.nextRoundCalled);
        assertEquals(6,server.getSpielerListe().get(0).getValues().size());
        assertEquals(6,server.getSpielerListe().get(1).getValues().size());
        assertEquals(43,server.getDeck().getSize());
    }

    @org.junit.jupiter.api.Test
    void chipRemove() throws RemoteException {
        client1.chipRemoveCalled=false;
        client2.chipRemoveCalled=false;
        server.chipRemove(20);
        assertTrue(client1.chipRemoveCalled);
        assertTrue(client2.chipRemoveCalled);
    }

    @org.junit.jupiter.api.Test
    void remoteRundenEnde() throws RemoteException {
        client1.rundenEndeCalled=false;
        client2.rundenEndeCalled=false;
        server.remoteRundenEnde(true);
        assertTrue(client1.rundenEndeCalled);
        assertTrue(client2.rundenEndeCalled);
    }



    @org.junit.jupiter.api.Test
    void spielEnde() throws RemoteException {
        server.spielEnde();
        assertTrue(client1.spielEndeCalled);
        assertTrue(client2.spielEndeCalled);
    }


}