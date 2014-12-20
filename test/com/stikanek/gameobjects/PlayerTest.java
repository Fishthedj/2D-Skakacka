package com.stikanek.gameobjects;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Pavel
 */
public class PlayerTest {
    
    public PlayerTest() {
    }
    /**
     * Test of getXOnScreen method, of class Player.
     */
    @Test
    public void testGetXOnScreen() {
        System.out.println("getXOnScreen");
        Player instance = new Player(10, 16, 2, 840);
        int expResult = 10;
        int result = instance.getXOnScreen();
        assertEquals(expResult, result);
        
        instance.setXOnMap(100);
        expResult = 100;
        result = instance.getXOnScreen();
        assertEquals(expResult, result);
        
        instance.setXOnMap(160);
        expResult = 160;
        result = instance.getXOnScreen();
        assertEquals(expResult, result);
        
        instance.setXOnMap(680);
        expResult = 160;
        result = instance.getXOnScreen();
        assertEquals(expResult, result);        
        
        instance.setXOnMap(681);
        expResult = 161;
        result = instance.getXOnScreen();
        assertEquals(expResult, result);
  
        instance.setXOnMap(840);
        expResult = 320;
        result = instance.getXOnScreen();
        assertEquals(expResult, result);        
    }
}
