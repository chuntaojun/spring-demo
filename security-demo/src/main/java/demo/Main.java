package demo;

import demo.bean.Authority;
import demo.proxy.JDKFactory;
import demo.proxy.proxyInterface;
import demo.security.impl.AdminServiceImpl;
import demo.security.impl.UserServiceImpl;

/**
 * @author tensor
 */
public class Main {

    public static void main(String[] args) {
        Session user = new Session("BB", "USER", new UserServiceImpl());
        user.run();
        Session admin = new Session("BB", "ADMIN", new AdminServiceImpl());
        admin.run();
    }

    static class Session implements Runnable {

        private Authority authority;
        private proxyInterface proxy;

        public Session(String name, String role, proxyInterface proxy) {
            authority = new Authority(name, role);
            this.proxy = proxy;
        }

        @Override
        public void run() {
            try {
                proxyInterface proxy = new JDKFactory(this.proxy, authority.getRole()).getProxy();
                proxy.print(authority);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
