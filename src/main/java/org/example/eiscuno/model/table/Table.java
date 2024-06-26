package org.example.eiscuno.model.table;

import org.example.eiscuno.model.card.ICard;

import java.util.ArrayList;

/**
 * Represents the table in the Uno game where cards are played.
 */
public class Table {
    private ArrayList<ICard> cardsTable;

    /**
     * Constructs a new Table object with no cards on it.
     */
    public Table(){
        this.cardsTable = new ArrayList<ICard>();
    }

    /**
     * Adds a card to the table.
     *
     * @param card The card to be added to the table.
     */
    public void addCardOnTheTable(ICard card){
        this.cardsTable.add(card);
    }

    /**
     * Retrieves the current card on the table.
     *
     * @return The card currently on the table.
     * @throws IndexOutOfBoundsException if there are no cards on the table.
     */
    public ICard getCurrentCardOnTheTable() throws IndexOutOfBoundsException {
        if (cardsTable.isEmpty()) {
            throw new IndexOutOfBoundsException("There are no cards on the table.");
        }
        return this.cardsTable.get(this.cardsTable.size()-1);
    }

    public ArrayList<ICard>  getCurrentCardsOnTheTable() throws IndexOutOfBoundsException {
        if (cardsTable.isEmpty() || cardsTable.size() == 1) {
            throw new IndexOutOfBoundsException("There are no cards on the table.");

        }

        try {
            ArrayList<ICard> cardsToReturn = new ArrayList<>(cardsTable.subList(0, cardsTable.size() - 1));
            ICard card = this.cardsTable.get(this.cardsTable.size()-1);
            this.cardsTable.clear();
            addCardOnTheTable(card);
            return cardsToReturn;
        } catch (Error e) {
            return null;
        }
    }
}
