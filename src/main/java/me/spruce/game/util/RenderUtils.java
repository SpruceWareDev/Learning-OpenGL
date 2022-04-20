package me.spruce.game.util;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtils {

    public static void color(double red, double green, double blue, double alpha) {
        glColor4d(red, green, blue, alpha);
    }
}
