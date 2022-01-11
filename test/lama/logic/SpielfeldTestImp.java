package lama.logic;

import lama.show.Brettmitte;
import lama.show.PlayerGrid;
import lama.show.Spielfeld;

import java.util.List;

public class SpielfeldTestImp extends Spielfeld {
    BrettmitteTestImp mitte = new BrettmitteTestImp();
    public boolean ended = false;

    public SpielfeldTestImp(){
        super();

    }

    @Override
    public void newRound() {

    }

    //@Override
    public void animation(int x, int y, int m, int n) {

    }

    //@Override
    public void Revome_animation(int spielerAnzahl) {

    }

    @Override
    public void activateHand(int id) {

    }

    @Override
    public void rundenEnde(boolean ended) {
        this.ended = ended;
    }

    @Override
    public void playerFade(int id) {

    }

    @Override
    public void updatePlayer(int id) {

    }


    public void spielEnde() {

    }


    public boolean getEnded() {
        return false;
    }

    @Override
    public List<PlayerGrid> getPlayer() {
        return null;
    }

    @Override
    public Brettmitte getMitte() {
        return mitte;
    }
}
