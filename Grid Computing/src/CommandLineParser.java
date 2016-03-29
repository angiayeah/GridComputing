

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.StringTokenizer;




//this file is used to start the console of Grid Computing System
//currently, there is only one instruction supported:
//gs x means that shut down xth GS in the Grip Computing System.
//you can write a instruction to shut down RM, that will be convenient for your work.
public class CommandLineParser 
{

   public static void main(String args[])
   {
	   while(true)
	   {
		   String entity_type = null;//the entity type can be node/rm/gs
		   String entity_id =null; //the id of the node/rm/gs like 1,2,3,4....
		   int i=0;
		   Scanner s = new Scanner(System.in);
		   System.out.println("please enter the command£º");
		   String line = s.nextLine();
		   StringTokenizer st=new StringTokenizer(line);
		   System.out.println("Your enter is: "+line);
		   IGrid_Scheduler GS = null;
		   IResource_Manager RM = null;
		   INode node = null;
		   while (st.hasMoreElements())
		   {
			   
			   if(i==0)
			   {
				   entity_type = st.nextToken();
			   }
			   
			   if(i==1)
			   {
				   entity_id = st.nextToken();
			   }
			   //System.out.println(st.nextToken());
			   i++;
		   }
		   System.out.println("entity_type is: "+ entity_type);
		   if(entity_type.equals("gs")||entity_type.equals("GS"))
		   {
			   int id = Integer.parseInt(entity_id);
			   String theurl ="rmi://localhost:"+ (5000+id)+"/GS"+id;
			   System.out.println("the url is: "+ theurl);
			   try {
				GS = (IGrid_Scheduler) Naming.lookup(theurl);
			} catch (MalformedURLException e) {
				System.out.println("Parser error 1");
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				System.out.println("Parser error 2");
				e.printStackTrace();
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				System.out.println("Parser error 3");
				e.printStackTrace();
			}
			   
			   try {
				System.out.println("GS"+id+" is now going to die");
				GS.goDie();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			   
		   }
	   }
   }
}
