package com.stikanek.collisions;

import com.stikanek.math.Vec2;

public class AabbToAabbParameters {
    private int distance;
    private int minorDistance;
    private Vec2 planeCenter;
    private Vec2 minorPlane;
    
    public AabbToAabbParameters(int distance, int minorDistance, Vec2 planeCenter, Vec2 minorPlane){
        this.distance = distance;
        this.minorDistance = minorDistance;
        this.planeCenter = planeCenter;
        this.minorPlane = minorPlane;
    }
    
    public int getDistance(){
        return distance;
    }
    
    public int getMinorDistance(){
        return minorDistance;
    }
    
    public Vec2 getPlaneCenter(){
        return planeCenter;
    }    
    
    public Vec2 getMinorPlane(){
        return minorPlane;
    }
    
}
