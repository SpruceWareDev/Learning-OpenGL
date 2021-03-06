package me.spruce.game.object;

import java.util.ArrayList;
import java.util.List;

public class GameObject {

    private String name;
    protected List<Component> components = new ArrayList<>();

    public GameObject(String name){
        this.name = name;
    }

    public <T extends Component> T getComponent(Class<T> component){
        for(Component c : components){
            if(component.isAssignableFrom(c.getClass())){
                try {
                    return component.cast(c);
                }catch (ClassCastException e){
                    e.printStackTrace();
                    assert false : "Error: Failed to cast to the component class.";
                }

            }
        }
        return null;
    }

    public <T extends Component> void removeComponent(Class<T> component){
        for(int i = 0; i < components.size(); i++){
            Component c = components.get(i);
            if(component.isAssignableFrom(c.getClass())){
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component c){
        this.components.add(c);
        c.gameObject = this;
    }

    public void update(float deltaTime){
        for(int i = 0; i < components.size(); i++){
            components.get(i).update(deltaTime);
        }
    }

    public void start(){
        for(int i = 0; i < components.size(); i++){
            components.get(i).start();
        }
    }
}
