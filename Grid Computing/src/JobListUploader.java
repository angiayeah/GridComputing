

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;



public class JobListUploader implements Runnable
{
	NodeGroup ng = null;
	int RMid =-1;
	String GS_url = null;
	IGrid_Scheduler GS =null;
	
	public JobListUploader(NodeGroup ngroup,int RMid,String GS_url)
	{
		ng = ngroup;
		this.RMid = RMid;
		this.GS_url = GS_url;
		try {
			GS = (IGrid_Scheduler)Naming.lookup(GS_url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.out.println("MalforeURLException");
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			System.out.println("RemoteException");
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			System.out.println("NotBoundException");
			e.printStackTrace();
		}
		
	}

	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		while(true)
		{
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
	}

}
