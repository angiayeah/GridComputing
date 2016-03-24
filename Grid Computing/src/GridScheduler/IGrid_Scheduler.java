package GridScheduler;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import ClientAndJob.Job;


public interface IGrid_Scheduler extends Remote {

public String sayHello(String name) throws java.rmi.RemoteException;

public void regiterRM(String url, int id) throws java.rmi.RemoteException;
public void uploadJob(Job job) throws RemoteException;

public void testStub(String url) throws java.rmi.RemoteException;

//public void checkRMalive(String url) throws java.rmi.RemoteException;

//public void checkGSalive(String url) throws java.rmi.RemoteException;

public boolean echoGSalive(String url) throws java.rmi.RemoteException;

public void stopGSchecker() throws java.rmi.RemoteException;
public void startGSchecker() throws java.rmi.RemoteException;

}
