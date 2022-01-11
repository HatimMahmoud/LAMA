package lama.show;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.ImagePattern;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


class Withdraw extends Button {

    Withdraw() {
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Aufgeben!");
        try {
            setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama\\src\\lama\\Flag\\Flag.png"))), CornerRadii.EMPTY, Insets.EMPTY)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        setPrefSize(79,79);
        tooltip.setShowDelay(new Duration(0.0));
        setTooltip(tooltip);
    }
}
