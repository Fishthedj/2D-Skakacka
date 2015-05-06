package com.stikanek.gameobjects;

import com.stikanek.collisions.AABB;
import com.stikanek.math.Vec2;
//TODO: consider adding abstract method public void update()
public abstract class MovingObject extends GameObject{
    protected Vec2 direction;
    protected boolean canJump;
    
//    protected MovingObject(Vec2 center, Vec2 halfExtent){
//        super(center, halfExtent);
//    }
    public abstract Vec2 getPredictedCenterPosition();
    
    public abstract Vec2 getCurrentDirection();
    
    public abstract AABB getPredictedAABB();
    
    public void setCanJump(boolean canJump){
        this.canJump = canJump;
    }
    
    public Vec2 getDirection(){
        return new Vec2(direction);
    }    
    
    public void setDirection(Vec2 direction){
        this.direction = direction;
    }
    
    public void applyAcceleration(Vec2 acceleration){
        direction.addTo(acceleration);
    }
}
