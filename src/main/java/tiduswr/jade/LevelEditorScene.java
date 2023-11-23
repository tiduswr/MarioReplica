package tiduswr.jade;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class LevelEditorScene extends Scene {

    private String vertexShaderSrc = "#version 330 core\n" +
            "\n" +
            "layout(location=0) in vec3 aPos;\n" +
            "layout(location=1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main(){\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos, 1.0);\n" +
            "}";

    private String fragmentShaderSrc = "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main(){\n" +
            "    color = fColor;\n" +
            "}";

    private int vertexID, fragmentID, shaderProgram;

    private float[] vertexArray = {
            //Pos(x, y, z)          //color (r, g, b, a)
            0.5f, -0.5f, 0f,      1f, 0f, 0f, 1f,  //Bottom right
            -0.5f, 0.5f, 0f,      0f, 1f, 0f, 1f,  //Top right
            0.5f, 0.5f, 0f,       0f, 0f, 1f, 1f, //Top right
            -0.5f, -0.5f, 0f,     1f, 1f, 0f, 1f
    };

    // IMPORTANT: Tem que estar em ordem anti-horário
    private int[] elementArray = {
            2, 1, 0, //Top right triangle
            0, 1, 3  // Bottom left triangle
    };

    private int vaoID, vboID, eboID;

    public LevelEditorScene(){

    }

    @Override
    public void init() {
        //Compile and link shaders

        //Load and compile the vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);

        //Vertex and Fragment compilation
        loadAndCompileShader(vertexID, vertexShaderSrc, "Vertex");
        loadAndCompileShader(fragmentID, fragmentShaderSrc, "Fragment");

        //Link shaders
        linkShaders();

        //Generates VBO
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        //Create VBO upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        //Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        //add the vertex attribute pointers
        int positionSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionSize + colorSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false,
                vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false,
                vertexSizeBytes, positionSize * floatSizeBytes);
        glEnableVertexAttribArray(1);

    }

    private void linkShaders(){
        //Link shaders and check for errors
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);

        //Check for linking errors
        int success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if(success == GL_FALSE){
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.err.println("Error: 'default.glsl'\n\tLinking Shaders failed.");
            System.err.println(glGetProgramInfoLog(shaderProgram, len));
            throw new RuntimeException("Error: 'default.glsl'\n\tLinking Shaders failed.");
        }
    }

    private void loadAndCompileShader(int id, String shaderSrc, String type){
        //Pass the shader source to the GPU
        glShaderSource(id, shaderSrc);
        glCompileShader(id);

        //Check for errors in compilation
        int success = glGetShaderi(id, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(id, GL_INFO_LOG_LENGTH);
            System.err.println("Error: 'default.glsl'");
            System.err.println(glGetShaderInfoLog(id, len));
            throw new RuntimeException("Error: 'default.glsl'\n\t" + type +" Shader compilation failed.");
        }

    }

    @Override
    public void update(float dt) {

        // Bind shader program
        glUseProgram(shaderProgram);

        // Bind the VAO that were using
        glBindVertexArray(vaoID);

        //Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        //Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        glUseProgram(0);
    }

}
