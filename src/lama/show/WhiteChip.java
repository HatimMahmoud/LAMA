package lama.show;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.ImagePattern;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class WhiteChip extends Button {

    public WhiteChip() {
        try {
            setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama\\src\\lama\\Lama_Chips\\Lama_Chip_White.png"))), CornerRadii.EMPTY, Insets.EMPTY)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        setPrefSize(35,35);
    }
}
