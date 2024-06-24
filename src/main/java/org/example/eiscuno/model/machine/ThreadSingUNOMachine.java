package org.example.eiscuno.model.machine;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.eiscuno.model.card.ICard;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class represents a thread that monitors the player's cards and handles
 * the situation where the player has only one card left but does not call "UNO".
 */
public class ThreadSingUNOMachine extends Thread{
    private ImageView idImagenMessage;
    private volatile boolean running = true;
    private ArrayList<ICard> cardsPlayer;
    private ArrayList<ICard> cardsMachine;
    private Player humanPlayer;
    private Player machinePlayer;
    private GameUno gameUno;
    private Button unoButton;
    private volatile boolean hasActived;
    private ThreadPlayMachine threadPlayMachine;

    /**
     * Constructor to initialize the thread with necessary game components.
     *
     * @param cardsPlayer the list of cards held by the human player
     * @param cardsMachine the list of cards held by the machine player
     * @param machinePlayer the machine player object
     * @param humanPlayer the human player object
     * @param gameUno the game logic controller
     */
    public ThreadSingUNOMachine(ArrayList<ICard> cardsPlayer,ArrayList<ICard> cardsMachine, Player machinePlayer, Player humanPlayer, GameUno gameUno, Button unoButton,ImageView idImagenMessage){
        this.idImagenMessage =  idImagenMessage;
        this.cardsPlayer = cardsPlayer;
        this.cardsMachine =cardsMachine;
        this.machinePlayer = machinePlayer;
        this.humanPlayer=humanPlayer;
        this.gameUno= gameUno;
        this.unoButton = unoButton;
        this.hasActived = false;
    }

    /**
     * The main logic loop for the thread. It continuously checks if the human
     * player has only one card left and if they have not pressed the "UNO" button.
     */
    @Override
    public void run(){
        while (true) {
            //System.out.println(humanPlayer.getCardsPlayer().size());
            if (hasActived ) {
                unoButton.setDisable(false);
                unoButton.setStyle("-fx-background-color: red;-fx-text-fill: white;-fx-font-weight: BOLD;");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                hasOneCardTheHumanPlayer();
            }

        }
    }

    /**
     * Checks if the human player has only one card left and if they have not clicked the "UNO" button.
     * If these conditions are met, the human player is penalized by drawing an extra card.
     */
    private void hasOneCardTheHumanPlayer(){


        Player player;
        if ( cardsPlayer.size() == 1) {
            player = this.humanPlayer;
        }else{
            player = this.machinePlayer;
        }

        if (!player.getIsProtected()) {
            System.out.println("hacer comer a" +player.getTypePlayer());
            gameUno.eatCard(player, 1);
        }
        if(machinePlayer.getCardsPlayer().size() == 1){
            idImagenMessage.setImage(getImagenCart());
            idImagenMessage.setVisible(true);
            Timer timer = new Timer();
            TimerTask pushButton = new TimerTask() {
                public void run() {
                    Platform.runLater(()->idImagenMessage.setVisible(false));
                }
            };
            timer.schedule(pushButton, 1000);
        }
        unoButton.setStyle("-fx-background-color: white;-fx-text-fill: red;-fx-font-weight: BOLD;");
        unoButton.setDisable(true);
        machinePlayer.setIsProtected(false);
        machinePlayer.setIsProtected(false);
        hasActived = false;
    }

    public void setHasActived(boolean hasActived){
        this.hasActived = hasActived;
    }

    public void setThreadPlayMachine(ThreadPlayMachine threadPlayMachine){
        this. threadPlayMachine = threadPlayMachine;

    }

    public Image getImagenCart(){
        try {
            Image image = new Image(String.valueOf(getClass().getResource("/org/example/eiscuno/images/action_uno.png")));
            ImageView card = new ImageView(image);
            card.setFitWidth(100);
            card.setFitHeight(100);
            return image;
        } catch (Error e) {
            System.out.println("Error: " + e);
            return null;
        }
    }
    public void stopThread() {
        running = false;
    }
}
