package narayana.performance.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: mmusgrov
 * Date: 2/28/13
 * Time: 9:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class Lookup {
    public static <T> T doLookup(String jndiUrl, boolean iiop, String ... altNames) throws NamingException {
        NamingException excp = new NamingException();

        for (String name : altNames) {
            try {
                if (jndiUrl == null)
                    return (T) (new InitialContext()).lookup(name);
                else
                    return (T) (getContext(iiop, jndiUrl).lookup(name));
            } catch (NamingException e) {
                excp = e;
            }
        }

        for (String name : altNames)
            System.out.printf("%s: lookup jndi name failed\n", name);

        throw excp;
    }

    private static Context getContext(boolean iiop, String jndiUrl) throws NamingException {
        return iiop ? getIIOPContext(jndiUrl) : getContext(jndiUrl);
    }

    private static Context getContext(String jndiUrl) throws NamingException {
        Properties properties = new Properties();
//TODO XXX these settings dont work on EAP6
        properties.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
        properties.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
        properties.put("java.naming.provider.url", jndiUrl);

        return new InitialContext(properties);
    }

    private static Context getIIOPContext(String endPoint) throws NamingException {
        Properties properties = new Properties();

        if (endPoint == null)
            endPoint = "localhost:3528";

        String provider = "corbaloc::ENDPOINT/NameService".replace("ENDPOINT", endPoint);

        properties.put("java.naming.factory.initial", "com.sun.jndi.cosnaming.CNCtxFactory");
        properties.put("java.naming.provider.url", provider);
        properties.put("java.naming.factory.object", "org.jboss.tm.iiop.client.IIOPClientUserTransactionObjectFactory");
        properties.put(Context.URL_PKG_PREFIXES, "org.jboss.naming.client:org.jnp.interfaces");
        properties.put("j2ee.clientName", "iiop-unit-test");

        return new InitialContext(properties);
    }

    public static <T> T lookup(String providerUrl, boolean iiop, String jndiName) {
        try {
            return doLookup(providerUrl, iiop, jndiName);
        } catch (NamingException e) {
            return null;
        }
    }
}
