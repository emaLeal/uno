package org.example.eiscuno.model.card;

import org.example.eiscuno.model.unoenum.EISCUnoEnum;

public class FactoryCard {

    public ICard createCard(String url, String typeCard, String color, String number, String type, EISCUnoEnum name) {
        return switch (typeCard) {
            case "Numero" -> new CardNumber(url, color, number);
            case "Accion" -> new CardAction(url,color, type, name);
            case "Especial"-> new CardSpecial(url,type, name);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }
}
