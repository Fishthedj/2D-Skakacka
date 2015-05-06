package com.stikanek.collisions;

import com.stikanek.gameobjects.GameObject;
import com.stikanek.gameobjects.MovingObject;
import com.stikanek.math.Vec2;
import com.stikanek.tiles.Tile;
import com.stikanek.tiles.Tile.TileType;
import com.stikanek.tiles.TileMap;
import com.stikanek.gameobjects.StaticObject;
        

public class Collisions {
    //singleton
    private Vec2 maximumMovement = new Vec2();
    boolean verticalInnerCollide = false;
    private final TileMap map;

    public Collisions(TileMap map){
        this.map = map;
    }
    
    public Vec2 getMaximumMovement(MovingObject mo){
        Vec2 adjustedMaxMovement = getMaximumPossibleMovement(mo);
        Vec2 originalDirection = mo.getDirection();
        Vec2 maxMovementWithZeroY;
        Vec2 maxMovementWithZeroX;       
        if(adjustedMaxMovement.getY() == 0 && originalDirection.getY() != 0){
//            verticalInnerCollide = true;
            mo.setDirection(new Vec2(originalDirection.getX(), 0));
            maxMovementWithZeroY = getMaximumPossibleMovement(mo);
            mo.setDirection(originalDirection);
            if(maxMovementWithZeroY.getX() != 0)
                return maxMovementWithZeroY;
        }
        if(adjustedMaxMovement.getX() == 0 && originalDirection.getX() != 0){
//            verticalInnerCollide = true;
            mo.setDirection(new Vec2(0, originalDirection.getY()));
            maxMovementWithZeroX = getMaximumPossibleMovement(mo);
            mo.setDirection(originalDirection);
            if(maxMovementWithZeroX.getY() != 0)
                return maxMovementWithZeroX;
        }
            return adjustedMaxMovement;
    }
    
    public Vec2 getMaximumPossibleMovement(MovingObject mo){
        maximumMovement = mo.getCurrentDirection();
        Vec2 tempMaximumMovement = new Vec2(maximumMovement);
        Vec2 currentPosition = mo.getCurrentCenterPosition();
        Vec2 predictedPosition = mo.getPredictedCenterPosition();
        Vec2 min = currentPosition.min(predictedPosition);//upper-left position of collision area
        Vec2 max = currentPosition.max(predictedPosition);//lower-right position of collision area
        
        min.addTo(mo.getAabb().getHalfExtent().getNegation());
        min.addTo(new Vec2(1,1));
        max.addTo(mo.getAabb().getHalfExtent());
        max.addTo(new Vec2(-1,-1));
        
        //get tiles in collision area
        int firstTileX = Math.max(map.worldCoordToTileX(min.getX()), 0);
        int firstTileY = Math.max(map.worldCoordToTileY(min.getY()), 0);
        
        int lastTileX = Math.min(map.worldCoordToTileX(max.getX()), map.getColumnsInMap() - 1);
        int lastTileY = Math.min(map.worldCoordToTileY(max.getY()), map.getRowsInMap() - 1);        
        
        //for all tiles in collision area
        for(int row = firstTileY; row <= lastTileY; row++){
            for(int column = firstTileX; column <= lastTileX; column++){
                if(map.getTileType(row, column) == TileType.BLOCKING){
                    Vec2 center = new Vec2(map.tileCoordToWorldX(column),map.tileCoordToWorldY(row));
                    Vec2 halfExtent = new Vec2(map.getTileWidth() / 2, map.getTileHeight() / 2);
                    CollisionTile collisionTile = new CollisionTile(center, halfExtent);
                    CollisionParams collisionParams = willCollide(mo, collisionTile);
 
                    if(collisionParams.collided){
                        if(Math.abs(collisionParams.getMaximumMovement().getX()) < Math.abs(tempMaximumMovement.getX()))
                            tempMaximumMovement.setX(collisionParams.getMaximumMovement().getX());
                        if(Math.abs(collisionParams.getMaximumMovement().getY()) < Math.abs(tempMaximumMovement.getY()))
                            tempMaximumMovement.setY(collisionParams.getMaximumMovement().getY());
                    }
                    verticalInnerCollide = false;
                }
            }
        }
//        return maximumMovement;
        return tempMaximumMovement;
    }
        
    private class CollisionTile extends StaticObject{
        public CollisionTile(Vec2 center, Vec2 halfExtent){
            this.center = center;
            aabb = new AABB(center, halfExtent);
        }
        
        @Override
        public Vec2 getCurrentCenterPosition(){
            return new Vec2(center);
        }
    }
    
    public CollisionParams willCollide(MovingObject gofst, GameObject goscnd){
        AabbToAabbParameters params = getPointToPlaneParameters(goscnd.getAabb(), gofst);
        return checkCollision(gofst, params);
    }
    public AabbToAabbParameters getPointToPlaneParameters(AABB aabbscnd, MovingObject mo){
        AABB aabbfst = mo.getPredictedAABB();
        Vec2 expandedExtent = aabbscnd.getHalfExtent().addTo(aabbfst.getHalfExtent());      
        Vec2 expandedCenter = aabbscnd.getCenter();
        Vec2 centersDifference = expandedCenter.sub(aabbfst.getCenter());
        Vec2 initialCenterDifferences = expandedCenter.sub(mo.getCenter());
        int halfExtentX = aabbscnd.getHalfExtent().getX();
        int halfExtentY = aabbscnd.getHalfExtent().getY();
        int moHalfExtentX = mo.getAabb().getHalfExtent().getX();
        int moHalfExtentY = mo.getAabb().getHalfExtent().getY();
        if(initialCenterDifferences.getX() > -halfExtentX - moHalfExtentX && initialCenterDifferences.getX() < halfExtentX + moHalfExtentX &&
                initialCenterDifferences.getY() - moHalfExtentY - halfExtentY <= 0 && initialCenterDifferences.getY() - moHalfExtentY - halfExtentY >= -2)
             mo.setCanJump(true);        
        Vec2 normalPlane = centersDifference.getMajorAxis().getNegationTo();
        Vec2 minorNormalPlane = centersDifference.getMinorAxis().getNegationTo();
//        int yDistance = centersDifference.dotProduct(new Vec2(0,1));  
        int distance = getDifferenceFromPointToPlane(normalPlane, expandedCenter, expandedExtent, aabbfst.getCenter());
        int minorDistance = getDifferenceFromPointToPlane(minorNormalPlane, expandedCenter, expandedExtent, aabbfst.getCenter());
        AabbToAabbParameters positionParameters = new AabbToAabbParameters(distance, minorDistance, normalPlane, minorNormalPlane);
        
//        
//        System.out.println("distance:" + distance);
//        System.out.println("minorDistance: " + minorDistance);
//        if(centersDifference.getY() >= 0 && (distance <= 0 || minorDistance <= 0) && !verticalInnerCollide){
//            mo.setCanJump(true);
//            System.out.println("condition true");
////        verticalInnerCollide = false;
//        }
        return positionParameters;
    }
    
    public int getDifferenceFromPointToPlane(Vec2 normalPlane, Vec2 aabbCenter, Vec2 aabbHalfExtent, Vec2 point){
        Vec2 planeCenter = normalPlane.mul(aabbHalfExtent).addTo(aabbCenter);
        Vec2 planeToPointDifference = point.sub(planeCenter);
        int difference = planeToPointDifference.dotProduct(normalPlane);
        return difference;
    }
         
    public CollisionParams checkCollision(MovingObject movingObject, AabbToAabbParameters pointToPlaneParams){
        CollisionParams collisionParams = new CollisionParams(false, movingObject.getCurrentDirection());
        int difference = pointToPlaneParams.getDistance();
        boolean collidedMajorPlane = Math.signum(difference) == -1;
        if(collidedMajorPlane){
            if(pointToPlaneParams.getPlaneCenter().getX() == 0){
                collisionParams.setMaximumMovementY(Math.signum(movingObject.getCurrentDirection().getY()) == 1.0? 
                        movingObject.getCurrentDirection().getY() + difference : movingObject.getCurrentDirection().getY() - difference);
            }else{
                collisionParams.setMaximumMovementX(Math.signum(movingObject.getCurrentDirection().getX()) == 1.0? 
                        movingObject.getCurrentDirection().getX() + difference : movingObject.getCurrentDirection().getX() - difference);
            }
        }
        int minorDifference = pointToPlaneParams.getMinorDistance();
        boolean collidedMinorPlane = Math.signum(minorDifference) == -1;
        if(collidedMinorPlane){
            if(pointToPlaneParams.getMinorPlane().getX() == 0){
                collisionParams.setMaximumMovementY(Math.signum(movingObject.getCurrentDirection().getY()) == 1.0? 
                        movingObject.getCurrentDirection().getY() + minorDifference : movingObject.getCurrentDirection().getY() - minorDifference);
            }else{
                collisionParams.setMaximumMovementX(Math.signum(movingObject.getCurrentDirection().getX()) == 1.0? 
                        movingObject.getCurrentDirection().getX() + minorDifference : movingObject.getCurrentDirection().getX() - minorDifference);    
            }
        }
        collisionParams.setCollided(collidedMajorPlane || collidedMinorPlane);
        return collisionParams;
    }      
    
    public int getSpeedInDirectionOfNormalPlane(MovingObject movingObject, AabbToAabbParameters pointToPlaneParams){
        if(pointToPlaneParams.getPlaneCenter().getX() == 0){
//            return movingObject.getDirection().getY();
            return movingObject.getCurrentDirection().getY();
        }else{
//            return movingObject.getDirection().getX();
            return movingObject.getCurrentDirection().getX();
        }
    }
    
    public class CollisionParams{
        boolean collided;
        Vec2 maximumMovement;
        
        CollisionParams(){
            this.collided = false;
            this.maximumMovement = new Vec2();
        }
        CollisionParams(boolean collided, Vec2 maximumMovement){
            this.collided = collided;
            this.maximumMovement = maximumMovement;
        }
        
        public void setCollided(boolean collided){
            this.collided = collided;
        }
        
        public void setMaximumMovement(Vec2 maximumMovement){
            this.maximumMovement = maximumMovement;
        }
        
        public void setMaximumMovementX(int x){
            maximumMovement.setX(x);
        }
        
        public void setMaximumMovementY(int y){
            maximumMovement.setY(y);
        }
        
        boolean getCollided(){
            return collided;
        }
        
        Vec2 getMaximumMovement(){
            return maximumMovement;
        }
        
    }
    //function that returns maximum possible movement in case of collision    
}
