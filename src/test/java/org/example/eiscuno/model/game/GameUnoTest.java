package org.example.eiscuno.model.game;

import javafx.stage.Stage;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GameUnoTest extends ApplicationTest {
    Player humanPlayer;
    Player machinePlayer;
    Deck deck;
    Table table;
    GameUno gameUno;

    @Override
    public void start(Stage stage) {

        // This is required to start the JavaFX application thread.
        humanPlayer = new Player("HUMAN_PLAYER");
        machinePlayer = new Player("MACHINE_PLAYER");
        deck = new Deck();
        table = new Table();
        gameUno = new GameUno(humanPlayer, machinePlayer, deck, table);
    }

    @Test
    void prueba(){
        System.out.println("prueba");
        assertEquals("RED", "RED");
    }

    @Test
    void placeRedCardOnTheTable(){
        boolean isRedCardPut = false;
        while (!isRedCardPut){
            var card = deck.takeCard();
            if(Objects.equals(card.getColor(),"RED")){
                table.addCardOnTheTable(card);
                isRedCardPut = true;
            }
        }
        assertEquals("RED", table.getCurrentCardOnTheTable().getColor());
    }

    @Test
    void placeBlueCardOnTheTable(){
        boolean isRedCardPut = false;
        while (!isRedCardPut){
            var card = deck.takeCard();
            if(Objects.equals(card.getColor(),"BLUE")){
                table.addCardOnTheTable(card);
                isRedCardPut = true;
            }
        }
        assertEquals("BLUE", table.getCurrentCardOnTheTable().getColor());
    }
    @Test
    void playerLooksForNumberFiveAndPlacesItOnTheTable(){
        boolean cardPut = false;
        while (!cardPut){
            gameUno.eatCard(humanPlayer,1);
            int size = humanPlayer.getCardsPlayer().size();
            var card = humanPlayer.getCardsPlayer().get(size-1);

            if(Objects.equals(card.getNumber(), "5")) {
                table.addCardOnTheTable(card);
                cardPut = true;
            }
        }
        assertEquals("5", table.getCurrentCardOnTheTable().getNumber());
    }

    @Test
    void playerLooksForNumberTwoAndPlacesItOnTheTable(){
        boolean cardPut = false;
        while (!cardPut){
            gameUno.eatCard(humanPlayer,1);
            int size = humanPlayer.getCardsPlayer().size();
            var card = humanPlayer.getCardsPlayer().get(size-1);

            if(Objects.equals(card.getNumber(), "2")){
                table.addCardOnTheTable(card);
                cardPut = true;
            }
        }
        assertEquals("2", table.getCurrentCardOnTheTable().getNumber());
    }

    @Test
    void eatTwoCardsMachine(){
        int cartNum = machinePlayer.getCardsPlayer().size();
        gameUno.eatCard(machinePlayer,2);
        assertEquals(cartNum + 2, machinePlayer.getCardsPlayer().size());
    }

    @Test
    void eatFourCardsMachine(){
        int cartNum = machinePlayer.getCardsPlayer().size();
        gameUno.eatCard(machinePlayer,4);
        assertEquals(cartNum + 4, machinePlayer.getCardsPlayer().size());
    }

    @Test
    void eatTwoCardsPlayer(){
        int cartNum = humanPlayer.getCardsPlayer().size();
        gameUno.eatCard(humanPlayer,2);
        assertEquals(cartNum + 2, humanPlayer.getCardsPlayer().size());
    }

    @Test
    void eatFourCardsPlayer(){
        int cartNum = humanPlayer.getCardsPlayer().size();
        gameUno.eatCard(humanPlayer,4);
        assertEquals(cartNum + 4, humanPlayer.getCardsPlayer().size());
    }




}