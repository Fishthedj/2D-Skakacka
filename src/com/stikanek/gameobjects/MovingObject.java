package com.stikanek.gameobjects;

import com.stikanek.collisions.AABB;
import com.stikanek.math.Vec2;

public abstract class MovingObject extends GameObject{
    protected Vec2 direction;
        
    public abstract Vec2 getPredictedCenterPosition();
    
    public abstract Vec2 getCurrentDirection();
    
    public abstract AABB getPredictedAABB();
    
    public Vec2 getDirection(){
        return new Vec2(direction);
    }    
}
