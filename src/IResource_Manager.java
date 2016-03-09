import java.rmi.Remote;

public interface IResource_Manager extends Remote {

public String sayHello(String name) throws java.rmi.RemoteException;



public void testStub() throws java.rmi.RemoteException;



public void getStub(String url, int NodeID) throws java.rmi.RemoteException;


}