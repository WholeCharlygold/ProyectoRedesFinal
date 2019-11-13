package Controlador;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Albarran Cruz Carlos Alberto
 */
public class ServidorRMI extends Thread implements Operaciones{
    private int puertoRMI;
    private Nodo actual;
    private Nodo siguiente;
    boolean ya_paso_por_aqui;
    public ServidorRMI(int puertoRMI,Nodo actual,Nodo siguiente) {
        this.puertoRMI=puertoRMI;
        this.actual=actual;
        this.siguiente=siguiente;
        ya_paso_por_aqui=false;
           try {
            java.rmi.registry.LocateRegistry.createRegistry(this.puertoRMI);
            //puerto default del rmiregistry
            System.out.println("RMI registro listo.");

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    public void run(){
        try{
            System.setProperty("java.rmi.server.codebase", "file:/c/Temp/Operacion/");
            Operaciones stub=(Operaciones)UnicastRemoteObject.exportObject(this,this.puertoRMI);
            //Ligamos el objeto remoto en el registro
            Registry registry=LocateRegistry.getRegistry(puertoRMI);
            registry.bind("Operacion", stub);
            
            System.err.println("Servidor listo...");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public int suma(int a, int b) {
        return a + b;
    }
    public double division(double a,double b){
        return a/b;
    }
    public int multiplicacion(int a,int b){
        return a*b;
    }
    
     public Nodo encontrarArchivo(String file) {
        File f = new File(System.getProperty("user.dir") + "/" + puertoRMI + "/" + file);
        actual.setPor_aqui_pasaron(true);
        
          System.out.println("Buscando en este servidor el archivo: "+file);
        if (f.exists()) {
            System.out.println("Se encontro el archivo: "+file);
            return actual;
        } else {
            System.out.println("No encontrado en este servidor el archivo : "+file);
            if(!ya_paso_por_aqui){
                ya_paso_por_aqui=true;
                 ClienteRMI clienteRMI=new ClienteRMI(siguiente.getPuertoRMI(), file);
                 clienteRMI.start();
                try {
                    clienteRMI.join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ServidorRMI.class.getName()).log(Level.SEVERE, null, ex);
                }
                ya_paso_por_aqui=false;
                 if(clienteRMI.encontrado!=null){
                     return siguiente;
                 }else{
                     return null;
                 }
                 
            }else{
                return null;
            }
           
           
        }
    }
    public static void main(String... args) {
       // new ServidorRMI(9001).start();
        
    }

}
