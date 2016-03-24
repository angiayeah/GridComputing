package GridScheduler;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import ClientAndJob.Job;
import ResourceManager.IResource_Manager;
public class Grid_Scheduler extends UnicastRemoteObject implements IGrid_Scheduler {
    // 这个实现必须有一个显式的构造函数，并且要抛出一个RemoteException异常  
	
	ArrayList<String> RM_url = new ArrayList<String>();
	ArrayList<Integer> RM_id = new ArrayList<Integer>();
	ArrayList<Integer> RM_status = new ArrayList<Integer>();
	//I think static list is enough, maybe you think ArrayList is better?
	
	//we know there are 2 RMs
	
	String myurl;
	int myid;
	public static int GS_NUM = 5;	//change the number of GS here
	public static int RM_NUM = 4;	//change the number of RM per GS here
	
	public GSchecker[] GSc = new GSchecker[GS_NUM-1];
	Thread GSc_Thread[] = new Thread[GS_NUM-1];
	public GSRMchecker[] GSRMc = new GSRMchecker[RM_NUM];
	Thread GSRMc_Thread[] = new Thread[RM_NUM];


	
	
	
	
	static String[] GridSchedulers = new String[]{"rmi://localhost:5001/GS1",
            "rmi://localhost:5002/GS2",
            "rmi://localhost:5003/GS3",
            "rmi://localhost:5004/GS4",
            "rmi://localhost:5005/GS5",};
	
	
	
    protected Grid_Scheduler() throws RemoteException {
        super();
    }
    
    protected Grid_Scheduler(String url, int id) throws RemoteException {
        super();
        myurl = url;
        myid = id;
        
        String theurl = "rmi://localhost:"+ (5000+id)+"/GS"+id;
        LocateRegistry.createRegistry(5000+id);
        try {
			java.rmi.Naming.rebind(theurl, this);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //the following loop is to start GSchecker for the other GS
        /*
        for(int i = 1;i<5;i++)
        {
        	System.out.println("Now initiating GSchecker in GS"+myid);
        	int id_tocheck = (myid + i)%5;
        	//System.out.println("id_tocheck is  "+id_tocheck);
        	String url_tocheck = "rmi://localhost:"+ (5000+id_tocheck)+"/GS"+id_tocheck;
        	//System.out.println("url_tocheck is  "+url_tocheck);
        	GSc[i-1] = new GSchecker(url_tocheck,myid,true);
        	GSc_Thread[i-1] = new Thread(GSc[i-1],"GSchecker of GS"+myid+ "for GS"+ id_tocheck);
        	GSc_Thread[i-1].start();    	
        	
        }
        */
        //start GSTMchecker for RM under this Grid_Scheduler
        for(int i = 1;i<=RM_NUM;i++)
        {      	
        	int id_tocheck = myid*4+i-1;
        	System.out.println("Now initiating GSRMchecker for RM"+id_tocheck+ " in GS"+myid);
        	//System.out.println("id_tocheck is  "+id_tocheck);
        	String url_tocheck = "rmi://localhost:"+ (6000+id_tocheck)+"/RM"+id_tocheck;
        	//System.out.println("url_tocheck is  "+url_tocheck);
        	GSRMc[i-1] = new GSRMchecker(url_tocheck,myid,true,id_tocheck);
        	GSRMc_Thread[i-1] = new Thread(GSRMc[i-1],"GSRMchecker of GS"+myid+ "for RM"+ id_tocheck);
        	GSRMc_Thread[i-1].start();    	
        	
        }
        
        
        
        
    }
    
    /**
     * RM upload jobs to GS
     * @throws RemoteException 
     * @throws NotBoundException 
     * @throws MalformedURLException 
     */
    
    public void startGSchecker()
    {
    	for(int i=0;i<4;i++)
    	{
    		GSc[i].startChecker();
    	}
    }
    
    
    
    public void stopGSchecker()
    {
    	for(int i=0;i<4;i++)
    	{
    		GSc[i].stopChecker();
    	}
    	//return this.GSc;
    }
    
    
    public void uploadJob(Job job) throws RemoteException
    {
    	
    	System.out.println("Check RM free nodes number");
    	int maxFNode = 0;
    	IResource_Manager freeRM = null;
    	
		for(int i=0;i<RM_NUM;i++)
		{
		  
			 String theurl = "rmi://localhost:"+(6000+i)+"/RM"+i;
			 try {
				IResource_Manager RM = (IResource_Manager)Naming.lookup(theurl);
				if (RM.getFNodeNumber() >= maxFNode)
				 {
					 freeRM = RM;
					 maxFNode = RM.getFNodeNumber();
				 }
				 System.out.println("RM"+RM.getID()+" free nodes number: "+RM.getFNodeNumber());
			} catch (MalformedURLException | RemoteException | NotBoundException e) {
				// TODO Auto-generated catch block
				System.out.println("Can't find this RM"+i+" go to next RM");
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
				
					System.out.println("GS can't sleep");
					//e1.printStackTrace();
				}
				
				continue;	//continue the loop
				
			}
			 
		}
	
		try {
			System.out.println("Assign job to RM" + freeRM.getID());
			freeRM.receiveJobs(job, true);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		//must accept
    }
    
    
    
    /**
     * 说明清楚此属性的业务含义
     */
    public void regiterRM(String url, int id) throws java.rmi.RemoteException
    {
    	RM_url.add(url);
    	RM_id.add(id);
    	
    	System.out.println(url+" register in GS"+myid);
    	
    	try {
			IResource_Manager RM = (IResource_Manager)Naming.lookup(url);
			RM.testStub(myurl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void testStub(String url)
    {
    	System.out.println("stub of GS"+myid+ " has been used by " +url);
    }
    
    
    
    //the following method is used to respond to a GS that "I am alive"
    public boolean echoGSalive(String url)
    {
    	return true;
    }
    
 
    
    
    
    private static final long serialVersionUID = 4077329331699640331L;
    public String sayHello(String name) throws RemoteException {
        return "Hello " + name + " ^_^ ";
    }
    public static void main(String[] args) {
        try {
        	
        	
        	IGrid_Scheduler[] GSs = new Grid_Scheduler[5];
        	for(int i =0;i<GS_NUM;i++)
        	{
        		System.out.println("now start to create GS");
        		String theurl = "rmi://localhost:"+ (5000+i)+"/GS"+i;
        		//System.out.println("create GS in： "+ theurl);
        		IGrid_Scheduler GS = new Grid_Scheduler(theurl,i);
        		GSs[i] = GS;
        		//LocateRegistry.createRegistry(5000+i);
                //java.rmi.Naming.rebind(theurl, GS);
                System.out.println("GS"+i+" is Ready");
        	}
        	
        	
        	
        	//the following codes are used to test stop/start the thread.
        	/*
        	Thread.sleep(5000);
        	
            for(int i =0;i<5;i++)
            {
            	String theurl = "rmi://localhost:"+ (5000+i)+"/GS"+i;
            	IGrid_Scheduler GS =(IGrid_Scheduler) Naming.lookup(theurl);
            	//GSchecker[]GSc = GS.getGSchecker();
            	GS.stopGSchecker();
            	for(int j=0;j<5;j++)
            	{
            		
            		//GSc[j].stopChecker();
            		System.out.println("stop!!!!!!!!!");
            	}
            	
            }
        	
            
            Thread.sleep(30000);
            
            
            for(int i =0;i<5;i++)
            {
            	String theurl = "rmi://localhost:"+ (5000+i)+"/GS"+i;
            	IGrid_Scheduler GS =(IGrid_Scheduler) Naming.lookup(theurl);
            	//GSchecker[]GSc = GS.getGSchecker();
            	GS.startGSchecker();
            	for(int j=0;j<5;j++)
            	{
            		
            		//GSc[j].startChecker();
            		System.out.println("start!!!!!!!!!!!");
            	}
            	
            }
        	*/
        	
            
        	 System.out.println("All GridSchedulers are ready");
        } catch (Exception e) 
        {
            e.printStackTrace();
            
            
            try {
				Thread.sleep(60000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
    }
}