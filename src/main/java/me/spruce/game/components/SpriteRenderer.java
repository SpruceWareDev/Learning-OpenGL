package me.spruce.game.components;

import me.spruce.game.object.Component;

public class SpriteRenderer extends Component {

    private boolean firstUpdate = true;

    @Override
    public void start(){
        System.out.println("Object started!");
    }

    @Override
    public void update(float deltaTime) {
        if(firstUpdate) {
            System.out.println("Update!");
            firstUpdate = false;
        }
    }
}
