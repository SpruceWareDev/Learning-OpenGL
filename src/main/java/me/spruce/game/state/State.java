package me.spruce.game.state;

import me.spruce.game.object.GameObject;
import me.spruce.game.renderer.Camera;

import java.util.ArrayList;
import java.util.List;

public abstract class State {

    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();

    public State(){

    }

    public abstract void init();

    public void start(){
        for(GameObject go : gameObjects){
            go.start();
        }
        isRunning = true;
    }

    public void addGameObjectToState(GameObject go){
        if(!isRunning){
            gameObjects.add(go);
        }else {
            gameObjects.add(go);
            go.start();
        }
    }

    public abstract void drawState(float deltaTime, float mouseX, float mouseY);

    public abstract void update(float deltaTime);
}
