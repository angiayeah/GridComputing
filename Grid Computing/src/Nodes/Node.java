package Nodes;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import javax.swing.JButton;

import ClientAndJob.Job;
import ClientAndJob.JobStatus;
import ResourceManager.IResource_Manager;

import java.lang.Math;
import java.net.MalformedURLException;

public class Node extends UnicastRemoteObject implements INode {
	
	String myResource_Manager = "";
	IResource_Manager RM;
	int mystatus = 1;    //1 means idle, 0 means busy
	String myurl;
	int myid = 1;
	static int RM_NUM = 4;
	static int Node_per_RM = 10;
	ArrayList<Job> jobList = new ArrayList<Job>();
	String[] Resource_Managers= new String[]{"rmi://localhost:6001/RM1",
			                                 "rmi://localhost:6002/RM2",
			                                 "rmi://localhost:6003/RM3",
			                                 "rmi://localhost:6004/RM4",
			                                 "rmi://localhost:6005/RM5",
			                                 "rmi://localhost:6006/RM6",
			                                 "rmi://localhost:6007/RM7",
			                                 "rmi://localhost:6008/RM8",
			                                 "rmi://localhost:6009/RM9",
			                                 "rmi://localhost:6010/RM10",
			                                 "rmi://localhost:6011/RM11",
			                                 "rmi://localhost:6012/RM12",
			                                 "rmi://localhost:6013/RM13",
			                                 "rmi://localhost:6014/RM14",
			                                 "rmi://localhost:6015/RM15",
			                                 "rmi://localhost:6016/RM16",
			                                 "rmi://localhost:6017/RM17",
			                                 "rmi://localhost:6018/RM18",
			                                 "rmi://localhost:6019/RM19",
			                                 "rmi://localhost:6020/RM20"};
	
    // 这个实现必须有一个显式的构造函数，并且要抛出一个RemoteException异常  
    protected Node() throws RemoteException {
        super();
        
    }
    protected Node(String url,int id) throws RemoteException {
        super();
        myurl = url;
        myid = id;
        mystatus = 1;
        int RM_id = (int) Math.floor(id/Node_per_RM);
        String RM_url = "rmi://localhost:"+(6000+RM_id)+"/RM"+RM_id;
        myResource_Manager = RM_url;
        
        System.out.println("RM_url is: "+RM_url);
        
        System.out.println("Node port registering");
        LocateRegistry.createRegistry(7000+id);
        
        System.out.println("Node port registration completed");
    	try {
			java.rmi.Naming.rebind(url, this);
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	System.out.println("Looking up for RM");
    	 
		try {
			IResource_Manager RM = (IResource_Manager)Naming.lookup(RM_url);
			RM.registerNode(url,id);
		} catch (MalformedURLException e) {
			
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (NotBoundException e) {
			System.out.println("Can't register Node"+myid+", RM"+RM_id+" is offline");
			//e.printStackTrace();
		}
		System.out.println("RM looking up completed");
    	
        
    }
    
    /**
     * return the status of node, busy 0, idle 1
     */
    
    public void changeRM(String RM_url)throws RemoteException
    {
    	//this function is used to change RM
    }
    public int getStatus()
    {
    	return mystatus;
    }
    
    public void receiveJobs(Job job, int index) throws RemoteException
    {  	
    	System.out.println("Receive Job: ID:" + job.getId() + " "+job.getDuration()+" "+job.getStatus());	
    	jobList.add(job);	//add job to list
    	mystatus = 0;		//become busy
    	int RM_id = (int) Math.floor(myid/Node_per_RM);
    	
    	try {
			Thread.sleep((long) job.getDuration());
			job.setStatus(JobStatus.Done);
	    	System.out.println("Job" + job.getId()+" is done! Node: " + this.myid);   	
	    	
	        String RM_url = "rmi://localhost:"+(6000+RM_id)+"/RM"+RM_id;
	        RM = (IResource_Manager)Naming.lookup(RM_url);
	        RM.completeJob(job, index);
	        mystatus = 1;		//back to free state again
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		//sleep doesn't really make sense.
    	catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to tell RM"+RM_id+ "Job"+job.getId()+" is done, it is offline");
		}
    	
        
    }
    
    /**
     * 说明清楚此属性的业务含义
     */
    private static final long serialVersionUID = 4077329331699640331L;
    public String sayHello(String name) throws RemoteException {
        return "Hello " + name + " ^_^ ";
    }
    
    
    //this function is for Resource Manager to test whether the stub from
    //this node works.
    public void testStub()
    {
    	System.out.println("this is node "+myid+",the stub of node has been used");
    }
    
    public void testStub(String url)
    {
    	System.out.println("stub of node"+myid+ " has been used by " +url);
    }
    
    
    public static void main(String[] args) {
        try {
            //INode Node = new Node();
            //LocateRegistry.createRegistry(7001);
            //java.rmi.Naming.rebind("rmi://localhost:7001/Node1", Node);
            //IResource_Manager RM = (IResource_Manager)Naming.lookup("rmi://localhost:6001/RM1");
            //RM.getStub(myurl,myid);
            //RM.testStub();
            
        	INode[] Nodes = new Node[Node_per_RM*RM_NUM];
            for(int i=0;i<Nodes.length;i++)
            {
            	System.out.println("now starting to create nodes");
            	String theurl = "rmi://localhost:"+(7000+i)+"/Node"+i;
            	Nodes[i] = new Node(theurl,i);
            	//LocateRegistry.createRegistry(7000+i);
            	//java.rmi.Naming.rebind(theurl, Nodes[i]);
            	//IResource_Manager RM = (IResource_Manager)Naming.lookup("rmi://localhost:6000/RM0");
            	//RM.registerNode(theurl,i);
            	//RM.testStub(myurl);
            	System.out.print("Node"+i+" is Ready");
            }
            
            
            System.out.println("All Nodes are ready");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
