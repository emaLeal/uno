package org.example.eiscuno.model.player;

import org.example.eiscuno.model.card.ICard;

import java.util.ArrayList;

/**
 * Represents a player in the Uno game.
 */
public class Player implements IPlayer {
    private ArrayList<ICard> cardsPlayer;
    private String typePlayer;
    private boolean isProtected = false;

    /**
     * Constructs a new Player object with an empty hand of cards.
     */
    public Player(String typePlayer){
        this.cardsPlayer = new ArrayList<ICard>();
        this.typePlayer = typePlayer;
    };

    /**
     * Adds a card to the player's hand.
     *
     * @param card The card to be added to the player's hand.
     */
    @Override
    public void addCard(ICard card){
        cardsPlayer.add(card);
    }

    /**
     * Retrieves all cards currently held by the player.
     *
     * @return An ArrayList containing all cards in the player's hand.
     */
    @Override
    public ArrayList<ICard> getCardsPlayer() {
        return cardsPlayer;
    }

    /**
     * Removes a card from the player's hand based on its index.
     *
     * @param index The index of the card to remove.
     */
    @Override
    public void removeCard(int index) {
        cardsPlayer.remove(index);
    }

    /**
     * Retrieves a card from the player's hand based on its index.
     *
     * @param index The index of the card to retrieve.
     * @return The card at the specified index in the player's hand.
     */
    @Override
    public ICard getCard(int index){
        return cardsPlayer.get(index);
    }

    public String getTypePlayer() {
        return typePlayer;
    }

    public boolean getIsProtected() {

        return isProtected;
    }

    public void setIsProtected(boolean isProtected) {
        this.isProtected = isProtected;
    }
}