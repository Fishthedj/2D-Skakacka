/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stikanek.collisions;

import com.stikanek.collisions.Collisions.CollisionParams;
import com.stikanek.gameobjects.GameObject;
import com.stikanek.gameobjects.MovingObject;
import com.stikanek.gameobjects.StaticObject;
import com.stikanek.math.Vec2;
import com.stikanek.tiles.TileMap;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Pavel
 */

//TODO: CHANGE OF DIRECTION IN Y
public class CollisionsTest {
    
    public CollisionsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of getMaximumPossibleMovement method, of class Collisions.
     */
//    @Test
//    public void testGetMaximumPossibleMovement() {
//        System.out.println("getMaximumPossibleMovement");
//        MovingObject mo = null;
//        Collisions instance = null;
//        Vec2 expResult = null;
//        Vec2 result = instance.getMaximumPossibleMovement(mo);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of willCollide method, of class Collisions.
     */
    @Test
    public void testWillCollide() {
        System.out.println("willCollide");
        //moving in x right with collision
        MovingObject gofst = new TestingMovingObject(new Vec2(1,1), new Vec2(1,1), new Vec2(3,0));
        GameObject goscnd = new TestingStaticObject(new Vec2(4,1), new Vec2(1,1));        
        Collisions instance = new Collisions(new TileMap(3,320));//random values in constructor
        CollisionParams collisionParams = instance.willCollide(gofst, goscnd);
        Vec2 maximumMovement = collisionParams.maximumMovement;
        System.out.println("maxmovement x: " + maximumMovement.getX());
        System.out.println("maxmovement y: " + maximumMovement.getY());
        Vec2 expResult = new Vec2(1,0);
        assertEquals(expResult, maximumMovement);
        
        //moving in x right with no collision
        gofst = new TestingMovingObject(new Vec2(1,1), new Vec2(1,1), new Vec2(3,0));
        goscnd = new TestingStaticObject(new Vec2(6,1), new Vec2(1,1));        
        collisionParams = instance.willCollide(gofst, goscnd);
        maximumMovement = collisionParams.maximumMovement;
        System.out.println("maxmovement x: " + maximumMovement.getX());
        System.out.println("maxmovement y: " + maximumMovement.getY());
        expResult = new Vec2(3,0);
        assertEquals(expResult, maximumMovement);
        
        //moving in x left with no collision
        gofst = new TestingMovingObject(new Vec2(6,1), new Vec2(1,1), new Vec2(-3,0));
        goscnd = new TestingStaticObject(new Vec2(1,1), new Vec2(1,1));        
        collisionParams = instance.willCollide(gofst, goscnd);
        maximumMovement = collisionParams.maximumMovement;
        System.out.println("maxmovement x: " + maximumMovement.getX());
        System.out.println("maxmovement y: " + maximumMovement.getY());
        expResult = new Vec2(-3,0);
        assertEquals(expResult, maximumMovement);
        
        //moving in x left with collision
        gofst = new TestingMovingObject(new Vec2(4,1), new Vec2(1,1), new Vec2(-3,0));
        goscnd = new TestingStaticObject(new Vec2(1,1), new Vec2(1,1));        
        collisionParams = instance.willCollide(gofst, goscnd);
        maximumMovement = collisionParams.maximumMovement;
        System.out.println("maxmovement x: " + maximumMovement.getX());
        System.out.println("maxmovement y: " + maximumMovement.getY());
        expResult = new Vec2(-1,0);
        assertEquals(expResult, maximumMovement);
        
        //moving in y up (negative direction) with collision
        gofst = new TestingMovingObject(new Vec2(2,4), new Vec2(1,1), new Vec2(0,-3));
        goscnd = new TestingStaticObject(new Vec2(2,1), new Vec2(1,1));        
        collisionParams = instance.willCollide(gofst, goscnd);
        maximumMovement = collisionParams.maximumMovement;
        System.out.println("maxmovement x: " + maximumMovement.getX());
        System.out.println("maxmovement y: " + maximumMovement.getY());
        expResult = new Vec2(0,-1);
        assertEquals(expResult, maximumMovement);
        
        //moving in y up with no collision
        gofst = new TestingMovingObject(new Vec2(2,7), new Vec2(1,1), new Vec2(0,-3));
        goscnd = new TestingStaticObject(new Vec2(2,1), new Vec2(1,1));        
        collisionParams = instance.willCollide(gofst, goscnd);
        maximumMovement = collisionParams.maximumMovement;
        System.out.println("maxmovement x: " + maximumMovement.getX());
        System.out.println("maxmovement y: " + maximumMovement.getY());
        expResult = new Vec2(0,-3);
        assertEquals(expResult, maximumMovement);
        
        //moving in y down (positive direction) with no collision
        gofst = new TestingMovingObject(new Vec2(2,1), new Vec2(1,1), new Vec2(0,3));
        goscnd = new TestingStaticObject(new Vec2(2,6), new Vec2(1,1));        
        collisionParams = instance.willCollide(gofst, goscnd);
        maximumMovement = collisionParams.maximumMovement;
        System.out.println("maxmovement x: " + maximumMovement.getX());
        System.out.println("maxmovement y: " + maximumMovement.getY());
        expResult = new Vec2(0,3);
        assertEquals(expResult, maximumMovement);
    }
    
    class TestingStaticObject extends StaticObject{
        public TestingStaticObject(Vec2 center, Vec2 halfExtent){
            this.center = center;
            this.aabb = new AABB(center, halfExtent);
        }
        
        @Override
        public Vec2 getCurrentCenterPosition(){
            return center;
        }
    }
    
    class TestingMovingObject extends MovingObject{
        public TestingMovingObject(Vec2 center, Vec2 halfExtent, Vec2 direction){
            this.center = center;
            this.direction = direction;
            this.aabb = new AABB(center, halfExtent);
        }
        
        @Override
        public AABB getPredictedAABB(){
            //incorrect implementation
            return this.aabb;
        }
        @Override
        public Vec2 getCurrentDirection(){
            return direction;
        }
        
        @Override
        public Vec2 getPredictedCenterPosition(){
            return new Vec2(center.addTo(direction));
        }
        
        @Override
        public Vec2 getCurrentCenterPosition(){
            return new Vec2(center);
        }
    }

    /**
     * Test of getPointToPlaneParameters method, of class Collisions.
     */
//    @Test
//    public void testGetPointToPlaneParameters() {
//        System.out.println("getPointToPlaneParameters");
//        AABB aabbfst = null;
//        AABB aabbscnd = null;
//        Collisions instance = null;
//        AabbToAabbParameters expResult = null;
//        AabbToAabbParameters result = instance.getPointToPlaneParameters(aabbfst, aabbscnd);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of getDifferenceFromPointToPlane method, of class Collisions.
     */
//    @Test
//    public void testGetDifferenceFromPointToPlane() {
//        System.out.println("getDifferenceFromPointToPlane");
//        Vec2 normalPlane = null;
//        Vec2 aabbCenter = null;
//        Vec2 aabbHalfExtent = null;
//        Vec2 point = null;
//        Collisions instance = null;
//        int expResult = 0;
//        int result = instance.getDifferenceFromPointToPlane(normalPlane, aabbCenter, aabbHalfExtent, point);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of checkCollision method, of class Collisions.
     */
//    @Test
//    public void testCheckCollision() {
//        System.out.println("checkCollision");
//        MovingObject movingObject = null;
//        AabbToAabbParameters pointToPlaneParams = null;
//        Collisions instance = null;
//        Collisions.CollisionParams expResult = null;
//        Collisions.CollisionParams result = instance.checkCollision(movingObject, pointToPlaneParams);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of getSpeedInDirectionOfNormalPlane method, of class Collisions.
     */
//    @Test
//    public void testGetSpeedInDirectionOfNormalPlane() {
//        System.out.println("getSpeedInDirectionOfNormalPlane");
//        MovingObject movingObject = null;
//        AabbToAabbParameters pointToPlaneParams = null;
//        Collisions instance = null;
//        int expResult = 0;
//        int result = instance.getSpeedInDirectionOfNormalPlane(movingObject, pointToPlaneParams);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
