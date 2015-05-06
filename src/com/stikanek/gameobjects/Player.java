package com.stikanek.gameobjects;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import com.stikanek.tiles.TileMap;
import com.stikanek.pictures.Animator;
import com.stikanek.pictures.Images;
import com.stikanek.mainclasses.StatePanel;
import com.stikanek.collisions.AABB;
import com.stikanek.math.Vec2;
import com.stikanek.gravity.GravityAffectable;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class Player extends MovingObject implements GravityAffectable{

    public enum State {

        STANDING_LEFT, STANDING_RIGHT, STARTED_RUNNING_RIGHT, STARTED_RUNNING_LEFT, RUNNING_RIGHT, RUNNING_LEFT,
        STARTED_JUMPING_LEFT, STARTED_JUMPING_RIGHT, STARTED_FALLING_LEFT, STARTED_FALLING_RIGHT,IN_AIR_LEFT, IN_AIR_RIGHT,
        ATTACKING_RIGHT, ATTACKING_LEFT;
    }
    public enum Effect{
        LEFT_KEY_T, RIGHT_KEY_T, LEFT_KEY_F, RIGHT_KEY_F, JUMPING_UP, FALLING_DOWN_T, FALLING_DOWN_F, ZERO_VERTICAL_SPEED, IS_ATTACKING,
        ATTACKING_ANIMATION_PLAYED;
    }
    public enum BooleanEffect{
        
    }
    private final int JUMP_SPEED = -24;
    private final int ATTACKING_RECTANGLE_EXTRA_HEIGHT = 11;
    private final int ATTACKING_RECTANGLE_WIDTH = 45;
    private int xOnMap;
    private int yOnMap;
    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;
    private final int mapWidth;
    private final int mapHeight;
    private final int yTreshold;
    private final int screenHeight;
    private final int width;
    private final int xHalfExtent;
    private final int yHalfExtent;
    private boolean canAttack;
    private boolean isAttacking;
    private boolean canDealDamage;
    private int damage;
//    private final TileMap map;
    private final Animator runningRightAnimator;
    private final Animator runningLeftAnimator;
    private final Animator startedJumpingRightAnimator;
    private final Animator startedJumpingLeftAnimator;
    private final Animator startedFallingRightAnimator;
    private final Animator startedFallingLeftAnimator;
    private final Animator attackingRightAnimator;
    private final Animator attackingLeftAnimator;
    private final BufferedImage[] runningRightFrames;
    private final BufferedImage[] runningLeftFrames;
    private final BufferedImage[] startedJumpingRightFrames;
    private final BufferedImage[] startedJumpingLeftFrames;
    private final BufferedImage[] startedFallingRightFrames;
    private final BufferedImage[] startedFallingLeftFrames;
    private final BufferedImage[] attackingRightFrames;
    private final BufferedImage[] attackingLeftFrames;
    private final BufferedImage standingRightImage;
    private final BufferedImage standingLeftImage;
    private final BufferedImage startedRunningRightImage;
    private final BufferedImage startedRunningLeftImage;
    private final BufferedImage inAirLeftImage;
    private final BufferedImage inAirRightImage;
    private State currentState;
    private final Set<Effect> appliedEffects = EnumSet.noneOf(Effect.class);
    private final Matcher matcher;

    public Player(int xOnMap, int yOnMap, int speed, int mapWidth, int mapHeight, int screenHeight) {
        //TODO:center from xOnMap, yOnMap, halfExtent from Image size
        this.xOnMap = xOnMap;//upper left corner
        this.yOnMap = yOnMap;//upper left corner
        direction = new Vec2(speed, 0);
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.screenHeight = screenHeight;
        yTreshold = (int)((double)1 / 3 * screenHeight);
        Images.setSubdirectoryPath("sprites/");
        runningRightFrames = Images.loadImageStripe("runningRight.gif", 13);
        runningLeftFrames = Images.loadImageStripe("runningLeft.gif", 13);
        startedJumpingRightFrames = Images.loadImageStripe("startedJumpingRight.gif", 5);
        startedJumpingLeftFrames = Images.loadImageStripe("startedJumpingLeft.gif", 5);
        startedFallingRightFrames = Images.loadImageStripe("startedFallingRight.gif", 5);
        startedFallingLeftFrames = Images.loadImageStripe("startedFallingLeft.gif", 5);
        attackingRightFrames = Images.loadImageStripe("attackingRight.gif", 12);
        attackingLeftFrames = Images.loadImageStripe("attackingLeft.gif", 12);
        
        runningRightAnimator = new Animator();
        runningRightAnimator.setFrames(runningRightFrames);
        runningRightAnimator.setDelay(1);
        
        runningLeftAnimator = new Animator();
        runningLeftAnimator.setFrames(runningLeftFrames);
        runningLeftAnimator.setDelay(1);
        
        startedJumpingRightAnimator = new Animator();
        startedJumpingRightAnimator.setFrames(startedJumpingRightFrames);
        startedJumpingRightAnimator.setDelay(1);
        
        startedJumpingLeftAnimator = new Animator();
        startedJumpingLeftAnimator.setFrames(startedJumpingLeftFrames);
        startedJumpingLeftAnimator.setDelay(1);
        
        startedFallingRightAnimator = new Animator();
        startedFallingRightAnimator.setFrames(startedFallingRightFrames);
        startedFallingRightAnimator.setDelay(1);
        
        startedFallingLeftAnimator = new Animator();
        startedFallingLeftAnimator.setFrames(startedFallingLeftFrames);
        startedFallingLeftAnimator.setDelay(1);
        
        attackingRightAnimator = new Animator();
        attackingRightAnimator.setFrames(attackingRightFrames);
        attackingRightAnimator.setDelay(1);
        
        attackingLeftAnimator = new Animator();
        attackingLeftAnimator.setFrames(attackingLeftFrames);
        attackingLeftAnimator.setDelay(1);
        
        standingRightImage = Images.loadImage("standingRight.gif");
        standingLeftImage = Images.loadImage("standingLeft.gif");
        startedRunningRightImage = Images.loadImage("startedRunningRight.gif");
        startedRunningLeftImage = Images.loadImage("startedRunningLeft.gif");
        inAirRightImage = Images.loadImage("inAirRight.gif");
        inAirLeftImage = Images.loadImage("inAirLeft.gif");
        width = getMaxWidthFromImages();
        currentState = State.STANDING_RIGHT;
        xHalfExtent = standingRightImage.getWidth() / 2;
        yHalfExtent = standingRightImage.getHeight() / 2;
        center = new Vec2(xOnMap + xHalfExtent, yOnMap + yHalfExtent);
        aabb = new AABB(center, new Vec2(xHalfExtent, yHalfExtent));
        canJump = true;
        canAttack = true;
        isAttacking = false;
        canDealDamage = true;
        damage = 20;
        matcher = new Matcher();
        matcher.addTransitions(createTransitions());
    }

    public void setCurrentState(State state) {
        currentState = state;
    }

    public void setLeft(boolean b) {
        left = b;
    }

    public void setRight(boolean b) {
        right = b;
    }

    public void setUp(boolean b){
        up = b;
    }
    
    public void setDown(boolean b){
        down = b;
    }
            
    public int getXOnMap() {
        return xOnMap;
    }
    
    public int getYOnMap(){
        return yOnMap;
    }
    
    public void setXOnMap(int x){
        xOnMap = x;
    }
    
    public int getXOnScreen(){
        int screenWidth = StatePanel.PWIDTH;
        if(xOnMap < screenWidth / 2)
            return xOnMap;
        else if(xOnMap > mapWidth - screenWidth / 2){
            return screenWidth - mapWidth + xOnMap;
        }
        else
            return screenWidth / 2;
    }
    
    public int getYOnScreen(){
        int yOnScreen = screenHeight - (mapHeight - yOnMap);
        return yOnScreen  >= yTreshold ? yOnScreen : yTreshold;
    }
    
    public boolean isOnGround(Vec2 maximumMovement){
        //problem: top hit? 
        return maximumMovement.getY() == 0;
    }
    
    @Override
    public Vec2 getPredictedCenterPosition(){
        Vec2 predictedPosition = new Vec2(center);
        Vec2 currentDirection = getCurrentDirection();
        predictedPosition.addTo(currentDirection);
        return predictedPosition;
    }
    
    @Override
    public AABB getPredictedAABB(){
        return new AABB(getPredictedCenterPosition(), aabb.getHalfExtent());
    }
    
    @Override
    public Vec2 getCurrentDirection(){
        Vec2 currentDirection = new Vec2();
        if(right)
            currentDirection.setX(direction.getX());
        if(left)
            currentDirection.setX(-direction.getX());
        currentDirection.setY(direction.getY());
        return currentDirection;
    }
    
    @Override
    public Vec2 getCurrentCenterPosition(){
        return new Vec2(center);
    }
    
    public State getCurrentState(){
        return currentState;
    }
    
    public void jump(){
        if(canJump){
            setDirection(new Vec2(getDirection().getX(), JUMP_SPEED));
            canJump = false;
        }
    }
    
    public int dealDamage(){
        return damage;
    }
    
    public boolean canDealDamage(){
        return canDealDamage;
    }
    
    public void setCanDealDamage(boolean value){
        canDealDamage = value;
    }
            
    public Rectangle getAttackingRectangle(){
        if(currentState == State.ATTACKING_RIGHT){
            return new Rectangle(xOnMap + 2 * xHalfExtent, yOnMap - ATTACKING_RECTANGLE_EXTRA_HEIGHT, 
                    ATTACKING_RECTANGLE_WIDTH, 2 * yHalfExtent + ATTACKING_RECTANGLE_EXTRA_HEIGHT);
        }else if(currentState == State.ATTACKING_LEFT){
            return new Rectangle(xOnMap - ATTACKING_RECTANGLE_WIDTH, yOnMap - ATTACKING_RECTANGLE_EXTRA_HEIGHT,
                    ATTACKING_RECTANGLE_WIDTH, 2 * yHalfExtent + ATTACKING_RECTANGLE_EXTRA_HEIGHT);
        }else{
            return new Rectangle();
        }
    }
    
    public void drawAttackingRectangle(Graphics2D g){
        if(currentState == State.ATTACKING_RIGHT){
            g.fillRect(getXOnScreen() + 2 * xHalfExtent, getYOnScreen() - ATTACKING_RECTANGLE_EXTRA_HEIGHT, 
                    ATTACKING_RECTANGLE_WIDTH, 2 * yHalfExtent + ATTACKING_RECTANGLE_EXTRA_HEIGHT);
        }else if(currentState == State.ATTACKING_LEFT){
            g.fillRect(getXOnScreen() - ATTACKING_RECTANGLE_WIDTH, getYOnScreen() - ATTACKING_RECTANGLE_EXTRA_HEIGHT, 
                    ATTACKING_RECTANGLE_WIDTH, 2 * yHalfExtent + ATTACKING_RECTANGLE_EXTRA_HEIGHT);
        }
    }
    
    public void attack(){
        if(canAttack){
            isAttacking = true;
            canAttack = false;
        }
    }
    
    public boolean isAttacking(){
        return isAttacking;
    }
    public void resetRunningAnimators(){
        runningLeftAnimator.reset();
        runningRightAnimator.reset();
    }
    
    public void addEffects(Effect ... effects){
        Collections.addAll(appliedEffects, effects);
    }
    
    public void addEffect(Effect effect){
        appliedEffects.add(effect);
    }
    
    public void removeEffects(){
        appliedEffects.removeAll(appliedEffects);
    }
    
    public Set<Effect> getAppliedEffects(){
        return appliedEffects;
    }
    
    public void setAppliedEffects(Vec2 maximumMovement){
        removeEffects();
        addEffect(left ? Effect.LEFT_KEY_T : Effect.LEFT_KEY_F);
        addEffect(right ? Effect.RIGHT_KEY_T : Effect.RIGHT_KEY_F);
        if(maximumMovement.getY() > 0)
            addEffect(Effect.FALLING_DOWN_T);
        if(maximumMovement.getY() < 0)
            addEffect(Effect.JUMPING_UP);
        if(maximumMovement.getY() == 0)
            addEffect(Effect.ZERO_VERTICAL_SPEED);
        if(isAttacking)
            addEffect(Effect.IS_ATTACKING);
        if(attackingRightAnimator.hasPlayedAnimationOnce() || attackingLeftAnimator.hasPlayedAnimationOnce())
            addEffect(Effect.ATTACKING_ANIMATION_PLAYED);
    }    
    
    public void updatePosition(Vec2 maximumMovement){
        boolean isNearLeftEdge = xOnMap <= StatePanel.PWIDTH / 2;
        boolean isNearRightEdge = xOnMap >= mapWidth - StatePanel.PWIDTH / 2;        
        if(left){
            if (isNearLeftEdge) {
                xOnMap = (xOnMap + maximumMovement.getX() >= 0) ? xOnMap + maximumMovement.getX() : 0;
            } else if (isNearRightEdge) {
                xOnMap += maximumMovement.getX();
            }else{
                xOnMap += maximumMovement.getX();
            }            
        }else if(right){
            if (isNearLeftEdge) {
                xOnMap += maximumMovement.getX();
            }else if(isNearRightEdge) {
                xOnMap = (xOnMap + maximumMovement.getX() >= mapWidth)? mapWidth : xOnMap + maximumMovement.getX();
            } else {
                xOnMap += maximumMovement.getX();
            }            
        }
            yOnMap += maximumMovement.getY();
            //update center and AABB coordinates
            center.setXY(xOnMap + xHalfExtent, yOnMap + yHalfExtent);
            aabb.setCenter(center);        
    }
    
    public void update(Vec2 maximumMovement){
        if(!isAttacking){
            updatePosition(maximumMovement);
        }
        setAppliedEffects(maximumMovement);
        setCurrentState(matcher.getNextState(this));
    }
    
    public void draw(Graphics2D g) {
        switch (currentState) {
            case STANDING_RIGHT:
                g.drawImage(standingRightImage, getXOnScreen(), getYOnScreen(), null);
                break;
            case STANDING_LEFT:
                g.drawImage(standingLeftImage, getXOnScreen(), getYOnScreen(), null);
                break;
            case STARTED_RUNNING_LEFT:
                g.drawImage(startedRunningLeftImage, getXOnScreen(), getYOnScreen(), null);
                break;
            case STARTED_RUNNING_RIGHT:
                g.drawImage(startedRunningRightImage, getXOnScreen(), getYOnScreen(), null);                
                break;
            case RUNNING_LEFT:
                g.drawImage(runningLeftAnimator.getCurrentImage(), getXOnScreen(), getYOnScreen(), null);
                break;
            case RUNNING_RIGHT:
                g.drawImage(runningRightAnimator.getCurrentImage(), getXOnScreen(), getYOnScreen(), null);
                break;
            case STARTED_JUMPING_LEFT:
                g.drawImage(startedJumpingLeftAnimator.getCurrentImage(), getXOnScreen(), getYOnScreen(), null);
                break;
            case STARTED_JUMPING_RIGHT:
                g.drawImage(startedJumpingRightAnimator.getCurrentImage(), getXOnScreen(), getYOnScreen(), null);
                break;
            case IN_AIR_RIGHT:
                g.drawImage(inAirRightImage, getXOnScreen(), getYOnScreen(), null);
                break;
            case IN_AIR_LEFT:
                g.drawImage(inAirLeftImage, getXOnScreen(), getYOnScreen(), null);
                break;
            case STARTED_FALLING_RIGHT:
                g.drawImage(startedFallingRightAnimator.getCurrentImage(), getXOnScreen(), getYOnScreen() - 30, null);
                break;
            case STARTED_FALLING_LEFT:
                g.drawImage(startedFallingLeftAnimator.getCurrentImage(), getXOnScreen(), getYOnScreen() - 30, null);
                break; 
            case ATTACKING_RIGHT:
                g.drawImage(attackingRightAnimator.getCurrentImage(), getXOnScreen() - 9, getYOnScreen() - 11, null);//getYOnScreen() - 11 taking into account different height of pictures 
                break;
            case ATTACKING_LEFT:
                g.drawImage(attackingLeftAnimator.getCurrentImage(), getXOnScreen() - 45, getYOnScreen() - 11, null);//getYOnScreen() - 11 taking into account different height of pictures
                break;
        }
    }
    
    class Matcher{
        private final List<Transition> transitions = new ArrayList<>();
        
        public void addTransition(Transition transition){
            transitions.add(transition);
        }
        
        public void addTransitions(List<Transition> transitionList){
            transitions.addAll(transitionList);
        }
        
        public boolean matches(Transition transition, State currentState, Set<Effect> appliedEffects){
//            return transition.getCurrentState() == currentState & transition.getEffects().containsAll(appliedEffects);
            return transition.getCurrentState() == currentState && appliedEffects.containsAll(transition.getEffects());
        }
        
        public State getNextState(Player player){
            State nextState = player.getCurrentState();
            State currentState = player.getCurrentState();
            Set<Effect> appliedEffects = player.getAppliedEffects();
            
            for(Transition transition:transitions){
                if(matches(transition, currentState, appliedEffects)){
                    transition.applyChanges();
                    return transition.getFinalState();
                }
            }
            return nextState;
        }
    }
    
    abstract class Transition{
        State currentState;
        State finalState;
        Set<Effect> effects = EnumSet.noneOf(Effect.class);
        
        Transition(State currentState, State finalState, Effect ... usedEffects){
            this.currentState = currentState;
            this.finalState = finalState;
            Collections.addAll(effects, usedEffects);
        }
        
        State getCurrentState(){
            return currentState;
        }
        
        State getFinalState(){
            return finalState;
        }
        
        Set<Effect> getEffects(){
            return effects;
        }
        
        abstract void applyChanges();        
    }
    
    final ArrayList<Transition> createTransitions(){
        ArrayList<Transition> transitionList = new ArrayList<>();
        transitionList.add(new Transition(State.STANDING_RIGHT, State.STANDING_LEFT, Effect.LEFT_KEY_T, Effect.ZERO_VERTICAL_SPEED){
            @Override
            void applyChanges(){
                canAttack = true;
            }
        });
        transitionList.add(new Transition(State.STANDING_LEFT, State.STANDING_RIGHT, Effect.RIGHT_KEY_T, Effect.ZERO_VERTICAL_SPEED){
            @Override
            void applyChanges(){
                canAttack = true;
            }
        });
        transitionList.add(new Transition(State.STANDING_LEFT, State.STARTED_RUNNING_LEFT, Effect.LEFT_KEY_T, Effect.ZERO_VERTICAL_SPEED){
            @Override
            void applyChanges(){
            }
        });
        transitionList.add(new Transition(State.STANDING_RIGHT, State.STARTED_RUNNING_RIGHT, Effect.RIGHT_KEY_T, Effect.ZERO_VERTICAL_SPEED){
            @Override
            void applyChanges(){
            }
        });
        transitionList.add(new Transition(State.STARTED_RUNNING_LEFT, State.ATTACKING_LEFT, Effect.IS_ATTACKING){
            @Override
            void applyChanges(){
                canAttack = false;
                isAttacking = true;
            }
        });        
        transitionList.add(new Transition(State.STARTED_RUNNING_LEFT, State.RUNNING_LEFT, Effect.LEFT_KEY_T){
            @Override
            void applyChanges(){
                
            }
        });
        transitionList.add(new Transition(State.STARTED_RUNNING_RIGHT, State.ATTACKING_RIGHT, Effect.IS_ATTACKING){
            @Override
            void applyChanges(){
                canAttack = false;
                isAttacking = true;
            }
        });        
        transitionList.add(new Transition(State.STARTED_RUNNING_RIGHT, State.RUNNING_RIGHT, Effect.RIGHT_KEY_T){
            @Override
            void applyChanges(){
            }
        });
        transitionList.add(new Transition(State.RUNNING_LEFT, State.ATTACKING_LEFT, Effect.IS_ATTACKING){
            @Override
            void applyChanges(){
                canAttack = false;
                isAttacking = true;
            }
        });        
        transitionList.add(new Transition(State.RUNNING_LEFT, State.RUNNING_LEFT, Effect.LEFT_KEY_T, Effect.ZERO_VERTICAL_SPEED){
            @Override
            void applyChanges(){
                runningLeftAnimator.update();
            }
        });
       transitionList.add(new Transition(State.RUNNING_RIGHT, State.ATTACKING_RIGHT, Effect.IS_ATTACKING){
            @Override
            void applyChanges(){
                canAttack = false;
                isAttacking = true;
            }
        });        
        transitionList.add(new Transition(State.RUNNING_RIGHT, State.RUNNING_RIGHT, Effect.RIGHT_KEY_T, Effect.ZERO_VERTICAL_SPEED){
            @Override
            void applyChanges(){
                runningRightAnimator.update();
            }
        });         
        transitionList.add(new Transition(State.RUNNING_LEFT, State.STANDING_LEFT, Effect.LEFT_KEY_F, Effect.RIGHT_KEY_F){
            @Override
            void applyChanges(){
                runningLeftAnimator.reset();
            }
        });  
        transitionList.add(new Transition(State.RUNNING_RIGHT, State.STANDING_RIGHT, Effect.LEFT_KEY_F, Effect.RIGHT_KEY_F){
            @Override
            void applyChanges(){
                runningRightAnimator.reset();
            }
        });         
        transitionList.add(new Transition(State.RUNNING_LEFT, State.STANDING_RIGHT, Effect.RIGHT_KEY_T, Effect.LEFT_KEY_F){
            @Override
            void applyChanges(){
                runningLeftAnimator.reset();
            }
        });
        transitionList.add(new Transition(State.RUNNING_RIGHT, State.STANDING_LEFT, Effect.LEFT_KEY_T, Effect.RIGHT_KEY_F){
            @Override
            void applyChanges(){
                runningRightAnimator.reset();
            }
        });         
        transitionList.add(new Transition(State.STANDING_LEFT, State.STARTED_JUMPING_LEFT, Effect.JUMPING_UP){
            @Override
            void applyChanges(){
                canAttack = false;
            }
        });
        transitionList.add(new Transition(State.STANDING_RIGHT, State.STARTED_JUMPING_RIGHT, Effect.JUMPING_UP){
            @Override
            void applyChanges(){
                canAttack = false;
            }
        });         
        transitionList.add(new Transition(State.STARTED_JUMPING_LEFT, State.STARTED_JUMPING_LEFT, Effect.JUMPING_UP){
            @Override
            void applyChanges(){
                startedJumpingLeftAnimator.update();
            }
        });
        transitionList.add(new Transition(State.STARTED_JUMPING_RIGHT, State.STARTED_JUMPING_RIGHT, Effect.JUMPING_UP){
            @Override
            void applyChanges(){
                startedJumpingRightAnimator.update();
            }
        });         
//        transitionList.add(new Transition(State.STARTED_JUMPING_LEFT, State.STARTED_JUMPING_LEFT, Effect.JUMPING_UP, Effect.LEFT_KEY_T){
//            @Override
//            void applyChanges(){
//            }
//        });         
        transitionList.add(new Transition(State.STARTED_JUMPING_LEFT, State.IN_AIR_LEFT, Effect.ZERO_VERTICAL_SPEED){
            @Override
            void applyChanges(){
                startedJumpingLeftAnimator.reset();
            }
        });
        transitionList.add(new Transition(State.STARTED_JUMPING_RIGHT, State.IN_AIR_RIGHT, Effect.ZERO_VERTICAL_SPEED){
            @Override
            void applyChanges(){
                startedJumpingRightAnimator.reset();
            }
        });         
        transitionList.add(new Transition(State.RUNNING_LEFT, State.STARTED_JUMPING_LEFT, Effect.JUMPING_UP, Effect.LEFT_KEY_T){
            @Override
            void applyChanges(){
                canAttack = false;
            }
        });         
        transitionList.add(new Transition(State.RUNNING_RIGHT, State.STARTED_JUMPING_RIGHT, Effect.JUMPING_UP, Effect.RIGHT_KEY_T){
            @Override
            void applyChanges(){
                canAttack = false;
            }
        }); 
        transitionList.add(new Transition(State.RUNNING_LEFT, State.STARTED_FALLING_LEFT, Effect.FALLING_DOWN_T){
            @Override
            void applyChanges(){
            }
        });         
        transitionList.add(new Transition(State.RUNNING_RIGHT, State.STARTED_FALLING_RIGHT, Effect.FALLING_DOWN_T){
            @Override
            void applyChanges(){
            }
        });         
        transitionList.add(new Transition(State.IN_AIR_LEFT, State.STARTED_FALLING_LEFT, Effect.FALLING_DOWN_T){
            @Override
            void applyChanges(){
            }
        });
        transitionList.add(new Transition(State.IN_AIR_RIGHT, State.STARTED_FALLING_RIGHT, Effect.FALLING_DOWN_T){
            @Override
            void applyChanges(){
            }
        });  
        transitionList.add(new Transition(State.IN_AIR_LEFT, State.STANDING_LEFT, Effect.ZERO_VERTICAL_SPEED){
            @Override
            void applyChanges(){
//                canAttack = true;
            }
        });         
        transitionList.add(new Transition(State.IN_AIR_RIGHT, State.STANDING_RIGHT, Effect.ZERO_VERTICAL_SPEED){
            @Override
            void applyChanges(){
//                canAttack = true;
            }
        });         
        transitionList.add(new Transition(State.STARTED_FALLING_LEFT, State.STARTED_FALLING_LEFT, Effect.FALLING_DOWN_T, Effect.LEFT_KEY_T){
            @Override
            void applyChanges(){
                startedFallingLeftAnimator.update();
            }
        });         
        transitionList.add(new Transition(State.STARTED_FALLING_RIGHT, State.STARTED_FALLING_RIGHT, Effect.FALLING_DOWN_T){
            @Override
            void applyChanges(){
                startedFallingRightAnimator.update();
            }
        });         
        transitionList.add(new Transition(State.STARTED_FALLING_LEFT, State.STARTED_FALLING_LEFT, Effect.FALLING_DOWN_T){
            @Override
            void applyChanges(){
                startedFallingLeftAnimator.update();
            }
        });
        transitionList.add(new Transition(State.STARTED_FALLING_LEFT, State.RUNNING_LEFT, Effect.LEFT_KEY_T, Effect.ZERO_VERTICAL_SPEED){
            @Override
            void applyChanges(){
                startedFallingLeftAnimator.reset();
                canAttack = true;
            }
        });  
        transitionList.add(new Transition(State.STARTED_FALLING_RIGHT, State.RUNNING_RIGHT, Effect.RIGHT_KEY_T, Effect.ZERO_VERTICAL_SPEED){
            @Override
            void applyChanges(){
                startedFallingRightAnimator.reset();
                canAttack = true;
            }
        });         
        transitionList.add(new Transition(State.STARTED_FALLING_LEFT, State.STANDING_LEFT, Effect.ZERO_VERTICAL_SPEED){
            @Override
            void applyChanges(){
                startedFallingLeftAnimator.reset();
                canAttack = true;
            }
        });     
        transitionList.add(new Transition(State.STARTED_FALLING_RIGHT, State.STANDING_RIGHT, Effect.ZERO_VERTICAL_SPEED){
            @Override
            void applyChanges(){
                startedFallingRightAnimator.reset();
                canAttack = true;
            }
        });         
        transitionList.add(new Transition(State.STANDING_LEFT, State.STARTED_FALLING_LEFT, Effect.FALLING_DOWN_T){
            @Override
            void applyChanges(){
                canAttack = false;
            }
        }); 
        transitionList.add(new Transition(State.STANDING_RIGHT, State.STARTED_FALLING_RIGHT, Effect.FALLING_DOWN_T){
            @Override
            void applyChanges(){
                canAttack = false;
            }
        }); 
        transitionList.add(new Transition(State.STARTED_RUNNING_LEFT, State.STANDING_LEFT, Effect.LEFT_KEY_F){
            @Override
            void applyChanges(){
                canAttack = true;
            }
        });         
        transitionList.add(new Transition(State.STARTED_RUNNING_RIGHT, State.STANDING_RIGHT, Effect.RIGHT_KEY_F){
            @Override
            void applyChanges(){
                canAttack = true;
            }
        });         
        transitionList.add(new Transition(State.RUNNING_LEFT, State.STARTED_JUMPING_LEFT, Effect.JUMPING_UP, Effect.LEFT_KEY_T){
            @Override
            void applyChanges(){
            }
        });
        transitionList.add(new Transition(State.RUNNING_RIGHT, State.STARTED_JUMPING_RIGHT, Effect.JUMPING_UP, Effect.RIGHT_KEY_T){
            @Override
            void applyChanges(){
            }
        });        
        transitionList.add(new Transition(State.STANDING_RIGHT, State.ATTACKING_RIGHT, Effect.IS_ATTACKING){
            @Override
            void applyChanges(){
                canAttack = false;
            }
        });
        transitionList.add(new Transition(State.STANDING_LEFT, State.ATTACKING_LEFT, Effect.IS_ATTACKING){
            @Override
            void applyChanges(){
                canAttack = false;
            }
        });        
        transitionList.add(new Transition(State.ATTACKING_RIGHT, State.STANDING_RIGHT, Effect.ATTACKING_ANIMATION_PLAYED){
           @Override
           void applyChanges(){
               attackingRightAnimator.reset();
               canAttack = true;
               isAttacking = false;
               canDealDamage = true;
           }
        });     
        transitionList.add(new Transition(State.ATTACKING_LEFT, State.STANDING_LEFT, Effect.ATTACKING_ANIMATION_PLAYED){
           @Override
           void applyChanges(){
               attackingLeftAnimator.reset();
               canAttack = true;
               isAttacking = false;
               canDealDamage = true;
           }
        });                    
        transitionList.add(new Transition(State.ATTACKING_RIGHT, State.ATTACKING_RIGHT, Effect.IS_ATTACKING){
           @Override
           void applyChanges(){
               attackingRightAnimator.update();
           }
        });
        transitionList.add(new Transition(State.ATTACKING_LEFT, State.ATTACKING_LEFT, Effect.IS_ATTACKING){
           @Override
           void applyChanges(){
               attackingLeftAnimator.update();
           }
        });           
        transitionList.add(new Transition(State.STARTED_RUNNING_RIGHT, State.ATTACKING_RIGHT, Effect.IS_ATTACKING){
            @Override
            void applyChanges(){
                canAttack = false;
                isAttacking = true;
            }
        });
        return transitionList;
    }

    /**
     * Returns max width of player from all images used to represent him.
     */
    private int getMaxWidthFromImages() {
        ArrayList<BufferedImage> allImages = new ArrayList<BufferedImage>();
        allImages.add(standingLeftImage);
        allImages.add(standingRightImage);
        allImages.add(startedRunningLeftImage);
        allImages.add(startedRunningRightImage);
        allImages.addAll(Arrays.asList(runningLeftFrames));
        allImages.addAll(Arrays.asList(runningRightFrames));
        int maxWidth = 0;
        for (BufferedImage image : allImages) 
            if (maxWidth < image.getWidth()) 
                maxWidth = image.getWidth();

        return maxWidth;
    }

}
