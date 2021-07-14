package com.example.app_coursework.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import com.example.app_coursework.MainActivity;
import com.example.app_coursework.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Calendar;
import java.util.Date;

public class Square {

    float[] vertices = {
            -1.0f, 1.0f, 0.0f, 0.0f, 1.0f,   // top left
            -1.0f, -1.0f, 0.0f, 0.0f, 0.0f,   // bottom left
            1.0f, -1.0f, 0.0f, 1.0f, 0.0f,   // bottom right

            1.0f, -1.0f, 0.0f, 1.0f, 0.0f,   // bottom right
            1.0f, 1.0f, 0.0f, 1.0f, 1.0f,   // top right
            -1.0f, 1.0f, 0.0f, 0.0f, 1.0f}; // top left

    private static final int mPositionSize = 3;
    private static final int mTexCoordSize = 2;
    private static final int mStrideBytes = (mPositionSize + mTexCoordSize) * 4;

    private Context mActivityContext;

    private final int mProgramHandle;
    private final int mPositionHandle;
    private final int mTexCoordHandle;
    private final int mTimeHandle;
    private final int mTextureHandle;

    private final int mTexture;

    private float timeCounter = 0.0f;

    private final String vertexShaderCode =
                    "precision mediump float;                   \n" +
                    "attribute vec3 a_Position;                 \n" +
                    "attribute vec2 a_TexCoord;                 \n" +
                    "varying vec2 v_TexCoord;                   \n" +
                    "void main()                                \n" +
                    "{                                          \n" +
                    "   gl_Position = vec4(a_Position, 1.0);    \n" +
                    "   v_TexCoord = a_TexCoord;                \n" +
                    "}                                            ";
//    private final String fragmentShaderCode =
////                    "#extension GL_EXT_shader_texture_lod : require\n" +
////                    "#extension GL_OES_standard_derivatives : require\n" +
//                    "precision mediump float;\n" +
//                    "varying vec2 v_TexCoord;\n" +
//                    "uniform sampler2D u_Texture;\n" +
//                    "uniform float u_Time;\n" +
//                    "void main()\n" +
//                    "{\n" +
//                    "   gl_FragColor = vec4(.2 * abs(sin(1.5 + u_Time * .7)), .2 * abs(sin(u_Time * .4)), .2 * abs(cos(u_Time * .3)), 1.0) / (distance(vec2(abs(sin(0.5 + u_Time * .6)), abs(cos(1.7 + u_Time * .4))), v_TexCoord) + 0.1);" +
//                    "}";

    private final String fragmentShaderCode =
            "precision highp float noise(float t) { return fract(sin(t*100.0)*1000.0); }\n" +

            "float noise2(vec2 p) { return noise(p.x + noise(p.y)); }\n" +
            "float raindot(vec2 uv, vec2 id, float t) {\n" +
                "vec2 p = 0.1 + 0.8 * vec2(noise2(id), noise2(id + vec2(1.0, 0.0)));\n" +
                "float r = clamp(0.5 - mod(t + noise2(id), 1.0), 0.0, 1.0);\n" +
                "return smoothstep(0.3 * r, 0.0, length(p - uv));\n" +
            "}\n" +

            "float trailDrop(vec2 uv, vec2 id, float t) {\n" +
                "float f = clamp(noise2(id) - 0.5, 0.0, 1.0);\n" +
                // wobbly path
                "float wobble = 0.5 + 0.2\n" +
                        "* cos(12.0 * uv.y)\n" +
                        "* sin(50.0 * uv.y);\n" +
                "float v = 1.0 - 300.0 / f * pow(uv.x - 0.5 + 0.2 * wobble, 2.0);\n" +
                // head
                "v *= clamp(30.0 * uv.y, 0.0, 1.0);\n" +
                "v *= clamp( uv.y + 7.0 * t - 0.6, 0.0, 1.0);\n" +
                // tail
                "v *= clamp(1.0 - uv.y - pow(t, 2.0), 0.0, 1.0);\n" +
                "return f * clamp(v * 10.0, 0.0, 1.0);\n" +
            "}\n" +

            "void mainImage( out vec4 fragColor, in vec2 fragCoord )\n" +
            "{\n" +
                "vec2 uv = fragCoord.xy / iResolution.xy;\n" +
                "vec2 uv1 = vec2(uv.x * 20.0, uv.y * 1.3 + noise(floor(uv.x * 20.0)));\n" +
                "vec2 uvi = floor(vec2(uv1.x, uv1.y ));\n" +
                "vec2 uvf = uv1 - uvi;\n" +
                "float v = trailDrop(uvf, uvi, mod(iTime + noise(floor(uv.x * 20.0)), 3.0) / 3.0);\n" +
                "v += raindot(fract(uv * 20.0 + vec2(0, 0.1 * iTime)), floor(uv * 20.0 + vec2(0, 0.1 * iTime)), iTime);\n" +
                "fragColor = textureLod(iChannel0, uv + vec2(dFdx(v), dFdy(v)), 3.0 / (v + 1.0));\n" +
        "}\n";

    public Square(Context activityContext) {

        this.mActivityContext = activityContext;

        // Load in the vertex shader.
        int vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);

        if (vertexShaderHandle != 0)
        {
            // Pass in the shader source.
            GLES20.glShaderSource(vertexShaderHandle, vertexShaderCode);

            // Compile the shader.
            GLES20.glCompileShader(vertexShaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(vertexShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0) {
                String status = GLES20.glGetShaderInfoLog(vertexShaderHandle);
                Log.e("Vertex Shader", status);

                GLES20.glDeleteShader(vertexShaderHandle);
            }
        }

        // Load in the fragement shader.
        int fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

        if (fragmentShaderHandle != 0)
        {
            // Pass in the shader source.
            GLES20.glShaderSource(fragmentShaderHandle, fragmentShaderCode);

            // Compile the shader.
            GLES20.glCompileShader(fragmentShaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(fragmentShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0) {
                String status = GLES20.glGetShaderInfoLog(fragmentShaderHandle);
                Log.e("Fragment Shader", status);

                GLES20.glDeleteShader(fragmentShaderHandle);
            }
        }

        // Create a program object and store the handle to it.
        mProgramHandle = GLES20.glCreateProgram();

        if (mProgramHandle != 0)
        {
            // Bind the vertex shader to the program.
            GLES20.glAttachShader(mProgramHandle, vertexShaderHandle);

            // Bind the fragment shader to the program.
            GLES20.glAttachShader(mProgramHandle, fragmentShaderHandle);

            // Bind attributes
            GLES20.glBindAttribLocation(mProgramHandle, 0, "a_Position");
            GLES20.glBindAttribLocation(mProgramHandle, 1, "a_TexCoord");

            // Link the two shaders together into a program.
            GLES20.glLinkProgram(mProgramHandle);

            // Get the link status.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(mProgramHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

            // If the link failed, delete the program.
            if (linkStatus[0] == 0) {
                String status = GLES20.glGetShaderInfoLog(mProgramHandle);
                Log.e("Shader", status);

                GLES20.glDeleteProgram(mProgramHandle);
            }
        }

        GLES20.glUseProgram(mProgramHandle);

        mTimeHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_Time");
        mTextureHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_Texture");

        mTexture = loadTexture(mActivityContext, R.drawable._80828_wr2018cover);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexture);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glUniform1i(mTextureHandle, 0);

        FloatBuffer vertexBuffer = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexBuffer.put(vertices).position(0);

        mPositionHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Position");
        mTexCoordHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_TexCoord");

        // Pass in the position information
        vertexBuffer.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionSize, GLES20.GL_FLOAT, false,
                mStrideBytes, vertexBuffer);

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Pass in the color information
        vertexBuffer.position(3);
        GLES20.glVertexAttribPointer(mTexCoordHandle, mTexCoordSize, GLES20.GL_FLOAT, false,
                mStrideBytes, vertexBuffer);

        GLES20.glEnableVertexAttribArray(mTexCoordHandle);
    }

    public void draw() {

        // Update time.
        timeCounter += 1f / 60f;

        GLES20.glUseProgram(mProgramHandle);

        GLES20.glUniform1f(mTimeHandle, timeCounter);

        // GLES20.glBindTexture(mTexture, GLES20.GL_TEXTURE_2D);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
    }

    public static int loadTexture(final Context context, final int resourceId)
    {
        final int[] textureHandle = new int[1];

        GLES20.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] != 0)
        {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;   // No pre-scaling

            // Read in the resource
            final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

            // Bind to the texture in OpenGL
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

            // Set filtering
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle();
        }

        if (textureHandle[0] == 0)
        {
            throw new RuntimeException("Error loading texture.");
        }

        return textureHandle[0];
    }

}
