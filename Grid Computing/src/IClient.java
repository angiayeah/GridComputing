import java.rmi.Remote;

public interface IClient extends Remote {

public String sayHello(String name) throws java.rmi.RemoteException;

}
