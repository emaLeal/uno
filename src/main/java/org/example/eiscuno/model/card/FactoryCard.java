package org.example.eiscuno.model.card;

public class FactoryCard {

    public ICard crearCarta(String url, String typeCard, String color, String number,String type) {
        return switch (typeCard) {
            case "Numero" -> new CardNumber(url, color, number);
            case "Accion" -> new CardAction(url,color, type);
            case "Especial"-> new CardSpecial(url,type);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }
}
