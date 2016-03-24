package GridScheduler;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

class GSchecker implements Runnable,java.io.Serializable
	{
		IGrid_Scheduler GS;
		int deadcount = 0;
		String GS_url;
		boolean alive = false;
		boolean isRun;
		int myid; //the id of the Grid which initiates this GSchecker
		public GSchecker(String url,int id,boolean isRun)
		{
		  GS_url = url;
		  myid = id;
		  this.isRun = isRun;
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
		
		  public void checkGSalive(String url)
		    {
		    	try {
		    		//System.out.println("Start to sleep");
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					System.out.println("Exception 1");
					checkGSalive(url);
				}
		    
		    	
		    	boolean alive = false;
		    	
		    	//notice that, if the try fail, that may indicate that the
		    	//GS is dead.
		    	try {
		    		//System.out.println("looking for other GS");
					GS = (IGrid_Scheduler)Naming.lookup(GS_url);
					//System.out.println("GS in: "+ GS_url+" has been found");
					//GS.testStub(myurl);
				} catch (MalformedURLException e) {
					System.out.println("Exception 2");
		    		deadcount++;
		    		checkGSalive(url);
					e.printStackTrace();
				} catch (RemoteException e) {
					System.out.println("Exception 3");
		    		deadcount++;
		    		checkGSalive(url);
					e.printStackTrace();
				} catch (NotBoundException e) {
					System.out.println("Exception 4");
		    		deadcount++;
		    		checkGSalive(url);
					e.printStackTrace();
				}
		    	

		    	
		    	try {
		    		//System.out.println("Is that alive");
					alive = GS.echoGSalive(url);
				} catch (RemoteException e) {
					System.out.println("Exception 5");
		    		deadcount++;
		    		checkGSalive(url);
					e.printStackTrace();
				}
		    	

		    	
		    	if(alive==false)
		    	{
		    		System.out.println("No response");
		    		deadcount++;
		    		//checkGSalive(url);
		    	}
		    	
		    	if(alive==true)
		    	{
		    		System.out.println("the GS in "+ url+" is alive");
		    		deadcount = 0;
		    		//checkGSalive(url);
		    	}
		    	
		    	if(deadcount>5)
		    	{
		    		System.out.println("the GS in "+ url+" is dead");
		    		try {
						Thread.sleep(myid*10000);//wait for a while according to myid
						//TODO:take action and inform other GSchecker that I have take action.
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		checkGSalive(url);
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
				while(isRun)
				{
					
					checkGSalive(GS_url);
					System.out.println("Check finished");
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
					System.out.println("The GSchecker of GS"+myid+" for "+GS_url+" is stopped");
				}
			}
			
		}
	} //GSchecker end