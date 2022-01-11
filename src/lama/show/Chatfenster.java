package lama.show;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import lama.Client;
import lama.logic.LeftMessage;
import lama.logic.Player;
import lama.logic.RightMessage;

import javax.print.DocFlavor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Chatfenster extends TitledPane {

    private final int clientID;
    private final ArrayList<String> badword = new ArrayList<>();
    private final HashMap<String, byte[]> emojiList;
    private ScrollPane scrollPane;
    private VBox vBox;
    private Player player;
    private Client client;
    private TextArea textArea;
    private Button send;
    private Integer partnerID;
    private boolean filter = false;


    public Chatfenster(Player player, int clientID, String s) {
        getStylesheets().add(getClass().getResource("gui.css").toExternalForm());
        this.player = player;
        this.clientID = clientID;
        this.setText(s);
        this.emojiList = new HashMap<>();
        fillMap();
        fillBadword();
        setMinSize(240, 370);
        setMaxSize(240, 370);
        init();
    }

    public Chatfenster(Client client, int clientID, String s) {
        getStylesheets().add(getClass().getResource("gui.css").toExternalForm());
        this.client = client;
        this.clientID = clientID;
        this.setText(s);
        this.emojiList = new HashMap<>();
        fillMap();
        fillBadword();
        try {
            this.player = client.getSpieler().get(clientID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        setMinSize(240, 370);
        setMaxSize(240, 370);
        init();
    }

    public Chatfenster(Client client, int clientID, String s, int partnerID) {
        getStylesheets().add(getClass().getResource("gui.css").toExternalForm());
        this.client = client;
        this.clientID = clientID;
        this.setText(s);
        this.partnerID = partnerID;
        this.emojiList = new HashMap<>();
        fillMap();
        fillBadword();
        try {
            this.player = client.getSpieler().get(clientID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        setMinSize(240, 370);
        setMaxSize(240, 370);
        init();
    }

    private void fillBadword() {
        badword.add("penis");
        badword.add("vollidiot");
        badword.add("wichser");
        badword.add("sack");
        badword.add("spast");
        badword.add("nutte");
        badword.add("scheiße");
        badword.add("hurensohn");
        badword.add("flachpfeife");
        badword.add("nichtsnutz");
        badword.add("fuck");
        badword.add("shit");
        badword.add("arschloch");
        badword.add("vollidiot");
        badword.add("vollpfosten");
        badword.add("arsch");
        badword.add("schwein");
        badword.add("fettsack");
        badword.add("fotze");
        badword.add("schlampe");
        badword.add("bitch");
        badword.add("stinkstiefel");
        badword.add("schlammblut");
        badword.add("schleimer");
        badword.add("lappen");
        badword.add("frettchen");
        badword.add("peniskopf");
        badword.add("analpilot");
    }

    private void init() {
        Pane helper = new Pane();
        scrollPane = new ScrollPane();
        vBox = new VBox();
        textArea = new TextArea();
        send = new Button();

        scrollPane.relocate(10, 10);
        scrollPane.setMinSize(220, 270);
        scrollPane.setMaxSize(220, 270);
        scrollPane.setVvalue(1.0);
        scrollPane.getStyleClass().add("scroll-pane");

        helper.setBackground(new Background(new BackgroundFill(new Color(0, 0, 0, 0.4), CornerRadii.EMPTY, Insets.EMPTY)));
        this.getStyleClass().add("titled-pane");

        vBox.relocate(20, 20);
        vBox.setMinSize(200, 270);
        vBox.setMaxWidth(210);
        vBox.setSpacing(13.0);
        vBox.heightProperty().addListener((ChangeListener<? super Number>) (observable, oldvalue, newValue) -> scrollPane.setVvalue((Double) newValue));

        textArea.setMinSize(150, 50);
        textArea.setMaxSize(150, 50);
        textArea.relocate(10, 290);
        textArea.setWrapText(true);
        textArea.setOpacity(0.7);
        textArea.setPromptText("Type in a text ...");
        send.relocate(190, 290);
        send.setPrefSize(25, 25);
        try {
            send.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(new FileInputStream("Lama/src/lama/Icons/right-arrow.png"))), CornerRadii.EMPTY, Insets.EMPTY)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        send.setOnAction(actionEvent -> {
            try {
                sendMessage(textArea.getText());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            textArea.clear();
        });
        scrollPane.setContent(vBox);
        helper.getChildren().addAll(textArea, send, scrollPane);
        this.setContent(helper);
    }

    private void sendMessage(String s) throws RemoteException {
        if (s.equals("")) {
            return;
        }
        s = stringformat(s);
        s = scanforEmoji(s);

        if (s.contains("/")) {
            s = chatAction(s);
        }
        String out= s;
        if(filter) {
            s = badwordfilter(s);
        }
        RightMessage l = new RightMessage(s, player);
        vBox.getChildren().add(l);
        if (client != null) {
            if (this.partnerID == null) {
                client.sendMessage(clientID, out);
            } else {
                client.sendPrivateMessage(clientID, out, partnerID);
                //System.out.println("sending from " + clientID + "message " + out + "To " + partnerID);
            }
        }
    }


    public String badwordfilter(String s) {
        String transformedS = s;
        transformedS = transformedS.replaceAll("3", "e");
        transformedS = transformedS.replaceAll("1", "i");
        transformedS = transformedS.replaceAll("!", "i");
        transformedS = transformedS.replaceAll("4", "a");
        transformedS = transformedS.replaceAll("@", "a");
        transformedS = transformedS.replaceAll("5", "s");
        transformedS = transformedS.replaceAll("7", "t");
        transformedS = transformedS.replaceAll("0", "o");
        transformedS = transformedS.replaceAll("9", "g");
        transformedS = transformedS.toLowerCase();
        String[] helper = transformedS.split(" ");
        String[] newString = s.split(" ");
        for (int i = 0; i < helper.length; i++) {
            if (badword.contains(helper[i])) {
                newString[i] = newString[i].replaceAll(".", "*");
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < newString.length; i++) {
            sb.append(newString[i] + " ");
        }
        String out = sb.toString();
        return out;
    }

    public String chatAction(String s) {
        String[] strings = s.split("/");
        for (int i = 0; i < strings.length; i++) {
            if (strings[i].contains("roll")) {
                String helper = "";
                String[] shelper = strings[i].split("roll");
                if (shelper.length > 0) {
                    for (int j = 0; j < shelper.length; j++) {
                        shelper[j] = shelper[j].replaceAll("[a-zA-Z]|(\\s)", "");
                    }
                    helper = shelper[1];
                }

                int max = 6;
                if (!helper.isEmpty()) {
                    try {
                        max = Integer.parseInt(helper);
                    } catch (Exception e) {
                        max = Integer.MAX_VALUE;
                    }
                }
                Random rand = new Random();
                int random = rand.nextInt(max);
                StringBuilder sB = new StringBuilder();
                sB.append(s + "\n");
                sB.append("You rolled:" + random);
                s = sB.toString();
            }
            if (strings[i].contains("coinflip")) {
                Random rand = new Random();
                int helper = rand.nextInt(2);
                StringBuilder sB = new StringBuilder();
                sB.append(s + "\n");
                if (helper == 1) {
                    sB.append("The Coin shows Head");
                } else if (helper == 0) {
                    sB.append("The Coin shows Tails");
                }
                s = sB.toString();
            }
        }
        return s;
    }

    public String stringformat(String s) {
        String[] strings = s.split("\n");
        int rowbreak = 0;
        ArrayList<Integer> helper = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            if (strings[i].isEmpty() || strings[i].isBlank()) {
                if (rowbreak >= 1) {
                    helper.add(i);
                } else {
                    rowbreak++;
                }
            } else {
                rowbreak = 0;
            }
        }
        String str = "";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            if (!helper.contains(i)) {
                if (i == strings.length - 1) {
                    builder.append(strings[i]);
                } else {
                    builder.append(strings[i] + "\n");
                }
            }
        }
        String out = builder.toString();
        return out;
    }

    public void recieveMessage(String s, int sender) {

        if (!client.getBoard().getMutedClients().contains(sender)) {
            try {
                Player p = client.getSpieler().get(sender);
                if(filter) {
                   s = badwordfilter(s);
                }
                LeftMessage l = new LeftMessage(s, p);
                vBox.getChildren().add(l);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public int getTargedID() {
        return this.partnerID;
    }

    private String scanforEmoji(String s) {
        Pattern p = Pattern.compile(":(\\w*):");
        Matcher m = p.matcher(s);
        if (m.find()) {
            int start = m.start();
            int end = m.end();
            try {
                s = s.substring(0, start) + new String(emojiList.get(m.group(1)), StandardCharsets.UTF_8) + s.substring(end);
            } catch (Exception e) {
            }
            s = scanforEmoji(s);
        }
        return s;
    }

    //TODO Fill the Map
    private void fillMap() {
        emojiList.put("smile", new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x81});
        emojiList.put("joy", new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x82});
        emojiList.put("rage", new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0xA1});
        emojiList.put("innocent", new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x87});
        emojiList.put("sweat_smile", new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x93});
        emojiList.put("wink", new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x89});
        emojiList.put("cross", new byte[]{(byte) 0xE2, (byte) 0x9D, (byte) 0x8C});
    }

    public void setFilter(boolean filter) {
        this.filter = filter;
    }
}
