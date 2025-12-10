package application.model;

import javafx.scene.image.Image;

public abstract class Card implements Playable {
    private String name;
    private Image image;
    private boolean matched;
    

    public Card(String name, Image image) {
        this.name = name;
        this.image = image;
        this.matched = false;
    }

    public String getName() { return name; }
    public Image getImage() { return image; }
    public boolean isMatched() { return matched; }
    public void setMatched(boolean matched) { this.matched = matched; }

 
    public abstract void performAction();
}

// Subclass example (Inheritance).
class BasicCard extends Card {
    public BasicCard(String name, Image image) {
        super(name, image);
    }

   
    
    @Override
    public void performAction() {
        System.out.println(getName() + " performAction called.");
    }
}