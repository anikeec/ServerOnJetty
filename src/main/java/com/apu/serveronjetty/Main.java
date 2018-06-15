/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.serveronjetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 *
 * @author apu
 */
public class Main {
    
    public static void main(String[] args) {
        
        System.out.println("Initializing server...");
        ServletContextHandler context = new ServletContextHandler();

        // The filesystem paths we will map
        String pwdPath = System.getProperty("user.dir") + "/src/main/webapp";
        
        // Setup the basic application "context" for this application at "/"
        // This is also known as the handler tree (in jetty speak)
        context.setResourceBase(pwdPath);
        context.setContextPath("/");
        
        // add special pathspec of "/home/" content mapped to the homePath
        ServletHolder holderHome = new ServletHolder("static-home", DefaultServlet.class);        
        holderHome.setInitParameter("dirAllowed","true");
        holderHome.setInitParameter("pathInfoOnly","true");
        context.addServlet(holderHome,"/home/*");
        
//        ServletHolder jsp = context.addServlet(JspServlet.class, "*.jsp");
//        jsp.setInitParameter("classpath", context.getClassPath());
        
        Server server = new Server(8082);        
        server.setHandler(context);
 
        System.out.println("Starting server...");
        try {
            server.start();
        } catch(Exception e) {
            System.out.println("Failed to start server!");
            return;
        }

        System.out.println("Server running...");
        while(true) {
            try {
                server.join();
            } catch(InterruptedException e) {
                System.out.println("Server interrupted!");
            }
        }
    }
}
