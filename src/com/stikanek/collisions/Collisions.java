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
        Vec2 tempMaximumMovement = new Vec2(maximumMovement);
        Vec2 currentPosition = mo.getCurrentCenterPosition();
        System.out.println("currentpos x: " + currentPosition.getX());//DELETE
        System.out.println("currentpos y: " + currentPosition.getY());//DELETE
        Vec2 predictedPosition = mo.getPredictedCenterPosition();
        System.out.println("predictedpos x: " + predictedPosition.getX());
        System.out.println("predictedpos y: " + predictedPosition.getY());
        Vec2 min = currentPosition.min(predictedPosition);//upper-left position of collision area
//        System.out.println("min x: " + min.getX() + "min y: " + min.getY());
        Vec2 max = currentPosition.max(predictedPosition);//lower-right position of collision area
//        System.out.println("max x: " + max.getX() + "max y: " + max.getY());
        
        //TODO: addTo halfExtent max, subFrom halfExtent min
        min.addTo(mo.getAabb().getHalfExtent().getNegation());
        min.addTo(new Vec2(1,1));
//        System.out.println("minx: " + min.getX());//DELETE
//        System.out.println("miny: " + min.getY());//DELETE
        max.addTo(mo.getAabb().getHalfExtent());
        max.addTo(new Vec2(-1,-1));
//        System.out.println("halfext x: " + mo.getAabb().getHalfExtent().getX() + " y: " + mo.getAabb().getHalfExtent().getY());
        System.out.println("maxx: " + max.getX());//DELETE  
        System.out.println("maxy: " + max.getY());//DELETE
        
        //get tiles in collision area
        int firstTileX = map.worldCoordToTileX(min.getX());
        int firstTileY = map.worldCoordToTileY(min.getY());
//        System.out.println("firstTileX: " + firstTileX); //DELETE %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//        System.out.println("firstTileY: " + firstTileY);//DELETE @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        
        int lastTileX = map.worldCoordToTileX(max.getX());
        int lastTileY = map.worldCoordToTileY(max.getY());
//        System.out.println("lastTileX: " + lastTileX); //DELETE %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//        System.out.println("lastTileY: " + lastTileY);//DELETE @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@    
        
        //for all tiles in collision area
        for(int row = firstTileY; row <= lastTileY; row++){
            for(int column = firstTileX; column <= lastTileX; column++){
                if(map.getTileType(row, column) == TileType.BLOCKING){
                    Vec2 center = new Vec2(map.tileCoordToWorldX(column),map.tileCoordToWorldY(row));
                    System.out.println("tile x: " + map.tileCoordToWorldX(column) + " y: " + map.tileCoordToWorldY(row));
                    Vec2 halfExtent = new Vec2(map.getTileWidth() / 2, map.getTileHeight() / 2);
                    CollisionTile collisionTile = new CollisionTile(center, halfExtent);
                    CollisionParams collisionParams = willCollide(mo, collisionTile);
                    System.out.println("row: " + row);
                    System.out.println("column: " + column);
                    if(collisionParams.collided){
//                        System.out.println("Maximum movement x: " + collisionParams.getMaximumMovement().getX() +
//                                " y: " + collisionParams.getMaximumMovement().getY());
//                        maximumMovement = collisionParams.getMaximumMovement();
                        if(Math.abs(collisionParams.getMaximumMovement().getX()) < Math.abs(tempMaximumMovement.getX()))
                            tempMaximumMovement.setX(collisionParams.getMaximumMovement().getX());
                        if(Math.abs(collisionParams.getMaximumMovement().getY()) < Math.abs(tempMaximumMovement.getY()))
                            tempMaximumMovement.setY(collisionParams.getMaximumMovement().getY());
                    }
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
        AabbToAabbParameters params = getPointToPlaneParameters(gofst.getPredictedAABB(), goscnd.getAabb());
        return checkCollision(gofst, params);
    }
    public AabbToAabbParameters getPointToPlaneParameters(AABB aabbfst, AABB aabbscnd){
        Vec2 expandedExtent = aabbscnd.getHalfExtent().addTo(aabbfst.getHalfExtent());      
        Vec2 expandedCenter = aabbscnd.getCenter();
        Vec2 centersDifference = expandedCenter.sub(aabbfst.getCenter());       
        Vec2 normalPlane = centersDifference.getMajorAxis().getNegationTo();
        System.out.println("normalPlane x: " + normalPlane.getX() + " y: " + normalPlane.getY());
        int distance = getDifferenceFromPointToPlane(normalPlane, expandedCenter, expandedExtent, aabbfst.getCenter());
        AabbToAabbParameters positionParameters = new AabbToAabbParameters(distance, normalPlane);
        return positionParameters;
    }
    
    public int getDifferenceFromPointToPlane(Vec2 normalPlane, Vec2 aabbCenter, Vec2 aabbHalfExtent, Vec2 point){
        Vec2 planeCenter = normalPlane.mul(aabbHalfExtent).addTo(aabbCenter);
        Vec2 planeToPointDifference = point.sub(planeCenter);
        System.out.println("planeToPointDifference x: " + planeToPointDifference.getX() + "y: " + planeToPointDifference.getY());
        int difference = planeToPointDifference.dotProduct(normalPlane);
        System.out.println("difference: " + difference);
//        int difference = Math.abs(planeToPointDifference.getX()) > 0 ? planeToPointDifference.getX() : planeToPointDifference.getY();
        return difference;
    }
         
    public CollisionParams checkCollision(MovingObject movingObject, AabbToAabbParameters pointToPlaneParams){
        CollisionParams collisionParams = new CollisionParams(false, movingObject.getCurrentDirection());
        int difference = pointToPlaneParams.getDistance();
        int speedInDirectionOfNormalPlane = getSpeedInDirectionOfNormalPlane(movingObject, pointToPlaneParams);
        System.out.println("speedInDirectionOfNormalPlane: " + speedInDirectionOfNormalPlane);
//        if((Math.abs(speedInDirectionOfNormalPlane) >= Math.abs(difference)) && (Math.signum(difference) != Math.signum(speedInDirectionOfNormalPlane))){
        if(Math.signum(difference) == -1){
            collisionParams.setCollided(true);
            System.out.println("collided");
            if(pointToPlaneParams.getPlaneCenter().getX() == 0){
                collisionParams.setMaximumMovementY(Math.signum(movingObject.getCurrentDirection().getY()) == 1.0? 
                        movingObject.getCurrentDirection().getY() + difference : movingObject.getCurrentDirection().getY() - difference);
                System.out.println("collisionParams x: " + collisionParams.getMaximumMovement().getX() + " y: " + collisionParams.getMaximumMovement().getY());
                    //collisionParams.setMaximumMovementY(-difference);//movingObject.getDirection().getY() + difference
            }else{
                    //collisionParams.setMaximumMovementX(-difference);//movingObject.getDirection().getX() + difference
                collisionParams.setMaximumMovementX(Math.signum(movingObject.getCurrentDirection().getX()) == 1.0? 
                        movingObject.getCurrentDirection().getX() + difference : movingObject.getCurrentDirection().getX() - difference);
                System.out.println("collisionParams x: " + collisionParams.getMaximumMovement().getX() + " y: " + collisionParams.getMaximumMovement().getY());
            }
        }
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
