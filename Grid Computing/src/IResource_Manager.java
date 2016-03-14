import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IResource_Manager extends Remote {

public String sayHello(String name) throws java.rmi.RemoteException;



public void testStub() throws java.rmi.RemoteException;

public void testStub(String url) throws java.rmi.RemoteException;

public void registerNode(String url, int NodeID) throws java.rmi.RemoteException;
public void receiveJobs(Job job, boolean mustAccept) throws MalformedURLException, RemoteException, NotBoundException;
public void completeJob(Job job, int index) throws java.rmi.RemoteException;

public INode checkFreeNode() throws MalformedURLException, RemoteException, NotBoundException,InterruptedException;
public void handToRS(Job job)throws RemoteException, MalformedURLException, NotBoundException;
public int getFNodeNumber() throws java.rmi.RemoteException;
public int getID() throws java.rmi.RemoteException;
}