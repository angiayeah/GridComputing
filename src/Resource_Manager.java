import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
public class Resource_Manager extends UnicastRemoteObject implements IResource_Manager{
    // 这个实现必须有一个显式的构造函数，并且要抛出一个RemoteException异常  
	

	
	String[] node_url = new String[50];
	int[] node_id = new int[50];
	int[] node_status = new int[50];
	
	int node_flag =0;
    

	
    protected Resource_Manager() throws RemoteException {
        super();
    }
    /**
     * 说明清楚此属性的业务含义
     */
    private static final long serialVersionUID = 4077329331699640331L;
    public String sayHello(String name) throws RemoteException {
        return "Hello " + name + " ^_^ ";
    }
    
    //node will use this function of stub (stub from Resource_Manager)
    //to register its url and nodeID to the Resource_Manager
    //So that the Resource Manager can use the url to get a stub of Node.
    public void getStub(String url, int nodeID)        
    
    {
    	node_url[node_flag] = url;
    	node_id[node_flag] = nodeID;
    	node_status[node_flag] = 1;
    	try {
			INode Node = (INode)Naming.lookup("rmi://localhost:7001/Node1");
			Node.testStub();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    //this function for Node to test whether the stub from this Resource_Manager
    //work in an appropriate way.
    public void testStub()
    {
    	System.out.println("stub of RM has been used");
    }
    
    public static void main(String[] args) {
        try {
            IResource_Manager RM = new Resource_Manager();
            LocateRegistry.createRegistry(6001);
            java.rmi.Naming.rebind("rmi://localhost:6001/RM1", RM);
            System.out.print("Ready");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


	
}