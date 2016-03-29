
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;


public class Grid_Scheduler extends UnicastRemoteObject implements IGrid_Scheduler {
    // 这个实现必须有一个显式的构造函数，并且要抛出一个RemoteException异常  
	
	ArrayList<String> RM_url = new ArrayList<String>();
	ArrayList<Integer> RM_id = new ArrayList<Integer>();
	ArrayList<Integer> RM_status = new ArrayList<Integer>();
	//I think static list is enough, maybe you think ArrayList is better?
	
	//we know there are 4 RMs
	
	String myurl;
	int myid;
	public static int GS_NUM = 5;	//change the number of GS here
	public static int RM_NUM = 4;	//change the number of RM per GS here
	int[] action_GS = new int[GS_NUM*RM_NUM]; //1 means there are actions to take, 0 means no
	                                     //-1 means the GS is offline or that is me
	
	boolean alive =false;
	public GSchecker[] GSc = new GSchecker[GS_NUM-1];
	Thread GSc_Thread[] = new Thread[GS_NUM-1];
	public GSRMchecker[] GSRMc = new GSRMchecker[RM_NUM];
	Thread GSRMc_Thread[] = new Thread[RM_NUM];
	RMGroup rmgroup = new RMGroup();
	HashMap<Integer, ArrayList<Job>> job_hashmap = new HashMap<Integer, ArrayList<Job>>();

	
	
	
	
	static String[] GridSchedulers = new String[]{"rmi://localhost:5001/GS1",
            "rmi://localhost:5002/GS2",
            "rmi://localhost:5003/GS3",
            "rmi://localhost:5004/GS4",
            "rmi://localhost:5005/GS5",};
	
	
	//this functioned is called when you receive a Message from other system members
	public synchronized void receiveMessage(ConfigMessage message) 
	{
		if(message.getType()==ConfigMessageType.crash&&message.getTargetType()==MemberType.GS)
		{
			
				System.out.println("this is GS"+myid+", Message received");
				
			
			//if the Message is about GS crash
			int target_id = message.getTargetID();
			if(true)
			{
				//other GS is dealing with this event, I do need to do it then.
				System.out.println("this is GS"+myid+" start to call GS crash action");
				if(message.getSourceType()!=MemberType.GSchecker)
				{
					int TTL = message.getTimeToLive();
					TTL--;
					message.setTimeToLive(TTL);
					//action_GS[target_id]=0;
				}
				
				if(message.getSourceType()!=MemberType.GSchecker)
				{
					//Message coming from other GS
					//that means that the action has been taken, we don't need
					//to do anything now.
					action_GS[target_id]=0; 
				}
				
				
				
				//if(message.getTimeToLive()>0)
				if(message.getSourceType()==MemberType.GSchecker&&action_GS[target_id]==1)
				{
					message.setSourceType(MemberType.GS);
					
					this.action_GScrash(message);
					//mark that "this GS has been disconnected", so we don't need to do it again
					action_GS[target_id]=0; 
					                        
				}
				
				
			}
			
		}
	}
	
	
	//this functioned is used to deal with "GS crash" event
	public synchronized void action_GScrash(ConfigMessage message)
	{
		System.out.println("action_GScrash in GS"+myid+" is now start");
		MemberType source = message.getSourceType();
		int source_id = message.getSourceID();
		int GS_crash_id = message.getTargetID();
		IGrid_Scheduler[] GS_toNotify = new Grid_Scheduler[GS_NUM];
		IResource_Manager[] RM_toNotify = new Resource_Manager[RM_NUM];
		for(int i=0;i<GS_NUM;i++)
		{
			String GS_toNotify_url = "rmi://localhost:"+ (5000+i)+"/GS"+i;
			IGrid_Scheduler	newGS =null;
			if(myid!=i&&GS_crash_id!=i&&source_id!=i)
			{
				try {
					
					newGS	=  (IGrid_Scheduler) Naming.lookup(GS_toNotify_url);
					//System.out.println("the class of newGS is: "+newGS.getClass()+"!!!!!!");
					//GS_toNotify[i] = newGS; 
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
				
				
					
					try {
						System.out.println("this is GS"+myid+", start to notify other GS");

						
							newGS.receiveMessage(message);
						
						
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
		}
		
		System.out.println("all GS notify");
		
		for(int i=0;i<RM_NUM*GS_NUM;i++)
		{
			String RM_toNotify_url = "rmi://localhost:"+ (6000+i)+"/RM"+i;
			IResource_Manager newRM = null;
			try {
				newRM =  (IResource_Manager) Naming.lookup(RM_toNotify_url);
				
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
			
			
				try {
					System.out.println("start to send message to RM");
					System.out.println("start to send message to RM");
					System.out.println("start to send message to RM");
					System.out.println("start to send message to RM");
					newRM.receiveMessage(message);
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
		
		
		
		
	}
	
	
	public void goDie()throws RemoteException
	{
		//stop all GSRM checker
		System.out.println("GS"+myid+" is dying");
		System.out.println("shuting down GSRMc");
		if(GSRMc!=null)
		{
			for(int i=0;i<RM_NUM;i++)
			{
				GSRMc[i].stopChecker();
			}
		}
		System.out.println("shuting down GSc");
		if(GSc!=null)
		{
			for(int i=0;i<GS_NUM-1;i++)
			{
				if(GSc[i]!=null)
				{
					GSc[i].stopChecker();
				}
				
			}
		}
		
		System.out.println("unbind url");
		try {
			Naming.unbind(myurl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("set alive to false");
		this.alive =false;
	}
	
	
    protected Grid_Scheduler() throws RemoteException {
        super();
    }
    
    protected Grid_Scheduler(String url, int id) throws RemoteException {
        super();
        myurl = url;
        myid = id;
        
        for(int i=0;i<GS_NUM*RM_NUM;i++)
        {
        	if(myid==i)
        	{
        		action_GS[i] = -1;   //set self to -1
        	}
        	
        	if(myid!=i)
        	{
        		action_GS[i] = 1;    //set other GS to 0
        	}
        }
        
        String theurl = "rmi://localhost:"+ (5000+id)+"/GS"+id;
        LocateRegistry.createRegistry(5000+id);
        try {
			java.rmi.Naming.rebind(theurl, this);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        //the following loop is to start GSchecker for the other GS
        
        for(int i = 1;i<5;i++)
        {
        	System.out.println("Now initiating GSchecker in GS"+myid);
        	int id_tocheck = (myid + i)%5;
        	//System.out.println("id_tocheck is  "+id_tocheck);
        	String url_tocheck = "rmi://localhost:"+ (5000+id_tocheck)+"/GS"+id_tocheck;
        	//System.out.println("url_tocheck is  "+url_tocheck);
        	GSc[i-1] = new GSchecker(url_tocheck,id_tocheck,myid,myurl,true);
        	GSc_Thread[i-1] = new Thread(GSc[i-1],"GSchecker of GS"+myid+ "for GS"+ id_tocheck);
        	GSc_Thread[i-1].start();    	
        	
        }
        
        
        
        this.alive = true;
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
    
    /**
     * log the newest joblist of RM to Grid_Scheduler
     * @param RM_id id of RM
     * @param jobs joblist(Arraylist) of the RM
     */
    public void trackJobs(int RM_id, ArrayList<Job> jobs)
    {
    	if (job_hashmap.containsKey(RM_id))
    	{
    		job_hashmap.replace(RM_id, jobs);
    	}
    	else
    	{
    		job_hashmap.put(RM_id, jobs);
    	}
    	
    	System.out.print("RM"+RM_id+" jobs: ");
    	for (Job job : job_hashmap.get(RM_id))
    	{
    		System.out.print("Job"+job.getId() + "  ");
    	}
    	System.out.println("");
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
    
    /** give the job logs of the RM that crashed to other RM under THIS GS
     * @param RM_id the id of the RM that crashed (if the RM_id+1 also crashed, iterate this method again)
     */
    public void transferRMJobs (int RM_id, ArrayList<Job> jobs)
    {
    	int newRM_id = (RM_id+1)%RM_NUM+myid*RM_NUM;
    	String newurl = "rmi://localhost:"+(6000+RM_id)+"/RM"+RM_id;
    	ArrayList<Job> joblist = null;
    	IResource_Manager newRM = null;
    	
    	if (jobs == null)
    	{
    		joblist = job_hashmap.get(RM_id);
    	}
    	else
    	{
    		joblist = jobs;
    	}
    	
    	try {
			newRM = (IResource_Manager) Naming.lookup(newurl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			transferRMJobs(newRM_id+1, joblist);
		}	
    	
    	if (joblist != null)
    	{
    		for (Job job : joblist)
        	{
        		try {
    				newRM.receiveJobs(job, true);
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
    	}
    	
    }
    
    
    /**
     * 说明清楚此属性的业务含义
     */
    public void registerRM(String url, int id,NodeGroup nodegroup) throws java.rmi.RemoteException
    {
    	RM_url.add(url);
    	RM_id.add(id);
    	rmgroup.addNodeGroup(id, nodegroup);
    	
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
    
    public void updateRMgroup(int id, NodeGroup nodegroup)
    {
    	rmgroup.addNodeGroup(id, nodegroup);
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