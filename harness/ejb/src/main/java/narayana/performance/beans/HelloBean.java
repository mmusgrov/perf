package narayana.performance.beans;

import javax.annotation.Resource;
import javax.ejb.RemoteHome;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.jboss.ejb3.annotation.IIOP;

@Stateless
@RemoteHome(HelloRemoteHome.class)
@IIOP(interfaceRepositorySupported=false)
public class HelloBean
{
    @Resource
    private SessionContext context;

    public void sayHello()
    {
        System.out.println("Hello World!");

        return;
    }
}

