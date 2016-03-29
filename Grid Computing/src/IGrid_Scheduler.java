
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;



public interface IGrid_Scheduler extends Remote {

public String sayHello(String name) throws java.rmi.RemoteException;

public void registerRM(String url, int id,NodeGroup nodegroup) throws java.rmi.RemoteException;
public void uploadJob(Job job) throws RemoteException;

public void testStub(String url) throws java.rmi.RemoteException;

//public void checkRMalive(String url) throws java.rmi.RemoteException;

//public void checkGSalive(String url) throws java.rmi.RemoteException;

public boolean echoGSalive(String url) throws java.rmi.RemoteException;

public void stopGSchecker() throws java.rmi.RemoteException;
public void startGSchecker() throws java.rmi.RemoteException;
public void updateRMgroup(int id, NodeGroup nodegroup) throws java.rmi.RemoteException;
public void goDie() throws java.rmi.RemoteException;
public void receiveMessage(ConfigMessage message) throws MalformedURLException,RemoteException,NotBoundException;
public void action_GScrash(ConfigMessage message) throws RemoteException;
public void trackJobs(int RM_id, ArrayList<Job> jobs) throws RemoteException;
public void transferRMJobs (int RM_id, ArrayList<Job> jobs) throws RemoteException;

}
