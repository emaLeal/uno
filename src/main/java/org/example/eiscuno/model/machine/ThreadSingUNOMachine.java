package org.example.eiscuno.model.machine;

import org.example.eiscuno.model.card.Card;
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
public class ThreadSingUNOMachine implements Runnable{
    private ArrayList<ICard> cardsPlayer;
    private ArrayList<ICard> cardsMachine;
    private Player humanPlayer;
    private Player machinePlayer;
    private GameUno gameUno;

    /**
     * Constructor to initialize the thread with necessary game components.
     *
     * @param cardsPlayer the list of cards held by the human player
     * @param cardsMachine the list of cards held by the machine player
     * @param machinePlayer the machine player object
     * @param humanPlayer the human player object
     * @param gameUno the game logic controller
     */
    public ThreadSingUNOMachine(ArrayList<ICard> cardsPlayer,ArrayList<ICard> cardsMachine, Player machinePlayer, Player humanPlayer, GameUno gameUno){
        this.cardsPlayer = cardsPlayer;
        this.cardsMachine =cardsMachine;
        this.machinePlayer = machinePlayer;
        this.humanPlayer=humanPlayer;
        this.gameUno= gameUno;
    }

    /**
     * The main logic loop for the thread. It continuously checks if the human
     * player has only one card left and if they have not pressed the "UNO" button.
     */
    @Override
    public void run(){
        while (true){
            try {
                Thread.sleep((long) (Math.random() * 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            hasOneCardTheHumanPlayer();
        }
    }

    /**
     * Checks if the human player has only one card left and if they have not clicked the "UNO" button.
     * If these conditions are met, the human player is penalized by drawing an extra card.
     */
    private void hasOneCardTheHumanPlayer(){
        if(cardsPlayer.size() == 1){
            Timer timer = new Timer();
            TimerTask pushButton = new TimerTask() {
                public void run() {
                    if (!humanPlayer.isButtonClick()) {
                        gameUno.eatCard(humanPlayer,1);
                        System.out.println("UNO");
                    }
                }
            };
            timer.schedule(pushButton, 1000);

        }
    }
}
