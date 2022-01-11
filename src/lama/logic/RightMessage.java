package lama.logic;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class RightMessage extends Pane {

    public RightMessage(String messageString , Player player){
        VBox hBox = new VBox();
        hBox.setMinWidth(170);
        hBox.setMaxWidth(170);
        hBox.setPrefHeight(29);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setBackground(new Background(new BackgroundFill(new Color(0.0,0.74,1,1), CornerRadii.EMPTY, Insets.EMPTY)));
        Text message = new Text (messageString);
        message.setTextAlignment(TextAlignment.RIGHT);
        message.setWrappingWidth(160);
        message.setFont(new Font("Comic Sans Ms",15));
        Text benutzerName = new Text(player.getName());
        benutzerName.setTextAlignment(TextAlignment.RIGHT);
        benutzerName.setFont(new Font("Comic Sans Ms",15));
        hBox.getChildren().addAll(benutzerName,message);
        hBox.relocate(30,0);
        this.getChildren().add(hBox);
    }


}
