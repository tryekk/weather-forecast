package com.example.app_coursework.opengl;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Square {

    private final int mProgram;

    private int positionHandle;
    private int texCoordHandle;
    private int colorHandle;

// private final String vertexShaderCode =
// 	"precision mediump float;" +
//         "attribute vec3 vPosition;" +
// 	"attribute vec2 vTexCoord;" +
//         "out vec2 aTexCoord;" +
//         "void main() {" +
//         "  gl_Position = vPosition;" +
//         "  aTexCoord = vTexCoord;" + // We are in the space -1.0 - 1.0 so just transform this to 0.0 - 1.0 to get fragment coordinates.
//         "}";

//     private final String fragmentShaderCode =
//         "precision mediump float;" +
//         "in vec2 aTexCoord" +
//         "uniform sampler2D u_Texture;" +
//         "uniform float u_Time;" +
//         "float noise(float t)" +
//         "{" +
//         "    return fract(sin(t*100.0)*1000.0);" +
//         "}" +
//         "float noise2(vec2 p)" +
//         "{" +
//         "    return noise(p.x + noise(p.y));" +
//         "}" +
//         "float raindot(vec2 uv, vec2 id, float t) { " +
//         "    vec2 p = 0.1 + 0.8 * vec2(noise2(id), noise2(id + vec2(1.0, 0.0)));" +
//         "    float r = clamp(0.5 - mod(t + noise2(id), 1.0), 0.0, 1.0);" +
//         "    return smoothstep(0.3 * r, 0.0, length(p - uv));" +
//         "}" +
//         "float trailDrop(vec2 uv, vec2 id, float t) {" +
//         "    float f = clamp(noise2(id) - 0.5, 0.0, 1.0);" +
//         "	// wobbly path"
//          +
//         "float wobble = 0.5 + 0.2 " +
//         "        * cos(12.0 * uv.y) " +
//         "        * sin(50.0 * uv.y);" +
//         "    float v = 1.0 - 300.0 / f * pow(uv.x - 0.5 + 0.2 * wobble, 2.0);" +
//         "    // head" +
//         "    v *= clamp(30.0 * uv.y, 0.0, 1.0);" +
//         "    v *= clamp( uv.y + 7.0 * t - 0.6, 0.0, 1.0);" +
//         "    // tail" +
//         "    v *= clamp(1.0 - uv.y - pow(t, 2.0), 0.0, 1.0);" +
//         "    return f * clamp(v * 10.0, 0.0, 1.0);" +
//         "}" +
//         "void main()" +
//         "{" +
//         "	vec2 uv = aTexCoord.xy;" +
//         "	vec2 uv1 = vec2(uv.x * 20.0, uv.y * 1.3 + noise(floor(uv.x * 20.0)));" +
//         "    vec2 uvi = floor(vec2(uv1.x, uv1.y ));" +
//         "    vec2 uvf = uv1 - uvi;" +
//         "    float v = trailDrop(uvf, uvi, mod(u_Time + noise(floor(uv.x * 20.0)), 3.0) / 3.0);" +
//         "    v += raindot(fract(uv * 20.0 + vec2(0, 0.1 * u_Time)), floor(uv * 20.0 + vec2(0, 0.1 * u_Time)), u_Time);" +
//         // "    gl_FragColor = texture(u_Texture, uv + vec2(dFdx(v), dFdy(v)), 3.0 / (v + 1.0));" + // Use this line once you have textures setup.
//         "    gl_FragColor = vec4(0.8, 0.5, 0.5, 1.0) * (1.0 / distance(vec2(0.5, 0.5), uv);" +
//         "}";
	
    private final String vertexShaderCode =
	    "precision mediump float;" +
        "attribute vec3 vPosition;" +
	    "attribute vec2 vTexCoord;" +
        "out vec2 aTexCoord;" +
        "void main() {" +
        "  gl_Position = vPosition;" +
        "  aTexCoord = vTexCoord;" + // We are in the space -1.0 - 1.0 so just transform this to 0.0 - 1.0 to get fragment coordinates.
        "}";

    private final String fragmentShaderCode =
        "precision mediump float;" +
        "in vec2 aTexCoord" +
        "void main()" +
        "{" +
        "    gl_FragColor = vec4(0.8, 0.5, 0.5, 1.0) * (1.0 / (distance(vec2(0.5, 0.5), aTexCoord) + 0.01);" +
        "}";


    private FloatBuffer vertexBuffer;
	
    // number of values per vertex in this array
    static final int POSITION_SIZE = 3;
    static final int TEXCOORD_SIZE = 2
    static final int VERTEX_STRIDE = (POSITION_SIZE + TEXCOORD_SIZE) * 4;
    static final int VERTEX_COUNT = 6;


    static float vertices[] = {
            -0.5f,  0.5f, 0.0f, 0.0f, 1.0f,   // top left
            -0.5f, -0.5f, 0.0f, 0.0f, 0.0f,   // bottom left
             0.5f, -0.5f, 0.0f, 1.0f, 0.0f,   // bottom right

             0.5f, -0.5f, 0.0f, 1.0f, 0.0f,   // bottom right
             0.5f,  0.5f, 0.0f, 1.0f, 1.0f,   // top right
            -0.5f,  0.5f, 0.0f, 0.0f, 1.0f }; // top left

    public Square() {
	    
        // initialize vertex byte buffer for shape coordinates  (vertex buffer)
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4); // (# of coordinate values * 4 bytes per float)
        byteBuffer.order(ByteOrder.nativeOrder());
	    
        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // Adding shaders
        int vertexShader = OpenGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = OpenGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);
    }

    public void draw() {
	    
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(positionHandle);
        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(positionHandle, POSITION_SIZE,
                GLES20.GL_FLOAT, false,
                VERTEX_STRIDE, vertexBuffer);
	    
	
	 // get handle to vertex shader's vTexCoord member
        texCoordHandle = GLES20.glGetAttribLocation(mProgram, "vTexCoord");
	// Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(texCoordHandle);
	// Prepare the triangle coordinate data
	GLES20.glVertexAttribPointer(texCoordHandle, TEXCOORD_SIZE,
                GLES20.GL_FLOAT, false,
                VERTEX_STRIDE, vertexBuffer);

        // Draw the square
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, VERTEX_COUNT);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(texCoordHandle);
    }

}
