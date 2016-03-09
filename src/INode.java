import java.rmi.Remote;

public interface INode extends Remote {

public String sayHello(String name) throws java.rmi.RemoteException;
public void testStub() throws java.rmi.RemoteException;


}