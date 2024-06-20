package org.example.eiscuno.model.machine;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.card.ICard;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

import java.util.ArrayList;
import java.util.Objects;

public class ThreadPlayMachine extends Thread {
    private Table table;
    private Player machinePlayer;
    private ImageView tableImageView;
    private GameUno gameUno;
    private GridPane gridPaneCardsMachine;
    private volatile boolean hasPlayerPlayed;

    public ThreadPlayMachine(Table table, Player machinePlayer, ImageView tableImageView, GridPane gridPaneCardsMachine, GameUno gameUno) {
        this.gridPaneCardsMachine = gridPaneCardsMachine;
        this.table = table;
        this.machinePlayer = machinePlayer;
        this.tableImageView = tableImageView;
        this.hasPlayerPlayed = false;
        this.gameUno = gameUno;
        printCardsMachine();
    }

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

    private void putCardOnTheTable() {
        int index;
        boolean found = false;
        ICard currentCardOnBoard = table.getCurrentCardOnTheTable();
        for (ICard card : machinePlayer.getCardsPlayer()) {
            String typeCurrentBoardCard = currentCardOnBoard.getName().name().split("_")[0];
            String typeCurrentCard = card.getName().name().split("_")[0];
            if (Objects.equals(typeCurrentBoardCard, typeCurrentCard) || Objects.equals(currentCardOnBoard.getColor(), card.getColor()) || card.getType() == "Especial") {
                index = machinePlayer.getCardsPlayer().indexOf(card);
                machinePlayer.removeCard(index);

                table.addCardOnTheTable(card);
                tableImageView.setImage(card.getImage());
                found = true;
                break;
            }


            if (!found) {

            }

        }
    }
        private void printCardsMachine () {
            gridPaneCardsMachine.getChildren().clear();
            ArrayList<ICard> currentVisibleMachine = machinePlayer.getCardsPlayer();

            for (int i = 0; i < currentVisibleMachine.size(); i++) {

                ICard card = currentVisibleMachine.get(i);

                ImageView cardImageView = card.getCard();


                gridPaneCardsMachine.add(cardImageView, i, 0);
            }
        }

        public void setHasPlayerPlayed ( boolean hasPlayerPlayed){
            this.hasPlayerPlayed = hasPlayerPlayed;
        }
}

