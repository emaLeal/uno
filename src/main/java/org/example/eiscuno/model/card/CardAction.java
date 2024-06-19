package org.example.eiscuno.model.card;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CardAction implements ICard{
    private String color;
    private String type;
    private String url;
    private Image image;
    private ImageView cardImageView;

    public CardAction(String url, String color, String type){
        this.color = color;
        this.type = type;
        this.image = new Image(String.valueOf(getClass().getResource(url)));
        this.cardImageView = createCardImageView();
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
}
