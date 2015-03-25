package com.stikanek.collisions;

import com.stikanek.math.Vec2;

public class AabbToAabbParameters {
    private int distance;
    private Vec2 planeCenter;
    
    public AabbToAabbParameters(int distance, Vec2 planeCenter){
        this.distance = distance;
        this.planeCenter = planeCenter;
    }
    
    public int getDistance(){
        return distance;
    }
    
    public Vec2 getPlaneCenter(){
        return planeCenter;
    }    
}
