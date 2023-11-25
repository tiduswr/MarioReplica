package tiduswr.render;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLOutput;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private int shaderProgramID;

    private String vertexSource;
    private String fragmentSource;
    private String filePath;

    public Shader(String filePath){
        this.filePath = filePath;
        try{
            String source = new String(Files.readAllBytes(Paths.get(filePath)));

            //0 has nothing
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            //Find first pattern after #type 'pattern'
            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\n", index);
            String firstPattern = source.substring(index, eol).trim();

            //Find second pattern after #type 'pattern'
            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\n", index);
            String secondPattern = source.substring(index, eol).trim();

            checkShaderTypeInFile(firstPattern, splitString[1]);
            checkShaderTypeInFile(secondPattern, splitString[2]);
        }catch(IOException ex){
            ex.printStackTrace();
            throw new RuntimeException("Error: Shader file '" + filePath + "' not loaded!");
        }
    }

    public void compile(){
        //Load and compile the vertex shader
        int vertexID = glCreateShader(GL_VERTEX_SHADER);
        int fragmentID = glCreateShader(GL_FRAGMENT_SHADER);

        //Vertex and Fragment compilation
        loadAndCompileShader(vertexID, vertexSource, "Vertex");
        loadAndCompileShader(fragmentID, fragmentSource, "Fragment");

        //Link shaders
        linkShaders(vertexID, fragmentID);
    }

    public void use(){
        // Bind shader program
        glUseProgram(shaderProgramID);
    }

    public void detach(){
        glUseProgram(0);
    }

    public void uploadMat4f(String varName, Matrix4f mat4){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    private void checkShaderTypeInFile(String pattern, String pieceOfCode) throws IOException{
        if(pattern.equalsIgnoreCase("vertex")){
            this.vertexSource = pieceOfCode;
        }else if(pattern.equalsIgnoreCase("fragment")){
            this.fragmentSource = pieceOfCode;
        }else{
            throw new IOException("Unexpected token '" + pattern + "'");
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
            System.err.println("Error: '" + filePath + "'");
            System.err.println(glGetShaderInfoLog(id, len));
            throw new RuntimeException("Error: '" + filePath + "'\n\t" + type +" Shader compilation failed.");
        }
    }

    private void linkShaders(int vertexID, int fragmentID){
        //Link shaders and check for errors
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        //Check for linking errors
        int success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if(success == GL_FALSE){
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.err.println("Error: 'default.glsl'\n\tLinking Shaders failed.");
            System.err.println(glGetProgramInfoLog(shaderProgramID, len));
            throw new RuntimeException("Error: 'default.glsl'\n\tLinking Shaders failed.");
        }
    }

}
