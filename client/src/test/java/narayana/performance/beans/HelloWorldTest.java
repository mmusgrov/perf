package narayana.performance.beans;

//import narayana.performance.ejb2.EJB2Home;
//import narayana.performance.ejb2.EJB2Remote;
import org.junit.Test;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.lang.String;
import java.lang.System;
import java.rmi.RemoteException;
import java.util.Properties;
import narayana.performance.util.Result;

public class HelloWorldTest {
    @Test
    public void verifyHelloWorld() throws NamingException, RemoteException, CreateException {
        Result result = null;
        String iiopNS = "localhost:3628";
        String jndiNS = "localhost:1199";
        boolean useIIOP = true;
        boolean useEjb2 = false;
        String ejb2Name = "OTSEjb2StatelessBean";
        String hwName = "HelloWorldJNDIName";

        String jndiName = useEjb2 ? ejb2Name : hwName;
        String ns = useIIOP ? iiopNS :jndiNS;


        Properties properties = new Properties();
        properties.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
        properties.put("java.naming.factory.url.pkgs", "=org.jboss.naming:org.jnp.interfaces");
        properties.put("java.naming.provider.url", "localhost:1099");

        System.out.printf("Looking up bean");
        Context ctx = new InitialContext(properties);
        String res;

/*        if (useEjb2) {
            EJB2Home home = (EJB2Home) ctx.lookup(jndiName);
            EJB2Remote bean = home.create();
            res = bean.doWork(result, useIIOP, useEjb2, ns);
        } else {*/
            HelloWorld bean = (HelloWorld) ctx.lookup(jndiName);
            useEjb2 = true;
            res = bean.doWork(result, useIIOP, useEjb2, ns);
//        }

        System.out.printf("%s%n", res);
    }
}
