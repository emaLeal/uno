package org.example.eiscuno.model.deck;

import org.example.eiscuno.model.card.FactoryCard;
import org.example.eiscuno.model.card.ICard;
import org.example.eiscuno.model.unoenum.EISCUnoEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

/**
 * Represents a deck of Uno cards.
 */
public class Deck {
    private Stack<ICard> deckOfCards;

    /**
     * Constructs a new deck of Uno cards and initializes it.
     */
    public Deck() {
        deckOfCards = new Stack<>();
        initializeDeck();
    }

    /**
     * Initializes the deck with cards based on the EISCUnoEnum values.
     */
    private void initializeDeck() {
        for (EISCUnoEnum cardEnum : EISCUnoEnum.values()) {
            FactoryCard factoryCard = new FactoryCard();
            ICard card = null;

            if (cardEnum.name().startsWith("GREEN_") ||
                    cardEnum.name().startsWith("YELLOW_") ||
                    cardEnum.name().startsWith("BLUE_") ||
                    cardEnum.name().startsWith("RED_")) {
                card = factoryCard.createCard(cardEnum.getFilePath(), "Numero",getCardColor(cardEnum.name()), getCardValue(cardEnum.name()),null, cardEnum);
                //Card card = new Card(cardEnum.getFilePath(), getCardValue(cardEnum.name()), getCardColor(cardEnum.name()));
            }else if(cardEnum.name().startsWith("SKIP_") || cardEnum.name().startsWith("RESERVE_") ||cardEnum.name().startsWith("TWO_WILD_DRAW_") )
                card = factoryCard.createCard(cardEnum.getFilePath(), "Accion",getCardColor(cardEnum.name()), null,"Accion", cardEnum);
            else if(cardEnum.name().equals("FOUR_WILD_DRAW") || cardEnum.name().equals("WILD"))
                card = factoryCard.createCard(cardEnum.getFilePath(), "Especial", null, null,"Especial", cardEnum);
            else
                continue;
            deckOfCards.push(card);
        }

        Collections.shuffle(deckOfCards);
    }

    private String getCardValue(String name) {
        if (name.endsWith("0")){
            return "0";
        } else if (name.endsWith("1")){
            return "1";
        } else if (name.endsWith("2")){
            return "2";
        } else if (name.endsWith("3")){
            return "3";
        } else if (name.endsWith("4")){
            return "4";
        } else if (name.endsWith("5")){
            return "5";
        } else if (name.endsWith("6")){
            return "6";
        } else if (name.endsWith("7")){
            return "7";
        } else if (name.endsWith("8")){
            return "8";
        } else if (name.endsWith("9")){
            return "9";
        } else {
            return null;
        }

    }

    private String getCardColor(String name){
        String color = "";
        if(name.contains("GREEN"))
            color = "GREEN";
        else if(name.contains("YELLOW"))
            color = "YELLOW";
        else if(name.contains("BLUE"))
            color = "BLUE";
        else if(name.contains("RED"))
            color = "RED";
        else
            color = null;

        return color;
    }

    /**
     * Takes a card from the top of the deck.
     *
     * @return the card from the top of the deck
     * @throws IllegalStateException if the deck is empty
     */
    public ICard takeCard() {
        if (deckOfCards.isEmpty()) {
            throw new IllegalStateException("No hay más cartas en el mazo.");
        }
        return deckOfCards.pop();
    }

    public Stack<ICard> GetCards(){
        return deckOfCards;
    }

    /**
     * Checks if the deck is empty.
     *
     * @return true if the deck is empty, false otherwise
     */
    public boolean isEmpty() {
        return deckOfCards.isEmpty();
    }

    public void updateDeck(ArrayList<ICard> carts){
        deckOfCards.addAll(carts);
    }
}
