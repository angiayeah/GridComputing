import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface INode extends Remote {


public String sayHello(String name) throws java.rmi.RemoteException;
public void testStub() throws java.rmi.RemoteException;
public void testStub(String url) throws java.rmi.RemoteException;
public void receiveJobs(Job job, int index) throws InterruptedException, MalformedURLException, RemoteException, NotBoundException;
public int getStatus() throws java.rmi.RemoteException;
}