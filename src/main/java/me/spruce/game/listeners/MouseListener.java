package me.spruce.game.listeners;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {

    private static MouseListener instance;
    private double scrollX, scrollY;
    private double posX, posY, lastY, lastX;
    private boolean mouseButtonPressed[] = new boolean[3];
    private boolean isDragging;

    private MouseListener(){
        this.scrollX = 0;
        this.scrollY = 0;
        this.posX = 0;
        this.posY = 0;
        this.lastX = 0;
        this.lastY = 0;
    }

    public static MouseListener get(){
        if(instance == null){
            instance = new MouseListener();
        }
        return instance;
    }

    public static void mousePositionCallBack(long window, double xpos, double ypos){
        get().lastX = get().posX;
        get().lastY = get().posY;
        get().posX = xpos;
        get().posY = ypos;
        get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int modifiers){
        if(action == GLFW_PRESS) {
            if(button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = true;
            }
        }else if(action == GLFW_RELEASE){
            if(button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = false;
                get().isDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset){
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame(){
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().posX;
        get().lastY = get().posY;
    }

    public static double getScrollX() {
        return get().scrollX;
    }

    public static double getScrollY() {
        return get().scrollY;
    }

    public static double getPosX() {
        return get().posX;
    }

    public static double getPosY() {
        return get().posY;
    }

    public static double getLastY() {
        return get().lastY;
    }

    public static double getLastX() {
        return get().lastX;
    }

    public static double getDx(){
        return get().lastX - get().posX;
    }

    public static double getDy(){
        return get().lastY - get().posY;
    }

    public static boolean isDragging(){
        return get().isDragging;
    }

    public static boolean isButtonDown(int button){
        if(button < get().mouseButtonPressed.length){
            return get().mouseButtonPressed[button];
        }else{
            return false;
        }
    }
}
