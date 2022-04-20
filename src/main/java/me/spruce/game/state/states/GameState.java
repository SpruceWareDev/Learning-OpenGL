package me.spruce.game.state.states;

import me.spruce.game.listeners.KeyListener;
import me.spruce.game.renderer.Camera;
import me.spruce.game.renderer.Shader;
import me.spruce.game.renderer.Texture;
import me.spruce.game.state.State;
import me.spruce.game.util.RenderUtils;
import me.spruce.game.util.Time;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class GameState extends State {

    private String vertexShaderSrc = "\n#version 460 core\nlayout (location=0) in vec3 aPos;\nlayout (location=1) in vec4 aColor;\n\nout vec4 fColor;\n\nvoid main() {\n    fColor = aColor;\n    gl_Position = vec4(aPos, 1.0);\n}";

    private String fragmentShaderSrc = "\n#version 460 core\n\nin vec4 fColor;\n\nout vec4 color;\n\nvoid main() {\n    color = fColor;\n}";

    private Shader defaultShader;
    private Texture testTexture;

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

    private int vaoID, vboID, eboID;

    public GameState(){

    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();
        this.testTexture = new Texture("assets/images/characters/knight_idle1.png");

        //********************************************************************************
        //generate VAO, VBO, and EBO buffer objects that can be sent to the GPU for render
        //********************************************************************************

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        //create VBO and upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        //create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        //Add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSizeBytes = (positionsSize + colorSize + uvSize) * Float.BYTES;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void drawState(float deltaTime, float mouseX, float mouseY) {
        defaultShader.use();

        //upload texture to shader
        defaultShader.uploadTexture("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        testTexture.bindTexture();

        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());

        //Bind the VAO
        glBindVertexArray(vaoID);

        //Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        //Unbind stuff after render
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        defaultShader.detach();
    }

    @Override
    public void update(float deltaTime) {

    }
}
