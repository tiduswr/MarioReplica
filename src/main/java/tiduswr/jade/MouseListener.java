package tiduswr.jade;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {

    private static MouseListener instance;
    private double scrollX, scrollY;
    private double xPos, yPos, lastY, lastX;
    private final boolean[] mouseButtonPressed = new boolean[3];
    private boolean isDragging;

    private MouseListener(){
        scrollX = 0d;
        scrollY = 0d;
        xPos = 0d;
        yPos = 0d;
        lastX = 0d;
        lastY = 0d;
    }

    public static MouseListener getInstance(){
        if(instance == null)
            instance = new MouseListener();
        return instance;
    }

    public static void mousePosCallback(long window, double xPos, double yPos){
        getInstance().lastX = getInstance().xPos;
        getInstance().lastY = getInstance().yPos;
        getInstance().xPos = xPos;
        getInstance().yPos = yPos;
        getInstance().isDragging = getInstance().mouseButtonPressed[0] ||
                getInstance().mouseButtonPressed[1] ||
                getInstance().mouseButtonPressed[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods){
        if(action == GLFW_PRESS){
            if(button < getInstance().mouseButtonPressed.length){
                getInstance().mouseButtonPressed[button] = true;
            }
        }else if(action == GLFW_RELEASE){
            if(button < getInstance().mouseButtonPressed.length){
                getInstance().mouseButtonPressed[button] = false;
                getInstance().isDragging = false;
            }
        }

    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset){
        getInstance().scrollX = xOffset;
        getInstance().scrollY = yOffset;
    }

    public static void endFrame(){
        getInstance().scrollX = 0d;
        getInstance().scrollY = 0d;
        getInstance().lastX = getInstance().xPos;
        getInstance().lastY = getInstance().yPos;
    }

    public static float getX(){
        return (float) getInstance().xPos;
    }

    public static float getY(){
        return (float) getInstance().yPos;
    }

    public static float getDx(){
        return (float) (getInstance().lastX - getInstance().xPos);
    }

    public static float getDy(){
        return (float) (getInstance().lastY - getInstance().yPos);
    }

    public static float getScrollX(){
        return (float) getInstance().scrollX;
    }

    public static float getScrollY(){
        return (float) getInstance().scrollY;
    }

    public static boolean isDragging(){
        return getInstance().isDragging;
    }

    public static boolean mouseButtonDown(int button){
        if(button < getInstance().mouseButtonPressed.length){
            return getInstance().mouseButtonPressed[button];
        }else{
            return false;
        }
    }

}
