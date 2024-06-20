package org.example.eiscuno.model.card;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.eiscuno.model.unoenum.EISCUnoEnum;

public class CardNumber implements ICard{

    private String color;
    private String number;
    private ImageView cardImageView;
    private Image image;
    private String type;
    private EISCUnoEnum name;
    private String url;

    public CardNumber(String url, String color, String number, EISCUnoEnum name){

        this.name = name;
        this.url = url;
        this.color = color;
        this.number = number;
        this.image = new Image(String.valueOf(getClass().getResource(url)));
        this.cardImageView = createCardImageView();
        this.type = "Numero";
    }

    @Override
    public ImageView createCardImageView() {
        ImageView card = new ImageView(this.image);
        card.setY(16);
        card.setFitHeight(90);
        card.setFitWidth(70);
        return card;
    }

    @Override
    public ImageView getCard() {
        return cardImageView;
    }

    public Image getImage() {
        return image;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public EISCUnoEnum getName() {
        return name;
    }
}
