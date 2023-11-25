package tiduswr.jade;

import lombok.Getter;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {

    @Getter
    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;

    @Getter
    private Vector2f position;

    public Camera(Vector2f position){
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        adjustProjection();
    }

    public void adjustProjection(){
        projectionMatrix.identity();
        projectionMatrix.ortho(0f, 32f * 40f, 0f,
                32f * 21f, 0f, 100f);
    }

    public Matrix4f getViewMatrix(){
        Vector3f cameraFront = new Vector3f(0f, 0f, -1f);
        Vector3f cameraUp = new Vector3f(0f, 1f, 0f);
        this.viewMatrix.identity();
        return viewMatrix.lookAt(new Vector3f(position.x, position.y, 20f),
                cameraFront.add(position.x, position.y, 0f),
                cameraUp);
    }

}
