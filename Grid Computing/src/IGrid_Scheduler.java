import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGrid_Scheduler extends Remote {

public String sayHello(String name) throws java.rmi.RemoteException;

public void regiterRM(String url, int id) throws java.rmi.RemoteException;
public void uploadJob(Job job) throws RemoteException, MalformedURLException, NotBoundException;

public void testStub(String url) throws java.rmi.RemoteException;

}
