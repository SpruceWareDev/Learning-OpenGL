package me.spruce.game.renderer;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

public class Texture {

    private String texturePath;
    private int textureID;

    public Texture(String texturePath){
        this.texturePath = texturePath;

        //Generate texture on GPU
        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        //Set texture parameters

        //Repeat image in both directions
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        //When stretching an image, pixelate and not blur.
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        //When shrinking an image pixelate and not blur.
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        //Load image
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        ByteBuffer image = stbi_load(texturePath, width, height, channels, 0);

        if(image != null){
            if(channels.get(0) == 3){
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
            }else if(channels.get(0) == 4) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            }else{
                assert false : "Error: (Texture) Could not load texture. Unknown number of channels";
            }
        }else {
            assert false : "Error: (Texture File) Could not load image: " + texturePath;
        }

        //frees image in memory (without this it is a massive memory leak. I hate C :0)
        stbi_image_free(image);
    }

    public void bindTexture(){
        glBindTexture(GL_TEXTURE_2D, textureID);
    }

    public void unbindTexture(){
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
