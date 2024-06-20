package org.example.eiscuno.model.card;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.eiscuno.model.unoenum.EISCUnoEnum;

public class CardAction implements ICard{
    private String color;
    private EISCUnoEnum name;
    private String url;
    private String type;
    private Image image;
    private ImageView cardImageView;

    public CardAction(String url, String color, String type, EISCUnoEnum name){
        this.color = color;
        this.type = type;
        this.url = url;
        this.image = new Image(String.valueOf(getClass().getResource(url)));
        this.cardImageView = createCardImageView();
        this.name = name;
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

    @Override
    public String getType() {
        return type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public EISCUnoEnum getName() {
        return name;
    }

    public void setName(EISCUnoEnum name) {
        this.name = name;
    }
}
