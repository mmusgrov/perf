package narayana.performance.beans;

import narayana.performance.ejb2.EJB2Home;
import narayana.performance.ejb2.EJB2Remote;
import narayana.performance.util.Lookup;
import narayana.performance.util.Result;
import org.jboss.ejb3.annotation.IIOP;
import org.jboss.ejb3.annotation.RemoteBinding;
import org.jboss.ejb3.annotation.defaults.RemoteBindingDefaults;

import javax.ejb.CreateException;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.rmi.PortableRemoteObject;
import java.rmi.RemoteException;

@Stateless
@Remote(HelloWorld.class)
@RemoteBinding(jndiBinding = "HelloWorldJNDIName")
//@RemoteBinding(factory= RemoteBindingDefaults.PROXY_FACTORY_IMPLEMENTATION_IOR, jndiBinding = "HelloWorldJNDIName")
@IIOP(interfaceRepositorySupported=false)


//@RemoteBindings({@RemoteBinding(factory=IORFactory.class),@RemoteBinding(factory=StatelessRemoteProxyFactory.class)})

public class HelloWorldBean implements HelloWorld {
    String msg = null;

    public String sayHello(String name) {
        System.out.printf("%s%n", getMsg());
        return "Hi " + name + " from " + getMsg();
    }

    public String doWork(String msg) {
        System.out.printf("%s%n", getMsg());
        return getMsg();
    }

    public String doWork(Result opts) {
        System.out.printf("%s%n", getMsg());
        return getMsg();
    }

    private String ejb2Work(Result opts, boolean iiop, String namingProvider) {
         String jndiName = iiop ? "OTSEjb2StatelessBean" : "Ejb2StatelessBean";
        try {
            Object ro = Lookup.getNamingContextForEJB2(iiop, namingProvider).lookup(jndiName);

            EJB2Home home = (EJB2Home) PortableRemoteObject.narrow(ro, EJB2Home.class);

            EJB2Remote remote = home.create();

            if (remote != null) {
                System.out.printf("Calling remote %s%n", remote.getClass().getName());
                return remote.doWork(opts);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return getMsg();
    }

    private String ejb3Work(Result opts, boolean iiop, String namingProvider) {
        HelloWorld remote = Lookup.lookup(namingProvider, iiop, "HelloWorldJNDIName");

        if (remote != null) {
            System.out.printf("Calling remote %s%n", remote.getClass().getName());
            return remote.doWork(opts);
        }


        try {
            HelloRemoteHome hrh = Lookup.lookup(namingProvider, iiop, "ear-1.0/HelloBean/home");
            if (hrh != null) {
                Hello h = hrh.create();
                h.sayHello();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return getMsg();

    }
    public String doWork(Result opts, boolean iiop, boolean ejb2, String namingProvider) {
        System.out.printf("%s%n", getMsg());

        return ejb2 ? ejb2Work(opts, iiop, namingProvider) : ejb3Work(opts, iiop, namingProvider);
    }

    private String getMsg() {
        if (msg == null) {
            msg = String.format("WorkBean: bindAddress=%s portBindings=%s",
                    System.getProperty("jboss.service.binding.set", "unknown"),
                    System.getProperty("jboss.bind.address", "unknown"));
        }

        return msg;
    }
}

