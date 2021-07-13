package com.example.app_coursework.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import com.example.app_coursework.MainActivity;
import com.example.app_coursework.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Calendar;
import java.util.Date;

public class Square {

    float[] vertices = {
            -1.0f, 1.0f, 0.0f, 0.0f, 0.0f,   // top left
            -1.0f, -1.0f, 0.0f, 0.0f, 1.0f,   // bottom left
            1.0f, -1.0f, 0.0f, 1.0f, 1.0f,   // bottom right

            1.0f, -1.0f, 0.0f, 1.0f, 1.0f,   // bottom right
            1.0f, 1.0f, 0.0f, 1.0f, 0.0f,   // top right
            -1.0f, 1.0f, 0.0f, 0.0f, 0.0f}; // top left

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

    public Square(Context activityContext) {

        this.mActivityContext = activityContext;

        StringBuilder builder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(mActivityContext.getAssets().open("rain.vs")))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String vertexShaderCode = builder.toString();

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

        builder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(mActivityContext.getAssets().open("rain.fs")))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String fragmentShaderCode = builder.toString();

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


        mTexture = loadTexture(mActivityContext, R.drawable.london);
        mTextureHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_Texture");
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

        GLES20.glBindTexture(mTexture, GLES20.GL_TEXTURE_2D);

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
