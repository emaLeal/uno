package org.example.eiscuno.model.machine;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.card.ICard;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

import java.util.ArrayList;
import java.util.Objects;
/**
 * A thread that handles the machine player's turn in the game.
 * This thread checks if the human player has played and then performs
 * the machine player's actions.
 */
public class ThreadPlayMachine extends Thread {
    private Table table;
    private Player machinePlayer;
    private ImageView tableImageView;
    private GameUno gameUno;
    private GridPane gridPaneCardsMachine;
    private volatile boolean hasPlayerPlayed;
    private Label labelTable;
    private Label labelMachine;

    /**
     * Initializes the thread with the necessary game components.
     *
     * @param table the game table
     * @param machinePlayer the machine player
     * @param tableImageView the image view for the table's current card
     * @param gridPaneCardsMachine the grid pane displaying the machine's cards
     * @param gameUno the game logic controller
     * @param labelTable the label showing the total number of cards in the deck
     * @param labelMachine the label showing the number of cards the machine has
     */
    public ThreadPlayMachine(Table table, Player machinePlayer, ImageView tableImageView, GridPane gridPaneCardsMachine, GameUno gameUno, Label labelTable, Label labelMachine) {
        this.gridPaneCardsMachine = gridPaneCardsMachine;
        this.table = table;
        this.machinePlayer = machinePlayer;
        this.tableImageView = tableImageView;
        this.hasPlayerPlayed = false;
        this.gameUno = gameUno;
        this.labelMachine = labelMachine;
        this.labelTable = labelTable;
        printCardsMachine();
    }
    /**
     * The main logic loop for the machine player's turn.
     * Continuously checks if the human player has played and then makes a move.
     */
    public void run() {
        while (true) {
            if (hasPlayerPlayed) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Aqui iria la logica de colocar la carta
                putCardOnTheTable();
                //printCardsMachine();
                Platform.runLater(this::printCardsMachine);

                hasPlayerPlayed = false;
            }
        }
    }
    /**
     * Tries to place a card on the table from the machine player's hand.
     * If no valid card is found, the machine player draws a card and tries again.
     */
    private void putCardOnTheTable() {
        int index;
        boolean found = false;
        ICard currentCardOnBoard = table.getCurrentCardOnTheTable();
        for (ICard card : machinePlayer.getCardsPlayer()) {
            String typeCurrentBoardCard = currentCardOnBoard.getName().name().split("_")[0];
            String typeCurrentCard = card.getName().name().split("_")[0];
            if (Objects.equals(currentCardOnBoard.getNumber(), card.getNumber()) ||Objects.equals(typeCurrentBoardCard, typeCurrentCard) || Objects.equals(currentCardOnBoard.getColor(), card.getColor()) || Objects.equals(card.getType(), "Especial")) {

                index = machinePlayer.getCardsPlayer().indexOf(card);
                machinePlayer.removeCard(index);
                Platform.runLater(this::updateLabel);
                table.addCardOnTheTable(card);
                tableImageView.setImage(card.getImage());
                found = true;
                break;
            }

        }
        if (!found) {
            gameUno.eatCard(machinePlayer,1);
            putCardOnTheTable();
        }
    }

    /**
     * Updates the visual representation of the machine player's cards.
     */
    private void printCardsMachine () {
        gridPaneCardsMachine.getChildren().clear();
        ArrayList<ICard> currentVisibleMachine = machinePlayer.getCardsPlayer();

        for (int i = 0; i < currentVisibleMachine.size(); i++) {

            ICard card = currentVisibleMachine.get(i);

            ImageView cardImageView = card.getCard();


            gridPaneCardsMachine.add(cardImageView, i, 0);
        }
    }

    /**
     * Sets the flag indicating if the human player has played.
     *
     * @param hasPlayerPlayed true if the human player has played, false otherwise
     */

    public void setHasPlayerPlayed ( boolean hasPlayerPlayed){
        this.hasPlayerPlayed = hasPlayerPlayed;
    }
    /**
     * Updates the labels showing the total number of cards in the deck
     * and the number of cards the machine player has.
     */

    private void updateLabel(){
        labelTable.setText("Total de cartas : " + gameUno.sizeDeck());
        labelMachine.setText("Cantidad de cartas de la maquina: "+ machinePlayer.getCardsPlayer().size() );
    }
}

