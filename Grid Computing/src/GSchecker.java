
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

class GSchecker implements Runnable,java.io.Serializable
	{
		IGrid_Scheduler GS;   //the GS to check
		int deadcount = 0;
		String GS_url;    //the url of the GS to check
		int GS_id;       //the id of the GS to check
		boolean alive = false;
		boolean isRun;
		int myid; //the id of the Grid which initiates this GSchecker
		String myGS_url= null;
		IGrid_Scheduler myGS;
		public GSchecker(String GSurl,int GSid,int myid,String myGS_url,boolean isRun)
		{
		  GS_url = GSurl;
		  this.GS_id = GSid;
		  this.myid = myid;
		  this.myGS_url = myGS_url; 
		  this.isRun = isRun;
		  try {
			myGS = (IGrid_Scheduler)Naming.lookup(myGS_url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
		
		//this is the function called when a GS is crashed
		//it will send a ConfigMessage Object to the GS who initiates this GSchecker.
		public void crashAction()
		{
			ConfigMessage message = new ConfigMessage(ConfigMessageType.crash,MemberType.GS,GS_id,GS_url,MemberType.GSchecker,myid);
			
			
				System.out.println("this is GS checker in GS"+myid+" for GS"+GS_id);
				System.out.println("start sending message to my GS");
				
				try {
					Thread.sleep(myid*10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					myGS.receiveMessage(message);
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NotBoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			
		}
		
		  public void checkGSalive(String url) throws MalformedURLException,RemoteException,NotBoundException
		    {

		    	if(deadcount>1)
		    	{
		    		boolean alive = false;
		    		System.out.println("the GS in "+ url+" is dead");
		    		System.out.println("the GS in "+ url+" is dead");
		    		System.out.println("the GS in "+ url+" is dead");
		    		System.out.println("the GS in "+ url+" is dead");
		    		try {
						Thread.sleep(myid*1000);//wait for a while according to myid
						//TODO:take action and inform other GSchecker that I have take action.
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		//checkGSalive(url);
		    		this.crashAction();
		    	}
			    
			  
		    	
		    	
		    	
		    	//notice that, if the try fail, that may indicate that the
		    	//GS is dead.
		    	
		    		//System.out.println("looking for other GS");
					GS = (IGrid_Scheduler)Naming.lookup(GS_url);
					alive = GS.echoGSalive(url);
					//System.out.println("GS in: "+ GS_url+" has been found");
					//GS.testStub(myurl);
				
		    	
//
//		    	
//		    	try {
//		    		//System.out.println("Is that alive");
//					alive = GS.echoGSalive(url);
//				} catch (RemoteException e) {
//					System.out.println("Exception 5");
//		    		deadcount++;
//		    		//checkGSalive(url);
//					e.printStackTrace();
//				}
		    	

		    	
		    	if(alive==false)
		    	{
		    		System.out.println("No response");
		    		deadcount++;
		    		//checkGSalive(url);
		    	}
		    	
		    	if(alive==true)
		    	{
		    		//System.out.println("the GS in "+ url+" is alive");
		    		deadcount = 0;
		    		//checkGSalive(url);
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
			int runloop = 0;
			while(true)
			{
				try {
					Thread.sleep(15000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("sleep 10000 fail");
				}
				
				
				while(isRun)
				{
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("sleep 25000 fail");
					}
					runloop++;
					System.out.println("this is the "+runloop+"th loop");
					try {
						checkGSalive(GS_url);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						deadcount++;
						e.printStackTrace();
						continue;
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						deadcount++;
						e.printStackTrace();
						continue;
					} catch (NotBoundException e) {
						// TODO Auto-generated catch block
						deadcount++;
						//e.printStackTrace();
						System.out.println("No bound");
						continue;
					}
					System.out.println("Check finished");
				
					try {
						Thread.sleep(20000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("sleep 25000 fail");
					}
				
				
				}
				
				
				
				if(isRun == false)
				{
					System.out.println("The GSchecker of GS"+myid+" for "+GS_url+" is stopped");
				}
			}
			
		}
	} //GSchecker end