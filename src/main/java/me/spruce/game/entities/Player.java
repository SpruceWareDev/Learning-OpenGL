package me.spruce.game.entities;

import me.spruce.game.components.SpriteRenderer;
import me.spruce.game.components.controller.MovementController;
import me.spruce.game.components.controller.PlayerController;
import me.spruce.game.object.Component;
import me.spruce.game.object.GameObject;

public class Player extends GameObject {

    private String name;

    private MovementController controller;
    private SpriteRenderer renderer;

    private float[] vertexArray = {
            //position                  //color                     //UV Coordinates
            60.5f, 0.5f, 0.0f,         1.0f, 0.0f, 0.0f, 1.0f,     1, 1,  //Bottom right
            0.5f, 100.5f, 0.0f,         0.0f, 1.0f, 0.0f, 1.0f,     0, 0,  //Top left
            60.5f, 100.5f, 0.0f,       0.0f, 0.0f, 1.0f, 1.0f,     1, 0,  //Top right
            0.5f, 0.5f, 0.0f,           1.0f, 1.0f, 0.0f, 1.0f,     0, 1   //Bottom left
    };

    //Must be in counter-clockwise order
    private int[] elementArray = {
            2, 1, 0,//Top right triangle
            0, 1, 3 //Bottom left triangle
    };

    private float moveSpeed = 100.0f;

    public Player(String name) {
        super(name);
        this.name = name;

        renderer = new SpriteRenderer("assets/images/characters/knight_idle1.png", vertexArray, elementArray);
        controller = new MovementController(new PlayerController());
        this.addComponent(controller);
        this.addComponent(renderer);
    }

    public void controlPlayer(float deltaTime){
        if(controller.getController().isRightRequested()){
            renderer.getCamera().position.x -= deltaTime * moveSpeed;
        }
        if(controller.getController().isLeftRequested()){
            renderer.getCamera().position.x += deltaTime * moveSpeed;
        }
        if(controller.getController().isUpRequested()){
            renderer.getCamera().position.y -= deltaTime * moveSpeed;
        }
        if(controller.getController().isDownRequested()){
            renderer.getCamera().position.y += deltaTime * moveSpeed;
        }
    }

    @Override
    public void start() {
        for (Component c : components){
            c.start();
        }
    }

    @Override
    public void update(float deltaTime){
        controlPlayer(deltaTime);
        for (Component c : components){
            c.update(deltaTime);
        }
    }
}
