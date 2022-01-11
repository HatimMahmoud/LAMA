package lama.show;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/***
 * Verwaltung der schwarzen Spielchips in Form eines Button (bisher ohne Funktion)
 */

public class BlackChip extends Button {

    public BlackChip()  {
        try {
            Background bck = new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama\\src\\lama\\Lama_Chips\\Lama_Chip_Black.png"))), CornerRadii.EMPTY, Insets.EMPTY));
            setBackground(bck);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        setPrefSize(35,35);
        setTextFill(Color.WHITE);
    }

}
