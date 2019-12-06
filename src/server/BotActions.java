/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

//Normal Bot Actions
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;

/**
 *
 * @author uidk2372
 */

//Normal BotActions
public abstract class BotActions {
    
    private static BOT_ACTIONS action;
   
	private static EV3LargeRegulatedMotor motorB;
	private static EV3LargeRegulatedMotor motorA;
	
	private static int TurnRotation = 197;
	private static int AdvanceRotation = 75;
	private static int SPEED = 125;
	private static int counter = 0;
	private static int RELAX_ROTATE =4; 
	
    
    public static void performAction(String actionMessage){
    	if(actionMessage.startsWith("turn")) {
    		//Change turn angle
    		try {
    			TurnRotation = Integer.parseInt(actionMessage.split(",")[1]);
    		}catch(Exception e){ System.out.println("Failed to change turn with input" + actionMessage);}
    	}
    	else if(actionMessage.startsWith("advance")) {
    		//Change advance angle
    		try {
    			AdvanceRotation = Integer.parseInt(actionMessage.split(",")[1]);
    		}catch(Exception e){ System.out.println("Failed to change advance with input" + actionMessage);}
    	}
    	else if(actionMessage.startsWith("speed")) {
    		//Change speed angle
    		try {
    			SPEED = Integer.parseInt(actionMessage.split(",")[1]);
    			setSpeed();
    		}catch(Exception e){ System.out.println("Failed to change speed with input" + actionMessage);}
    	}
    	else {
    		//Move Robot
    		try{
                action = BotActions.BOT_ACTIONS.valueOf(actionMessage);
                if(motorA==null || motorB==null)
                {
                	initiliazileMotors();
                }
            }
            catch(Exception e){
                action = BotActions.BOT_ACTIONS.stop;
            }

            switch(action){
                case forward:
                    BotActions.moveForward();
                    counter++;
                    break;
                case backward:
                    BotActions.moveBackward();
                    counter++;
                    break;
                case left:
                    BotActions.turnLeft();
                    counter++;
                    break;
                case right:
                    BotActions.turnRight();
                    counter++;
                    break;
                case stop:
                    BotActions.stop();
                    break;
                default:
                    //invalid message. Do nothing.
                    System.out.println("Invalid action. Continue...");
                    break;
            }
            while(motorA.isMoving() || motorB.isMoving()) {Thread.yield();}
            System.out.println("Exec count: "+ counter);
    	}
    }
    
    public static void initiliazileMotors() {
		motorA = new EV3LargeRegulatedMotor(MotorPort.B);
        motorB = new EV3LargeRegulatedMotor(MotorPort.C);
        setSpeed();
	}
    
    public static void setSpeed() {
        motorA.setSpeed(SPEED);
        motorB.setSpeed(SPEED);
	}
    
    private static void moveForward(){
        stop();
        System.out.println("Moving bot forward...");
        //motorA.forward();
        //motorB.forward();
        //Fixed
        motorA.rotate(AdvanceRotation, true);
        motorB.rotate(AdvanceRotation, true);
    }
    
    private static void moveBackward(){
        stop();
        System.out.println("Moving bot backward...");
        //TODO
        //motorA.backward();
        //motorB.backward();
        //Fixed
        motorA.rotate(-AdvanceRotation, true);
        motorB.rotate(-AdvanceRotation, true);
    }
        
    private static void stop(){
        System.out.println("Stopping bot...");
        //TODO
        motorA.stop(true);
        motorB.stop(true);
        //Fixed
    }
    
    private static void turnLeft(){
        stop();
        System.out.println("Turning bot left...");
        //Continous
        //motorA.backward();
        //motorB.forward();
        //Fixed
        motorA.rotate(-TurnRotation-RELAX_ROTATE, true);
        motorB.rotate(TurnRotation+RELAX_ROTATE+2, true);
    }
    
    private static void turnRight(){
        stop();
        System.out.println("Turning bot right...");
        //Continous
        //motorA.forward();
        //motorB.backward();
        //Fixed
        motorA.rotate(TurnRotation+4, true);
        motorB.rotate(-TurnRotation-2, true);
    }
    
    public enum BOT_ACTIONS{
        forward,
        backward,
        stop,
        left,
        right
    }
}