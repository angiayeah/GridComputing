import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
public class Node extends UnicastRemoteObject implements INode {
    // 这个实现必须有一个显式的构造函数，并且要抛出一个RemoteException异常  
    protected Node() throws RemoteException {
        super();
    }
    /**
     * 说明清楚此属性的业务含义
     */
    private static final long serialVersionUID = 4077329331699640331L;
    public String sayHello(String name) throws RemoteException {
        return "Hello " + name + " ^_^ ";
    }
    public static void main(String[] args) {
        try {
            INode hello = new Node();
            java.rmi.Naming.rebind("rmi://localhost:1099/hello", hello);
            System.out.print("Ready");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
