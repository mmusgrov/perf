package narayana.performance.beans;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;


public interface HelloRemoteHome extends EJBHome
{
   Hello create() throws CreateException, RemoteException;
}
