
import java.util.ArrayList;



public class RMProfile implements java.io.Serializable
{
	ArrayList<String> nodes_url = new ArrayList<String>();
	ArrayList<Integer> nodes_id = new ArrayList<Integer>();
	ArrayList<Integer> nodes_status = new ArrayList<Integer>();
	ArrayList<Job> jobList = new ArrayList<Job>();
	//the attributes above is gonna to be deprecated
	
	
	//there can be at most 200 nodes in a node group;
	RMProfile[] nodeprofile = new RMProfile[200];
	String RM_url =null;
	int RM_id = -1;
	
	public RMProfile()
	{
		
	}
	
	public RMProfile(ArrayList<String> node_url,ArrayList<Integer> node_id,ArrayList<Integer> node_status,ArrayList<Job> job,String RM_url,int RM_id)
	{
		this.nodes_url = node_url;
		this.nodes_id = node_id;
		this.nodes_status = node_status;
		this.jobList = job;
		this.RM_url = RM_url;
		this.RM_id = RM_id;
	}
	
	public String getRMurl()
	{
		return RM_url;
	}
	
	public int getRMid()
	{
		return RM_id;
	}
	
	public void addByID(int id, RMProfile np)
	{
		nodeprofile[id] = np; 
	}
	
	public RMProfile getByID(int id)
	{
		return nodeprofile[id];
	}

}
