import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
public class Node extends UnicastRemoteObject implements INode {
    // ���ʵ�ֱ�����һ����ʽ�Ĺ��캯��������Ҫ�׳�һ��RemoteException�쳣  
    protected Node() throws RemoteException {
        super();
    }
    /**
     * ˵����������Ե�ҵ����
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
