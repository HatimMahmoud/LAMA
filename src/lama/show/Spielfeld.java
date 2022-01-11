package lama.show;

import javafx.animation.PathTransition;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
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
import lama.logic.Paar;
import lama.logic.Player;
import lama.logic.Scoreboard;
import lama.logic.Spiellogik;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;



public class Spielfeld extends Grid {
    private Spiellogik spielLogik;
    private int spielerAnzahl;
    private final List<PlayerGrid> spielerListe = new ArrayList<>();
    private ArrayList<Player> playerList;
    private ArrayList<Label> labelList;
    private Brettmitte mitte;
    private final Button tipp = new Button("Tipp zeigen");
    private final Button closetipp = new Button("Tipp schließen");
    private final Tooltip tt1 = new Tooltip("Tipp: Die Karte, die ein graues Pop-Up Fenster hat, kann nicht abgelegt werden!");
    private final Tooltip tt2 = new Tooltip("Wenn du schlau bist,brauchst du keinen Tipp mehr!");
    private Button btn1;
    private boolean zeigTipp = false;
    private boolean isBot;
    private Scoreboard scBoard;
    private Grid boardnutzer;
    private String name;
    private Chatfenster chatfenster;
    private IosSwitch checkBox1;
    private IosSwitch checkBox2;
    private IosSwitch checkBox3;
    private IosSwitch checkBox4;
    private ArrayList<ArrayList> allEnemyCards;
    private boolean autoSortToggle=true;
    private boolean tippToggle=true;
    Button sortButton = new Button();
    Button sortButtonDsec = new Button();


    public  Spielfeld(int sa,boolean isBot, int mode, String name,ArrayList<ArrayList> allEnemyCards,int[] dif) throws RemoteException {
        this.spielLogik = new Spiellogik(sa, isBot, this, mode,dif);
        this.spielerAnzahl = sa;
        this.isBot = isBot;
        this.name = name;
        this.allEnemyCards = allEnemyCards;
        playerList = spielLogik.getSpielerListe();
        this.btn1 = new Button("start");
        init();
    }

    public  Spielfeld(int sa,boolean isBot, int mode, int rounds, String name,ArrayList<ArrayList> allEnemyCards,int[] dif) throws RemoteException {
        this.spielLogik = new Spiellogik(sa, isBot, this, mode, rounds,dif);
        playerList = spielLogik.getSpielerListe();
        this.spielerAnzahl = sa;
        this.isBot = isBot;
        this.name = name;
        this.allEnemyCards = allEnemyCards;
        this.btn1 = new Button("start");
        init();
    }

    public Spielfeld(int sa,ArrayList<ArrayList> allEnemyCards){
        this.spielerAnzahl = sa;
        this.isBot = false;
        this.allEnemyCards = allEnemyCards;
        this.btn1 = new Button("start");
        init();
    }
    //default-Konstruktor für Testzwecke
    public Spielfeld() {
    }
    private void init() {
        createGrid(1800, 1000);
        mitte = new Brettmitte(spielLogik.getAblage());
        add(mitte, 800,500, 300, 80);
        back();
        placement();
        scoreboard();
        tipinit();
        sort();
        chatinit();
        gearbox();
        newRound();
        spielerListe.get(0).getWithdraw().setOnAction(actionEvent -> {
            try {
                if(spielLogik.withdraw(0)) spielLogik.skip();
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
            Integer curVal = Integer.parseInt(db.getString());
            if(possible(curVal,mitte.getTop().getValue())){
                try {
                    spielLogik.putCard(curVal,0);
                    mitte.update(spielLogik.getAblage());
                    spielLogik.skip();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void back()  {
        try {
            setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama\\src\\lama\\textures\\holz.jpg"))), CornerRadii.EMPTY, Insets.EMPTY)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void gearbox() {
            boardnutzer = new Grid();
            labelList = new ArrayList<>();
            int c = 1;
            Button open = new Button();
            Button close = new Button();
            try {
                close.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama/src/lama/Icons/gear-on.png"))),CornerRadii.EMPTY,Insets.EMPTY)));
                open.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama/src/lama/Icons/gear.png"))),CornerRadii.EMPTY,Insets.EMPTY)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            open.setPrefSize(40,40);
            close.setPrefSize(40,40);
            boardnutzer.setBackground(new Background(new BackgroundFill(new Color(0.0,0.0,0.0,0.4),CornerRadii.EMPTY,Insets.EMPTY)));
            boardnutzer.setDisable(true);
            boardnutzer.setVisible(false);
            close.setDisable(true);
            close.setVisible(false);
            open.setOnAction(actionEvent ->{
                open.setDisable(true);
                open.setVisible(false);
                close.setDisable(false);
                close.setVisible(true);
                boardnutzer.setDisable(false);
                boardnutzer.setVisible(true);
            });
            close.setOnAction(actionEvent -> {
                open.setDisable(false);
                open.setVisible(true);
                close.setDisable(true);
                close.setVisible(false);
                boardnutzer.setDisable(true);
                boardnutzer.setVisible(false);
            });
            this.add(open,1725, 100, 75,75);
            this.add(close,1725,100,75,75);
            this.add(boardnutzer,1550,10,250,90);

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
        Label l2= new Label("Tipps");
        l2.setStyle("-fx-font-scale: 12;-fx-text-fill: white");
        boardnutzer.add(checkBox1,240,150,100,100);
        boardnutzer.add(l,1725,150,100,100);
        boardnutzer.add(checkBox2,240,250,100,100);
        boardnutzer.add(l1,1725,250,100,100);
        boardnutzer.add(checkBox3,240,350,100,100);
        boardnutzer.add(l2,1725,350,100,100);

        checkBox1.setOnMouseClicked(actionEvent -> {
            if (checkBox1.isSelected()) {
                chatfenster.setFilter(true);
            }
            else {
                chatfenster.setFilter(false);
            }
        });
        checkBox2.setOnMouseClicked(actionEvent -> {
            if (checkBox2.isSelected()) {
                sortButton.setVisible(false);
                sortButtonDsec.setVisible(true);
            }
            else {
                sortButton.setVisible(false);
                sortButtonDsec.setVisible(false);
            }
        });

        checkBox3.setOnMouseClicked(actionEvent -> {
            if (!checkBox3.isSelected()) {
                tipp.setVisible(false);
                closetipp.setVisible(false);
            }
            else {
                tipp.setVisible(true);
                closetipp.setVisible(false);
            }
        });


        // this.add(checkBox3,1100,840,75,75);
        // this.add(checkBox4,1100,700,75,75);
    }

    /***
     * Initialisierung einer Liste von Spielern gemäß der Spielerzahl, Verteilung auf das Brett
     * @throws FileNotFoundException Falls die Chip-Bilder für Spieler nicht gefunden werden
     */

    private void placement() {
        ArrayList<Player> playerList = spielLogik.getSpielerListe();
        int c = 1;

        Label userName = new Label(name);
        add(userName ,850,700,300,100);
        userName.getStylesheets().add(getClass().getResource("gui.css").toExternalForm());
        userName.setStyle("-fx-font-weight: bold;-fx-font-size: 20px");

        switch (spielerAnzahl) {
            case 2:
                spielerListe.add(new PlayerGrid(0,playerList.get(0),name,allEnemyCards));
                spielerListe.add(new PlayerGrid(180,playerList.get(1),"Bot 1",allEnemyCards));

                //Set animation Coords
                spielerListe.get(0).setAll(1180,870,-120,-250);
                spielerListe.get(1).setAll(600,170,300,250);

                //set remove animation
                spielerListe.get(0).setRemove(100,200);
                spielerListe.get(1).setRemove(-230,-230);


                //Add to the Grid
                add(spielerListe.get(0),700, 800, 670, 150);
                add(spielerListe.get(1),600, 150, 580, 170);

                break;
            case 3:
                this.spielerListe.add(new PlayerGrid(0, playerList.get(0),name,allEnemyCards));
                for (int i = 1; i <spielerAnzahl ; i++) {
                    spielerListe.add(new PlayerGrid(180,playerList.get(i),"Bot "+ i,allEnemyCards));
                }
                //Set animation Coords
                spielerListe.get(0).setAll(1180,870,-120,-250);
                spielerListe.get(1).setAll(50,170,700,250);
                spielerListe.get(2).setAll(1150,170,-120,250);

                //set remove animation
                spielerListe.get(0).setRemove(100,200);
                spielerListe.get(1).setRemove(-680,-250);
                spielerListe.get(2).setRemove(100,-250);

                //Add to the Grid
                add(spielerListe.get(0),700, 800, 670, 150);
                add(spielerListe.get(1),50, 150, 580, 170);

                add(spielerListe.get(2),1150, 150, 580, 170);

                break;
            case 4:
                this.spielerListe.add(new PlayerGrid(0,playerList.get(0),name,allEnemyCards));
                for (int i = 1; i < spielerAnzahl; i++) {
                    this.spielerListe.add(new PlayerGrid(i * 90,playerList.get(i),"Bot "+i,allEnemyCards));
                }

                //Set animation Coords
                spielerListe.get(0).setAll(1180,870,-120,-250);
                spielerListe.get(1).setAll(210,800,600,-200);
                spielerListe.get(2).setAll(600,170,300,250);
                spielerListe.get(3).setAll(1600,320,-400,150);

                //set remove animation
                spielerListe.get(0).setRemove(100,200);
                spielerListe.get(1).setRemove(-550,200);
                spielerListe.get(2).setRemove(-230,-230);
                spielerListe.get(3).setRemove(400,-150);

                //Add to the Grid
                add(spielerListe.get(0),700, 800, 670, 150);
                add(spielerListe.get(1),200, 300, 170, 580);


                add(spielerListe.get(2),600, 150, 580, 170);

                add(spielerListe.get(3),1550, 300, 170, 500);


                break;
            case 5:
                this.spielerListe.add(new PlayerGrid(0,playerList.get(0),name,allEnemyCards));
                for (int i = 1; i <spielerAnzahl -1; i++) {
                    if (i == 3) {
                        this.spielerListe.add(new PlayerGrid(180,playerList.get(c),"Bot "+c,allEnemyCards));
                        c++;
                    }
                    this.spielerListe.add(new PlayerGrid(i*90,playerList.get(c),"Bot "+c,allEnemyCards));
                    c++;
                }
                //Set animation Coords
                spielerListe.get(0).setAll(1180,870,-120,-250);
                spielerListe.get(1).setAll(210,800,600,-200);
                spielerListe.get(2).setAll(50,170,700,250);
                spielerListe.get(3).setAll(1150,170,-120,250);
                spielerListe.get(4).setAll(1600,320,-400,150);


                //set remove animation
                spielerListe.get(0).setRemove(100,200);
                spielerListe.get(1).setRemove(-550,200);
                spielerListe.get(2).setRemove(-680,-250);
                spielerListe.get(3).setRemove(100,-280);
                spielerListe.get(4).setRemove(400,-150);


                //Add to the Grid
                add(spielerListe.get(0),700, 800, 670, 150);
                add(spielerListe.get(1),200, 300, 170, 580);
                add(spielerListe.get(2),50, 150, 580, 170);
                add(spielerListe.get(3),1150, 150, 580, 170);
                add(spielerListe.get(4),1550, 300, 170, 500);



                break;
            case 6:
                this.spielerListe.add(new PlayerGrid(0,playerList.get(0),name,allEnemyCards));
                for (int i = 1; i <spielerAnzahl-2 ; i++) {
                    if (i == 2 || i == 3) {
                        this.spielerListe.add(new PlayerGrid(180,playerList.get(c),"Bot "+c,allEnemyCards));
                        System.out.println("Player " + c + " created");
                        c++;

                    }
                    this.spielerListe.add(new PlayerGrid(i*90,playerList.get(c),"Bot "+c,allEnemyCards));
                    System.out.println("Player " + c + " created");
                    c++;
                }

                //Set animation Coords
                spielerListe.get(0).setAll(1180,870,-120,-250);
                spielerListe.get(1).setAll(210,800,600,-200);
                spielerListe.get(2).setAll(50,110,700,250);
                spielerListe.get(3).setAll(600,170,300,250);
                spielerListe.get(4).setAll(1150,110,-120,250);
                spielerListe.get(5).setAll(1600,320,-400,150);

                //set remove animation
                spielerListe.get(0).setRemove(100,200);
                spielerListe.get(1).setRemove(-550,200);
                spielerListe.get(2).setRemove(-650,-280);
                spielerListe.get(3).setRemove(-230,-230);
                spielerListe.get(4).setRemove(100,-250);
                spielerListe.get(5).setRemove(400,-150);

                //Add to the Grid
                add(spielerListe.get(0),700, 800, 670, 150);
                add(spielerListe.get(1),200, 300, 170, 580);
                add(spielerListe.get(2),50, 100, 580, 170);
                add(spielerListe.get(3),600, 150, 580, 170);
                add(spielerListe.get(4),1150, 100, 500, 170);
                add(spielerListe.get(5),1550, 300, 170, 500);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + spielerAnzahl);
        }
    }

    private void scoreboard(){
        scBoard = new Scoreboard(playerList);
        PriorityQueue<Paar> scoreList = scBoard.getScoreList();
        board = new Grid();
        labelList = new ArrayList<>();
        int c = 1;
        for (Paar p: scoreList){
            Label l = new Label(" "+p.getName()+"   "+p.getPoints());
            labelList.add(l);
            l.setFont(new Font("Comic Sans Ms",16));
            l.setTextFill(Paint.valueOf("White"));
            boardnutzer.add(l,0,c*30,290,30);
            c++;
        }

        Button open = new Button();
        Button close = new Button();
        try {
            close.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama/src/lama/Icons/Close_Arrow.png"))),CornerRadii.EMPTY,Insets.EMPTY)));
            open.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama/src/lama/Icons/Open_Arrow.png"))),CornerRadii.EMPTY,Insets.EMPTY)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        open.setPrefSize(100,100);
        close.setPrefSize(100,100);
        boardnutzer.setBackground(new Background(new BackgroundFill(new Color(0.0,0.0,0.0,0.4),CornerRadii.EMPTY,Insets.EMPTY)));
        boardnutzer.setDisable(true);
        boardnutzer.setVisible(false);
        close.setDisable(true);
        close.setVisible(false);
        open.setOnAction(actionEvent ->{
            open.setDisable(true);
            open.setVisible(false);
            close.setDisable(false);
            close.setVisible(true);
            boardnutzer.setDisable(false);
            boardnutzer.setVisible(true);
        });
        close.setOnAction(actionEvent -> {
            open.setDisable(false);
            open.setVisible(true);
            close.setDisable(true);
            close.setVisible(false);
            boardnutzer.setDisable(true);
            boardnutzer.setVisible(false);
        });


        this.add(open,1725,900,75,75);
        this.add(close,1470,900,75,75);
        this.add(boardnutzer,1550,840+(6-c)*30,250,c*30);
    }

    private void scoreboardUpdate(){
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
        tt1.setShowDelay(new Duration(0.0));
        tipp.setTooltip(tt1);
        closetipp.setVisible(false);
        closetipp.setDisable(true);
        tt2.setShowDelay(new Duration(0.0));
        closetipp.setTooltip(tt2);
        tipp.setOnAction(actionEvent -> {
            try {
                tipp.setDisable(true);
                tipp.setVisible(false);
                closetipp.setDisable(false);
                closetipp.setVisible(true);
                zeigTipp = true;
                if (spielerListe.get(0).getVisibility()) {
                    updatePlayer(0);}
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
                    updatePlayer(0);}
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        this.add(tipp, 1200, 800, 140, 35);
        this.add(closetipp, 1200, 800, 150, 35);
    }

    private void sort() {
        if (autoSortToggle) {

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
    }
    private void chatinit() {
        chatfenster = new Chatfenster(spielerListe.get(0).getP(),0,"All Chat");
        Button open = new Button();
        Button close = new Button();

        try {
            open.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama/src/lama/Icons/messenger.png"))),CornerRadii.EMPTY,Insets.EMPTY)));
            close.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama/src/lama/Icons/messenger_close.png"))),CornerRadii.EMPTY,Insets.EMPTY)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        chatfenster.setVisible(false);
        chatfenster.setDisable(true);
        close.setVisible(false);
        close.setDisable(true);

        open.setOnAction(actionEvent ->{
            if(spielerAnzahl>3) {
                this.getChildren().remove(spielerListe.get(1));
                add(spielerListe.get(1), 500, 300, 170, 580);
            }
            open.setVisible(false);
            open.setDisable(true);
            close.setVisible(true);
            close.setDisable(false);
            chatfenster.setVisible(true);
            chatfenster.setDisable(false);
        });

        close.setOnAction(actionEvent -> {
            if(spielerAnzahl>3) {
                this.getChildren().remove(spielerListe.get(1));
                add(spielerListe.get(1), 200, 300, 170, 580);
            }
            open.setVisible(true);
            open.setDisable(false);
            close.setVisible(false);
            close.setDisable(true);
            chatfenster.setVisible(false);
            chatfenster.setDisable(true);
        });
        this.add(open,400,900,75,100);
        this.add(close,400,900,75,100);
        this.add(chatfenster,50,525,230,370);
    }

    public void newRound() {
        for (PlayerGrid p : spielerListe) {
            p.setCards(p.getP().getValues());
            p.drawHand();
        }
        btn1.setPrefSize(200,35);
        add(btn1,800,400, 200, 35);
        btn1.setStyle("-fx-font-size: 17px; -fx-border-radius: 4; -fx-border-width: 4 ; -fx-border-color: darkblue;-fx-background-radius: 6; -fx-background-color: LightBlue;");
        btn1.setOnAction((Action)->{
            mitte.getTop().setFront();
            if(!isBot) {
                for (int i = 0; i < spielerAnzahl; i++) {
                    spielerListe.get(i).setPlayer();
                }
            }
            spielerListe.get(0).setPlayer();
            try {
                spielLogik.start();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            getChildren().remove(btn1);
        });


        for (PlayerGrid p : spielerListe){
            p.drawHand();
        }
        mitte.update(spielLogik.getAblage());
        mitte.getTop().setBack();

        mitte.getButton().setOnAction(actionEvent ->{
            if (!isBot) {
                int id = spielLogik.getCurrent();
                Integer c = null;
                try {
                    c = spielLogik.drawCard(id);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                if (c!=null){
                    try {
                        spielLogik.skip();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    activateHand(id);
                }
            }
            if (isBot) {
                int id = spielLogik.getCurrent();
                if (id == 0) {
                    Integer c = null;
                    try {
                        c = spielLogik.drawCard(id);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    if (c!=null){
                        try {
                            spielLogik.skip();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        activateHand(id);

                    }
                }
            }
        });

    }


    public void animation(int x,int y,int m , int n, int count ,int points){
        int white = points%10;
        int black = points/10;
        Grid chips = new Grid();
        WhiteChip w1 = new WhiteChip();
        w1.setText(Integer.toString(white));
        BlackChip b1 = new BlackChip();
        b1.setText(Integer.toString(black));
        chips.setPrefSize(100, 110);
        chips.createGrid(100, 110);
        add(chips, x, y, 100, 100);
        if (count == 0){
            chips.add(w1,20,22, 35, 35);
        }
        if (count == 1){
            chips.add(b1,70, 22, 35, 35);
        }
        if (count == 2) {
            chips.add(w1, 20, 22, 35, 35);
            chips.add(b1, 70, 22, 35, 35);
        }
        movement(m, n, chips, w1, b1);
    }
    public void remove_animation(int m,int n,int points ){
        Grid chips = new Grid();
        WhiteChip w1 = new WhiteChip();
        BlackChip b1 = new BlackChip();
        chips.setPrefSize(100, 110);
        chips.createGrid(100, 110);
        add(chips,1020,520 , 100, 100);
        if(points==10){
            chips.add(b1, 70, 22, 35, 35);
        }
        else if(points==1){
            chips.add(w1, 20, 22, 35, 35);
        }
        movement(m, n, chips, w1, b1);
    }

    private void movement(int m, int n, Grid chips, WhiteChip w1, BlackChip b1) {
        Path path=new Path();
        path.getElements().add(new MoveTo(m,n));
        path.getElements().add(new CubicCurveTo(0, 0, 0, 0, 0, 0));
        PathTransition pt=new PathTransition();
        PathTransition pt1=new PathTransition();
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

    private boolean possible(Integer curVal, int value) {
        if (curVal == value){
            return true;
        }
        if(value== 7 && curVal == 1){
            return true;
        }
        return value + 1 == curVal;
    }


    public void activateHand(int id)  {
        spielerListe.get(id).setCards(spielerListe.get(id).getP().getValues());
        if (spielerListe.get(id).getVisibility()) {
            for (Card c : spielerListe.get(id).getCards()) {
                int mm = spielLogik.getAblage().top();
                if (spielerListe.get(id).getVisibility()) {
                    if (c.getValue() != mm
                            && (c.getValue() != mm + 1)) {
                        c.getGray();
                    }
                }
                c.setOnAction(actionEvent -> {
                    try {
                        if (spielLogik.putCard(c.getValue(), id)) {
                            mitte.update(spielLogik.getAblage());
                            spielLogik.skip();

                        }
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


    public void rundenEnde(boolean ended) {
        scoreboardUpdate();
        for (PlayerGrid p : spielerListe) {
            p.setCards(p.getP().getValues());
            p.drawHand();
            p.update();
        }
        if (!ended){
            Button btn2 = new Button("Nächste Runde starten?");
            btn2.setPrefSize(200,35);
            btn2.setStyle("-fx-font-size: 17px; -fx-border-radius: 4; -fx-border-width: 4 ; -fx-border-color: darkblue;-fx-background-radius: 6; -fx-background-color: LightBlue;");
            add(btn2,800,400, 200, 35);
            btn2.setOnAction((Action)->{
                getChildren().remove(btn2);
                try {
                    spielLogik.newRound();
                    newRound();
                } catch ( RemoteException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private Integer faded;
    public void playerFade(int id){
        Grid highlight;
        if (id == 0){
            highlight = spielerListe.get(id).getCardHand();
        }
        else{
            highlight = spielerListe.get(id);
        }

        if (faded == null) {
            faded = id;
            spielerListe.get(id).fade();
            highlight.setStyle("-fx-border-radius: 20;-fx-border-width: 6;-fx-border-color: darkred;-fx-background-radius: 22;-fx-background-color: red");
        }
        else{
            spielerListe.get(faded).setStyle("-fx-background-color: transparent;-fx-border-color: transparent");
            if (faded == 0) {
                spielerListe.get(faded).getCardHand().setStyle("-fx-background-color: transparent;-fx-border-color: transparent");
            }
            faded = id;
            spielerListe.get(id).fade();
            highlight.setStyle("-fx-border-radius: 20;-fx-border-width: 6;-fx-border-color: darkred;-fx-background-radius: 22;-fx-background-color: red");

        }
    }

    public void updatePlayer(int id)  {
        if (id == 0) {
            if (spielerListe.get(0).getVisibility() && !zeigTipp) {
                spielerListe.get(id).drawHand();
                for (int q = 0; q < spielerListe.get(0).getCards().size(); q++) {
                    spielerListe.get(0).getCards().get(q).setFront();
                }
            } else if (spielerListe.get(0).getVisibility() && zeigTipp) {
                spielerListe.get(id).drawHand();
                for (int q = 0; q < spielerListe.get(0).getCards().size(); q++) {
                    if (spielerListe.get(0).getCards().get(q).getValue() == mitte.getTop().getValue()) {
                        spielerListe.get(0).getCards().get(q).setFront();
                    } else if (spielerListe.get(0).getCards().get(q).getValue() == mitte.getTop().getValue() + 1) {
                        spielerListe.get(0).getCards().get(q).setFront();
                    } else if (spielerListe.get(0).getCards().get(q).getValue() == 1 && mitte.getTop().getValue() == 7) {
                        spielerListe.get(0).getCards().get(q).setFront();
                    } else {
                        spielerListe.get(0).getCards().get(q).getGray();
                    }
                }
            }
        }
        else spielerListe.get(id).drawHand();
    }



    public List<PlayerGrid> getPlayer(){
        return spielerListe;
    }
    public Brettmitte getMitte(){
        return this.mitte;
    }


}
