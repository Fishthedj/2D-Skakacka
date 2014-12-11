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
        Player instance = new Player(10, 16, 2, 1000);
        int expResult = 10;
        int result = instance.getXOnScreen();
        assertEquals(expResult, result);
        
        instance.setXOnMap(200);
        expResult = 160;
        result = instance.getXOnScreen();
        assertEquals(expResult, result);
        
        instance.setXOnMap(980);
        expResult = 300;
        result = instance.getXOnScreen();
        assertEquals(expResult, result);
    }
}
