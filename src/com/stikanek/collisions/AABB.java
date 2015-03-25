package com.stikanek.collisions;

import com.stikanek.math.Vec2;

public class AABB {
   private Vec2 center;
    private Vec2 halfExtent;
    
    public AABB(){
        this(null, null);
    }
    
    public AABB(Vec2 center, Vec2 halfExtent){
        this.center = center;
        this.halfExtent = halfExtent;
    }
    
    public AABB(AABB aabb){
        this(aabb.center, aabb.halfExtent);
    }
    
    public void setCenter(Vec2 center){
        this.center = center;
    }
    
    public void setHalfCenter(Vec2 halfExtend){
        this.halfExtent = halfExtend;
    }
    
    public Vec2 getCenter(){
        return new Vec2(center.getX(), center.getY());
    }
    
    public Vec2 getHalfExtent(){
        return new Vec2(halfExtent.getX(), halfExtent.getY());
    }    
}
