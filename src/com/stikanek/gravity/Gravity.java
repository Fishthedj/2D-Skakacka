package com.stikanek.gravity;

import com.stikanek.gameobjects.MovingObject;
import com.stikanek.math.Vec2;
import java.util.ArrayList;

public class Gravity {
    //consider removing counter;
    private int counter = 0;
    public static final int GRAVITY_CONSTANT = 2;
    public final Vec2 gravityAcceleration = new Vec2(0, GRAVITY_CONSTANT);
    //TODO: singleton
    public void applyGravity(ArrayList<MovingObject> movingObjects){
        counter++;
//        if(counter == 2){
            for(MovingObject mo : movingObjects){
                if(mo instanceof GravityAffectable)
                    applyGravity(mo);
//            }
            counter = 0;
        }
    }
    
    public void applyGravity(MovingObject movingObject){      
        movingObject.applyAcceleration(gravityAcceleration);
    }
}
