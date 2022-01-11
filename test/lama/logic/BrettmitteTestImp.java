package lama.logic;

import javafx.scene.control.Button;
import lama.show.Brettmitte;
import lama.show.Card;

public class BrettmitteTestImp extends Brettmitte {
    public BrettmitteTestImp(Ablagestapel ablage, Deck deck) {
        super(ablage);
    }

    @Override
    public void update(Ablagestapel a) {

    }

    //@Override
    public void emptyDeck() {
    }



    @Override
    public void addChip(int color) {

    }

    @Override
    public void removeChips(int points) {

    }

    @Override
    public Button getButton() {
        return null;
    }

    @Override
    public void end() {

    }

    @Override
    public Card getTop() {
        return null;
    }


    public BrettmitteTestImp() {
        super();
    }
}
