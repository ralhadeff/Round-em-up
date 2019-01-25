package com.raphael.roundemup;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.raphael.framework.Screen;
import com.raphael.framework.impl.GLGame;

public class Main extends GLGame {
    boolean firstTimeCreate = true;
    
    public Screen getStartScreen() {
        return new GameScreen(this);
    }
    
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {         
        super.onSurfaceCreated(gl, config);
        if(firstTimeCreate) {
            Assets.load(this);
            firstTimeCreate = false;            
        } else {
            Assets.reload();
        }
    }     
    
    @Override
    public void onPause() {
        super.onPause();
    }
}
