package org.example.eiscuno.model.card;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.eiscuno.model.unoenum.EISCUnoEnum;

public class CardSpecial implements ICard{
    private String type;
    private String url;
    private Image image;
    private ImageView cardImageView;
    private EISCUnoEnum name;

    public CardSpecial(String url, String color, String type, EISCUnoEnum name){
        this.url = url;
        this.type = type;
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
        return null;
    }
    public void setColor(String color) {
        System.out.println(color);
    }
    public String getNumber(){return null;}
    public EISCUnoEnum getName() {
        return name;
    }

    public void setName(EISCUnoEnum name) {
        this.name = name;
    }
}
