package org.example.eiscuno.model.machine;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.example.eiscuno.model.card.CardSpecial;
import org.example.eiscuno.model.card.ICard;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

import java.util.*;

/**
 * A thread that handles the machine player's turn in the game.
 * This thread checks if the human player has played and then performs
 * the machine player's actions.
 */
public class ThreadPlayMachine extends Thread {
    private volatile boolean running = true;
    private Table table;
    private Player machinePlayer;
    private Player humanPlayer;
    private ImageView tableImageView;
    private GameUno gameUno;
    private GridPane gridPaneCardsMachine;
    private GridPane getGridPaneCardsPlayer;
    private volatile boolean hasPlayerPlayed;
    private Label labelTable;
    private Label labelMachine;
    private CheckBox idShowCarts;
    private Label colorInteractionLabel;
    private ThreadSingUNOMachine threadSingUNOMachine;
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
    public ThreadPlayMachine(Table table, Player machinePlayer, Player humanPlayer, ImageView tableImageView, GridPane gridPaneCardsMachine, GridPane gridPaneCardsPlayer, GameUno gameUno, Label labelTable, Label labelMachine, CheckBox idShowCarts, Label colorInteractionLabel, ThreadSingUNOMachine threadSingUNOMachine) {
        this.threadSingUNOMachine = threadSingUNOMachine;
        this.idShowCarts = idShowCarts;
        this.gridPaneCardsMachine = gridPaneCardsMachine;
        this.getGridPaneCardsPlayer = gridPaneCardsPlayer;
        this.table = table;
        this.machinePlayer = machinePlayer;
        this.humanPlayer = humanPlayer;
        this.tableImageView = tableImageView;
        this.hasPlayerPlayed = false;
        this.gameUno = gameUno;
        this.labelMachine = labelMachine;
        this.labelTable = labelTable;
        this.colorInteractionLabel= colorInteractionLabel;
        printCardsMachine();
    }
    /**
     * The main logic loop for the machine player's turn.
     * Continuously checks if the human player has played and then makes a move.
     */
    public void run() {
        while (running) {
            if (hasPlayerPlayed) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Aqui iria la logica de colocar la carta
                try {
                    getGridPaneCardsPlayer.setDisable(true);
                    Platform.runLater(this::changeColor);
                    putCardOnTheTable();

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                Platform.runLater(this::printCardsMachine);
                Platform.runLater(this::changeColor);

            }
        }
    }
    /**
     * Tries to place a card on the table from the machine player's hand.
     * If no valid card is found, the machine player draws a card and tries again.
     */
    private void putCardOnTheTable() throws InterruptedException {
        int index;
        boolean found = false;
        ICard currentCardOnBoard = table.getCurrentCardOnTheTable();
        for (ICard card : machinePlayer.getCardsPlayer()) {
            String typeCurrentBoardCard = currentCardOnBoard.getName().name().split("_")[0];
            String typeCurrentCard = card.getName().name().split("_")[0];

            if ((Objects.equals(card.getType(), "Numero") && Objects.equals(currentCardOnBoard.getNumber(), card.getNumber())) ||Objects.equals(typeCurrentBoardCard, typeCurrentCard) || Objects.equals(currentCardOnBoard.getColor(), card.getColor()) || Objects.equals(card.getType(), "Especial")) {
                index = machinePlayer.getCardsPlayer().indexOf(card);
                machinePlayer.removeCard(index);
                Platform.runLater(this::updateLabel);
               if (card.getName().name().startsWith("TWO")){
                    gameUno.eatCard(humanPlayer, 2);
               } else if (card.getName().name().startsWith("FOUR")) {
                   System.out.println("Entrando a +4");
                   CardSpecial currentCardOnBoardSpecial = (CardSpecial) card;
                   gameUno.eatCard(humanPlayer, 4);
                   String[] opciones = {"RED", "BLUE", "YELLOW", "GREEN"};
                   int randomOption = new Random().nextInt(4);
                   currentCardOnBoardSpecial.setColor(opciones[randomOption]);
                   System.out.println("NUEVO COLOR " + opciones[randomOption]);
               } else if (card.getName().name().startsWith("RESERVE") || card.getName().name().startsWith("SKIP")) {
                   hasPlayerPlayed = true;
               } else if (card.getName().name().startsWith("WILD")) {
                   System.out.println("Entrando a cambio de color");
                   CardSpecial currentCardOnBoardSpecial = (CardSpecial) card;
                    String[] opciones = {"RED", "BLUE", "YELLOW", "GREEN"};
                   int randomOption = new Random().nextInt(4);
                   currentCardOnBoardSpecial.setColor(opciones[randomOption]);
                   System.out.println("NUEVO COLOR " + opciones[randomOption]);
                   hasPlayerPlayed = false;
                   getGridPaneCardsPlayer.setDisable(false);
               } else {
                   System.out.println("Por defecto");
                   hasPlayerPlayed = false;
                   getGridPaneCardsPlayer.setDisable(false);
               }
                table.addCardOnTheTable(card);
                tableImageView.setImage(card.getImage());
                found = true;

                if (machinePlayer.getCardsPlayer().size() == 1) {
                    threadSingUNOMachine.setHasActived(true);
                    machinePlayer.setIsProtected(true);
                   // machinePlayer.schedule(pushButton, 1000);
                    Timer timer = new Timer();
                    TimerTask pushButton = new TimerTask() {
                        public void run() {
                            System.out.println("se imprimio la maquina");
                            System.out.println(machinePlayer.getCardsPlayer().size());
                            Platform.runLater(()->printCardsMachine());

                        }
                    };
                    Platform.runLater(this::printCardsMachine);
                    timer.schedule(pushButton, 2500);

                }
                break;
            }

        }
        if (!found) {
            gameUno.eatCard(machinePlayer,1);

            Thread.sleep(500);
            Platform.runLater(this::printCardsMachine);
            putCardOnTheTable();
        }
    }

    /**
     * Updates the visual representation of the machine player's cards.
     */
    public void printCardsMachine () {
        gridPaneCardsMachine.getChildren().clear();

        ArrayList<ICard> currentVisibleMachine = machinePlayer.getCardsPlayer();
        ImageView imagenCard;
        for (int i = 0; i < currentVisibleMachine.size(); i++) {
            try {
                if(idShowCarts.isSelected()){
                    ICard card = currentVisibleMachine.get(i);
                    imagenCard = card.getCard();
                }else{
                    imagenCard = GetImagenCart();

                }
                ImageView cardImageView = imagenCard;


                gridPaneCardsMachine.add(cardImageView, i, 0);
            } catch (Error e) {
                System.out.println("Error: " + e);
            }
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
        changeColor();
    }

    /**
     * Updates the color interaction label to display the current color of the card on the table.
     */
    private void changeColor() {
        colorInteractionLabel.setText("Color Actual: " + table.getCurrentCardOnTheTable().getColor());
        colorInteractionLabel.setStyle("-fx-text-fill: "+table.getCurrentCardOnTheTable().getColor().toLowerCase()+";-fx-font-weight: BOLD;");
    }

    /**
     * Loads and returns an ImageView of a card image.
     *
     * @return an ImageView containing the image of a card, or null if an error occurs.
     */
    private ImageView GetImagenCart(){
        try {
            Image image = new Image(String.valueOf(getClass().getResource("/org/example/eiscuno/cards-uno/card_uno.png")));
            ImageView card = new ImageView(image);
            card.setY(16);
            card.setFitHeight(90);
            card.setFitWidth(70);
            return card;
        } catch (Error e) {
            System.out.println("Error: " + e);
            return null;
        }
    }
    public void stopThread() {
        running = false;
    }
}

