/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.VictorSPXPIDSetConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.hal.sim.mockdata.PDPDataJNI;
import edu.wpi.first.hal.sim.mockdata.RoboRioDataJNI;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.ExampleSubsystem;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  public static ExampleSubsystem m_subsystem = new ExampleSubsystem();
  public static OI m_oi;

  Command m_autonomousCommand;
  SendableChooser<Command> m_chooser = new SendableChooser<>();
  // Creating VictorSPX definitions for the victors to drive the robot.
  WPI_VictorSPX vic1 = new WPI_VictorSPX(1); //Canbus Device ID#1 (Left)
  WPI_VictorSPX vic2 = new WPI_VictorSPX(2); // Canbus Device ID#2
  WPI_VictorSPX vic3 = new WPI_VictorSPX(3); // Canbus Device ID#3 (Left)
  WPI_VictorSPX vic4 = new WPI_VictorSPX(4); // Canbus Device ID#4
  WPI_TalonSRX tal1 = new WPI_TalonSRX(5); // canbus Device ID#5
  SpeedControllerGroup left = new SpeedControllerGroup(vic1, vic3);
  SpeedControllerGroup right = new SpeedControllerGroup(vic2, vic4);
  //Create DifferentialDrive using Victor1 and Victor3 which we'll call later...
  DifferentialDrive DifDrive = new DifferentialDrive(left, right);
  Compressor maincomp = new Compressor(); // Initialize compressor
  DoubleSolenoid HatchA = new DoubleSolenoid(0, 2); // Forward, Reverse (OLD >) 5 A, 7 B
  DoubleSolenoid HatchB = new DoubleSolenoid(1,3); // Forward, Reverse (OLD >) 4 A, 6 B
  // Create Value for hatch minipulator extended.
  boolean hatchextended = false;
  //
  NetworkTableInstance inst = NetworkTableInstance.getDefault();
  
  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_oi = new OI();
    m_chooser.setDefaultOption("Default Auto", new ExampleCommand());
    // chooser.addOption("My Auto", new MyAutoCommand());
   // CameraServer.getInstance().startAutomaticCapture();
   // CameraServer.getInstance().startAutomaticCapture(1);
    SmartDashboard.putData("Auto mode", m_chooser);
    maincomp.setClosedLoopControl(true);
    HatchA.set(DoubleSolenoid.Value.kOff);
    HatchB.set(DoubleSolenoid.Value.kOff);
    // End setting up Victors to follow other victors....

  }
 
  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   * You can use it to reset any subsystem information you want to clear when
   * the robot is disabled.
   */
  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString code to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional commands to the
   * chooser code above (like the commented example) or additional comparisons
   * to the switch structure below with additional strings & commands.
   */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_chooser.getSelected();

    /*
     * String autoSelected = SmartDashboard.getString("Auto Selector",
     * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
     * = new MyAutoCommand(); break; case "Default Auto": default:
     * autonomousCommand = new ExampleCommand(); break; }
     */

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.start();
    }
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  
  
  
  @Override
  public void teleopPeriodic() {
    
    DifDrive.arcadeDrive(-RobotMap.mainJoystick.getRawAxis(1), RobotMap.mainJoystick.getRawAxis(0)); // X axis Z axis.
    Scheduler.getInstance().run();
    tal1.set(RobotMap.HatchandBall.getRawAxis(1));
    //TARGET, TARGET SIZE, TARGET CENTER!
    
    NetworkTable tableRect = inst.getTable("Target");
    NetworkTableEntry TargetSize = tableRect.getEntry("TargetSize");
    NetworkTableEntry TargetCenter = tableRect.getEntry("TargetCenter");
    //System.out.println("TargetSize: " + TargetSize.getValue().getDouble() + " TargetCenter: " + TargetCenter.getValue().getDouble());
    

    //Hatch manipulator handle...
    if(RobotMap.HatchandBall.getRawButtonPressed(1)){
      HatchA.set(DoubleSolenoid.Value.kForward);
      HatchB.set(DoubleSolenoid.Value.kForward);
      System.out.println("Button Pressed!");
    }
    if(RobotMap.HatchandBall.getRawButtonPressed(2)){
      System.out.println("Button Released!");
      HatchA.set(DoubleSolenoid.Value.kReverse);
      HatchB.set(DoubleSolenoid.Value.kReverse);
      //HatchA.set(DoubleSolenoid.Value.kOff);
      //HatchB.set(DoubleSolenoid.Value.kOff);
    }

   /* if(RobotMap.HatchandBall.getRawButtonPressed(2)){
      HatchA.set(DoubleSolenoid.Value.kReverse);
      HatchB.set(DoubleSolenoid.Value.kReverse);
    }else{
      HatchA.set(DoubleSolenoid.Value.kOff);
      HatchB.set(DoubleSolenoid.Value.kOff);
    }*/
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
