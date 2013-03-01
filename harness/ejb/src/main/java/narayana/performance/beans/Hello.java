package narayana.performance.beans;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;


public interface Hello extends EJBObject
{
    public void sayHello() throws RemoteException;
}
