package lama.logic;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.Serializable;


public class LeftMessage extends Pane implements Serializable {

    public LeftMessage(String messageString , Player player){
        VBox hBox = new VBox();
        hBox.setMinWidth(170);
        hBox.setMaxWidth(170);
        hBox.setPrefHeight(29);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setBackground(new Background(new BackgroundFill(new Color(1,0.65,0.96,1), CornerRadii.EMPTY, Insets.EMPTY)));
        Text message = new Text (messageString);
        message.setTextAlignment(TextAlignment.LEFT);
        message.setWrappingWidth(160);
        message.setFont(new Font("Comic Sans Ms",15));
        Text benutzerName = new Text(player.getName());
        benutzerName.setTextAlignment(TextAlignment.LEFT);
        benutzerName.setFont(new Font("Comic Sans Ms",15));
        hBox.getChildren().addAll(benutzerName,message);
        hBox.relocate(10,0);
        this.getChildren().add(hBox);
    }
}
