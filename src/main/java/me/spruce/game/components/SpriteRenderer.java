package me.spruce.game.components;

import me.spruce.game.object.Component;
import me.spruce.game.renderer.Camera;
import me.spruce.game.renderer.Shader;
import me.spruce.game.renderer.Texture;
import me.spruce.game.util.Time;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class SpriteRenderer extends Component {

    private Shader shader;
    private Texture texture;
    private String texturePath;

    private float[] vertexArray = {};

    //Must be in counter-clockwise order
    private int[] elementArray = {};

    private Camera camera;

    private int vaoID, vboID, eboID;

    private boolean firstUpdate = true;

    public SpriteRenderer(String texturePath, float[] vertexArray, int[] elementArray){
        this.texturePath = texturePath;
        this.vertexArray = vertexArray;
        this.elementArray = elementArray;

        this.shader = new Shader("assets/shaders/default.glsl");
        this.texture = new Texture(texturePath);
    }

    @Override
    public void start(){
        System.out.println("Sprite Renderer started!");

        this.camera = new Camera(new Vector2f());

        shader.compile();
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
    public void update(float deltaTime) {
        if(firstUpdate) {
            System.out.println("Sprite Renderer First Update!");
            firstUpdate = false;
        }

        shader.use();

        //upload texture to shader
        shader.uploadTexture("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        texture.bindTexture();

        shader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        shader.uploadMat4f("uView", camera.getViewMatrix());
        shader.uploadFloat("uTime", Time.getTime());

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

        shader.detach();
    }

    public Camera getCamera(){
        return camera;
    }
}
