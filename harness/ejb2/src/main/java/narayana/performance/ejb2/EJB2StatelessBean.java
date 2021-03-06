/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 * (C) 2008
 * @author JBoss Inc.
 */
package narayana.performance.ejb2;

import narayana.performance.util.Lookup;
import narayana.performance.util.Result;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import java.io.IOException;
import java.util.Properties;

public class EJB2StatelessBean implements SessionBean
{
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




    public String doWork(Result opts, boolean iiop, boolean ejb2, String namingProvider) {
        System.out.printf("%s%n", getMsg());
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

    private String getMsg() {
        if (msg == null) {
            msg = String.format("WorkBean: bindAddress=%s portBindings=%s",
                    System.getProperty("jboss.service.binding.set", "unknown"),
                    System.getProperty("jboss.bind.address", "unknown"));
        }

        return msg;
    }


    public void setSessionContext(SessionContext context) { }
    public void ejbCreate() { }
    public void ejbActivate() { }
    public void ejbPassivate() { }
    public void ejbRemove() { }
}
