package tiduswr.jade;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene {

    private boolean changingScene = false;
    private float timeToChangeScene = 2f;

    public LevelEditorScene(){
        System.out.println("Inside level editor scene");
    }

    @Override
    public void update(float dt) {

        if(!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)){
            changingScene = true;
        }

        if(changingScene && timeToChangeScene > 0){
            timeToChangeScene -= dt;
            Window w = Window.getInstance();
            float factor = (dt * 5f);

            w.setR(w.getR() - factor);
            w.setG(w.getG() - factor);
            w.setB(w.getB() - factor);
        }else if (changingScene){
            Window.changeScene(1);
        }


    }

}
