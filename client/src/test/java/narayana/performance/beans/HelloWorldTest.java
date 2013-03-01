package narayana.performance.beans;

import org.junit.Test;
import narayana.performance.beans.HelloWorld;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.lang.String;
import java.lang.System;
import java.util.Properties;
import narayana.performance.util.Result;

public class HelloWorldTest {
    @Test
    public void verifyHelloWorld() throws NamingException {
        Result result = null;
        String iiopNS = "localhost:3628";
        String jndiNS = "localhost:1199";
        boolean useIIOP = true;

        Properties properties = new Properties();
        properties.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
        properties.put("java.naming.factory.url.pkgs", "=org.jboss.naming:org.jnp.interfaces");
        properties.put("java.naming.provider.url", "localhost:1099");

        Context ctx = new InitialContext(properties);
        HelloWorld bean = (HelloWorld) ctx.lookup("HelloWorldJNDIName");
        String res;

        if (useIIOP)
            res = bean.doWork(result, useIIOP, iiopNS);
        else
            res = bean.doWork(result, useIIOP, jndiNS);

        System.out.printf("%s%n", res);
    }
}
