/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stikanek.tiles;

import java.awt.Graphics2D;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Pavel
 */
public class TileMapTest {
    
    public TileMapTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of getMapWidth method, of class TileMap.
     */
    @Test
    public void testGetMapWidth() {
        System.out.println("getMapWidth");
        TileMap instance = null;
        int expResult = 0;
        int result = instance.getMapWidth();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isNearEdges method, of class TileMap.
     */
    @Test
    public void testIsNearEdges() {
        System.out.println("isNearEdges");
        TileMap instance = null;
        boolean expResult = false;
        boolean result = instance.isNearEdgesHorizontally();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadTileMap method, of class TileMap.
     */
    @Test
    public void testLoadTileMap() {
        System.out.println("loadTileMap");
        String file = "";
        TileMap instance = null;
        instance.loadTileMap(file);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadTiles method, of class TileMap.
     */
    @Test
    public void testLoadTiles() {
        System.out.println("loadTiles");
        String file = "";
        TileMap instance = null;
        instance.loadTiles(file);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class TileMap.
     */
    @Test
    public void testUpdate() {
        System.out.println("update");
        int x = 0;
        int y = 0;
        TileMap instance = null;
        instance.update(x,y);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of draw method, of class TileMap.
     */
    @Test
    public void testDraw() {
        System.out.println("draw");
        Graphics2D g = null;
        TileMap instance = null;
        instance.draw(g);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
