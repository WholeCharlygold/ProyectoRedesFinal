/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.*;

/**
 *
 * @author Albarran Cruz Carlos Alberto
 */
public class Interfaz {

    private Nodo anterior;
    private Nodo siguiente;
    private Nodo actual;
    private String archivo_a_localizar;
    private int puertoRMI;

    public Interfaz(int puertoRMI) {
        this.puertoRMI = puertoRMI;

    }

    public void setArchivo(String archivo_a_localizar) {
        this.archivo_a_localizar = archivo_a_localizar;
    }

    public Nodo getAnterior() {
        return anterior;
    }

    public void setAnterior(Nodo anterior) {
        this.anterior = anterior;
    }

    public Nodo getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }

    public Nodo getActual() {
        return actual;
    }

    public void setActual(Nodo actual) {
        this.actual = actual;
    }

    public String getArchivo_a_localizar() {
        return archivo_a_localizar;
    }

    public void setArchivo_a_localizar(String archivo_a_localizar) {
        this.archivo_a_localizar = archivo_a_localizar;
    }

    public int getPuertoRMI() {
        return puertoRMI;
    }

    public void setPuertoRMI(int puertoRMI) {
        this.puertoRMI = puertoRMI;
    }

    public static void main(String... args) {
        Nodo anterior, siguiente, actual;
        Scanner in = new Scanner(System.in);
        LinkedList<Nodo> nodos = new LinkedList<>();
        System.out.println("Introduzca el numero de puerto");
        int puertoRMI = in.nextInt();
        //System.out.println("Introduzca el nombre de archivo");
        String file = "hola.txt";
        Nodo encontrado = null;
        int contador = 0;
        int numero_nodos=3;
        //  Interfaz interfaz=new Interfaz(puertoRMI);
        /* 
      ServidorFlujo servidorFlujo=new ServidorFlujo(puertoRMI+100);
      servidorFlujo.start();*/

       

        ServidorMulticast servidorMulticast = new ServidorMulticast(String.valueOf(puertoRMI + 100));
        servidorMulticast.start();
        ClienteMulticast clienteMulticast = new ClienteMulticast(numero_nodos);
        clienteMulticast.start();
        new ServidorFlujo(puertoRMI + 100).start();
        try {
            clienteMulticast.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
        }
        nodos = clienteMulticast.lista_servidores;
        nodos.sort(new MyCompNode());
        System.out.println(nodos);
        in.nextLine();
        int index_actual = -1;
        try {
                index_actual = nodos.indexOf(new Nodo(puertoRMI + 100, InetAddress.getLocalHost()));
            } catch (UnknownHostException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
                actual = nodos.get(index_actual);
                siguiente = nodos.get(Math.floorMod((index_actual + 1), numero_nodos));
                anterior = nodos.get(Math.floorMod((index_actual - 1), numero_nodos));
                 new ServidorRMI(puertoRMI,actual,siguiente).start();
        for (;;) {
                 System.out.println("Buscar archivo... ");
                file = in.nextLine();
              
                ClienteRMI clienteRMI = new ClienteRMI(siguiente.getPuertoRMI(), file);
                clienteRMI.start();
                try {
                    clienteRMI.join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
                }
                encontrado = clienteRMI.encontrado;

                System.out.println("Encontrado en el puerto: " + encontrado.getPuertoRMI());
                try {
                    if (encontrado!=null) {
                        System.out.println("ARCHIVO ENCONTRADO");
                        ClienteFlujo clienteFlujo = new ClienteFlujo(encontrado.getPuerto(), file);
                        clienteFlujo.start();
                        Thread.sleep(1000);
                    } else  {
                        System.out.println("ARCHIVO NO ENCONTRADO");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            
        }

    }

}
