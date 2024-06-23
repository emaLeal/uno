package org.example.eiscuno.controller;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
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

import java.util.*;

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
    private ImageView idImagenMessage;
    @FXML
    private Label labelMachine;

    @FXML
    private Label labelTable;
    @FXML
    private VBox idChooseColor;
    @FXML
    private CheckBox idShowCarts;
    @FXML
    private Label colorInteractionLabel;
    @FXML
    private Button unoButton;

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
        threadSingUNOMachine = new ThreadSingUNOMachine(this.humanPlayer.getCardsPlayer(),this.machinePlayer.getCardsPlayer(),this.machinePlayer,this.humanPlayer,this.gameUno);
        Thread t = new Thread(threadSingUNOMachine, "ThreadSingUNO");
        t.start();

        threadPlayMachine = new ThreadPlayMachine(this.table, this.machinePlayer,this.humanPlayer ,this.tableImageView, this.gridPaneCardsMachine, this.gridPaneCardsPlayer,this.gameUno, this.labelTable, this.labelMachine, this.idShowCarts, this.colorInteractionLabel);
        threadPlayMachine.start();

        try {
            String[] opciones = {"GREEN", "YELLOW", "RED", "BLUE"};
            int cardNumber = new Random().nextInt(10);
            int pos = new Random().nextInt(4);
            FactoryCard factoryCard = new FactoryCard();
            EISCUnoEnum value = EISCUnoEnum.valueOf(opciones[pos] + "_" + String.valueOf(cardNumber));
            ICard initialCard = factoryCard.createCard(value.getFilePath(), "Numero", opciones[pos], String.valueOf(cardNumber), null , value);
            gameUno.playCard(initialCard);
            tableImageView.setImage(initialCard.getImage());
        } catch (Error e) {
            System.out.println("Error: " + e);
        }
        updateLabel();
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
                            //System.out.println(card.getName().name().startsWith("TWO"));
                            gameUno.playCard(card);
                            tableImageView.setImage(card.getImage());
                            humanPlayer.removeCard(findPosCardsHumanPlayer(card));
                            if(!WinnerValidate()){
                                if (card.getName().name().startsWith("TWO_WILD_DRAW")) {
                                    gameUno.eatCard(machinePlayer, 2);
                                    threadPlayMachine.printCardsMachine();
                                } else if (card.getName().name().startsWith("FOUR")) {
                                    gameUno.eatCard(machinePlayer, 4);
                                    threadPlayMachine.printCardsMachine();
                                } else if(card.getName().name().startsWith("WILD") || card.getName().name().startsWith("RESERVE") || card.getName().name().startsWith("SKIP")){
                                    System.out.println("Cambio de color o reversa o saltar turno");
                                }else {
                                    //ejecuta despues que la maquina lanze
                                    TimerTask task = new TimerTask() {
                                        public void run() {
                                            Platform.runLater(()->printCardsHumanPlayer());
                                            if (!WinnerValidate()){
                                                gridPaneCardsPlayer.setDisable(false);
                                            }
                                        }
                                    };
                                    Timer timer = new Timer("Timer");
                                    long delay = 3000L;
                                    timer.schedule(task, delay);
                                    threadPlayMachine.setHasPlayerPlayed(true);
                                }
                            }
                            if (humanPlayer.getCardsPlayer().size() != 1) {
                                humanPlayer.setButtonClick(false);
                            }
                            printCardsHumanPlayer();
                            if (humanPlayer.getCardsPlayer().size() > 1) {
                                unoButton.setStyle("-fx-background-color: white;-fx-text-fill: red;-fx-font-weight: BOLD;");
                            }
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
        switch (card.getType()) {
            case "Numero":{
                if (Objects.equals(currentGenericCard.getColor(), card.getColor()) || Objects.equals(currentGenericCard.getNumber(), card.getNumber()))
                    isPossible = true;
                break;
            }
            case "Accion": {
                String typeCurrentBoardCard = currentGenericCard.getName().name().split("_")[0];
                String typeCurrentCard = card.getName().name().split("_")[0];

                if (Objects.equals(typeCurrentBoardCard, typeCurrentCard) || Objects.equals(currentGenericCard.getColor(), card.getColor()))
                    isPossible = true;
                break;
            }
            case "Especial":
                String typeCurrentCard = card.getName().name().split("_")[0];
                System.out.println(typeCurrentCard);
                if(Objects.equals(typeCurrentCard, "FOUR") || Objects.equals(typeCurrentCard, "WILD"))
                    idChooseColor.setVisible(true);
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
        try {
            gameUno.eatCard(humanPlayer, 1);
            updateLabel();
            printCardsHumanPlayer();
        } catch (Error e) {
            System.out.println("Error: " + e);
        }
    }

    /**
     * Updates the labels to display the current number of cards in the deck
     * and the number of cards the machine player has.
     */
    void updateLabel(){
        labelTable.setText("Total de cartas : " + deck.GetCards().size());
        labelMachine.setText("Cantidad de cartas de la maquina: "+ machinePlayer.getCardsPlayer().size() );
        if (humanPlayer.getCardsPlayer().size() > 1)
            unoButton.setStyle("-fx-background-color: white;-fx-text-fill: red;-fx-font-weight: BOLD;");

        changeColor();
    }

    /**
     * Handles the action of saying "Uno".
     *
     * @param event the action event
     */
    @FXML
    void onHandleUno(ActionEvent event) {
        if ( humanPlayer.getCardsPlayer().size() == 1){
            System.out.println("se canto UNO");
            humanPlayer.setButtonClick(true);
            unoButton.setStyle("-fx-background-color: red;-fx-text-fill: white;-fx-font-weight: BOLD;");
        }
        if (machinePlayer.getCardsPlayer().size() == 1 && !machinePlayer.isButtonClick()) {
            System.out.println("Cantaste uno a la maquina");
            gameUno.eatCard(machinePlayer, 1);
            unoButton.setStyle("-fx-background-color: darkred;-fx-text-fill: white;-fx-font-weight: BOLD;");
        }


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
        Button colorButton = (Button) event.getSource();
        ICard currentCardData = table.getCurrentCardOnTheTable();
        String color = "";
        switch (colorButton.getText()) {
            case "Rojo":
                color = "RED";
                currentCardData.setColor("RED");
                break;
            case "Azul":
                color = "BLUE";
                break;
            case "Verde":
                color = "GREEN";
                currentCardData.setColor("GREEN");
                break;
            case "Amarillo":
                color = "YELLOW";
                currentCardData.setColor("YELLOW");
                break;
            default:
                color = "NOT_FOUND";
                break;
        }
        try {
            if (!color.equals("NOT_FOUND"))
                currentCardData.setColor(color);
            if (currentCardData.getName().name().startsWith("WILD"))
                threadPlayMachine.setHasPlayerPlayed(true);
            idChooseColor.setVisible(false);
        } catch (Error e) {
            System.out.println("Error: " + e);
        }
    }

    @FXML
    private void showCarts(){
        threadPlayMachine.printCardsMachine();
    }
    
    private boolean WinnerValidate(){
        ImageView imageView = new ImageView();
        Image image;
        imageView.setY(16);
        imageView.setFitHeight(90);
        imageView.setFitWidth(70);
        if(humanPlayer.getCardsPlayer().isEmpty()){
            try {
                image = new Image(String.valueOf(getClass().getResource("/org/example/eiscuno/images/ganaste.png")));
                imageView.setImage(image);
                idImagenMessage.setImage(image);
                idImagenMessage.setVisible(true);
                tableImageView.setImage(null);
                return true;
            } catch (Error e) {
                System.out.println("Error: " + e);
                return false;
            }
        } else if (machinePlayer.getCardsPlayer().isEmpty()) {
            try {
                image = new Image(String.valueOf(getClass().getResource("/org/example/eiscuno/images/perdiste.png")));
                imageView.setImage(image);
                idImagenMessage.setImage(image);
                idImagenMessage.setVisible(true);
                tableImageView.setImage(null);
                return true;
            } catch ( Error e) {
                System.out.println("Error: " + e);
                return false;
            }
        }

        return false;
    }

    private void changeColor() {
        if (table.getCurrentCardOnTheTable().getColor() != null) {
            colorInteractionLabel.setText("Color Actual: " + table.getCurrentCardOnTheTable().getColor());
            colorInteractionLabel.setStyle("-fx-text-fill: "+table.getCurrentCardOnTheTable().getColor().toLowerCase()+";-fx-font-weight: BOLD;");
        }
    }
}
