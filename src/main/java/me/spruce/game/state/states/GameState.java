package me.spruce.game.state.states;

import me.spruce.game.components.SpriteRenderer;
import me.spruce.game.entities.Player;
import me.spruce.game.listeners.KeyListener;
import me.spruce.game.object.GameObject;
import me.spruce.game.renderer.Camera;
import me.spruce.game.renderer.Shader;
import me.spruce.game.renderer.Texture;
import me.spruce.game.state.State;
import me.spruce.game.util.RenderUtils;
import me.spruce.game.util.Time;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.CallbackI;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class GameState extends State {

    private Player testPlayer;

    public GameState(){

    }

    @Override
    public void init() {
        this.testPlayer = new Player("Test Player");
        this.addGameObjectToState(this.testPlayer);
    }

    @Override
    public void drawState(float deltaTime, float mouseX, float mouseY) {

    }

    @Override
    public void update(float deltaTime) {
        for(GameObject go : this.gameObjects){
            go.update(deltaTime);
        }
    }
}
