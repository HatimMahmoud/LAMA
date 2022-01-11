package lama.show;

import javafx.animation.PathTransition;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.util.Duration;
import lama.Client;
import lama.logic.Paar;
import lama.logic.Player;
import lama.logic.Scoreboard;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class ClientSpielfeld extends Grid {
    private final Client client;
    private final int spielerAnzahl;
    private final List<PlayerGrid> spielerListe = new ArrayList<>();
    private Brettmitte mitte;
    private final Button btn1 = new Button("start");
    private final int clientId;
    private final Button btn2 = new Button("Nächste Runde starten?");
    private Scoreboard scBoard;
    private Grid board;
    private ArrayList<Player> playerList;
    private ArrayList<Label> labelList;
    private ArrayList<Integer> playerPoints;
    private Chatfenster chatfenster;
    private Background muted, unmuted;
    private final Accordion accordion;
    private final ArrayList<Chatfenster> allChats;
    private final ArrayList<Integer> mutedClients;
    private final ArrayList<ArrayList> allEnemyCards;
    private final Button tipp = new Button("Tipp zeigen");
    private final Button closetipp = new Button("Tipp schließen");
    private final Tooltip tt1 = new Tooltip("Tipp: Die Karte, die ein graues Pop-Up Fenster hat, kann nicht abgelegt werden!");
    private final Tooltip tt2 = new Tooltip("Wenn du schlau bist,brauchst du keinen Tipp mehr!");
    private boolean zeigTipp = false;
    private Button chatopen;
    private Button mutebutton1;
    private final String name;

    private IosSwitch checkBox1;
    private IosSwitch checkBox2;
    private IosSwitch checkBox3;
    private IosSwitch checkBox4;
    private boolean autoSortToggle=true;
    private boolean tippToggle=true;
    Button sortButton = new Button();
    Button sortButtonDsec = new Button();



    public ClientSpielfeld(Client client, int spielerAnzahl, ArrayList<ArrayList> allEnemyCards, String name) throws RemoteException {
        this.client = client;
        this.clientId = client.getId();
        this.allEnemyCards = allEnemyCards;
        this.spielerAnzahl = spielerAnzahl;
        this.name = name;

        createGrid(1800, 1000);
        playerList = client.getSpieler();
        accordion = new Accordion();
        allChats = new ArrayList<>();
        mutedClients = new ArrayList<>();
        init();
    }

    private void init() throws RemoteException {
        mitte = new Brettmitte(client.getAblage());
        add(mitte, 800, 500, 300, 80);
        back();
        placement();
        scoreboard();
        tipinit();
        sort();
        chatinit();
        gearbox();
        newRound();
        playerPoints = new ArrayList<>();
        spielerListe.get(0).getWithdraw().setOnAction(actionEvent -> {
            try {
                client.withdraw();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        mitte.setOnDragOver(dragEvent -> dragEvent.acceptTransferModes(TransferMode.ANY));

        mitte.setOnDragDropped(dragEvent -> {
            Dragboard db = dragEvent.getDragboard();
            System.out.println(db.getString());
            String s = db.getString();
            System.out.println("s" + s);
            System.out.println(Integer.parseInt(s));
            int curVal = Integer.parseInt(db.getString());
            if (possible(curVal, mitte.getTop().getValue())) {
                try {
                    client.putCard(curVal);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean possible(int curVal, int value) {
        if (curVal == value) {
            return true;
        }
        if (value == 7 && curVal == 1) {
            return true;
        }
        return value + 1 == curVal;
    }

    private void back() {
        try {
            setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama\\src\\lama\\textures\\holz.jpg"))), CornerRadii.EMPTY, Insets.EMPTY)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void placement() throws RemoteException {
        ArrayList<Player> playerList = client.getSpieler();
        final boolean[] muteflag = {false, false, false, false, false};
        int c = 0;
        try {
            unmuted = (new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama/src/lama/Icons/lautsprecher.png"))), CornerRadii.EMPTY, Insets.EMPTY)));
            muted = (new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama/src/lama/Icons/lautsprecher_muted.png"))), CornerRadii.EMPTY, Insets.EMPTY)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        mutebutton1 = new Button();
        Button mutebutton2 = new Button();
        Button mutebutton3 = new Button();
        Button mutebutton4 = new Button();
        Button mutebutton5 = new Button();

        mutebutton1.setBackground(unmuted);
        mutebutton2.setBackground(unmuted);
        mutebutton3.setBackground(unmuted);
        mutebutton4.setBackground(unmuted);
        mutebutton5.setBackground(unmuted);
        mutebutton1.setOnAction(actionEvent -> {
            if (!muteflag[0]) {
                mutebutton1.setBackground(muted);
                mutebutton1.setStyle("-fx-border-width: 2;-fx-border-color: yellow;");
                mutedClients.add((clientId + 1) % spielerAnzahl);
                muteflag[0] = true;
            } else {
                mutebutton1.setBackground(unmuted);
                mutebutton1.setStyle("-fx-border-width: 2;-fx-border-color: transparent;");
                mutedClients.remove((Object) ((clientId + 1) % spielerAnzahl));
                muteflag[0] = false;
            }

        });

        mutebutton2.setOnAction(actionEvent -> {
            if (!muteflag[1]) {
                mutebutton2.setBackground(muted);
                mutebutton2.setStyle("-fx-border-width: 2;-fx-border-color: yellow;");
                mutedClients.add((clientId + 2) % spielerAnzahl);
                muteflag[1] = true;
            } else {
                mutebutton2.setBackground(unmuted);
                mutebutton2.setStyle("-fx-border-width: 2;-fx-border-color: transparent;");
                mutedClients.remove((Object) ((clientId + 2) % spielerAnzahl));
                muteflag[1] = false;
            }

        });

        mutebutton3.setOnAction(actionEvent -> {
            if (!muteflag[2]) {
                mutebutton3.setBackground(muted);
                mutebutton3.setStyle("-fx-border-width: 2;-fx-border-color: yellow;");
                mutedClients.add((clientId + 3) % spielerAnzahl);
                muteflag[2] = true;
            } else {
                mutebutton3.setBackground(unmuted);
                mutebutton3.setStyle("-fx-border-width: 2;-fx-border-color: transparent;");
                mutedClients.remove((Object) ((clientId + 3) % spielerAnzahl));
                muteflag[2] = false;
            }

        });
        mutebutton4.setOnAction(actionEvent -> {
            if (!muteflag[3]) {
                mutebutton4.setBackground(muted);
                mutebutton4.setStyle("-fx-border-width: 2;-fx-border-color: yellow;");
                mutedClients.add((clientId + 4) % spielerAnzahl);
                muteflag[3] = true;
            } else {
                mutebutton4.setBackground(unmuted);
                mutebutton4.setStyle("-fx-border-width: 2;-fx-border-color: transparent;");
                mutedClients.remove((Object) ((clientId + 4) % spielerAnzahl));
                muteflag[3] = false;
            }

        });
        mutebutton5.setOnAction(actionEvent -> {
            if (!muteflag[4]) {
                mutebutton5.setBackground(muted);
                mutebutton5.setStyle("-fx-border-width: 2;-fx-border-color: yellow;");
                mutedClients.add((clientId + 5) % spielerAnzahl);
                muteflag[4] = true;
            } else {
                mutebutton5.setBackground(unmuted);
                mutebutton5.setStyle("-fx-border-width: 2;-fx-border-color: transparent;");
                mutedClients.remove((Object) ((clientId + 5) % spielerAnzahl));
                muteflag[4] = false;
            }

        });

        Label userName = new Label(name);
        add(userName, 850, 700, 300, 100);
        userName.getStylesheets().add(getClass().getResource("gui.css").toExternalForm());
        userName.setStyle("-fx-font-weight: bold;-fx-font-size: 20px");

        switch (spielerAnzahl) {
            case 2:
                for (int i = 0; i < spielerAnzahl; i++) {
                    this.spielerListe.add(new PlayerGrid(i * 180, playerList.get((clientId + i) % spielerAnzahl), playerList.get((clientId + i) % spielerAnzahl).getName(), allEnemyCards));
                }
                //Add to the Grid
                add(spielerListe.get(0), 700, 850, 670, 110);
                add(spielerListe.get(1), 600, 150, 580, 150);

                //set remove animation
                spielerListe.get(0).setRemove(100, 200);
                spielerListe.get(1).setRemove(-230, -230);

                //Add Mute Buttons
                add(mutebutton1, 550, 150, 64, 64);
                break;
            case 3:
                this.spielerListe.add(new PlayerGrid(0, playerList.get(clientId), playerList.get((clientId) % spielerAnzahl).getName(), allEnemyCards));
                for (int i = 1; i < spielerAnzahl; i++) {
                    spielerListe.add(new PlayerGrid(180, playerList.get((clientId + i) % spielerAnzahl), playerList.get((clientId + i) % spielerAnzahl).getName(), allEnemyCards));
                }

                //Add to the Grid
                add(spielerListe.get(0), 700, 800, 670, 150);
                add(spielerListe.get(1), 50, 150, 580, 170);
                add(spielerListe.get(2), 1150, 150, 580, 170);

                //set remove animation
                spielerListe.get(0).setRemove(100, 200);
                spielerListe.get(1).setRemove(-680, -250);
                spielerListe.get(2).setRemove(100, -250);

                //Add Mute Buttons
                add(mutebutton1, 100, 250, 64, 64);
                add(mutebutton2, 1200, 250, 64, 64);
                break;
            case 4:
                for (int i = 0; i < spielerAnzahl; i++) {
                    this.spielerListe.add(new PlayerGrid(i * 90, playerList.get((clientId + i) % spielerAnzahl), playerList.get((clientId + i) % spielerAnzahl).getName(), allEnemyCards));
                }

                //Add to the Grid
                add(spielerListe.get(0), 700, 800, 670, 150);
                add(spielerListe.get(1), 200, 300, 170, 580);
                add(spielerListe.get(2), 600, 150, 580, 170);
                add(spielerListe.get(3), 1600, 300, 170, 580);

                //set remove animation
                spielerListe.get(0).setRemove(100, 200);
                spielerListe.get(1).setRemove(-550, 200);
                spielerListe.get(2).setRemove(-230, -230);
                spielerListe.get(3).setRemove(400, -150);

                //Add Mute Buttons
                add(mutebutton1, 350, 800, 64, 64);
                add(mutebutton2, 600, 100, 64, 64);
                add(mutebutton3, 1570, 330, 64, 64);
                break;
            case 5:
                for (int i = 0; i < spielerAnzahl - 1; i++) {
                    if (i == 3) {
                        this.spielerListe.add(new PlayerGrid(180, playerList.get((clientId + c) % spielerAnzahl), playerList.get((clientId + c) % spielerAnzahl).getName(), allEnemyCards));
                        c++;
                    }
                    this.spielerListe.add(new PlayerGrid(i * 90, playerList.get((clientId + c) % spielerAnzahl), playerList.get((clientId + c) % spielerAnzahl).getName(), allEnemyCards));
                    c++;
                }

                //Add to the Grid
                add(spielerListe.get(0), 700, 800, 670, 150);
                add(spielerListe.get(1), 200, 300, 170, 580);
                add(spielerListe.get(2), 50, 150, 580, 170);
                add(spielerListe.get(3), 1150, 150, 580, 170);
                add(spielerListe.get(4), 1600, 300, 170, 500);

                //set remove animation
                spielerListe.get(0).setRemove(100, 200);
                spielerListe.get(1).setRemove(-550, 200);
                spielerListe.get(2).setRemove(-680, -250);
                spielerListe.get(3).setRemove(100, -280);
                spielerListe.get(4).setRemove(400, -150);

                //Add Mute Buttons
                add(mutebutton1, 350, 850, 64, 64);
                add(mutebutton2, 50, 100, 64, 64);
                add(mutebutton3, 1150, 100, 64, 64);
                add(mutebutton4, 1570, 330, 64, 64);
                break;
            case 6:
                for (int i = 0; i < spielerAnzahl - 2; i++) {
                    if (i == 2 || i == 3) {
                        this.spielerListe.add(new PlayerGrid(180, playerList.get((clientId + c) % spielerAnzahl), playerList.get((clientId + c) % spielerAnzahl).getName(), allEnemyCards));
                        c++;
                    }
                    this.spielerListe.add(new PlayerGrid(i * 90, playerList.get((clientId + c) % spielerAnzahl), playerList.get((clientId + c) % spielerAnzahl).getName(), allEnemyCards));
                    c++;
                }
                //Add to the Grid
                add(spielerListe.get(0), 700, 800, 670, 150);
                add(spielerListe.get(1), 200, 300, 170, 580);
                add(spielerListe.get(2), 50, 100, 580, 170);
                add(spielerListe.get(3), 600, 150, 580, 170);
                add(spielerListe.get(4), 1150, 100, 500, 170);
                add(spielerListe.get(5), 1600, 300, 170, 500);

                //Set animation Coords
                spielerListe.get(0).setAll(1180, 870, -120, -250);
                spielerListe.get(1).setAll(210, 800, 600, -200);
                spielerListe.get(2).setAll(50, 110, 700, 250);
                spielerListe.get(3).setAll(600, 170, 300, 250);
                spielerListe.get(4).setAll(1150, 110, -120, 250);
                spielerListe.get(5).setAll(1570, 320, -400, 150);

                //set remove animation
                spielerListe.get(0).setRemove(100, 200);
                spielerListe.get(1).setRemove(-550, 200);
                spielerListe.get(2).setRemove(-650, -280);
                spielerListe.get(3).setRemove(-230, -230);
                spielerListe.get(4).setRemove(100, -250);
                spielerListe.get(5).setRemove(400, -150);

                //Add Mute Button
                add(mutebutton1, 350, 800, 64, 64);
                add(mutebutton2, 50, 50, 64, 64);
                add(mutebutton3, 600, 100, 64, 64);
                add(mutebutton4, 1150, 50, 64, 64);
                add(mutebutton5, 1570, 330, 64, 64);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + spielerAnzahl);
        }
    }

    private void scoreboard() {
        scBoard = new Scoreboard(playerList);
        PriorityQueue<Paar> scoreList = scBoard.getScoreList();
        board = new Grid();
        labelList = new ArrayList<>();
        int c = 1;
        for (Paar p : scoreList) {
            Label l = new Label(" " + p.getName() + "   " + p.getPoints());
            labelList.add(l);
            l.setFont(new Font("Comic Sans Ms", 16));
            l.setTextFill(Paint.valueOf("White"));
            board.add(l, 0, c * 30, 290, 30);
            c++;
        }

        Button open = new Button();
        Button close = new Button();
        try {
            close.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama/src/lama/Icons/Close_Arrow.png"))), CornerRadii.EMPTY, Insets.EMPTY)));
            open.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama/src/lama/Icons/Open_Arrow.png"))), CornerRadii.EMPTY, Insets.EMPTY)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        open.setPrefSize(100, 100);
        close.setPrefSize(100, 100);
        board.setBackground(new Background(new BackgroundFill(new Color(0.0, 0.0, 0.0, 0.4), CornerRadii.EMPTY, Insets.EMPTY)));
        board.setDisable(true);
        board.setVisible(false);
        close.setDisable(true);
        close.setVisible(false);
        open.setOnAction(actionEvent -> {
            open.setDisable(true);
            open.setVisible(false);
            close.setDisable(false);
            close.setVisible(true);
            board.setDisable(false);
            board.setVisible(true);
        });
        close.setOnAction(actionEvent -> {
            open.setDisable(false);
            open.setVisible(true);
            close.setDisable(true);
            close.setVisible(false);
            board.setDisable(true);
            board.setVisible(false);
        });


        this.add(open, 1725, 900, 75, 75);
        this.add(close, 1450, 900, 75, 75);
        this.add(board, 1500, 810, 300, c * 30);
    }

    public void scoreboardUpdate() throws RemoteException {
        playerList = client.getSpieler();
        scBoard.setPlayerList(playerList);
        scBoard.update();
        PriorityQueue<Paar> scoreList = scBoard.getScoreList();
        for (Label label : labelList) {
            Paar p = scoreList.poll();
            if (p != null) {
                label.setText(" " + p.getName() + "   " + p.getPoints());
            }
        }
    }

    private void tipinit() {
        zeigTipp = false;
        add(tipp, 1200, 800, 140, 35); // show Tipp
        tt1.setShowDelay(new Duration(0.0));
        tipp.setTooltip(tt1);
        closetipp.setVisible(false);
        closetipp.setDisable(true);
        tt2.setShowDelay(new Duration(0.0));
        closetipp.setTooltip(tt2);
        tipp.setDisable(false);
        tipp.setVisible(true);
        tipp.setOnAction(actionEvent -> {
            try {
                tipp.setDisable(true);
                tipp.setVisible(false);
                closetipp.setDisable(false);
                closetipp.setVisible(true);
                zeigTipp = true;
                if (spielerListe.get(0).getVisibility()) {
                    updatePlayer(0, spielerListe.get(0).getVals());
                    activateHand();
                }
                this.add(closetipp, 1200, 800, 150, 35);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        closetipp.setOnAction(actionEvent -> {
            try {
                tipp.setDisable(false);
                tipp.setVisible(true);
                closetipp.setDisable(true);
                closetipp.setVisible(false);
                zeigTipp = false;
                if (spielerListe.get(0).getVisibility()) {
                    updatePlayer(0, spielerListe.get(0).getVals());
                    activateHand();
                }
                this.add(tipp, 1200, 800, 140, 35);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void sort() {
        Button sortButton = new Button();
        Button sortButtonDsec = new Button();
        sortButton.setPrefSize(100, 100);
        sortButtonDsec.setPrefSize(100, 100);
        try {
            sortButton.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama/src/lama/Icons/sort.png"))), CornerRadii.EMPTY, Insets.EMPTY)));
            sortButtonDsec.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama/src/lama/Icons/sortrev.png"))), CornerRadii.EMPTY, Insets.EMPTY)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        sortButton.setOnAction(actionEvent -> {
            spielerListe.get(0).sortCards();
            sortButton.setDisable(true);
            sortButton.setVisible(false);
            sortButtonDsec.setDisable(false);
            sortButtonDsec.setVisible(true);
        });
        sortButtonDsec.setOnAction(actionEvent -> {
            spielerListe.get(0).sortCardsDesc();
            sortButton.setDisable(false);
            sortButton.setVisible(true);
            sortButtonDsec.setDisable(true);
            sortButtonDsec.setVisible(false);
        });
        this.add(sortButton, 1000, 940, 75, 75);
        this.add(sortButtonDsec, 1000, 940, 75, 75);
    }
    public void gearbox() {

        board = new Grid();
        labelList = new ArrayList<>();
        int c = 1;
        Button open = new Button();
        Button close = new Button();
        try {
            close.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama/src/lama/Icons/gear-on.png"))), CornerRadii.EMPTY, Insets.EMPTY)));
            open.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama/src/lama/Icons/gear.png"))), CornerRadii.EMPTY, Insets.EMPTY)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        open.setPrefSize(40, 40);
        close.setPrefSize(40, 40);
        board.setBackground(new Background(new BackgroundFill(new Color(0.0, 0.0, 0.0, 0.4), CornerRadii.EMPTY, Insets.EMPTY)));
        board.setDisable(true);
        board.setVisible(false);
        close.setDisable(true);
        close.setVisible(false);
        open.setOnAction(actionEvent -> {
            open.setDisable(true);
            open.setVisible(false);
            close.setDisable(false);
            close.setVisible(true);
            board.setDisable(false);
            board.setVisible(true);
        });
        close.setOnAction(actionEvent -> {
            open.setDisable(false);
            open.setVisible(true);
            close.setDisable(true);
            close.setVisible(false);
            board.setDisable(true);
            board.setVisible(false);
        });
        this.add(open, 1725, 100, 75, 75);
        this.add(close, 1725, 100, 75, 75);
        this.add(board, 1550, 10, 250, 90);

        checkBox1 = IosSwitchBuilder.create()
                .prefSize(76, 46)
                .selected(false)
                .build();
        checkBox2 = IosSwitchBuilder.create()
                .prefSize(76, 46)
                .selected(false)
                .selectedColor(Color.RED)
                .build();
        checkBox3 = IosSwitchBuilder.create()
                .prefSize(76, 46)
                .selected(false)
                .selectedColor(Color.CORNFLOWERBLUE)
                .showOnOffText(true)
                .build();
        Label l = new Label("Schimpfwortfilter");
        l.setStyle("-fx-font-scale: 12;-fx-text-fill: white");
        Label l1 = new Label("Automatisches Sortieren");
        l1.setStyle("-fx-font-scale: 12;-fx-text-fill: white");
        Label l2 = new Label("Tipps");
        l2.setStyle("-fx-font-scale: 12;-fx-text-fill: white");
        board.add(checkBox1, 240, 150, 100, 100);
        board.add(l, 1725, 150, 100, 100);
        board.add(checkBox2, 240, 250, 100, 100);
        board.add(l1, 1725, 250, 100, 100);
        board.add(checkBox3, 240, 350, 100, 100);
        board.add(l2, 1725, 350, 100, 100);

        checkBox1.setOnMouseClicked(actionEvent -> {
            if (checkBox1.isSelected()) {
                chatfenster.setFilter(true);
            } else {
                chatfenster.setFilter(false);
            }
        });
        checkBox2.setOnMouseClicked(actionEvent -> {
            if (checkBox2.isSelected()) {
                sortButton.setVisible(false);
                sortButtonDsec.setVisible(true);
            } else {
                sortButton.setVisible(false);
                sortButtonDsec.setVisible(false);
            }
        });

        checkBox3.setOnMouseClicked(actionEvent -> {
            if (!checkBox3.isSelected()) {
                tipp.setVisible(false);
                closetipp.setVisible(false);
            } else {
                tipp.setVisible(true);
                closetipp.setVisible(false);
            }
        });


    }

        private void chatinit() {
        chatfenster = new Chatfenster(client, clientId, "AllChat");
        chatopen = new Button();
        Button close = new Button();

        try {
            chatopen.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama/src/lama/Icons/messenger.png"))), CornerRadii.EMPTY, Insets.EMPTY)));
            close.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama/src/lama/Icons/messenger_close.png"))), CornerRadii.EMPTY, Insets.EMPTY)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        chatfenster.setVisible(false);
        chatfenster.setDisable(true);
        close.setVisible(false);
        close.setDisable(true);

        chatopen.setOnAction(actionEvent -> {
            if (spielerAnzahl > 3) {
                this.getChildren().remove(spielerListe.get(1));
                add(spielerListe.get(1), 500, 300, 170, 580);
                this.getChildren().remove(mutebutton1);
                add(mutebutton1, 610, 800, 64, 64);
            }
            chatopen.setVisible(false);
            chatopen.setDisable(true);
            chatopen.setStyle("-fx-border-color: transparent");
            close.setVisible(true);
            close.setDisable(false);
            chatfenster.setVisible(true);
            chatfenster.setDisable(false);
            for (Chatfenster c : allChats) {
                c.setVisible(true);
                c.setDisable(false);
            }

        });

        close.setOnAction(actionEvent -> {
            if (spielerAnzahl > 3) {
                this.getChildren().remove(spielerListe.get(1));
                add(spielerListe.get(1), 200, 300, 170, 580);
                this.getChildren().remove(mutebutton1);
                add(mutebutton1, 350, 800, 64, 64);
            }
            chatopen.setVisible(true);
            chatopen.setDisable(false);
            close.setVisible(false);
            close.setDisable(true);
            chatfenster.setVisible(false);
            chatfenster.setDisable(true);
            for (Chatfenster c : allChats) {
                c.setVisible(false);
                c.setDisable(true);
            }
        });
        accordion.getPanes().add(chatfenster);
        this.add(chatopen, 400, 900, 75, 100);
        this.add(close, 400, 900, 75, 100);
        this.add(accordion, 50, 525, 230, 370);
    }

    public void addChatParnet(int id) {
        Chatfenster newChat = new Chatfenster(client, clientId, playerList.get(id).getName(), id);
        newChat.setVisible(false);
        newChat.setDisable(true);
        accordion.getPanes().add(newChat);
        allChats.add(newChat);
    }

    ArrayList<Integer> getMutedClients() {
        return this.mutedClients;
    }

    public void recieveMessage(String s, int senderID) {
        if (!this.chatfenster.isVisible()) {
            this.chatopen.setStyle("-fx-border-color: red;-fx-border-width: 2");
        }
        chatfenster.recieveMessage(s, senderID);
    }

    public void recievePrivateMessage(int clientId, String s) {
        if (!this.chatfenster.isVisible()) {
            this.chatopen.setStyle("-fx-border-color: red;-fx-border-width: 2");
        }
        for (Chatfenster allChat : allChats) {
            if (allChat.getTargedID() == clientId) {
                allChat.recieveMessage(s, clientId);
            }

        }
    }

    public void newRound() throws RemoteException {
        getChildren().remove(btn2);

        btn1.setPrefSize(200, 35);
        btn1.setStyle("-fx-font-size: 17px; -fx-border-radius: 4; -fx-border-width: 4 ; -fx-border-color: darkblue;-fx-background-radius: 6; -fx-background-color: LightBlue;");
        add(btn1, 800, 400, 200, 35);

        btn1.setOnAction((Action) -> {
            try {
                playerList = client.getSpieler();
                scoreboardUpdate();
                client.start();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });

        mitte.update(client.getAblage());
        mitte.getTop().setBack();

        for (PlayerGrid p : spielerListe) {
            p.drawHand();
        }

        mitte.getButton().setOnAction(actionEvent -> {
            try {
                client.drawCard();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public void start() {
        mitte.getTop().setFront();
        spielerListe.get(0).setVisibility(true);
        spielerListe.get(0).setPlayer();
        getChildren().remove(btn1);
    }

    public void withdraw() {
        spielerListe.get(0).setVisibility(false);
        spielerListe.get(0).drawHand();
    }

    public void activateHand() {
        if (spielerListe.get(0).getVisibility()) {
            for (Card c : spielerListe.get(0).getCards()) {
                c.setOnAction(actionEvent -> {
                    try {
                        client.putCard(c.getValue());
                        System.out.println("Karte abgelegt");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                });
                c.setOnDragDetected(mouseEvent -> {
                    Dragboard db = c.startDragAndDrop(TransferMode.ANY);
                    ClipboardContent cb = new ClipboardContent();
                    cb.putString(Integer.toString(c.getValue()));
                    cb.putImage(c.getImage());

                    db.setContent(cb);
                });

            }
        }
    }

    private Integer faded;

    public void playerFade(int id) {


        Grid highlight;
        if (id == 0) {
            highlight = spielerListe.get(id).getCardHand();
        } else {
            highlight = spielerListe.get(id);
        }

        if (faded == null) {
            faded = id;
            spielerListe.get(id).fade();
            highlight.setStyle("-fx-border-radius: 20;-fx-border-width: 6;-fx-border-color: darkred;-fx-background-radius: 22;-fx-background-color: red");
        } else {
            spielerListe.get(faded).setStyle("-fx-background-color: transparent;-fx-border-color: transparent");
            if(faded == 0) {
                spielerListe.get(faded).getCardHand().setStyle("-fx-background-color: transparent;-fx-border-color: transparent");
            }
            faded = id;
            spielerListe.get(id).fade();
            highlight.setStyle("-fx-border-radius: 20;-fx-border-width: 6;-fx-border-color: darkred;-fx-background-radius: 22;-fx-background-color: red");

        }
    }


    public void updatePlayer(int id, ArrayList<Integer> vals) {
        try {
            playerList = client.getSpieler();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        spielerListe.get(id).getP().setWithdrawn(playerList.get(id).getWithdrawn());
        System.out.println("In the Client " + spielerListe.get(id).getP().getWithdrawn());
        spielerListe.get(id).setCards(vals);
        if (spielerListe.get(0).getVisibility() && !zeigTipp) {
            spielerListe.get(id).drawHand();
            for (int q = 0; q < spielerListe.get(0).getCards().size(); q++) {
                spielerListe.get(0).getCards().get(q).setFront();
            }
        } else if (spielerListe.get(0).getVisibility() && zeigTipp) {
            spielerListe.get(id).drawHand();
            for (int q = 0; q < spielerListe.get(0).getCards().size(); q++) {
                if (spielerListe.get(0).getCards().get(q).getValue() != mitte.getTop().getValue()
                        && (spielerListe.get(0).getCards().get(q).getValue() != (mitte.getTop().getValue() + 1) % 7)
                ) {
                    spielerListe.get(0).getCards().get(q).getGray();
                } else spielerListe.get(0).getCards().get(q).setFront();
            }
        } else spielerListe.get(id).drawHand();
    }

    public void rundenEnde(boolean end) throws RemoteException {
        scoreboardUpdate();
        ArrayList<Player> players = client.getSpieler();

        for (int i = clientId; i < spielerAnzahl; i++) {
            int points = players.get(i).getPointDif();
            playerPoints.add(points);
        }
        for (int i = 0; i < clientId; i++) {
            int points = players.get(i).getPointDif();
            playerPoints.add(points);
        }
        removeAnimation(spielerAnzahl, playerPoints);

        playerPoints.clear();
        for (int i = 0; i < spielerAnzahl; i++) {
            PlayerGrid p = spielerListe.get(i);
            p.setP(players.get((clientId + i) % spielerAnzahl));
            p.drawHand();
            p.update();
        }
        if (!end) {
            btn2.setPrefSize(200, 35);
            add(btn2, 800, 400, 200, 35);
            btn2.setStyle("-fx-font-size: 17px; -fx-border-radius: 4; -fx-border-width: 4 ; -fx-border-color: darkblue;-fx-background-radius: 6; -fx-background-color: LightBlue;");
            btn2.setOnAction((Action) -> {
                try {
                    client.newRound();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void animation(int x, int y, int m, int n, int points, int pos) {
        int white = points % 10;
        int black = points / 10;
        Grid chips = new Grid();
        WhiteChip w1 = new WhiteChip();
        w1.setText(Integer.toString(white));
        BlackChip b1 = new BlackChip();
        b1.setText(Integer.toString(black));
        chips.setPrefSize(100, 110);
        chips.createGrid(100, 110);
        if (points == -1 || points == -10) {
            if (points == -1) {
                chips.add(w1, 20, 22, 35, 35);
            } else {
                chips.add(b1, 70, 22, 35, 35);
            }
            add(chips, 1020, 520, 100, 100);
        } else {
            add(chips, x, y, 100, 100);
        }
        if (white > 0 && black <= 0) {
            chips.add(w1, 20, 22, 35, 35);
        }
        if (black > 0 && white <= 0) {
            chips.add(b1, 70, 22, 35, 35);
        }
        if (black > 0 && white > 0) {
            chips.add(w1, 20, 22, 35, 35);
            chips.add(b1, 70, 22, 35, 35);
        }
        Path path = new Path();

        if (points == -1 || points == -10) {
            System.out.println(spielerListe.get(pos).getM1() + "    " + spielerListe.get(pos).getN1());
            path.getElements().add(new MoveTo(spielerListe.get(pos).getM1(), spielerListe.get(pos).getN1()));
            System.err.println(path.getElements());
        } else {
            path.getElements().add(new MoveTo(m, n));
        }
        path.getElements().add(new CubicCurveTo(0, 0, 0, 0, 0, 0));
        PathTransition pt = new PathTransition();
        PathTransition pt1 = new PathTransition();
        pt.setDuration(Duration.millis(4000));
        pt.setPath(path);
        pt.setNode(w1);
        pt.play();
        pt.setAutoReverse(false);
        pt1.setDuration(Duration.millis(4000));
        pt1.setPath(path);
        pt1.setNode(b1);
        pt1.setAutoReverse(false);
        pt1.play();

        Task<Void> t = new Task<>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(4000);
                return null;
            }
        };
        t.setOnSucceeded(workerStateEvent -> {
            try {

                chips.getChildren().remove(w1);
                chips.getChildren().remove(b1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        new Thread(t).start();
    }

    private void removeAnimation(int spielerAnzahl, ArrayList<Integer> playerPoints) {

        if (spielerAnzahl == 2) {
            animation(1180, 870, -120, -250, playerPoints.get(0), 0);// 0 degree
            animation(600, 170, 300, 250, playerPoints.get(1), 1);//180 degree
        }
        if (spielerAnzahl == 3) {
            animation(1180, 870, -120, -250, playerPoints.get(0), 0);
            animation(50, 170, 700, 250, playerPoints.get(1), 1);//3 Spiler links oben
            animation(1150, 170, -120, 250, playerPoints.get(2), 2); // 3 Spieler rechts oben
        }
        if (spielerAnzahl == 4) {
            animation(1180, 870, -120, -250, playerPoints.get(0), 0);// 0 degree
            animation(210, 800, 600, -200, playerPoints.get(1), 1);// 4 Spieler links
            animation(600, 170, 300, 250, playerPoints.get(2), 2);//180 degree
            animation(1600, 320, -400, 150, playerPoints.get(3), 3);// 4 Spieler rechts

        }
        if (spielerAnzahl == 5) {
            animation(1180, 870, -120, -250, playerPoints.get(0), 0);// 0 degree
            animation(210, 800, 600, -200, playerPoints.get(1), 1);// 4 Spieler links
            animation(50, 170, 700, 250, playerPoints.get(2), 2); // 5 Spiler links oben
            animation(1150, 170, -120, 250, playerPoints.get(3), 3);// 5 Spieler rechts oben
            animation(1600, 320, -400, 150, playerPoints.get(4), 4);// 4 Spieler rechts
        }
        if (spielerAnzahl == 6) {
            animation(1180, 870, -120, -250, playerPoints.get(0), 0);// 0 degree
            animation(210, 800, 600, -200, playerPoints.get(1), 1);// 4 Spieler links
            animation(50, 110, 700, 250, playerPoints.get(2), 2); // 5 Spiler links oben
            animation(600, 170, 300, 250, playerPoints.get(3), 3);//180 degree
            animation(1150, 110, -120, 250, playerPoints.get(4), 4);// 5 Spieler rechts oben
            animation(1600, 320, -400, 150, playerPoints.get(5), 5);// 4 Spieler rechts
        }
    }

    public Brettmitte getMitte() {
        return this.mitte;
    }


    public void newPlayer(int newClientID) throws RemoteException {
        client.getSpieler();
        int toUpdate = newClientID - clientId;
        spielerListe.get(toUpdate).setLabel(playerList.get(toUpdate).getName());

    }
}
