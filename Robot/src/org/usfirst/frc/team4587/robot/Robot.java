package org.usfirst.frc.team4587.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import org.usfirst.frc.team4587.robot.commands.ExampleCommand;
import org.usfirst.frc.team4587.robot.subsystems.ExampleSubsystem;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
	public static OI oi;
	private RobotDrive Drive;
	private Solenoid FirstSol , SecSol;
	private boolean ActFirst;
	private boolean ActSec;
    private Joystick Controller;
    private SpeedController FrontLeft, FrontRight, BackLeft, BackRight, Rotate;
    private Encoder RotateEncoder;
    //private final DriverStationLCD lcd = DriverStationLCD.getInstance();
    private int count;
    
    public void RobotDriveInit() 
    {
    	Drive = new RobotDrive(FrontLeft, FrontRight, BackLeft, BackRight);
    }
    
    //sets which motor is used and which PWM is slot is being used
    public void TalonInit() 
    {
    	//driving motors
        FrontLeft = new Talon(0);
        FrontRight = new Talon(1);
        BackLeft = new Talon(2);
        BackRight = new Talon(3);
        //testing for encoder
        Rotate = new Talon(4);
    }
    
    //sets which solenoid to drive
    public void SolenoidInit() 
    {
    	FirstSol = new Solenoid(0);
    	SecSol = new Solenoid(1);
    }
    //sets which controller to pull data from
    public void ControllerInit() 
    {
        Controller = new Joystick(0);
    }
    
    //which sensor is used
    public void SensorInit()
    {
    	//i dont know the numbers that go inside the "()"
    	//RotateEncoder = new Encoder();
    	//set encoder counter to "count"
    	Encoder enc;
    	enc = new Encoder(0,1,false,EncodingType.k4X);
    }
    //sets the amount of space the joystick has to move before being sensed 
    private double getDeadZone(double num, double dead) 
    {
        if (num < 0) 
        {
            if (num < dead) 
            {
                return num;
            } 
            else 
            {
                return 0;
            }
        } 
        else 
        {
            if (num > dead) 
            {
                return num;
            } 
            else 
            {
                return 0;
            }
        }
    }

    Command autonomousCommand;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
		oi = new OI();
        // instantiate the command used for the autonomous period
        autonomousCommand = new ExampleCommand();
        
        ControllerInit();
        //which talon driving
    	TalonInit();
    	RobotDriveInit();
    	//which sensor to use
    	SensorInit();
    	//which solenoid to use
    	SolenoidInit();
    	//turns off all Solenoids on start up
    	ActFirst = false;
    	ActSec = false;
    	//number of encoder rotations
    	count = 0;
    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

    public void autonomousInit() {
        // schedule the autonomous command (example)
        if (autonomousCommand != null) autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    public void disabledInit(){

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        
      //driving 
    	Drive.arcadeDrive(getDeadZone(-Controller.getY(), 1.0),
                getDeadZone(-Controller.getThrottle(), 1.0),  true  );
    	
    	//testPenumatics(First Solenoid)
    	if(Controller.getRawButton(5))
    	{	
    		if(ActFirst == false)
    		{
    			FirstSol.set(true);
    			ActFirst = true;
    		}
    		else
    		{	
    			FirstSol.set(false);
    			ActFirst = false;
    		}
    	}	
    	//testPenumatics(Second Solenoid)
    	if(Controller.getRawButton(6))
    	{	
    		if(ActSec == false)
    		{
    			SecSol.set(true);
    			ActSec = true;
    		}
    		else
    		{	
    			SecSol.set(false);
    			ActSec = false;
    		}
    	}	
    	
    	if(Controller.getRawButton(1))
    	//run motor till encoder rotates 2
    	{
    		//displays the encoder rotation count
    		//lcd.println(DriverStationLCD.Line.kUser2, 1, "Encoder rotations :" + count);
    		if(count <5)
    		{
    			//drive motor till count is equal to 2
    			Rotate.set(1);
    			//takes in encoder rotations
    		}
    		else
    		{
    			//resets the encoder
    			RotateEncoder.reset();
    			count =0;
    		}
    	}
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
    
    public Robot() 
    {
        myRobot = new RobotDrive(0, 1);
        myRobot.setExpiration(0.1);
        leftStick = new Joystick(0);
        rightStick = new Joystick(1);
    }
    
    public void operatorControl() 
    {
        myRobot.setSafetyEnabled(true);
        while (isOperatorControl() && isEnabled()) {
        	myRobot.tankDrive(leftStick, rightStick);
            Timer.delay(0.005);		// wait for a motor update time
        }
    }
}
