package org.example.eiscuno.model.card;

public class FactoryCard {

    public ICard crearCarta(String type, String color, String number) {
        return switch (type) {
            case "Numero" -> new CardNumber(color, number);
            case "Accion" -> new CardAction(color, type);
            case "Especial"-> new CardSpecial(type);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }
}
