package me.spruce.game.object;

public abstract class Component {

    public GameObject gameObject;

    public abstract void update(float deltaTime);

    public void start(){

    }
}
