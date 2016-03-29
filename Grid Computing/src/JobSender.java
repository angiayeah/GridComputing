
import java.awt.print.Printable;
import java.net.MalformedURLException;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;

import org.omg.PortableServer.POAPackage.WrongAdapter;


public class JobSender extends UnicastRemoteObject implements IJobSender{
    // 这个实现必须有一个显式的构造函数，并且要抛出一个RemoteException异常  
	
    String myurl;
    int myid;
    static int RMreceive_id = 1;
    int RM_NUM = 20;
    
	
    protected JobSender() throws RemoteException {
        super();
    }
    
    protected JobSender(String url, int id) throws RemoteException {
        super();
        myurl = url;
        myid = id;
        
        
        LocateRegistry.createRegistry(8000+id);	//only one job sender, should be 8001
        try {
			java.rmi.Naming.rebind(myurl, this);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    
    /**
     * 
     * @param RM_id the id of RM, should be 0 or 1
     * @throws RemoteException
     * send jobs to specific RM
     * @throws InterruptedException 
     */
    public void sendJobs(int RM_id) throws RemoteException, InterruptedException
    {	
        
        try {
        	System.out.println("Sending Jobs to RM" + RM_id + "...");
        	String RM_url = "rmi://localhost:"+(6000+RM_id)+"/RM"+RM_id;
        	IResource_Manager RM = (IResource_Manager)Naming.lookup(RM_url);
        	
        	/********while true can be dangerous!!! This meant to keep sending jobs*******/
        	long JOB_ID = 1;
        	Random random = new Random();
        	int round = 1;
        	
        	while (round<2)
        	{
        		for (int i=0; i<10 ; i++)
            	{
        			int duration = random.nextInt(5000)+3000;		//generate job with random duration
        			
        			try {
        				RM.receiveJobs(new Job(duration, JOB_ID),false);
        				System.out.println("Send Job:"+JOB_ID);
					} catch (Exception e) {
						System.out.println("wrong here!!!");
						e.printStackTrace();
						continue;
					}
            		
            		JOB_ID++;
            	}
        		round++;
        		Thread.sleep(5000);		//sleep for 5 seconds
        	}        	
        	
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			if (RMreceive_id < RM_NUM-1)
			{
				RMreceive_id++;
			}
			else
			{
				RMreceive_id = 0;
			}
			sendJobs(RMreceive_id);
			//e.printStackTrace();
		}
    }
    /**
     * 说明清楚此属性的业务含义
     */
    private static final long serialVersionUID = 4077329331699640331L;
    public String sayHello(String name) throws RemoteException {
        return "Hello " + name + " ^_^ ";
    }
    
    
    //this function for Node to test whether the stub from this Resource_Manager
    //work in an appropriate way.
    public void testStub()
    {
    	System.out.println("stub of JS has been used");
    }
    
    public void testStub(String url)
    {
    	System.out.println("stub of JS"+myid+ " has been used by " +url);
    }
    
    public static void main(String[] args) throws RemoteException {
    	String theurl = "rmi://localhost:"+(8001)+"/JS"+1;	//only one job sender
    	JobSender JS = new JobSender(theurl,1);

    	  System.out.println("now start to create JS");
          System.out.print("Job Sender is Ready");
          try {
			JS.sendJobs(RMreceive_id);
		} catch (InterruptedException e) {
			
			//e.printStackTrace();
		}
        	

    }



	
}