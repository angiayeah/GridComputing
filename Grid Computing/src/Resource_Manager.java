import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
public class Resource_Manager extends UnicastRemoteObject implements IResource_Manager{
    // 这个实现必须有一个显式的构造函数，并且要抛出一个RemoteException异常  
	
    String myurl;
    int myid;
	
	ArrayList<String> nodes_url = new ArrayList<String>();
	ArrayList<Integer> nodes_id = new ArrayList<Integer>();
	ArrayList<Integer> nodes_status = new ArrayList<Integer>();
	ArrayList<Job> jobList = new ArrayList<Job>();
	int jobIndex = 0;
	int NODE_NUM = 0;	//node number under this RM
	int FNODE_NUM = 0;	//number of nodes free
	IGrid_Scheduler GS;
	
	int node_flag =0;
	
	String[] Grid_Schedulers = new String[]{"rmi://localhost:5001/GS1",
			                                "rmi://localhost:5002/GS2",
			                                "rmi://localhost:5003/GS3",
			                                "rmi://localhost:5004/GS4",
			                                "rmi://localhost:5005/GS5"};
    

	//Here we start a inner class
		class receiveJob_thread implements Runnable
		{
			INode node;
			Job thejob;
			int theindex;
			public receiveJob_thread(INode fnode,Job job,int job_index)
			{
				node = fnode;
				thejob = job;
				theindex =job_index;
			}
			
			public void run()
			 {
				 try {
					node.receiveJobs(thejob, theindex);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotBoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
		}
		
    protected Resource_Manager() throws RemoteException {
        super();
    }
    
    protected Resource_Manager(String url, int id) throws RemoteException {
        super();
        myurl = url;
        myid = id;
        
        
        LocateRegistry.createRegistry(6000+id);
        try {
			java.rmi.Naming.rebind(url, this);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
		
        
        try {
			GS = (IGrid_Scheduler)Naming.lookup("rmi://localhost:5000/GS0");
			GS.regiterRM(url, id);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    /**
     * 说明清楚此属性的业务含义
     */
    private static final long serialVersionUID = 4077329331699640331L;
    public String sayHello(String name) throws RemoteException {
        return "Hello " + name + " ^_^ ";
    }
    
    
    /**
     * 
     * @return the id of this RM
     */
    public int getID()
    {
    	return myid;
    }
    
    //node will use this function of stub (stub from Resource_Manager)
    //to register its url and nodeID to the Resource_Manager
    //So that the Resource Manager can use the url to get a stub of Node.
    public void registerNode(String url, int nodeID)        
    
    {
    	nodes_url.add(url);
    	nodes_id.add(new Integer(nodeID));
    	nodes_status.add(new Integer(1));
    	
    	System.out.println(url+"register in GS"+myid);
    	try {
			INode Node = (INode)Naming.lookup(url);
			Node.testStub(myurl);
			NODE_NUM++;			//count number of nodes
			FNODE_NUM++;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * receive jobs from JobSender, assign it to free Nodes, otherwise give it to RS (not implemented RS part yet)
     * @param mustAccept true if the job is from GS, false otherwise
     */
    public void receiveJobs(Job job,boolean mustAccept) throws MalformedURLException, RemoteException, NotBoundException
    {
    	
    	System.out.println("Receive Job: ID:" + job.getId() + " "+job.getDuration()+" "+job.getStatus());
    	jobList.add(job);			//be careful, the index is based on job id!	
    	
    	INode freeNode = checkFreeNode();
    	
    	if (freeNode != null)
    	{		
    		job.setStatus(JobStatus.Running);	
    		//freeNode.receiveJobs(job,jobIndex);	//change to threads now		
			
			receiveJob_thread rt = new receiveJob_thread(freeNode,job,jobIndex);
			jobIndex++;
			Thread t1 = new Thread(rt,"this is a thread");
			t1.start();
			
			if (FNODE_NUM >=1)		//somehow the FNODE_NUM goes under 0, I don't know why
			{
				FNODE_NUM --;	//free node number -1
			}
			System.out.println("RM"+myid+" Number of free nodes: " + FNODE_NUM);
    		
    	}
    	else 
    	{
    		if (!mustAccept)
    		{
    			handToRS(job);	//hand in jobs to RS
    		}
    		else
    		{
    			receiveJobs(job, true); 	//must accept, call the function again, not sure if this is good
    		}
    	}
    	
    }
    
    /**
     * complete jobs
     */
    public void completeJob(Job job, int index)
    {
    	jobList.set(index, job);	//replace the old job with the changed status to "done".
    	System.out.println("Job"+job.getId()+" is done!");
    	FNODE_NUM++;	//free node number +1
    }
    
    /**
     * hand in jobs to RS when the nodes are full, not implemented yet
     * @throws NotBoundException 
     * @throws MalformedURLException 
     * @throws RemoteException 
     */
    public void handToRS(Job job) throws RemoteException, MalformedURLException, NotBoundException
    {
    	System.out.println("All nodes busy, can't assign jobs, give it to GS");
    	GS.uploadJob(job);
    }
    
    /**
     * return free node, otherwise return null
     */
    public INode checkFreeNode() throws MalformedURLException, RemoteException, NotBoundException
    {
    	for (String nodeURL : nodes_url)
    	{
    		if (nodeURL != null)
    		{    			
        		INode node = (INode)Naming.lookup(nodeURL);
        		if (node.getStatus() == 1 )
        		{
        			System.out.println("Assign to node: "+nodeURL);
        			return node;
        		}
    		} 		
    	}
    	return null;
    }
    
    
    /**
     * return the number of free nodes under this RM
     */
    public int getFNodeNumber()
    {
    	return FNODE_NUM;
    }
    
    
    public void testStub()
    {
    	System.out.println("stub of RM has been used");
    }
    
    public void testStub(String url)
    {
    	System.out.println("stub of RM"+myid+ " has been used by " +url);
    }
    
    public static void main(String[] args) {
        try {
              for(int i=0;i<2;i++)
              {
            	  System.out.println("now start to create RMs");
            	  String theurl = "rmi://localhost:"+(6000+i)+"/RM"+i;
            	  IResource_Manager RM = new Resource_Manager(theurl,i);
                  //LocateRegistry.createRegistry(6000+i);
                  //java.rmi.Naming.rebind(theurl, RM);
                  //IGrid_Scheduler GS = (IGrid_Scheduler)Naming.lookup("rmi://localhost:5000/GS0");
                  //.GS.regiterRM(theurl, i);
                  System.out.print("RM"+i+" is Ready");
              }
              System.out.println("All Resource Managers are ready");
        	
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


	
}