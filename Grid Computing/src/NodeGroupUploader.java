

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;


//this is a seperate thread object used to automatically
//upload NodeGroup to GS.
public class NodeGroupUploader implements Runnable
{
	NodeGroup ng = null; //So far, ng is not used, but don't delete it.
	int RMid =-1;
	String GS_url = null;    //the target GS
	String RM_url = null;   //the url of the RM who initiate this thread
	IResource_Manager RM = null; 
	String[] otherGS_url = new String[3];
	//{"rmi://localhost:5000/GS0",
	//"rmi://localhost:5001/GS1",
	//"rmi://localhost:5002/GS2",
	//"rmi://localhost:5003/GS3",
	//"rmi://localhost:5004/GS4"};
	
	ArrayList<Job> jobList = new ArrayList<Job>();
	IGrid_Scheduler GS = null;
	IGrid_Scheduler[] otherGS =new Grid_Scheduler[4];
	boolean alive = false;
	
	public NodeGroupUploader(NodeGroup ngroup,int RMid,String GS_url,String RM_url,ArrayList<Job> jobList)
	{
		
		
		ng = ngroup;
		this.RMid = RMid;
		this.GS_url = GS_url;
		this.RM_url = RM_url;
		this.jobList = jobList;
		
		try {
			GS = (IGrid_Scheduler)Naming.lookup(GS_url);
			System.out.println("NodeGroupUploader from "+RM_url+" to "+ GS_url+ " is now in checkpoint 1");
			RM = (IResource_Manager)Naming.lookup(RM_url);
			System.out.println("NodeGroupUploader from "+RM_url+" to "+ GS_url+ " is now in checkpoint 2");
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
		
		alive = true;
		
	}

	@Override
	public void run() 
	{
		try {
			//wait 10s right after initiation
			Thread.sleep(10000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// TODO Auto-generated method stub
		while(true)
		{
			while(alive)
			{
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
					try {
						RM.sendNodeGroup(GS_url);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NotBoundException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						System.out.println("No bound Exception in NodeGroup uploader");
						continue;
					}
				
				
				
				
			}
			
			
			
		}
		
	}

}
