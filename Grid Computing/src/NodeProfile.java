


//this class wrap important information of nodes
//i.e. node id, node url
public class NodeProfile implements java.io.Serializable
{
   String node_url =null;
   int node_id =-1;
   int node_status = 1;  //by default, a node is idle
   public NodeProfile(String url, int nodeid,int nodestatus)
   {
	   this.node_url = url;
	   this.node_id = nodeid;
	   this.node_status = nodestatus;
	   
   }
   
   public String getNodeurl()
   {
	   return node_url;
   }
   
   public int getNodeid()
   {
	   return node_id;
   }
   
   public int getNodestatus()
   {
	   return node_status;
   }
}
