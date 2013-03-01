package narayana.performance.beans;

import narayana.performance.util.Result;

public interface HelloWorld {
    String sayHello(String name);
    String doWork(String msg);
    String doWork(Result opts);
    String doWork(Result opts, boolean iiop, boolean ejb2, String namingProvider);
}
