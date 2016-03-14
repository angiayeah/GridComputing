import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
public class Grid_Scheduler extends UnicastRemoteObject implements IGrid_Scheduler {
    // 这个实现必须有一个显式的构造函数，并且要抛出一个RemoteException异常  
	
	ArrayList<String> RM_url = new ArrayList<String>();
	ArrayList<Integer> RM_id = new ArrayList<Integer>();
	ArrayList<Integer> RM_status = new ArrayList<Integer>();
	String myurl;
	int myid;
	
	
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
        
        
    }
    
    /**
     * RM upload jobs to GS
     * @throws RemoteException 
     * @throws NotBoundException 
     * @throws MalformedURLException 
     */
    public void uploadJob(Job job) throws RemoteException, MalformedURLException, NotBoundException
    {
    	
    	System.out.println("Check RM free nodes number");
    	int maxFNode = 0;
    	IResource_Manager freeRM = null;
    	
		for(int i=0;i<2;i++)
		{
		  
			 String theurl = "rmi://localhost:"+(6000+i)+"/RM"+i;
			 IResource_Manager RM = (IResource_Manager)Naming.lookup(theurl);
			 if (RM.getFNodeNumber() >= maxFNode)
			 {
				 freeRM = RM;
				 maxFNode = RM.getFNodeNumber();
			 }
			 System.out.println("RM"+RM.getID()+" free nodes number: "+RM.getFNodeNumber());
		}
		
		System.out.println("Assign job to RM" + freeRM.getID());
		freeRM.receiveJobs(job, true);		//must accept
    }
    
    
    
    /**
     * 说明清楚此属性的业务含义
     */
    public void regiterRM(String url, int id) throws java.rmi.RemoteException
    {
    	RM_url.add(url);
    	RM_id.add(id);
    	
    	System.out.println(url+"register in GS"+myid);
    	
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
    
    
    
    
    private static final long serialVersionUID = 4077329331699640331L;
    public String sayHello(String name) throws RemoteException {
        return "Hello " + name + " ^_^ ";
    }
    public static void main(String[] args) {
        try {
        	for(int i =0;i<1;i++)
        	{
        		System.out.println("now start to create GS");
        		String theurl = "rmi://localhost:"+ (5000+i)+"/GS"+i;
        		IGrid_Scheduler GS = new Grid_Scheduler(theurl,i);
        		//LocateRegistry.createRegistry(5000+i);
                //java.rmi.Naming.rebind(theurl, GS);
                System.out.println("GS"+i+" is Ready");
        	}
            
        	 System.out.println("All GridSchedulers are ready");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}