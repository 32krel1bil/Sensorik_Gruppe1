
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package senoric_gr1__robotiono;



import java.util.TimerTask;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

import rec.robotino.api2.Bumper;
import rec.robotino.api2.Com;
import rec.robotino.api2.OmniDrive;
import rec.robotino.api2.DistanceSensor;

/**
 *
 * @author ek
 */
public class Sensoric_gr1__robotino {

  protected final Com _com;
    protected final OmniDrive _omniDrive;
    protected final Bumper _bumper;    
   protected final DistanceSensor _distanceSensor1;

    public Sensoric_gr1__robotino()
    {
            _com = new MyCom();
            _omniDrive = new OmniDrive();
            _bumper = new Bumper();
            _distanceSensor1 = new DistanceSensor ();

            _omniDrive.setComId(_com.id());
            _bumper.setComId(_com.id());
            
            _distanceSensor1.setComId(_com.id());
            _distanceSensor1.setSensorNumber (0);
            

            if(_distanceSensor1.voltage() >2.0)
                doSomething();  //object in near area
    }

    public boolean isConnected()
    {
            return _com.isConnected();
    }

    public void connect(String hostname, boolean block)
    {
            System.out.println("Connecting...");
            _com.setAddress( hostname );
            _com.connectToServer(block);
    }

    public void disconnect()
    {
            _com.disconnectFromServer();
    }

    public void setVelocity(float vx, float vy, float omega)
    {
            _omniDrive.setVelocity( vx, vy, omega );
    }
	
    public void rotate(float[] inArray, float[] outArray, float deg)
    {
        float rad = 2 * (float)Math.PI / 360.0f * deg;
        outArray[0] = (float)Math.cos(rad) * inArray[0] - (float)Math.sin(rad) * inArray[1];
        outArray[1] = (float)Math.sin(rad) * inArray[0] + (float)Math.cos(rad) * inArray[1];
    }
    
    public void turn() throws InterruptedException
    {
    	System.out.println("Driving...");
        float[] startVector = new float[] {0.2f, 0.0f };
        float[] dir = new float[2];
        float a = 0.0f;
        while (_com.isConnected() && false == _bumper.value() )
        {
            //rotate 360degrees in 5s
	        rotate( startVector, dir, a );
	        a = 360.0f * _com.msecsElapsed() / 5000;

	        _omniDrive.setVelocity( dir[0], dir[1], 0 );

            Thread.sleep(100);
        }
    }
public void turnAngle (float angle) throws InterruptedException          {
    
    float angularVel = 0;
     _omniDrive.setVelocity( 0.0f, 0.0f, 0 ); 
     Thread.sleep(100);
     angularVel = angle * _com.msecsElapsed() / 5000;
     _omniDrive.setVelocity( 0.001f, 0.0f, angularVel );
     System.out.printf("Turning ... %f  degree\n",angle);  
     Thread.sleep(1000);
     _omniDrive.setVelocity( 0.0f, 0.0f, 0 );

}
    public void driveTriangle(int time_s1,int time_s2,int time_s3,float v1,float v2, float v3) throws InterruptedException
    {//reiner Grundcode
        
    	System.out.println("Driving1...");
        for(int i = 0; i < (time_s1 * 10); ++i)   
                        
                    {
            _omniDrive.setVelocity( v1, 0.0f, 0 );
            Thread.sleep(100);
        }
        _omniDrive.setVelocity( 0.0f, 0.0f, 0 );
        System.out.println("Stopped1 ...");
        
        System.out.println("Driving2...");
        for(int i = 0; i < (time_s2 * 10); ++i)
             
                    {
            _omniDrive.setVelocity( 0.0f, v2 , 0 );
            Thread.sleep(100);
        }
        _omniDrive.setVelocity( 0.0f, 0.0f, 0 );
        System.out.println("Stopped2 ...");
        
        System.out.println("Driving3...");
        for(int i = 0; i < (time_s3 * 10); ++i)
             
                    {
            _omniDrive.setVelocity( v3, v3, 0 );
            Thread.sleep(100);
        }
        _omniDrive.setVelocity( 0.0f, 0.0f, 0 );
        System.out.println("Stopped3 ...");
        
         
        
        
       
    }

        

  
    
    private void doSomething() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * The class MyCom derives from rec.robotino.api2.Com and implements some of the virtual event handling methods.
     * This is the standard approach for handling these Events.
     */
    class MyCom extends Com
    {
            Timer _timer;

            public MyCom()
            {
                    _timer = new Timer();
                    _timer.scheduleAtFixedRate(new OnTimeOut(), 0, 20);
            }

            class OnTimeOut extends TimerTask
            {
                    public void run()
                    {
                            processEvents();
                    }
            }

            @Override
            public void connectedEvent()
            {
                    System.out.println( "Connected" );
            }

            @Override
            public void errorEvent(String errorStr)
            {
                    System.err.println( "Error: " + errorStr );
            }

            @Override
            public void connectionClosedEvent()
            {
                    System.out.println( "Disconnected" );
            }
    }
	
    public static void main(String args[])
    {
            String hostname = "172.26.1.1";
            String hostname_sim = "127.0.0.1";
            if( args.length == 1)
            {
                    hostname = args[0].toString();
            }

            Sensoric_gr1__robotino robotino = new Sensoric_gr1__robotino();

            try
            {
                    //robotino.connect(hostname, true);
                    robotino.connect(hostname_sim, true);
            
                   for (int i = 0; i <3; i++){
                      robotino.driveTriangle(3,3,3,0.3f,0.4f,-0.3535533906f);//hier werden alle Werte gespeichert und verändert
                      //oben reiner Grundcode
                    }  
                    
                    //robotino.turnAngle(90f);

                    //robotino.turn();
                    robotino.disconnect();
            }
            catch (Exception e)
            {
                    System.out.println(e.toString());
            }
    }
}