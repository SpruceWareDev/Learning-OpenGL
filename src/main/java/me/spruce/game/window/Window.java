package me.spruce.game.window;

import me.spruce.game.listeners.KeyListener;
import me.spruce.game.listeners.MouseListener;
import me.spruce.game.state.State;
import me.spruce.game.state.states.GameState;
import me.spruce.game.util.Time;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private int width, height;
    private String title;

    private long glfwWindow = NULL;

    private static Window window = null;

    private static int currentStateIndex = -1;
    private static State currentState = null;

    private Window() {
        this.width = 1280;
        this.height = 720;
        this.title = "Game thingy idk lmao.";
    }

    public static void changeScene(int newSceneIndex){
        switch (newSceneIndex){
            case 0:
                currentState = new GameState();
                currentState.init();
                currentStateIndex = 0;
                break;
        }
    }

    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }
        return Window.window;
    }

    public void run() {
        System.out.println("LWJGL Init " + Version.getVersion());

        init();
        loop();

        //Free memory
        glfwDestroyWindow(glfwWindow);

        //Terminate GLFW and free the error callback set in init method;
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() {
        //Setup error callback to console
        GLFWErrorCallback.createPrint(System.err).set();

        //Init GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize glfw.");
        }

        //config GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        //Create the window
        glfwWindow = glfwCreateWindow(width, height, title, NULL, NULL);

        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to craete the window.");
        }

        //Add mouse listeners
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePositionCallBack);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);

        //Add keyboard listeners
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        //Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);

        //Enable vsync
        glfwSwapInterval(1);

        //Make the window visible
        glfwShowWindow(glfwWindow);

        GL.createCapabilities();

        changeScene(0);
    }

    public void loop() {
        float beginTime = Time.getTime();
        float endTime = Time.getTime();
        float deltaTime = 0f;

        while (!glfwWindowShouldClose(glfwWindow)) {
            //Poll events
            glfwPollEvents();

            glClearColor(1f, 1f, 1f, 1f);
            glClear(GL_COLOR_BUFFER_BIT);

            currentState.drawState(deltaTime, (float) MouseListener.getPosX(),(float) MouseListener.getPosY());
            currentState.update(deltaTime);

            glfwSwapBuffers(glfwWindow);

            endTime = Time.getTime();
            deltaTime = endTime - beginTime;
            beginTime = endTime;
        }
    }
}
