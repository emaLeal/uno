package org.example.eiscuno.model.card;

public class CardAction implements ICard{
    private String color;
    private String type;

    public CardAction(String color, String type){
        this.color = color;
        this.type = type;
    }
}
