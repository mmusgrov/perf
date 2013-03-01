package narayana.performance.beans;

import narayana.performance.util.Lookup;
import narayana.performance.util.Result;
import org.jboss.ejb3.annotation.RemoteBinding;
import org.jboss.ejb3.annotation.defaults.RemoteBindingDefaults;

import javax.ejb.CreateException;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.rmi.RemoteException;

@Stateless
@Remote(HelloWorld.class)
//@RemoteBinding(jndiBinding = "HelloWorldJNDIName")
@RemoteBinding(factory= RemoteBindingDefaults.PROXY_FACTORY_IMPLEMENTATION_IOR, jndiBinding = "HelloWorldJNDIName")
//@IIOP(interfaceRepositorySupported=false)


//@RemoteBindings({@RemoteBinding(factory=IORFactory.class),@RemoteBinding(factory=StatelessRemoteProxyFactory.class)})

public class HelloWorldBean implements HelloWorld {
    String msg = null;

    public String sayHello(String name) {
        System.out.printf("%s%n", getMsg());
        return "Hi " + name + " from " + getMsg();
    }

    @Override
    public String doWork(String msg) {
        System.out.printf("%s%n", getMsg());
        return getMsg();
    }

    @Override
    public String doWork(Result opts) {
        System.out.printf("%s%n", getMsg());
        return getMsg();
    }

    @Override
    public String doWork(Result opts, boolean iiop, String namingProvider) {
        System.out.printf("%s%n", getMsg());
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

    private String getMsg() {
        if (msg == null) {
            msg = String.format("WorkBean: bindAddress=%s portBindings=%s",
                    System.getProperty("jboss.service.binding.set", "unknown"),
                    System.getProperty("jboss.bind.address", "unknown"));
        }

        return msg;
    }
}

