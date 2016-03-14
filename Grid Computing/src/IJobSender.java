import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IJobSender extends Remote {

public String sayHello(String name) throws java.rmi.RemoteException;



public void testStub() throws java.rmi.RemoteException;

public void testStub(String url) throws java.rmi.RemoteException;

public void sendJobs(int RM_id) throws RemoteException, InterruptedException;


}