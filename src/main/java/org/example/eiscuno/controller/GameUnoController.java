package org.example.eiscuno.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
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
    @FXML
    private Label labelMachine;

    @FXML
    private Label labelTable;
    @FXML
    private VBox idChooseColor;

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
        updateLabel();
        threadSingUNOMachine = new ThreadSingUNOMachine(this.humanPlayer.getCardsPlayer());
        Thread t = new Thread(threadSingUNOMachine, "ThreadSingUNO");
        t.start();

        threadPlayMachine = new ThreadPlayMachine(this.table, this.machinePlayer, this.tableImageView, this.gridPaneCardsMachine,this.gameUno, this.labelTable, this.labelMachine);
        threadPlayMachine.start();

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
                        if (!card.getName().name().startsWith("SKIP")){
                            threadPlayMachine.setHasPlayerPlayed(true);
                        }
                        printCardsHumanPlayer();
                        updateLabel();
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

    /**
     * Determines if a specific card can be played based on the current game rules.
     *
     * @param card the card to check for playability
     * @return true if the card can be played, false otherwise
     *
     */
    private boolean isCardPossible(ICard card) {
        ICard currentGenericCard = table.getCurrentCardOnTheTable();
        boolean isPossible = false;

        System.out.println(card.getType());
        switch (card.getType()) {
            case "Numero":{

                if (Objects.equals(currentGenericCard.getColor(), card.getColor()) || Objects.equals(currentGenericCard.getNumber(), card.getNumber()) || Objects.equals(currentGenericCard.getType(), "Especial"))
                    isPossible = true;
                break;

            }
            case "Accion": {
                String typeCurrentBoardCard = currentGenericCard.getName().name().split("_")[0];
                String typeCurrentCard = card.getName().name().split("_")[0];

                if (Objects.equals(typeCurrentBoardCard, typeCurrentCard) || Objects.equals(currentGenericCard.getColor(), card.getColor()) || Objects.equals(currentGenericCard.getType(), "Especial"))
                    {
                        if(Objects.equals(typeCurrentCard, "TWO")) {
                            gameUno.eatCard(machinePlayer, 2);
                    }
                    isPossible = true;
                }
                break;
            }
            case "Especial":
                String typeCurrentCard = card.getName().name().split("_")[0];
                System.out.println(typeCurrentCard);
                if(Objects.equals(typeCurrentCard, "FOUR")){
                    gameUno.eatCard(machinePlayer, 4);
                    idChooseColor.setVisible(true);
                }else if(Objects.equals(typeCurrentCard, "WILD")){
                    idChooseColor.setVisible(true);
                }
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
     */
    @FXML
    void onHandleTakeCard() {
        // Implement logic to take a card here
        gameUno.eatCard(humanPlayer, 1);
        updateLabel();
        printCardsHumanPlayer();
    }

    /**
     * Updates the labels to display the current number of cards in the deck
     * and the number of cards the machine player has.
     */
    void updateLabel(){
        labelTable.setText("Total de cartas : " + deck.GetCards().size());
        labelMachine.setText("Cantidad de cartas de la maquina: "+ machinePlayer.getCardsPlayer().size() );
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

    /**
     * Handles the action of choosing a color in the game.
     * This method is called when the user interacts with the color selection.
     *
     * @param event the ActionEvent triggered by the UI interaction, typically a button click
     */
    @FXML
    void chooseColor(ActionEvent event){
        idChooseColor.setVisible(false);
    }


}
