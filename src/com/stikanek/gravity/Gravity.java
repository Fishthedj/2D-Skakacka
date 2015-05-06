package com.stikanek.gravity;

import com.stikanek.gameobjects.MovingObject;
import com.stikanek.math.Vec2;
import java.util.ArrayList;

public class Gravity {
    //consider removing counter;
//    private int counter = 0;
    public static final int GRAVITY_CONSTANT = 4;
    public final Vec2 gravityAcceleration = new Vec2(0, GRAVITY_CONSTANT);
    //TODO: singleton
    public void applyGravity(ArrayList<MovingObject> movingObjects){
//        counter++;
//        if(counter == 2){
        movingObjects.stream().filter((mo) -> (mo instanceof GravityAffectable)).forEach((mo) -> {
            applyGravity(mo);
//            }
//            counter = 0;
        });
    }
    
    public void applyGravity(MovingObject movingObject){      
        movingObject.applyAcceleration(gravityAcceleration);
    }
}
