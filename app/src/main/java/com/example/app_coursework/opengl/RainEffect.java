package com.example.app_coursework.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import com.example.app_coursework.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class RainEffect {

    private static final int sPositionSize = 3;
    private static final int sStrideBytes = (sPositionSize) * 4;

    private static final float[] sVertices = {
            -1.0f,  1.0f, 0.0f, // top left
            -1.0f, -1.0f, 0.0f, // bottom left
             1.0f, -1.0f, 0.0f, // bottom right

             1.0f, -1.0f, 0.0f, // bottom right
             1.0f,  1.0f, 0.0f, // top right
            -1.0f,  1.0f, 0.0f  // top left
    };

    private final Context mActivityContext;

    private int mProgramHandle;

    private int mTimeLoc;
    private int mResolutionLoc;
    private int mTextureLoc;

    private float mTime;

    public RainEffect(Context activityContext, int width, int height) {
        this.mActivityContext = activityContext;

        init(width, height);
    }

    private void init(int width, int height) {
        String vertexSource = loadSource(mActivityContext, "rain.vs");

        // Load in the vertex shader.
        int vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);

        if (vertexShaderHandle != 0)
        {
            // Pass in the shader source.
            GLES20.glShaderSource(vertexShaderHandle, vertexSource);

            // Compile the shader.
            GLES20.glCompileShader(vertexShaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(vertexShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0) {
                String status = GLES20.glGetShaderInfoLog(vertexShaderHandle);
                Log.e("[Vertex Shader]", status);

                GLES20.glDeleteShader(vertexShaderHandle);
            }
        }

        String fragmentSource = loadSource(mActivityContext, "rain.fs");

        // Load in the fragement shader.
        int fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

        if (fragmentShaderHandle != 0)
        {
            // Pass in the shader source.
            GLES20.glShaderSource(fragmentShaderHandle, fragmentSource);

            // Compile the shader.
            GLES20.glCompileShader(fragmentShaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(fragmentShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0) {
                String status = GLES20.glGetShaderInfoLog(fragmentShaderHandle);
                Log.e("[Fragment Shader]", status);

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

            // Link the two shaders together into a program.
            GLES20.glLinkProgram(mProgramHandle);

            // Get the link status.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(mProgramHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

            // If the link failed, delete the program.
            if (linkStatus[0] == 0) {
                String status = GLES20.glGetShaderInfoLog(mProgramHandle);
                Log.e("[Shader Link]", status);

                GLES20.glDeleteProgram(mProgramHandle);
            }
        }

        GLES20.glUseProgram(mProgramHandle);

        mTimeLoc = GLES20.glGetUniformLocation(mProgramHandle, "u_Time");
        mResolutionLoc = GLES20.glGetUniformLocation(mProgramHandle, "u_Resolution");
        mTextureLoc = GLES20.glGetUniformLocation(mProgramHandle, "u_Texture");

        GLES20.glUniform2f(mResolutionLoc, width, height);

        int texture = loadTexture(mActivityContext, R.drawable.radek_vebr_pimy68gnh9o_unsplash);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glUniform1i(mTextureLoc, 0);

        FloatBuffer vertexBuffer = ByteBuffer.allocateDirect(sVertices.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexBuffer.put(sVertices).position(0);

        int positionHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Position");

        // Pass in the position information
        vertexBuffer.position(0);
        GLES20.glVertexAttribPointer(positionHandle, sPositionSize, GLES20.GL_FLOAT, false,
                sStrideBytes, vertexBuffer);

        GLES20.glEnableVertexAttribArray(positionHandle);
    }

    public void onResolutionChanged(int width, int height) {
        // Update resolution uniform
        GLES20.glUniform2f(mResolutionLoc, width, height);
    }

    public void render() {
        mTime += 1f / 60f;

        // Update time uniform
        GLES20.glUniform1f(mTimeLoc, mTime);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
    }

    private static String loadSource(Context activityContext, String path) {
        StringBuilder builder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(activityContext.getAssets().open(path)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        } catch (IOException e) {
            Log.e("[Load Shader]", "Failed to load shader source for file \"" + path + "\". " + e.getMessage());
        }

        return builder.toString();
    }

    private static int loadTexture(final Context context, final int resourceId)
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
