package com.stikanek.gameobjects;

import com.stikanek.math.Vec2;
import com.stikanek.collisions.AABB;
import java.awt.Graphics2D;

public abstract class GameObject {
    protected Vec2 center;
    protected Vec2 halfExtent;
    protected AABB aabb;
    
//    protected GameObject(Vec2 center, Vec2 halfExtent){
//        this.center = center;
//        aabb = new AABB(center, halfExtent);
//    }

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
