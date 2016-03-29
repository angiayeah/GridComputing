
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;



class GSRMchecker implements Runnable,java.io.Serializable
{
	IResource_Manager RM;
	IGrid_Scheduler GS;
	int deadcount = 0;
	String RM_url;
	boolean alive = false;
	boolean isRun;
	int myid; //the id of the Grid which initiates this GSchecker
	int RM_id;
	
	public GSRMchecker(String url,int id,boolean isRun,int RMid)
	{
	  RM_url = url;
	  myid = id;
	  this.isRun = isRun;
	  RM_id = RMid;
	  String gsurl = "rmi://localhost:"+ (5000+id)+"/GS"+id;;
	  try {
		GS = (IGrid_Scheduler)Naming.lookup(gsurl);
	} catch (MalformedURLException | RemoteException | NotBoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	public void crashAction()
	{
		try {
			Thread.sleep(myid*10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	  public void checkRMalive(String url) throws RemoteException
	    {
		  
	    	try {
	    		//System.out.println("Start to sleep");
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.out.println("Exception 1");
				//checkRMalive(url);
			}
	    
	    	
	    	boolean alive = false;
	    	
	    	//notice that, if the try fail, that may indicate that the
	    	//GS is dead.
	    	try {
	    		//System.out.println("looking for other GS");
				RM = (IResource_Manager)Naming.lookup(url);
				alive = RM.echoRMalive(url);
				//System.out.println("GS in: "+ GS_url+" has been found");
				//GS.testStub(myurl);
			} catch (MalformedURLException e) {
				System.out.println("Exception 2");
	    		deadcount++;
				//e.printStackTrace();
			} catch (RemoteException e) {
				System.out.println("Exception 3");
	    		deadcount++;
				//e.printStackTrace();
			} catch (NotBoundException e) {
				System.out.println("RM" + RM_id+" Exception 4  " + deadcount+"  "+alive);
	    		deadcount++;
				//e.printStackTrace();
			}
	    	

	    	
	    	if(alive==false)
	    	{
	    		System.out.println("RM"+RM_id+" No response!!!  " + deadcount);
	    		deadcount++;
	    		//checkGSalive(url);
	    	}
	    	
	    	if(alive==true)
	    	{
	    		System.out.println("RM"+RM.getID()+" is alive");
	    		deadcount = 0;
	    		//checkGSalive(url);
	    	}
	    	
	    	if(deadcount>=5)
	    	{
	    		System.out.println("the RM in "+ url+" is dead!!!");
	    		GS.transferRMJobs(RM.getID(), null);
	    		//checkRMalive(url);
	    	}
	    	
	    }
	  
	public void stopChecker()
	{
		isRun = false;
	}
	
	public void startChecker()
	{
		isRun = true;
	}

	@Override
	public void run() 
	{
		while(true)
		{
			if(isRun == false)
			{
				System.out.println("RMchecker of GS "+myid+ "for "+RM_id+" is dead");
			}
			while(isRun)
			{
				
				try {
					checkRMalive(RM_url);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("sleep 5000 fail");
			}
			
			if(isRun == false)
			{
				System.out.println("The GSRMchecker of GS"+myid+" for RM"+RM_url+" is stopped");
			}
		}
		
	}
} //