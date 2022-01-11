package lama.logic;

import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class SpiellogikTest {


    @Test
    void game() throws RemoteException {
        SpielfeldTestImp board = new SpielfeldTestImp();
        int[] dif = {2,2};
        Spiellogik logic = new Spiellogik(3, true, board,2,dif);
        assertFalse(logic.getStart());
        logic.start();
        assertTrue(logic.getStart());
        assertTrue(logic.getSpielerListe().get(logic.getCurrent()).getActive());
        assertFalse(logic.getSpielerListe().get((logic.getCurrent()+1)%3).getActive());
        if ((logic.getAblage().top()==2||logic.getAblage().top() == 3) && logic.getSpielerListe().get(logic.getCurrent()).getValues().contains(3)){
            assertTrue(logic.putCard(3,logic.getCurrent()));
            logic.skip();
        }
        else assertFalse(logic.putCard(3,logic.getCurrent()));
        int c = logic.getCurrent();
        assertTrue(logic.withdraw(c));
        logic.skip();
        int nc = logic.getCurrent();
        if (c==2) {
            assertEquals(0,logic.getCurrent());
        }
        else {
            assertEquals(c+1, logic.getCurrent());
        }
        Integer test1 = logic.drawCard(logic.getCurrent());
        logic.skip();
        assertNotNull(test1);
        assertNotEquals(c, logic.getCurrent());
        assertNotNull(logic.drawCard(logic.getCurrent()));
        logic.skip();
        assertEquals(nc,logic.getCurrent());
        logic.drawCard(nc);
        logic.skip();
        logic.withdraw(logic.getCurrent());
        logic.skip();
        assertNull(logic.drawCard(nc));
        logic.skip();
        assertEquals(nc,logic.getCurrent());
        logic.withdraw(nc);
        assertFalse(logic.getStart());
        logic.skip();
        logic.newRound();
        assertFalse(board.ended);
        logic.start();
        for (int i=0; i<3; i++) {
            logic.withdraw(logic.getCurrent());
            logic.skip();
        }
        assertTrue(board.ended);
    }

    @Test
    void newRound() {
    }

    @Test
    void skip() throws RemoteException {
        int[] dif = {2};
        Spiellogik logic = new Spiellogik(2, true, new SpielfeldTestImp(),3,dif);
        logic.start();

    }



    @Test
    void update() {
    }

    @Test
    void delayedAction() {
    }

    @Test
    void conversion() {
    }

    @Test
    void chipReturn() {
    }

    @Test
    void chipRemove() {
    }

    @Test
    void remoteRundenEnde() {
    }

    @Test
    void spielEnde() {
    }

    @Test
    void drawCard() {
    }

    @Test
    void putCard() {
    }

    @Test
    void withdraw() {
    }

    @Test
    void start() {

    }
}