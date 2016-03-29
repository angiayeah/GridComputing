



public class RMGroup 
{
   NodeGroup ng[] = new NodeGroup[20];
   public RMGroup()
   {
	   for(int i=0;i<20;i++)
	   {
		   ng[i] = null;
	   }
   }
   
   public void addNodeGroup(int id, NodeGroup nodegroup)
   {
	   //System.out.println("adding new NodeGroup from RM"+id);
	   ng[id] = nodegroup;
	   int RM_id = ng[id].getRMid();
	   //System.out.println("NodeGroup added");
	   if(ng[id].getByID(RM_id*10)!=null)
	   {
		   //System.out.println("the first node in this group is node"+ng[id].getByID(RM_id*10).getNodeid()); 
	   }
	   
   }
}
