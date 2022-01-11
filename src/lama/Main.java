package lama;

import com.sun.javafx.application.HostServicesDelegate;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import javafx.util.Duration;
import lama.show.Spielfeld;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

/***
 * Hauptklasse des Projektes, startet das Spiel
 */

public class Main extends Application {

    //Einfacher Zugriff auf die Sichtbarkeit des Grids
    private Spielfeld board;
    private Scene entry,choose,botCount,serverConfig,botDif,modeSelect,join;
    private Pane entryPane;
    private Pane chosePane;
    private Pane botCountPane;
    private Pane serverConfigPane;
    private Pane modeSelectPane;
    private Pane joinPane;
    private Background bckCard1 ,bckCard2,bckCard3,bckCard4,bckCard5,bckCard6,bckBack,bckArrow,bckHost,bckJoin,backDif1,backDif2,backDif3;
    private Button singlePlayer, multiPlayer,rules;
    private Button hostGame, joinGame;
    private Button oneBot, twoBot, threeBot, fourBot, fiveBot, next;
    private Button onePlayer, twoPlayer, threePlayer, fourPlayer, fivePlayer, sixPlayer, oneBotServer, twoBotServer, threeBotServer, fourBotServer, fiveBotServer, sixBotServer, nextServer, resetPlayer, resetBot;
    private Button startJoin;
    private Button modeOne, modeTwo, modeThree, start;
    private Label welcome;
    private Label hostL, joinL;
    private Label countL, nameL;
    private Label nameLServer, ipLServer,countBotL,countPlayerL;
    private Label nameLJoin,ipLJoin;
    private Label modeL;
    private TextField nameTf;
    private TextField ipTfServer, nameTfServer;
    private TextField ipTfJoin, nameTfJoin;
    private TextField rounds;
    private boolean isPlayer = false;
    private boolean isHost;
    private int countPlayer,countBots,mode=0;
    private String ip,roundText;
    private String name;
    private ArrayList<ArrayList> allEnemyCards;
    private int[] botDifs;
    private final Duration delay = new Duration (0.0);
    private final Duration visible = new Duration(15000.0);

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.centerOnScreen();
        primaryStage.setOnCloseRequest(windowEvent -> Runtime.getRuntime().exit(0));
        Image stageIcon = new Image(new FileInputStream("Lama/src/lama/Icons/llama.png"));
        allEnemyCards = new ArrayList<>();
        loadimages();
        loadBackgrounds();
        entryInit(primaryStage);
        choseInit(primaryStage);
        botCountInit(primaryStage);
        serverConfigInit(primaryStage);
        joinInit(primaryStage);
        modeSelectInit(primaryStage);

        primaryStage.setMaxHeight(1000);
        primaryStage.setMinHeight(300);
        primaryStage.setMaxWidth(1800);
        primaryStage.setMinWidth(500);

        primaryStage.getIcons().add(stageIcon);
        primaryStage.setTitle("L.A.M.A");
        primaryStage.setScene(entry);
        primaryStage.show();
    }

    private void loadimages() {
        Task<Void> imageLoader = new Task<>(){
            @Override
            protected Void call() throws Exception {
                ArrayList<ArrayList> enemyLeft = new ArrayList<>();
                ArrayList<ArrayList> enemyUpper = new ArrayList<>();
                ArrayList<ArrayList> enemyRight = new ArrayList<>();
                ArrayList<Background> enemyColorLeft = new ArrayList<>();
                ArrayList<Background> enemyGreyLeft = new ArrayList<>();
                ArrayList<Background> enemyColorUpper = new ArrayList<>();
                ArrayList<Background> enemyGreyUpper = new ArrayList<>();
                ArrayList<Background> enemyColorRight = new ArrayList<>();
                ArrayList<Background> enemyGreyRight = new ArrayList<>();
                ArrayList<Background> empty = new ArrayList<>();
                for (int i = 0; i <3 ; i++) {
                    for (int j = 0; j <2 ; j++) {
                        for (int k = 0; k <8 ; k++) {
                            if (i == 0 ) {
                                if (j== 0) {
                                    Background b = new Background(new BackgroundImage(new Image(new FileInputStream("Lama/src/lama/Background/Left/Enemy_" + (k + 1) + "_left.png"),220,480,true,false),BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,new BackgroundSize(500,500,true,true,true,false)));
                                    enemyColorLeft.add(b);
                                }
                                else{
                                    Background b = new Background(new BackgroundImage(new Image(new FileInputStream("Lama/src/lama/Background/Left/Enemy_" + (k + 1) + "_withdraw_left.png"),220,480,true,false),BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,new BackgroundSize(500,500,true,true,true,false)));
                                    enemyGreyLeft.add(b);
                                }
                            }
                            else if(i ==1 ){
                                if (j== 0) {
                                    Background b = new Background(new BackgroundImage(new Image(new FileInputStream("Lama/src/lama/Background/Upper/Enemy_" + (k + 1) + "_upper.png"),220,480,true,false),BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,new BackgroundSize(500,500,true,true,true,false)));
                                    enemyColorUpper.add(b);
                                }
                                else{
                                    Background b = new Background(new BackgroundImage(new Image(new FileInputStream("Lama/src/lama/Background/Upper/Enemy_" + (k + 1) + "_withdraw_upper.png"),220,480,true,false),BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,new BackgroundSize(500,500,true,true,true,false)));
                                    enemyGreyUpper.add(b);
                                }
                            }
                            else {
                                if (j== 0) {
                                    Background b = new Background(new BackgroundImage(new Image(new FileInputStream("Lama/src/lama/Background/Right/Enemy_" + (k + 1) + "_right.png"),220,480,true,false),BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,new BackgroundSize(500,500,true,true,true,false)));
                                    enemyColorRight.add(b);
                                }
                                else{
                                    Background b = new Background(new BackgroundImage(new Image(new FileInputStream("Lama/src/lama/Background/Right/Enemy_" + (k + 1) + "_withdraw_right.png"),220,480,true,false),BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,new BackgroundSize(500,500,true,true,true,false)));
                                    enemyGreyRight.add(b);
                                }
                            }
                        }
                    }
                }
                Background b = new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama/src/lama/Background/empty.png"))),CornerRadii.EMPTY,Insets.EMPTY));
                empty.add(b);
                enemyLeft.add(enemyColorLeft);
                enemyLeft.add(enemyGreyLeft);
                enemyUpper.add(enemyColorUpper);
                enemyUpper.add(enemyGreyUpper);
                enemyRight.add(enemyColorRight);
                enemyRight.add(enemyGreyRight);
                allEnemyCards.add(enemyLeft);
                allEnemyCards.add(enemyUpper);
                allEnemyCards.add(enemyRight);
                allEnemyCards.add(empty);
                System.err.println("LOADING COMPLETE");
                return null;
            }
        };
        new Thread(imageLoader).start();
    }

    private void loadBackgrounds() {
        try {
         bckCard1 = new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama\\src\\lama\\Lama_Cards\\Card_1.png"))), CornerRadii.EMPTY, Insets.EMPTY));
         bckCard2 = new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama\\src\\lama\\Lama_Cards\\Card_2.png"))), CornerRadii.EMPTY, Insets.EMPTY));
         bckCard3 = new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama\\src\\lama\\Lama_Cards\\Card_3.png"))), CornerRadii.EMPTY, Insets.EMPTY));
         bckCard4 = new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama\\src\\lama\\Lama_Cards\\Card_4.png"))), CornerRadii.EMPTY, Insets.EMPTY));
         bckCard5 = new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama\\src\\lama\\Lama_Cards\\Card_5.png"))), CornerRadii.EMPTY, Insets.EMPTY));
         bckCard6 = new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama\\src\\lama\\Lama_Cards\\Card_6.png"))), CornerRadii.EMPTY, Insets.EMPTY));
         bckBack = new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama\\src\\lama\\textures\\holz.jpg"))), CornerRadii.EMPTY, Insets.EMPTY));
         bckArrow = new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama\\src\\lama\\Icons\\Zurück_Pfeil.png"))), CornerRadii.EMPTY, Insets.EMPTY));
         bckHost = new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama\\src\\lama\\Icons\\Host.png"))), CornerRadii.EMPTY, Insets.EMPTY));
         bckJoin = new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama\\src\\lama\\Icons\\Join.png"))), CornerRadii.EMPTY, Insets.EMPTY));
         backDif1 = new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama/src/lama/Icons/dif1.png"))), CornerRadii.EMPTY, Insets.EMPTY));
         backDif2 = new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama/src/lama/Icons/dif2.png"))), CornerRadii.EMPTY, Insets.EMPTY));
         backDif3 = new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama/src/lama/Icons/dif3.png"))), CornerRadii.EMPTY, Insets.EMPTY));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void entryInit(Stage primaryStage) {
        entryPane = new Pane();
        entryPane.setBackground(bckBack);
        entry = new Scene(entryPane,500,300);
        entry.getStylesheets().add(getClass().getResource("gui.css").toExternalForm());

        //Labels
        welcome = new Label("       Wilkommen zu Lama       \n      was möchtest du tun?       ");

        //Buttons
        singlePlayer = new Button("Einzelspieler");
        multiPlayer = new Button("Mehrspieler");
        rules = new Button("Regeln");

        singlePlayer.getStyleClass().add("textButton");
        multiPlayer.getStyleClass().add("textButton");
        rules.getStyleClass().add("textButton");

        singlePlayer.setOnAction(actionEvent -> {
            primaryStage.setScene(botCount);
            primaryStage.centerOnScreen();
            primaryStage.show();
        });
        multiPlayer.setOnAction(actionEvent -> {
            primaryStage.setScene(choose);
            primaryStage.centerOnScreen();
            primaryStage.show();
        });
        rules.setOnAction(actionEvent -> {
            try {
                Desktop.getDesktop().browse(new URI("https://www.brettspiele-magazin.de/lama/"));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }

        });

        scaleEntryPane();
        entryPane.heightProperty().addListener((observableValue, number, t1) -> scaleEntryPane());
        entryPane.widthProperty().addListener((observableValue, number, t1) -> scaleEntryPane());


        entryPane.getChildren().addAll(singlePlayer,multiPlayer,rules,welcome);
    }

    private void choseInit(Stage primaryStage) {
        chosePane = new Pane();
        chosePane.setBackground(bckBack);

        choose = new Scene(chosePane,700,300);
        choose.getStylesheets().add(getClass().getResource("gui.css").toExternalForm());

        //Label initalisieren
        hostL = new Label("Hoste ein Spiel");
        joinL = new Label("Joine einem Spiel");

        //Tooltips initalisieren
        Duration d = new Duration(0.0);
        Tooltip at2 = new Tooltip("Hoste ein Spiel");
        Tooltip at3 = new Tooltip("Joine einem Spiel");
        at2.setShowDelay(d);
        at3.setShowDelay(d);

        //Buttons bearbeiten
        hostGame = new Button();
        joinGame = new Button();

        hostGame.setBackground(bckHost);
        joinGame.setBackground(bckJoin);

        setSize(hostGame,150,150);
        setSize(joinGame,150,150);

        hostGame.setTooltip(at2);
        joinGame.setTooltip(at3);

        //Button funktionen
        hostGame.setOnAction(actionEvent -> {
            isPlayer = false;
            primaryStage.setScene(serverConfig);
            primaryStage.centerOnScreen();
            primaryStage.show();
        });
        joinGame.setOnAction(actionEvent -> {
            isPlayer = false;
            primaryStage.setScene(join);
            primaryStage.centerOnScreen();
            primaryStage.show();
        });

        //Resizen
        scaleChoosePane();

        chosePane.widthProperty().addListener((observableValue, number, t1) -> scaleChoosePane());
        chosePane.heightProperty().addListener((observableValue, number, t1) -> scaleChoosePane());

        //Back Button
        Button back = new Button();
        back.setMinSize(50,50);
        back.relocate(10,10);
        back.setBackground(bckArrow);
        back.setOnAction(actionEvent -> {
            primaryStage.setScene(entry);
            primaryStage.centerOnScreen();
            primaryStage.show();
        });

        //Der Pane hinzufügen
        chosePane.getChildren().addAll(back, hostGame, joinGame, hostL, joinL);
    }

    private void botCountInit(Stage primaryStage) {
        botCountPane = new Pane();
        botCountPane.setBackground(bckBack);

        botCount = new Scene(botCountPane, 600,400);
        botCount.getStylesheets().add(getClass().getResource("gui.css").toExternalForm());


        //Label
        countL = new Label("Mit wie vielen Gegnern möchtest du spielen?");
        nameL = new Label("Wie willst du heißen?");

        //Textfield
        nameTf = new TextField();
        nameTf.setPromptText("Name");

        //Tooltips
        Tooltip bt1 = new Tooltip("Spiele gegen einen Gegner");
        Tooltip bt2 = new Tooltip("Spiele gegen zwei Gegner");
        Tooltip bt3 = new Tooltip("Spiele gegen drei Gegner");
        Tooltip bt4 = new Tooltip("Spiele gegen vier Gegner");
        Tooltip bt5 = new Tooltip("Spiele gegen fünf Gegner");
        bt1.setShowDelay(new Duration(0.0));
        bt2.setShowDelay(new Duration(0.0));
        bt3.setShowDelay(new Duration(0.0));
        bt4.setShowDelay(new Duration(0.0));
        bt5.setShowDelay(new Duration(0.0));

        //Buttons
        oneBot = new Button();
        twoBot = new Button();
        threeBot = new Button();
        fourBot = new Button();
        fiveBot = new Button();
        next = new Button("Weiter");

        oneBot.setBackground(bckCard1);
        twoBot.setBackground(bckCard2);
        threeBot.setBackground(bckCard3);
        fourBot.setBackground(bckCard4);
        fiveBot.setBackground(bckCard5);
        next.getStyleClass().add("textButton");


        setSize(oneBot,60,80);
        setSize(twoBot,60,80);
        setSize(threeBot,60,80);
        setSize(fourBot,60,80);
        setSize(fiveBot,60,80);

        oneBot.setTooltip(bt1);
        twoBot.setTooltip(bt2);
        threeBot.setTooltip(bt3);
        fourBot.setTooltip(bt4);
        fiveBot.setTooltip(bt5);

        oneBot.setOnAction(actionEvent -> {
            countBots = 1;
            oneBot.setStyle("-fx-border-color: red;-fx-border-width: 4" );
            twoBot.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            threeBot.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fourBot.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fiveBot.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
        });

        twoBot.setOnAction(actionEvent -> {
            countBots = 2;
            oneBot.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            twoBot.setStyle("-fx-border-color: red;-fx-border-width: 4" );
            threeBot.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fourBot.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fiveBot.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
        });

        threeBot.setOnAction(actionEvent -> {
            countBots = 3;
            oneBot.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            twoBot.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            threeBot.setStyle("-fx-border-color: red;-fx-border-width: 4" );
            fourBot.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fiveBot.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
        });

        fourBot.setOnAction(actionEvent -> {
            countBots = 4;
            oneBot.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            twoBot.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            threeBot.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fourBot.setStyle("-fx-border-color: red;-fx-border-width: 4" );
            fiveBot.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
        });

        fiveBot.setOnAction(actionEvent -> {
            countBots = 5;
            oneBot.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            twoBot.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            threeBot.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fourBot.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fiveBot.setStyle("-fx-border-color: red;-fx-border-width: 4" );
        });

        next.setOnAction(actionEvent -> {
            name = nameTf.getText();
            if (countBots== 0){
                GuiHelper.showErrorOrWarningAlert(Alert.AlertType.WARNING,"Ohne Gegner kein Spiel","Du brauchst mindestens einen Gegner","Klicke auf eine um die Anzahl der Spieler festzulegen");

            }
            else if (name.isEmpty()||name.isBlank()){
                GuiHelper.showErrorOrWarningAlert(Alert.AlertType.WARNING,"Ohne Name kein Spieler","Du brauchst einen Namen","Einfach schnell eingeben!");

            }
            else {
                isHost = false;
                isPlayer= true;
                name = nameTf.getText();
                botDifInit(primaryStage);
                primaryStage.setScene(botDif);
                primaryStage.centerOnScreen();
                primaryStage.show();
            }
        });

        //Skalieren
        scaleCountPane();
        botCountPane.heightProperty().addListener((observableValue, number, t1) -> scaleCountPane());
        botCountPane.widthProperty().addListener((observableValue, number, t1) -> scaleCountPane());

        Button back = new Button();
        back.setBackground(bckArrow);
        setSize(back,50,50);
        back.relocate(10,10);
        back.setOnAction(actionEvent -> {
            nameTf.clear();
            countBots = 0;
            oneBot.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            twoBot.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            threeBot.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fourBot.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fiveBot.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            primaryStage.setScene(entry);
            primaryStage.centerOnScreen();
            primaryStage.show();
        });

        //Hinzufügen
        botCountPane.getChildren().addAll(oneBot, twoBot, threeBot, fourBot, fiveBot, next, countL, nameL, nameTf, back);
    }

    private void serverConfigInit(Stage primaryStage) {
        serverConfigPane = new Pane();
        serverConfigPane.setBackground(bckBack);
        serverConfig = new Scene(serverConfigPane,900,600);
        serverConfig.getStylesheets().add(getClass().getResource("gui.css").toExternalForm());
        //Server Config Scene

        //Label wird initalisiert
        ipLServer = new Label("Bitte gebe hier deine Ip von Hamachi ein!");
        countPlayerL = new Label("Bitte wähle die Anzahl der menschlichen Spieler!");
        countBotL = new Label("Bitte Wähle die Anzahl der Bots!");
        nameLServer = new Label("Wie willst du heißen?");


        //Buttons initalisieren
        onePlayer = new Button();
        twoPlayer = new Button();
        threePlayer = new Button();
        fourPlayer = new Button();
        fivePlayer = new Button();
        sixPlayer = new Button();
        oneBotServer = new Button();
        twoBotServer = new Button();
        threeBotServer = new Button();
        fourBotServer = new Button();
        fiveBotServer = new Button();
        fourBotServer = new Button();
        fiveBotServer = new Button();
        sixBotServer = new Button();
        nextServer = new Button("Weiter");
        resetPlayer = new Button("Reset Player");
        resetBot = new Button("Reset Bots");

        nextServer.getStyleClass().add("textButton");
        resetBot.getStyleClass().add("textButton");
        resetPlayer.getStyleClass().add("textButton");


        //Textfield initialisieren
        ipTfServer = new TextField();
        ipTfServer.setPromptText("Ip");
        nameTfServer = new TextField();
        nameTfServer.setPromptText("Name");

        //Hintergründe an die Buttons binden
        onePlayer.setBackground(bckCard1);
        twoPlayer.setBackground(bckCard2);
        threePlayer.setBackground(bckCard3);
        fourPlayer.setBackground(bckCard4);
        fivePlayer.setBackground(bckCard5);
        sixPlayer.setBackground(bckCard6);
        oneBotServer.setBackground(bckCard1);
        twoBotServer.setBackground(bckCard2);
        threeBotServer.setBackground(bckCard3);
        fourBotServer.setBackground(bckCard4);
        fiveBotServer.setBackground(bckCard5);
        sixBotServer.setBackground(bckCard6);


        //Größe der Buttosn setzten
        setSize(onePlayer,60,80);
        setSize(twoPlayer,60,80);
        setSize(threePlayer,60,80);
        setSize(fourPlayer,60,80);
        setSize(fivePlayer,60,80);
        setSize(sixPlayer,60,80);
        setSize(oneBotServer,60,80);
        setSize(twoBotServer,60,80);
        setSize(threeBotServer,60,80);
        setSize(fourBotServer,60,80);
        setSize(fiveBotServer,60,80);
        setSize(sixBotServer,60,80);

        //Den Buttons ihre Funktion geben

        onePlayer.setOnAction(actionEvent -> {
            countPlayer = 1;
            onePlayer.setStyle("-fx-border-color: red;-fx-border-width: 4" );
            twoPlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            threePlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fourPlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fivePlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            sixPlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            oneBotServer.setDisable(false);
            twoBotServer.setDisable(false);
            threeBotServer.setDisable(false);
            fourBotServer.setDisable(false);
            fiveBotServer.setDisable(false);
            sixBotServer.setDisable(true);
            oneBotServer.setOpacity(1);
            twoBotServer.setOpacity(1);
            threeBotServer.setOpacity(1);
            fourBotServer.setOpacity(1);
            fiveBotServer.setOpacity(1);
            sixBotServer.setOpacity(0.5);

        });
        twoPlayer.setOnAction(actionEvent -> {
            countPlayer = 2;
            onePlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            twoPlayer.setStyle("-fx-border-color: red;-fx-border-width: 4" );
            threePlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fourPlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fivePlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            sixPlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            oneBotServer.setDisable(false);
            twoBotServer.setDisable(false);
            threeBotServer.setDisable(false);
            fourBotServer.setDisable(false);
            fiveBotServer.setDisable(true);
            sixBotServer.setDisable(true);
            oneBotServer.setOpacity(1);
            twoBotServer.setOpacity(1);
            threeBotServer.setOpacity(1);
            fourBotServer.setOpacity(1);
            fiveBotServer.setOpacity(0.5);
            sixBotServer.setOpacity(0.5);
        });
        threePlayer.setOnAction(actionEvent -> {
            countPlayer = 3;
            onePlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            twoPlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            threePlayer.setStyle("-fx-border-color: red;-fx-border-width: 4" );
            fourPlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fivePlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            sixPlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            oneBotServer.setDisable(false);
            twoBotServer.setDisable(false);
            threeBotServer.setDisable(false);
            fourBotServer.setDisable(true);
            fiveBotServer.setDisable(true);
            sixBotServer.setDisable(true);
            oneBotServer.setOpacity(1);
            twoBotServer.setOpacity(1);
            threeBotServer.setOpacity(1);
            fourBotServer.setOpacity(0.5);
            fiveBotServer.setOpacity(0.5);
            sixBotServer.setOpacity(0.5);

        });
        fourPlayer.setOnAction(actionEvent -> {
            countPlayer = 4;
            onePlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            twoPlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            threePlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fourPlayer.setStyle("-fx-border-color: red;-fx-border-width: 4" );
            fivePlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            sixPlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            oneBotServer.setDisable(false);
            twoBotServer.setDisable(false);
            threeBotServer.setDisable(true);
            fourBotServer.setDisable(true);
            fiveBotServer.setDisable(true);
            sixBotServer.setDisable(true);
            oneBotServer.setOpacity(1);
            twoBotServer.setOpacity(1);
            threeBotServer.setOpacity(0.5);
            fourBotServer.setOpacity(0.5);
            fiveBotServer.setOpacity(0.5);
            sixBotServer.setOpacity(0.5);
        });
        fivePlayer.setOnAction(actionEvent -> {
            countPlayer = 5;
            onePlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            twoPlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            threePlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fourPlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fivePlayer.setStyle("-fx-border-color: red;-fx-border-width: 4" );
            sixPlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            oneBotServer.setDisable(false);
            twoBotServer.setDisable(true);
            threeBotServer.setDisable(true);
            fourBotServer.setDisable(true);
            fiveBotServer.setDisable(true);
            sixBotServer.setDisable(true);
            oneBotServer.setOpacity(1);
            twoBotServer.setOpacity(0.5);
            threeBotServer.setOpacity(0.5);
            fourBotServer.setOpacity(0.5);
            fiveBotServer.setOpacity(0.5);
            sixBotServer.setOpacity(0.5);
        });
        sixPlayer.setOnAction(actionEvent -> {
            countPlayer = 6;
            onePlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            twoPlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            threePlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fourPlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fivePlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            sixPlayer.setStyle("-fx-border-color: red;-fx-border-width: 4" );
            oneBotServer.setDisable(true);
            twoBotServer.setDisable(true);
            threeBotServer.setDisable(true);
            fourBotServer.setDisable(true);
            fiveBotServer.setDisable(true);
            sixBotServer.setDisable(true);
            oneBotServer.setOpacity(0.5);
            twoBotServer.setOpacity(0.5);
            threeBotServer.setOpacity(0.5);
            fourBotServer.setOpacity(0.5);
            fiveBotServer.setOpacity(0.5);
            sixBotServer.setOpacity(0.5);
        });
        oneBotServer.setOnAction(actionEvent -> {
            countBots = 1;
            oneBotServer.setStyle("-fx-border-color: red;-fx-border-width: 4" );
            twoBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            threeBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fourBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fiveBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            sixBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            onePlayer.setDisable(false);
            twoPlayer.setDisable(false);
            threePlayer.setDisable(false);
            fourPlayer.setDisable(false);
            fivePlayer.setDisable(false);
            sixPlayer.setDisable(true);
            onePlayer.setOpacity(1);
            twoPlayer.setOpacity(1);
            threePlayer.setOpacity(1);
            fourPlayer.setOpacity(1);
            fivePlayer.setOpacity(1);
            sixPlayer.setOpacity(0.5);
        });
        twoBotServer.setOnAction(actionEvent -> {
            countBots = 2;
            oneBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            twoBotServer.setStyle("-fx-border-color: red;-fx-border-width: 4" );
            threeBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fourBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fiveBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            sixBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            onePlayer.setDisable(false);
            twoPlayer.setDisable(false);
            threePlayer.setDisable(false);
            fourPlayer.setDisable(false);
            fivePlayer.setDisable(true);
            sixPlayer.setDisable(true);
            onePlayer.setOpacity(1);
            twoPlayer.setOpacity(1);
            threePlayer.setOpacity(1);
            fourPlayer.setOpacity(1);
            fivePlayer.setOpacity(0.5);
            sixPlayer.setOpacity(0.5);
        });
        threeBotServer.setOnAction(actionEvent -> {
            countBots = 3;
            oneBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            twoBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            threeBotServer.setStyle("-fx-border-color: red;-fx-border-width: 4" );
            fourBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fiveBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            sixBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            onePlayer.setDisable(false);
            twoPlayer.setDisable(false);
            threePlayer.setDisable(false);
            fourPlayer.setDisable(true);
            fivePlayer.setDisable(true);
            sixPlayer.setDisable(true);
            onePlayer.setOpacity(1);
            twoPlayer.setOpacity(1);
            threePlayer.setOpacity(1);
            fourPlayer.setOpacity(0.5);
            fivePlayer.setOpacity(0.5);
            sixPlayer.setOpacity(0.5);
        });
        fourBotServer.setOnAction(actionEvent -> {
            countBots = 4;
            oneBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            twoBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            threeBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fourBotServer.setStyle("-fx-border-color: red;-fx-border-width: 4" );
            fiveBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            sixBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            onePlayer.setDisable(false);
            twoPlayer.setDisable(false);
            threePlayer.setDisable(true);
            fourPlayer.setDisable(true);
            fivePlayer.setDisable(true);
            sixPlayer.setDisable(true);
            onePlayer.setOpacity(1);
            twoPlayer.setOpacity(1);
            threePlayer.setOpacity(0.5);
            fourPlayer.setOpacity(0.5);
            fivePlayer.setOpacity(0.5);
            sixPlayer.setOpacity(0.5);
        });
        fiveBotServer.setOnAction(actionEvent -> {
            countBots = 5;
            oneBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            twoBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            threeBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fourBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fiveBotServer.setStyle("-fx-border-color: red;-fx-border-width: 4" );
            sixBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            onePlayer.setDisable(false);
            twoPlayer.setDisable(true);
            threePlayer.setDisable(true);
            fourPlayer.setDisable(true);
            fivePlayer.setDisable(true);
            sixPlayer.setDisable(true);
            onePlayer.setOpacity(1);
            twoPlayer.setOpacity(0.5);
            threePlayer.setOpacity(0.5);
            fourPlayer.setOpacity(0.5);
            fivePlayer.setOpacity(0.5);
            sixPlayer.setOpacity(0.5);
        });
        sixBotServer.setOnAction(actionEvent -> {
            countBots = 6;
            oneBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            twoBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            threeBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fourBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fiveBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            sixBotServer.setStyle("-fx-border-color: red;-fx-border-width: 4" );
            onePlayer.setDisable(true);
            twoPlayer.setDisable(true);
            threePlayer.setDisable(true);
            fourPlayer.setDisable(true);
            fivePlayer.setDisable(true);
            sixPlayer.setDisable(true);
            onePlayer.setOpacity(0.5);
            twoPlayer.setOpacity(0.5);
            threePlayer.setOpacity(0.5);
            fourPlayer.setOpacity(0.5);
            fivePlayer.setOpacity(0.5);
            sixPlayer.setOpacity(0.5);

        });
        nextServer.setOnAction(actionEvent -> {
            ip = ipTfServer.getText();
            name = nameTfServer.getText();
            if(countPlayer == 0){
                if (countBots == 0){
                    GuiHelper.showErrorOrWarningAlert(Alert.AlertType.WARNING,"Du benötigst Spieler!","Bitte wähle mindestens 2 Spieler aus!","Du brauchst Spieler!");
                }
                if (countBots >= 1){
                    GuiHelper.showErrorOrWarningAlert(Alert.AlertType.WARNING,"Du hast nur Bots ausgewählt!","Das macht doch keinen Spaß!","Du brauchst mindestens einen Spieler!");
                }
            }
            else if (countBots == 0&&countPlayer<2){
                GuiHelper.showErrorOrWarningAlert(Alert.AlertType.WARNING,"Du benötigst mehr Spieler!","Bitte wähle mindestens 2 Spieler aus!","Alleine kann man doch nicht spielen ;)");
            }
            else if (name.isEmpty()||name.isBlank()){
                GuiHelper.showErrorOrWarningAlert(Alert.AlertType.WARNING,"Ohne Name kein Spieler","Du brauchst einen Namen","Einfach schnell eingeben!");
            }
            else if (ip.isEmpty()|| ip.isBlank()){
                GuiHelper.showErrorOrWarningAlert(Alert.AlertType.WARNING,"Ohne Ip kein Server","Du brauchst eine Ip","Einfach schnell eingeben!");
            }
            else {
                isHost = true;
                name = nameTfServer.getText();
                if(countBots!=0){
                    botDifInit(primaryStage);
                    primaryStage.setScene(botDif);
                    primaryStage.centerOnScreen();
                    primaryStage.show();
                }
                else {
                    primaryStage.setScene(modeSelect);
                    primaryStage.centerOnScreen();
                    primaryStage.show();
                }
            }
        });

        resetPlayer.setOnAction(actionEvent -> {
            countPlayer = 0;
            onePlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            twoPlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            threePlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fourPlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fivePlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            sixPlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            oneBotServer.setDisable(false);
            twoBotServer.setDisable(false);
            threeBotServer.setDisable(false);
            fourBotServer.setDisable(false);
            fiveBotServer.setDisable(false);
            sixBotServer.setDisable(false);
            oneBotServer.setOpacity(1);
            twoBotServer.setOpacity(1);
            threeBotServer.setOpacity(1);
            fourBotServer.setOpacity(1);
            fiveBotServer.setOpacity(1);
            sixBotServer.setOpacity(1);
        });

        resetBot.setOnAction(actionEvent -> {
            countBots = 0;
            oneBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            twoBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            threeBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fourBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fiveBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            sixBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            onePlayer.setDisable(false);
            twoPlayer.setDisable(false);
            threePlayer.setDisable(false);
            fourPlayer.setDisable(false);
            fivePlayer.setDisable(false);
            sixPlayer.setDisable(false);
            onePlayer.setOpacity(1);
            twoPlayer.setOpacity(1);
            threePlayer.setOpacity(1);
            fourPlayer.setOpacity(1);
            fivePlayer.setOpacity(1);
            sixPlayer.setOpacity(1);
        });
        scaleServerConfigPane();
        //Resizen
        serverConfigPane.widthProperty().addListener((observableValue, number, t1) -> scaleServerConfigPane());
        serverConfigPane.heightProperty().addListener((observableValue, number, t1) -> scaleServerConfigPane());

        //ZurückButton
        Button back = new Button();
        back.setMinSize(50,50);
        back.relocate(10,10);
        back.setBackground(bckArrow);
        back.setOnAction(actionEvent -> {
            countBots = 0;
            countPlayer = 0;
            onePlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            twoPlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            threePlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fourPlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fivePlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            sixPlayer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            oneBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            twoBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            threeBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fourBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            fiveBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            sixBotServer.setStyle("-fx-border-color: transparent;-fx-border-width: 0" );
            onePlayer.setDisable(false);
            twoPlayer.setDisable(false);
            threePlayer.setDisable(false);
            fourPlayer.setDisable(false);
            fivePlayer.setDisable(false);
            sixPlayer.setDisable(false);
            oneBotServer.setDisable(false);
            twoBotServer.setDisable(false);
            threeBotServer.setDisable(false);
            fourBotServer.setDisable(false);
            fiveBotServer.setDisable(false);
            sixBotServer.setDisable(false);
            onePlayer.setOpacity(1);
            twoPlayer.setOpacity(1);
            threePlayer.setOpacity(1);
            fourPlayer.setOpacity(1);
            fivePlayer.setOpacity(1);
            sixPlayer.setOpacity(1);
            oneBotServer.setOpacity(1);
            twoBotServer.setOpacity(1);
            threeBotServer.setOpacity(1);
            fourBotServer.setOpacity(1);
            fiveBotServer.setOpacity(1);
            sixBotServer.setOpacity(1);
            primaryStage.setScene(choose);
            primaryStage.centerOnScreen();
            primaryStage.show();
        });

        //Adden von allen Dingen
        serverConfigPane.getChildren().addAll(back, onePlayer, twoPlayer, threePlayer, fourPlayer, fivePlayer, sixPlayer, oneBotServer, twoBotServer, threeBotServer, fourBotServer, fiveBotServer, sixBotServer, nextServer, resetPlayer, resetBot, ipLServer, countPlayerL, countBotL,nameLServer, ipTfServer, nameTfServer);
    }

    private void botDifInit(Stage primaryStage){
        Pane botDifPane = new Pane();
        botDifPane.setBackground(bckBack);
        botDif = new Scene(botDifPane,450,200+countBots*100);
        botDif.getStylesheets().add(getClass().getResource("gui.css").toExternalForm());

        Label setBotDif = new Label("Wähle wie stark deine Gegner sein sollen!");

        Button next2 = new Button("Weiter");
        next2.getStyleClass().add("textButton");

        botDifs = new int[countBots];

        for (int i = 0; i <countBots ; i++) {
            Group g = new Group();
            Label l = new Label("Bot "+(i+1));

            Duration d = new Duration(0);

            Tooltip easyTt = new Tooltip("Einfach");
            Tooltip mediumTt = new Tooltip("Normal");
            Tooltip hardTt = new Tooltip("Schwer");

            easyTt.setShowDelay(d);
            mediumTt.setShowDelay(d);
            hardTt.setShowDelay(d);

            Button easy = new Button();
            Button medium = new Button();
            Button hard = new Button();

            easy.setBackground(backDif1);
            medium.setBackground(backDif2);
            hard.setBackground(backDif3);

            setSize(easy,64,64);
            setSize(medium,64,64);
            setSize(hard,64,64);

            easy.setTooltip(easyTt);
            medium.setTooltip(mediumTt);
            hard.setTooltip(hardTt);

            botDifs[i] = 1;
            easy.setStyle("-fx-border-color: red;-fx-border-width: 4");
            medium.setStyle("-fx-border-color: transparent;");
            hard.setStyle("-fx-border-color: transparent;");

            int finalI = i;
            easy.setOnAction(actionEvent -> {
                botDifs[finalI]=1;
                easy.setStyle("-fx-border-color: red;-fx-border-width: 4");
                medium.setStyle("-fx-border-color: transparent;");
                hard.setStyle("-fx-border-color: transparent;");

            });

            medium.setOnAction(actionEvent -> {
                botDifs[finalI]=2;
                easy.setStyle("-fx-border-color: transparent;");
                medium.setStyle("-fx-border-color: red;-fx-border-width: 4");
                hard.setStyle("-fx-border-color: transparent;");
            });

            hard.setOnAction(actionEvent -> {
                botDifs[finalI]=3;
                easy.setStyle("-fx-border-color: transparent;");
                medium.setStyle("-fx-border-color: transparent;");
                hard.setStyle("-fx-border-color: red;-fx-border-width: 4");
            });

            l.relocate(50,150+i*100);
            easy.relocate(150,150+i*100);
            medium.relocate(250,150+i*100);
            hard.relocate(350,150+i*100);
            g.getChildren().addAll(l,easy,medium,hard);
            botDifPane.getChildren().add(g);
        }

        next2.setOnAction(actionEvent ->{
                primaryStage.setScene(modeSelect);
                primaryStage.centerOnScreen();
                primaryStage.show();
        });

        Button back = new Button();
        back.setBackground(bckArrow);
        setSize(back,50,50);
        back.relocate(10,10);
        back.setOnAction(actionEvent ->{
            if(isHost){
                primaryStage.setScene(serverConfig);
                primaryStage.centerOnScreen();
                primaryStage.show();
            }
            else {
                primaryStage.setScene(botCount);
                primaryStage.centerOnScreen();
                primaryStage.show();
            }
        });

        setBotDif.relocate(40,50);
        next2.relocate(210,150+countBots*100);
        botDifPane.getChildren().addAll(setBotDif, next2,back);

    }

    private void joinInit(Stage primaryStage) {
        joinPane = new Pane();
        joinPane.setBackground(bckBack);
        join = new Scene(joinPane,600,400);
        join.getStylesheets().add(getClass().getResource("gui.css").toExternalForm());

        //Label
        ipLJoin = new Label("Ip des Servers auf den du Joinen willst");
        nameLJoin = new Label("Wie willst du heißen?");

        //Textfield
        ipTfJoin = new TextField();
        ipTfJoin.setPromptText("Ip");
        nameTfJoin = new TextField();
        nameTfJoin.setPromptText("Name");

        //Button
        startJoin = new Button("Joine mit dieser Ip");
        startJoin.getStyleClass().add("textButton");
        //Position setzten
        scaleJoinPane();

        joinPane.widthProperty().addListener((observableValue, number, t1) -> scaleJoinPane());

        joinPane.heightProperty().addListener((observableValue, number, t1) -> scaleJoinPane());


        startJoin.setOnAction(actionEvent ->{
            ip = ipTfJoin.getText();
            name = nameTfJoin.getText();

            if (ip.isBlank()||ip.isEmpty()){
                GuiHelper.showErrorOrWarningAlert(Alert.AlertType.WARNING,"Ohne Ip kein Server","Du brauchst eine Ip","Einfach schnell eingeben!");
            }
            else if (name.isEmpty()||name.isBlank()){
                GuiHelper.showErrorOrWarningAlert(Alert.AlertType.WARNING,"Ohne Name kein Spieler","Du brauchst einen Namen","Einfach schnell eingeben!");

            }
            else {
                try {
                    System.setProperty("java.security.policy", "file:./security.policy");
                    Registry registry = LocateRegistry.getRegistry(ip, 1091);
                    System.out.println("Registry vorhanden");
                    ServerIF server = (ServerIF) registry.lookup("Chat");
                    System.out.println("Client läuft");
                    Client cl = new Client(server, name,allEnemyCards);
                    primaryStage.setScene(new Scene(cl.getBoard(), 1280, 720));
                    primaryStage.centerOnScreen();
                    primaryStage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button back = new Button();
        setSize(back,50,50);
        back.setBackground(bckArrow);
        back.relocate(10,10);
        back.setOnAction(actionEvent -> {
            primaryStage.setScene(choose);
            primaryStage.centerOnScreen();
            primaryStage.show();
        });

        joinPane.getChildren().addAll(ipLJoin, nameLJoin, ipTfJoin, nameTfJoin, startJoin, back);
    }

    private void modeSelectInit(Stage primaryStage) {
        modeSelectPane = new Pane();
        modeSelectPane.setBackground(bckBack);
        modeSelect = new Scene(modeSelectPane, 600, 300);
        modeSelect.getStylesheets().add(getClass().getResource("gui.css").toExternalForm());

        //Labels
        modeL = new Label("Wähle den Spielmodus!");

        //Tooltips
        Tooltip et1 = new Tooltip("Spiele eine normale Runde Lama.\nDas Spiel ist vorbei sobald eine Person 40 Punkte hat.\nSobald dies der Fall ist hat der Spieler mit den wenigsten Punkten gewonnen\nund der Spieler mit den meisten verloren ");
        Tooltip et2 = new Tooltip("Spiele so lange wie du lust hast.\nAm Ende von jeder Runde kannst du entscheiden, ob dies die letzte war\noder die nächste gestartet werden soll");
        Tooltip et3 = new Tooltip("Lege eine Rundenanzahl fest.\nSobald alle Runden gespielt wurden, ist das Spiel vorbei.\nGewonnen hat der Spieler mit den wenigsten Punkten und verloren der Spieler mit den meisten.");

        et1.setShowDuration(visible);
        et1.setShowDelay(delay);
        et2.setShowDuration(visible);
        et2.setShowDelay(delay);
        et3.setShowDuration(visible);
        et3.setShowDelay(delay);

        //Textfield
        rounds = new TextField();
        rounds.setPromptText("Rundenanzahl");
        rounds.setDisable(true);

        //Buttons
        modeOne = new Button("Normal");
        modeTwo = new Button("Unendlich");
        modeThree = new Button("X-Runden");
        start = new Button("Start!");

        start.getStyleClass().add("textButton");
        modeOne.getStyleClass().add("textButton");
        modeTwo.getStyleClass().add("textButton");
        modeThree.getStyleClass().add("textButton");

        modeOne.setTooltip(et1);
        modeTwo.setTooltip(et2);
        modeThree.setTooltip(et3);

        modeOne.setStyle("-fx-border-color: red;-fx-border-width: 4" );

        modeOne.setOnAction(actionEvent -> {
            mode = 0;
            rounds.setDisable(true);
            modeOne.setStyle("-fx-border-color: red;-fx-border-width: 4" );
            modeTwo.setStyle("-fx-border-color: darkblue;-fx-border-width: 4" );
            modeThree.setStyle("-fx-border-color: darkblue;-fx-border-width: 4" );
        });

        modeTwo.setOnAction(actionEvent -> {
            mode = 1;
            rounds.setDisable(true);
            modeOne.setStyle("-fx-border-color: darkblue;-fx-border-width: 4"  );
            modeTwo.setStyle("-fx-border-color: red;-fx-border-width: 4" );
            modeThree.setStyle("-fx-border-color: darkblue;-fx-border-width: 4"  );
        });

        modeThree.setOnAction(actionEvent -> {
            mode = 2;
            rounds.setDisable(false);
            modeOne.setStyle("-fx-border-color: darkblue;-fx-border-width: 4"  );
            modeTwo.setStyle("-fx-border-color: darkblue;-fx-border-width: 4"  );
            modeThree.setStyle("-fx-border-color: red;-fx-border-width: 4" );
        });

        start.setOnAction(actionEvent -> {
            int rounds = 0;
            if (mode==2) {
                roundText = this.rounds.getText();
                if(roundText.isEmpty() || roundText.isBlank()){
                    GuiHelper.showErrorOrWarningAlert(Alert.AlertType.WARNING,"Fehlende Rundenzahl!","Fehlende Rundenzahl!","Du musst eingeben, wie viele Runden du spielen willst!");
                    return;
                }
                try {
                    rounds = Integer.parseInt(this.rounds.getText());
                } catch (NumberFormatException e){
                    GuiHelper.showErrorOrWarningAlert(Alert.AlertType.WARNING,"Keine Zahl!","Sehr Lustig ^^","Bitte gib eine Zahl ein!");
                    return;
                }
                if(rounds<=0) {
                    GuiHelper.showErrorOrWarningAlert(Alert.AlertType.WARNING,"Zu kleine Zahl!","Wie soll das gehen?","Bitte gib eine positive Zahl ein!");
                    return;
                }
            }
            if(isHost){
                ServerDriver sd;
                if(mode != 2) {
                    sd = new ServerDriver(countPlayer,countBots,ip, mode,botDifs);
                }
                else {
                    sd = new ServerDriver(countPlayer,countBots,ip, mode, rounds,botDifs);
                }
                try {
                    sd.start();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                try {
                    System.setProperty("java.rmi.server.hostname", ip);
                    System.setProperty("java.security.policy", "file:./security.policy");
                    Registry registry = LocateRegistry.getRegistry(1091);
                    System.err.println("Registry vorhanden");
                    ServerIF server = (ServerIF) registry.lookup("Chat");
                    System.err.println("Client läuft");
                    Client cl = new Client(server,name,allEnemyCards);

                    primaryStage.setScene(new Scene(cl.getBoard(), 1280, 720));
                    primaryStage.centerOnScreen();
                    primaryStage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                if (mode != 2) {
                    try {
                        board = new Spielfeld(1 + countBots, isPlayer, mode,name,allEnemyCards,botDifs);
                        primaryStage.setScene(new Scene(board, 1280, 720));
                        primaryStage.centerOnScreen();
                        primaryStage.show();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        board = new Spielfeld(1 + countBots, isPlayer, mode, rounds,name,allEnemyCards,botDifs);
                        primaryStage.setScene(new Scene(board, 1280, 720));
                        primaryStage.centerOnScreen();
                        primaryStage.show();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //Resizen
        scaleModeSelectPane();
        modeSelectPane.widthProperty().addListener((observableValue, number, t1) -> scaleModeSelectPane());
        modeSelectPane.heightProperty().addListener((observableValue, number, t1) -> scaleModeSelectPane());

        Button back = new Button();
        back.setBackground(bckArrow);
        setSize(back,50,50);
        back.relocate(10,10);
        back.setOnAction(actionEvent -> {
            mode = 0;
            rounds.setDisable(true);
            modeOne.setStyle("-fx-border-color: red;-fx-border-width: 4" );
            modeTwo.setStyle("-fx-border-color: darkblue;-fx-border-width: 4"  );
            modeThree.setStyle("-fx-border-color: darkblue;-fx-border-width: 4"  );
            primaryStage.setScene(botDif);
            primaryStage.centerOnScreen();
            primaryStage.show();
        });
        //Alles der Pane hinzufügen
        modeSelectPane.getChildren().addAll(modeOne, modeTwo, modeThree, start, back, rounds, modeL);

    }

    private void scaleEntryPane() {
        welcome.relocate(entryPane.getWidth()/2-143,entryPane.getHeight()/9-20);
        singlePlayer.relocate(entryPane.getWidth()/2-55,3*entryPane.getHeight()/9);
        multiPlayer.relocate(entryPane.getWidth()/2-53,5*entryPane.getHeight()/9);
        rules.relocate(entryPane.getWidth()/2-35,7*entryPane.getHeight()/9);
    }

    private void scaleModeSelectPane() {
        modeL.relocate(modeSelectPane.getWidth()/2-100,modeSelectPane.getHeight()/7);
        modeOne.relocate(modeSelectPane.getWidth()/7,3*modeSelectPane.getHeight()/7);
        modeTwo.relocate(3*(modeSelectPane.getWidth()/7),3*modeSelectPane.getHeight()/7);
        modeThree.relocate(5*(modeSelectPane.getWidth()/7),3*modeSelectPane.getHeight()/7);
        rounds.relocate(5*(modeSelectPane.getWidth()/7)-30,5*modeSelectPane.getHeight()/7-30);
        start.relocate(modeSelectPane.getWidth()/2-45,6*modeSelectPane.getHeight()/7);
    }

    private void scaleCountPane() {
        countL.relocate(botCountPane.getWidth()/2-210,5* botCountPane.getHeight()/11);
        nameL.relocate(botCountPane.getWidth()/2-98, botCountPane.getHeight()/11);
        oneBot.relocate(botCountPane.getWidth()/11,7* botCountPane.getHeight()/11);
        twoBot.relocate(3* botCountPane.getWidth()/11,7* botCountPane.getHeight()/11);
        threeBot.relocate(5* botCountPane.getWidth()/11,7* botCountPane.getHeight()/11);
        fourBot.relocate(7* botCountPane.getWidth()/11,7* botCountPane.getHeight()/11);
        fiveBot.relocate(9* botCountPane.getWidth()/11,7* botCountPane.getHeight()/11);
        next.relocate(5* botCountPane.getWidth()/11-20,9* botCountPane.getHeight()/11+20);
        nameTf.relocate(botCountPane.getWidth()/2-85,3* botCountPane.getHeight()/11);
    }

    private void scaleJoinPane() {
        ipLJoin.relocate(joinPane.getWidth()/2-168, joinPane.getHeight()/11);
        nameLJoin.relocate(joinPane.getWidth()/2-98,5* joinPane.getHeight()/11);
        ipTfJoin.relocate(joinPane.getWidth()/2-85,3* joinPane.getHeight()/11);
        nameTfJoin.relocate(joinPane.getWidth()/2-85,7* joinPane.getHeight()/11);
        startJoin.relocate(joinPane.getWidth()/2-80,9* joinPane.getHeight()/11);
    }

    private void scaleChoosePane() {
        hostL.relocate(1*chosePane.getWidth()/5,chosePane.getHeight()/5-40);
        joinL.relocate(3*chosePane.getWidth()/5-10,chosePane.getHeight()/5-40);
        hostGame.relocate(1*(chosePane.getWidth()/5),chosePane.getHeight()/3);
        joinGame.relocate(3*(chosePane.getWidth()/5),chosePane.getHeight()/3);
    }

    private void scaleServerConfigPane() {
        onePlayer.relocate(1*serverConfigPane.getWidth()/15,7*serverConfigPane.getHeight()/15);
        twoPlayer.relocate(3*serverConfigPane.getWidth()/15,7*serverConfigPane.getHeight()/15);
        threePlayer.relocate(5*serverConfigPane.getWidth()/15,7*serverConfigPane.getHeight()/15);
        fourPlayer.relocate(7*serverConfigPane.getWidth()/15,7*serverConfigPane.getHeight()/15);
        fivePlayer.relocate(9*serverConfigPane.getWidth()/15,7*serverConfigPane.getHeight()/15);
        sixPlayer.relocate(11*serverConfigPane.getWidth()/15,7*serverConfigPane.getHeight()/15);
        oneBotServer.relocate(1*serverConfigPane.getWidth()/15,11*serverConfigPane.getHeight()/15);
        twoBotServer.relocate(3*serverConfigPane.getWidth()/15,11*serverConfigPane.getHeight()/15);
        threeBotServer.relocate(5*serverConfigPane.getWidth()/15,11*serverConfigPane.getHeight()/15);
        fourBotServer.relocate(7*serverConfigPane.getWidth()/15,11*serverConfigPane.getHeight()/15);
        fiveBotServer.relocate(9*serverConfigPane.getWidth()/15,11*serverConfigPane.getHeight()/15);
        sixBotServer.relocate(11*serverConfigPane.getWidth()/15,11*serverConfigPane.getHeight()/15);
        nextServer.relocate(serverConfigPane.getWidth()/2-40,13*serverConfigPane.getHeight()/15+15);
        resetPlayer.relocate(13*serverConfigPane.getWidth()/15,7*serverConfigPane.getHeight()/15);
        resetBot.relocate(13*serverConfigPane.getWidth()/15,11*serverConfigPane.getHeight()/15);
        ipLServer.relocate(3*serverConfigPane.getWidth()/4-182,1*serverConfigPane.getHeight()/15);
        countPlayerL.relocate(serverConfigPane.getWidth()/2-240,5*serverConfigPane.getHeight()/15);
        countBotL.relocate(serverConfigPane.getWidth()/2-155,9*serverConfigPane.getHeight()/15+15);
        nameLServer.relocate(serverConfigPane.getWidth()/4-98,serverConfigPane.getHeight()/15);
        ipTfServer.relocate(3*serverConfigPane.getWidth()/4-85,3*serverConfigPane.getHeight()/15);
        nameTfServer.relocate(serverConfigPane.getWidth()/4-85,3*serverConfigPane.getHeight()/15);
    }

    private void setSize(Button b,int x,int y) {
        b.setMinSize(x,y);
        b.setMaxSize(x,y);
    }

    public static void main(String[] args) {
        launch(args);
    }
}