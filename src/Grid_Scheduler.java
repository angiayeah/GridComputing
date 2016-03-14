import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
public class Grid_Scheduler extends UnicastRemoteObject implements IGrid_Scheduler {
    // ���ʵ�ֱ�����һ����ʽ�Ĺ��캯��������Ҫ�׳�һ��RemoteException�쳣  
    protected Grid_Scheduler() throws RemoteException {
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
            IGrid_Scheduler GS = new Grid_Scheduler();
            java.rmi.Naming.rebind("rmi://localhost:6000/GS1", GS);
            System.out.print("Ready");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}