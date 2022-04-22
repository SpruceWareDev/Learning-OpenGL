package me.spruce.game.components.controller;

import me.spruce.game.listeners.KeyListener;

import java.awt.event.KeyEvent;

public class PlayerController implements Controller{
    @Override
    public boolean isUpRequested() {
        return KeyListener.isKeyPressed(KeyEvent.VK_W);
    }

    @Override
    public boolean isDownRequested() {
        return KeyListener.isKeyPressed(KeyEvent.VK_S);
    }

    @Override
    public boolean isLeftRequested() {
        return KeyListener.isKeyPressed(KeyEvent.VK_A);
    }

    @Override
    public boolean isRightRequested() {
        return KeyListener.isKeyPressed(KeyEvent.VK_D);
    }
}
