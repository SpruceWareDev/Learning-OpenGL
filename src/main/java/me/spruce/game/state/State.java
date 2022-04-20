package me.spruce.game.state;

import me.spruce.game.renderer.Camera;

public abstract class State {

    protected Camera camera;

    public State(){

    }

    public abstract void init();

    public abstract void drawState(float deltaTime, float mouseX, float mouseY);

    public abstract void update(float deltaTime);
}
