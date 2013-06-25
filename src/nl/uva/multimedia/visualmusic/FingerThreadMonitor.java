package nl.uva.multimedia.visualmusic;

import android.view.SurfaceHolder;

/**
 * Created by klaplong on 6/20/13.
 */
public class FingerThreadMonitor {
    protected float x, y;
    protected int width, height;
    protected int fingerId;

    public FingerThreadMonitor() {
        this.x = 0;
        this.y = 0;
    }

    public FingerThreadMonitor(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static <T>T createInstance(Class c) throws InstantiationException,
            IllegalAccessException {
        T t = (T)c.newInstance();
        return t;
    }

    public void setFingerId(int fingerId) {
        this.fingerId = fingerId;
    }

    public int getFingerId() {
        return this.fingerId;
    }


    public void move(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setWidth(int width){
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}
