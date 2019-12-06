/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

//Normal Bot Actions
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;

//Pilot Bot Actions
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

/**
 *
 * @author uidk2372
 */

//Normal BotActions
public abstract class BotActions {
    
    private static BOT_ACTIONS action;
   
	private static EV3LargeRegulatedMotor motorB;
	private static EV3LargeRegulatedMotor motorA;
	
	private static int TurnRotation = 195;
	private static int AdvanceRotation = 40;
	private static int SPEED = 100;
	private static int counter = 0;
	//private static int RELAX_ROTATE =5; 
	
    
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
        motorA.rotate(-TurnRotation, true);
        motorB.rotate(TurnRotation-5, true);
    }
    
    private static void turnRight(){
        stop();
        System.out.println("Turning bot right...");
        //Continous
        //motorA.forward();
        //motorB.backward();
        //Fixed
        motorA.rotate(TurnRotation, true);
        motorB.rotate(-TurnRotation+7, true);
    }
    
    public enum BOT_ACTIONS{
        forward,
        backward,
        stop,
        left,
        right
    }
}

//MovePilot BotActions
//public abstract class BotActions {
//    
//    private static BOT_ACTIONS action;
//   
//	private static Wheel WHEEL_A;
//	private static Wheel WHEEL_B;
//	
//	private static Chassis CHASSIS;
//	private static MovePilot PILOT;
//	
//	private static double WHEEL_SIZE = MovePilot.WHEEL_SIZE_EV3;
//	private static double ADVANCE_DISTANCE = 5;
//	private static double TURN_ROTATION = 90;
//	private static int SPEED = 100;
//	
//    
//    public static void performAction(String actionMessage){
//		//Move Robot
//		try{
//            if(WHEEL_A==null || WHEEL_B==null)
//            {
//            	initiliazileWheels();
//            }
//            
//
//            action = BotActions.BOT_ACTIONS.valueOf(actionMessage);
//            
//
//    		
//        	if(actionMessage.startsWith("wheel:")) {
//        		//Change WHEEL SIZE
//        		try {
//        			WHEEL_SIZE = Integer.parseInt(actionMessage.split(":")[1]);
//        		}catch(Exception e){ System.out.println("Failed to change wheel size with input" + actionMessage);}
//        	}
//        	else if(actionMessage.startsWith("advace:")) {
//        		//Change ADVANCE DISTANCE
//        		try {
//        			ADVANCE_DISTANCE = Integer.parseInt(actionMessage.split(":")[1]);
//        		}catch(Exception e){ System.out.println("Failed to change advance size with input" + actionMessage);}
//        	}
//        	else if(actionMessage.startsWith("rotate:")) {
//        		//Change ADVANCE DISTANCE
//        		try {
//        			TURN_ROTATION = Integer.parseInt(actionMessage.split(":")[1]);
//        		}catch(Exception e){ System.out.println("Failed to change rotation angle with input" + actionMessage);}
//        	}
//        	else if(actionMessage.startsWith("speed:")) {
//        		//Change speed
//        		try {
//        			SPEED = Integer.parseInt(actionMessage.split(":")[1]);
//        			setPilot();
//        		}catch(Exception e){ System.out.println("Failed to change speed with input" + actionMessage);}
//        	}
//        	else {
//
//                switch(action){
//                    case forward:
//                        BotActions.moveForward();
//                        break;
//                    case backward:
//                        BotActions.moveBackward();
//                        break;
//                    case left:
//                        BotActions.turnLeft();
//                        break;
//                    case right:
//                        BotActions.turnRight();
//                        break;
//                    case stop:
//                        BotActions.stop();
//                        break;
//                    default:
//                        //invalid message. Do nothing.
//                        System.out.println("Invalid action. Continue...");
//                        break;
//                }
//        	}
//        }
//        catch(Exception e){
//        	System.out.println(e.getMessage());
//        }
//
//    }
//    
//    public static void initiliazileWheels() {
//    	try {
//	    	WHEEL_A = WheeledChassis.modelWheel(Motor.A, WHEEL_SIZE);
//	    	WHEEL_B = WheeledChassis.modelWheel(Motor.C, WHEEL_SIZE);
//	    	setPilot();
//    	}
//    	catch(Exception e) {
//    		System.out.println(e.getMessage());
//    	}
//	}
//    
//    public static void setPilot() {
//    	CHASSIS = new WheeledChassis(new Wheel[] {WHEEL_A, WHEEL_B}, 2);
//    	PILOT = new MovePilot(CHASSIS);
//	}
//    
//    private static void moveForward(){
//        System.out.println("Moving bot forward...");
//        PILOT.travel(ADVANCE_DISTANCE, true);
//    }
//    
//    private static void moveBackward(){
//        System.out.println("Moving bot backward...");
//        PILOT.travel(-ADVANCE_DISTANCE, true);
//    }
//        
//    private static void stop(){
//        System.out.println("Stopping bot...");
//        PILOT.stop();
//    }
//    
//    private static void turnLeft(){
//        System.out.println("Turn Left...");
//    	PILOT.rotate(TURN_ROTATION, true);
//    }
//    
//    private static void turnRight(){
//        System.out.println("Turn Right...");
//    	PILOT.rotate(-TURN_ROTATION, true);
//    }
//    
//    public enum BOT_ACTIONS{
//        forward,
//        backward,
//        stop,
//        left,
//        right
//    }
//}


