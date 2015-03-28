package com.stikanek.math;

public class Vec2 {
    private int x;
    private int y;
    
    public Vec2(){
        this(0,0);
    }
    
    public Vec2(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    public Vec2(Vec2 vec){
        x = vec.x;
        y = vec.y;
    }
    public Vec2 add(Vec2 vec){
        return new Vec2(x + vec.x, y + vec.y);
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public Vec2 addTo(Vec2 vec){
        x += vec.x;
        y += vec.y;
        return this;
    }
    
    public Vec2 subFrom(Vec2 vec){
        x -= vec.x;
        y -= vec.y;
        return this;
    }
    
    public void setX(int x){
        this.x = x;
    }
    
    public void setY(int y){
        this.y = y;
    }
    
    public Vec2 addX(int ix){
        return new Vec2(x + ix, y);
    }
    
    public void addXTo(int x){
        this.x += x;
    }
    
    public void addYTo(int y){
        this.y += y;
    }
    
    public Vec2 addY(int iy){
        return new Vec2(x, y + iy);
    }
    
    public Vec2 subX(int ix){
        return new Vec2(x - ix, y);
    }
    
    public Vec2 subY(int iy){
        return new Vec2(x, y - iy);
    }
    
    public int dotProduct(Vec2 v){
        return x * v.x + y * v.y;
    }
    
    public Vec2 getNegationTo(){
        x = -x;
        y = -y;
        return this;
    }
    
    public Vec2 getNegation(){
        return new Vec2(-x, -y);
    }
    
    public Vec2 getMajorAxis(){
        if(Math.abs(x) > Math.abs(y))
            return new Vec2(Scalars.sign(x), 0);
        else
            return new Vec2(0, Scalars.sign(y));
    }
    
    public Vec2 mul(Vec2 coefficient){
        return new Vec2(x * coefficient.x, y * coefficient.y);
    }
    
    public Vec2 sub(Vec2 subtrahend){
        return new Vec2(x - subtrahend .x, y - subtrahend.y);
    }
    
    public Vec2 maxTo(Vec2 v){
        x = Math.max(x, v.x);
        y = Math.max(y, v.y);
        return this;
    }
    
    public Vec2 minTo(Vec2 v){
        x = Math.min(x, v.x);
        y = Math.min(y, v.y);
        return this;
    }
    
    public Vec2 max(Vec2 v){
        return new Vec2(x,y).maxTo(v);
    }
    
    public Vec2 min(Vec2 v){
        return new Vec2(x,y).minTo(v);
    }
    
    public void setXY(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    @Override
    public boolean equals(Object obj){
        if(obj instanceof Vec2){
            return x == ((Vec2)obj).x && y == ((Vec2)obj).y;
        }else
            return false;
    }
}
