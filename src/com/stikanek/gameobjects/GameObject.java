package com.stikanek.gameobjects;

import com.stikanek.math.Vec2;
import com.stikanek.collisions.AABB;

public abstract class GameObject {
    protected Vec2 center;
    protected AABB aabb;

    public abstract Vec2 getCurrentCenterPosition();
    
    public AABB getAabb(){
        return new AABB(aabb);
    }    
    
    public void setCenter(Vec2 center){
        this.center = center;
    }
    
    public Vec2 getCenter(){
        return new Vec2(center);
    }
}
