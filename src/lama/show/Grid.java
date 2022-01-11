package lama.show;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

class Grid extends GridPane {

    void createGrid(int numCol, int numRow){
        for (int i = 0; i < numCol; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / numCol);
            getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < numRow; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / numRow);
            getRowConstraints().add(rowConst);
        }
    }
}
