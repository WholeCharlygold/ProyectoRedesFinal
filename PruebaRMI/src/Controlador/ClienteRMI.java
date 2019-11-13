/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

/**
 *
 * @author Albarran Cruz Carlos Alberto
 */
public class ClienteRMI extends Thread {

    public int puertoRMI;
    public String file;
    public Nodo encontrado;
    public ClienteRMI(int puertoRMI, String file) {
        this.puertoRMI = puertoRMI;
        this.file = file;

    }
    
    public void run(){
         try{
            Registry registry=LocateRegistry.getRegistry(this.puertoRMI);
            Operaciones stub=(Operaciones) registry.lookup("Operacion");
            encontrado=stub.encontrarArchivo(this.file);
            if (encontrado!=null){
                System.out.println("Archivo encontrado en: "+encontrado.getPuertoRMI());
            }else{
                System.out.println("No se encontro el archivo");
            }
            
           
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       new ClienteRMI(9001, "hola.txt").start();
    }

}
