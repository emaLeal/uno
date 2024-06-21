package org.example.eiscuno.model.card;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.eiscuno.model.unoenum.EISCUnoEnum;

public interface ICard {

     ImageView createCardImageView();
     ImageView getCard();
     Image getImage();
     String getType();
     EISCUnoEnum getName();
     String getColor();
     void setColor(String color);
     String getNumber();

}
