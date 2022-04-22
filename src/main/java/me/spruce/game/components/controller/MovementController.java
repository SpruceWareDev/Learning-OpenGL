package me.spruce.game.components.controller;

import me.spruce.game.object.Component;

public class MovementController extends Component{

    private PlayerController controller;

    public MovementController(PlayerController controller){
        this.controller = controller;
    }

    @Override
    public void update(float deltaTime) {

    }

    public PlayerController getController(){
        return controller;
    }
}
