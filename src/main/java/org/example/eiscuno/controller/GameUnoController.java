package org.example.eiscuno.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.example.eiscuno.model.card.*;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.machine.ThreadPlayMachine;
import org.example.eiscuno.model.machine.ThreadSingUNOMachine;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.model.unoenum.EISCUnoEnum;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

/**
 * Controller class for the Uno game.
 */
public class GameUnoController {

    @FXML
    private GridPane gridPaneCardsMachine;

    @FXML
    private GridPane gridPaneCardsPlayer;

    @FXML
    private ImageView tableImageView;

    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;
    private Table table;
    private GameUno gameUno;
    private int posInitCardToShow;

    private ThreadSingUNOMachine threadSingUNOMachine;
    private ThreadPlayMachine threadPlayMachine;

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        initVariables();
        this.gameUno.startGame();
        printCardsHumanPlayer();

        threadSingUNOMachine = new ThreadSingUNOMachine(this.humanPlayer.getCardsPlayer());
        Thread t = new Thread(threadSingUNOMachine, "ThreadSingUNO");
        t.start();

        threadPlayMachine = new ThreadPlayMachine(this.table, this.machinePlayer, this.tableImageView, this.gridPaneCardsMachine,this.gameUno);
        threadPlayMachine.start();

        // empezar una carta
        String[] opciones = {"GREEN", "YELLOW", "RED", "BLUE"};
        int cardNumber = new Random().nextInt(10);
        int pos = new Random().nextInt(4);
        FactoryCard factoryCard = new FactoryCard();
        EISCUnoEnum value = EISCUnoEnum.valueOf(opciones[pos] + "_" + String.valueOf(cardNumber));
        ICard initialCard = factoryCard.createCard(value.getFilePath(), "Numero", opciones[pos], String.valueOf(cardNumber), null , value);
        gameUno.playCard(initialCard);
        tableImageView.setImage(initialCard.getImage());

    }

    /**
     * Initializes the variables for the game.
     */
    private void initVariables() {
        this.humanPlayer = new Player("HUMAN_PLAYER");
        this.machinePlayer = new Player("MACHINE_PLAYER");
        this.deck = new Deck();
        this.table = new Table();
        this.gameUno = new GameUno(this.humanPlayer, this.machinePlayer, this.deck, this.table);
        this.posInitCardToShow = 0;
    }

    /**
     * Prints the human player's cards on the grid pane.
     */
    private void printCardsHumanPlayer() {
        this.gridPaneCardsPlayer.getChildren().clear();
        ICard[] currentVisibleCardsHumanPlayer = this.gameUno.getCurrentVisibleCardsHumanPlayer(this.posInitCardToShow);

        for (int i = 0; i < currentVisibleCardsHumanPlayer.length; i++) {
            ICard card = currentVisibleCardsHumanPlayer[i];
            ImageView cardImageView = card.getCard();

            cardImageView.setOnMouseClicked((MouseEvent event) -> {
                // Aqui deberian verificar si pueden en la tabla jugar esa carta
                try {
                    if (isCardPossible(card)) {
                        gameUno.playCard(card);
                        tableImageView.setImage(card.getImage());
                        humanPlayer.removeCard(findPosCardsHumanPlayer(card));
                        threadPlayMachine.setHasPlayerPlayed(true);
                        printCardsHumanPlayer();

                    }
                }catch (Error e) {
                    System.out.println(e);
                }
            });

            this.gridPaneCardsPlayer.add(cardImageView, i, 0);
        }
    }


    /**
     * Finds the position of a specific card in the human player's hand.
     *
     * @param card the card to find
     * @return the position of the card, or -1 if not found
     */
    private Integer findPosCardsHumanPlayer(ICard card) {
        for (int i = 0; i < this.humanPlayer.getCardsPlayer().size(); i++) {
            if (this.humanPlayer.getCardsPlayer().get(i).equals(card)) {
                return i;
            }
        }
        return -1;
    }

    private boolean isCardPossible(ICard card) {
        ICard currentGenericCard = table.getCurrentCardOnTheTable();
        boolean isPossible = false;

        switch (currentGenericCard.getType()) {
            case "Numero":{
                CardNumber currentBoardCard = (CardNumber) currentGenericCard;
                switch (card.getType()) {
                    case "Numero": {
                        CardNumber currentCard = (CardNumber) card;
                        if (Objects.equals(currentBoardCard.getColor(), currentCard.getColor()) || Objects.equals(currentBoardCard.getNumber(), currentCard.getNumber()))
                            isPossible = true;
                        break;
                    }
                    case "Accion": {
                        CardAction currentCard = (CardAction) card;
                        if (currentBoardCard.getColor() == currentCard.getColor()){
                            System.out.println(currentBoardCard.getColor());
                            System.out.println(currentCard.getColor());
                            isPossible = true;
                        }
                        break;
                    }
                    case "Especial": {
                        CardSpecial currentCard = (CardSpecial) card;
                        isPossible = true;
                        break;
                    }
                    default:
                        System.out.println(":D");
                        break;
                }
                break;
            }
            case "Accion": {
                CardAction currentBoardCard = (CardAction) currentGenericCard;
                switch (card.getType()) {
                    case "Numero":{
                        CardNumber currentCard = (CardNumber) card;
                        if (currentBoardCard.getColor() == currentCard.getColor())
                            isPossible = true;
                        break;
                    }
                    case "Accion": {
                        CardAction currentCard = (CardAction) card;
                        String typeCurrentBoardCard = currentBoardCard.getName().name().split("_")[0];
                        String typeCurrentCard = currentCard.getName().name().split("_")[0];

                        if (typeCurrentBoardCard == typeCurrentCard || currentBoardCard.getColor() == currentCard.getColor())
                            isPossible = true;
                        break;
                    }
                    case "Especial": {
                        CardSpecial currentCard = (CardSpecial) card;
                        isPossible = true;
                        break;
                    }
                }
                break;
            }
            case "Especial":
                CardSpecial currentBoardCard = (CardSpecial) card;
               
                isPossible = true;
            default:
                System.out.println(":D");
                break;
        }
        return isPossible;
    }

    /**
     * Handles the "Back" button action to show the previous set of cards.
     *
     * @param event the action event
     */
    @FXML
    void onHandleBack(ActionEvent event) {
        if (this.posInitCardToShow > 0) {
            this.posInitCardToShow--;
            printCardsHumanPlayer();
        }
    }

    /**
     * Handles the "Next" button action to show the next set of cards.
     *
     * @param event the action event
     */
    @FXML
    void onHandleNext(ActionEvent event) {
        if (this.posInitCardToShow < this.humanPlayer.getCardsPlayer().size() - 4) {
            this.posInitCardToShow++;
            printCardsHumanPlayer();
        }
    }

    /**
     * Handles the action of taking a card.
     *
     * @param event the action event
     */
    @FXML
    void onHandleTakeCard(ActionEvent event) {
        // Implement logic to take a card here
        gameUno.eatCard(humanPlayer, 1);
        printCardsHumanPlayer();
    }

    /**
     * Handles the action of saying "Uno".
     *
     * @param event the action event
     */
    @FXML
    void onHandleUno(ActionEvent event) {
        // Implement logic to handle Uno event here
    }


}
