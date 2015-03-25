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
    private final TileMap map;

    public Collisions(TileMap map){
        this.map = map;
    }
    public Vec2 getMaximumPossibleMovement(MovingObject mo){
//        maximumMovement = mo.getDirection(); PICOVINA
        maximumMovement = mo.getCurrentDirection();
        Vec2 currentPosition = mo.getCurrentCenterPosition();
        System.out.println("currentpos x: " + currentPosition.getX());//DELETE
        System.out.println("currentpos y: " + currentPosition.getY());//DELETE
        Vec2 predictedPosition = mo.getPredictedCenterPosition();
        System.out.println("predictedpos x: " + predictedPosition.getX());
        System.out.println("predictedpos y: " + predictedPosition.getY());
        Vec2 min = currentPosition.min(predictedPosition);//upper-left position of collision area
        Vec2 max = currentPosition.max(predictedPosition);//lower-right position of collision area
        //TODO: addTo halfExtent max, subFrom halfExtent min
        min.addTo(mo.getAabb().getHalfExtent().getNegation());
        System.out.println("minx: " + min.getX());//DELETE
        System.out.println("miny: " + min.getY());//DELETE
        max.addTo(mo.getAabb().getHalfExtent());
        System.out.println("maxx: " + max.getX());//DELETE  
        System.out.println("maxy: " + max.getY());//DELETE
        
        //get tiles in collision area
        int firstTileX = map.worldCoordToTileX(min.getX());
        int firstTileY = map.worldCoordToTileY(min.getY());
        System.out.println("firstTileX: " + firstTileX); //DELETE %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
        System.out.println("firstTileY: " + firstTileY);//DELETE @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        
        int lastTileX = map.worldCoordToTileX(max.getX());
        int lastTileY = map.worldCoordToTileY(max.getY());
        System.out.println("lastTileX: " + lastTileX); //DELETE %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
        System.out.println("lastTileY: " + lastTileY);//DELETE @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@    
        System.out.println("------------------------------------------------------------------------");
        
        //for all tiles in collision area
        for(int row = firstTileY; row <= lastTileY; row++){
            for(int column = firstTileX; column <= lastTileX; column++){
                if(map.getTileType(row, column) == TileType.BLOCKING){
                    Vec2 center = new Vec2(map.tileCoordToWorldX(column),map.tileCoordToWorldY(row));
                    Vec2 halfExtent = new Vec2(map.getTileWidth() / 2, map.getTileHeight() / 2);
                    CollisionTile collisionTile = new CollisionTile(center, halfExtent);
                    CollisionParams collisionParams = willCollide(mo, collisionTile);
                    if(collisionParams.collided){
                        maximumMovement = collisionParams.getMaximumMovement();
                    }
                }
            }
        }
        return maximumMovement;
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
        AabbToAabbParameters params = getPointToPlaneParameters(gofst.getAabb(), goscnd.getAabb());
        return checkCollision(gofst, params);
    }
    public AabbToAabbParameters getPointToPlaneParameters(AABB aabbfst, AABB aabbscnd){
        Vec2 expandedExtent = aabbscnd.getHalfExtent().addTo(aabbfst.getHalfExtent());      
        Vec2 expandedCenter = aabbscnd.getCenter();
        Vec2 centersDifference = expandedCenter.sub(aabbfst.getCenter());       
        Vec2 normalPlane = centersDifference.getMajorAxis().getNegationTo();
        int distance = getDifferenceFromPointToPlane(normalPlane, expandedCenter, expandedExtent, aabbfst.getCenter());
        AabbToAabbParameters positionParameters = new AabbToAabbParameters(distance, normalPlane);
        return positionParameters;
    }
    
    public int getDifferenceFromPointToPlane(Vec2 normalPlane, Vec2 aabbCenter, Vec2 aabbHalfExtent, Vec2 point){
        Vec2 planeCenter = normalPlane.mul(aabbHalfExtent).addTo(aabbCenter);
        Vec2 planeToPointDifference = point.sub(planeCenter);
        int difference = Math.abs(planeToPointDifference.getX()) > 0 ? planeToPointDifference.getX() : planeToPointDifference.getY();
        return difference;
    }
         
    public CollisionParams checkCollision(MovingObject movingObject, AabbToAabbParameters pointToPlaneParams){
        CollisionParams collisionParams = new CollisionParams(false, movingObject.getDirection());
        int difference = pointToPlaneParams.getDistance();
        int speedInDirectionOfNormalPlane = getSpeedInDirectionOfNormalPlane(movingObject, pointToPlaneParams);
        if((Math.abs(speedInDirectionOfNormalPlane) >= Math.abs(difference)) && (Math.signum(difference) != Math.signum(speedInDirectionOfNormalPlane))){
            collisionParams.setCollided(true);
            if(pointToPlaneParams.getPlaneCenter().getX() == 0){
//                System.out.println("y: " + movingObject.getDirection().getY());//DELETE#####
//                System.out.println("diff: " + difference);                //DELETE######
                collisionParams.setMaximumMovementY(-difference);//movingObject.getDirection().getY() + difference
            }else{
//                System.out.println("x: " + movingObject.getDirection().getX());//DELETE#####
//                System.out.println("diff: " + difference);                //DELETE######
                collisionParams.setMaximumMovementX(-difference);//movingObject.getDirection().getX() + difference
            }
        }
        return collisionParams;
    }      
    
    public int getSpeedInDirectionOfNormalPlane(MovingObject movingObject, AabbToAabbParameters pointToPlaneParams){
        if(pointToPlaneParams.getPlaneCenter().getX() == 0){
            return movingObject.getDirection().getY();
            
        }else{
            return movingObject.getDirection().getX();
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
