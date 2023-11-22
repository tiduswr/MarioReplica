package tiduswr.jade;

public class LevelScene extends Scene{

    public LevelScene(){
        System.out.println("Inside level scene");
        Window w = Window.getInstance();
        w.setR(1);
        w.setG(1);
        w.setB(1);
    }

    @Override
    public void update(float dt) {

    }

}
