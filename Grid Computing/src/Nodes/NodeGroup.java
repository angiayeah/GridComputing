package Nodes;
import java.util.ArrayList;

public class NodeGroup implements java.io.Serializable
{
	ArrayList<String> nodes_url = new ArrayList<String>();
	ArrayList<Integer> nodes_id = new ArrayList<Integer>();
	ArrayList<Integer> nodes_status = new ArrayList<Integer>();
	
	public NodeGroup(ArrayList<String> nodes_url,ArrayList<Integer> nodes_id,ArrayList<Integer> nodes_status)
	{
		this.nodes_url = nodes_url;
		this.nodes_id = nodes_id;
		this.nodes_status = nodes_status;
	}

}
