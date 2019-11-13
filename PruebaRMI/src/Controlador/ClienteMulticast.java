/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.net.*;
import java.util.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author Albarran Cruz Carlos Alberto
 */
public class ClienteMulticast extends Thread {

    MulticastSocket s;
    String msj;
    InetAddress gpo;
    LinkedList<Nodo> lista_servidores;
    int num_servidores;

    public  ClienteMulticast(int num_servidores) {
        lista_servidores = new LinkedList<>();
        this.num_servidores=num_servidores;

        try {
            s = new MulticastSocket(4000);
            s.setReuseAddress(true);
            s.setTimeToLive(1);
            msj = "8000";
            gpo = InetAddress.getByName("228.1.1.1");
            s.joinGroup(gpo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void run() {
        try {

            long start = System.currentTimeMillis();
            long end = start + 10 * 1000; // 60 seconds * 1000 ms/sec
            while (lista_servidores.size()!=num_servidores) {
                DatagramPacket p = new DatagramPacket(new byte[10], 10);
                s.receive(p);
                String recibido = new String(p.getData(), StandardCharsets.UTF_8);
                String nuevo = "";

                for (int i = 0; i < recibido.length(); i++) {
                    if (recibido.charAt(i) != 0) {
                        nuevo = nuevo.concat(String.valueOf(recibido.charAt(i)));
                    }
                }

                // recibido=recibido.substring(1, index);
                //System.out.println(recibido.length());
                System.out.println("Mensaje recibido: " + recibido + " con un TTL=" + s.getTimeToLive());
                System.out.println("Servidor descubierto: "
                        + p.getAddress() + " puerto:"
                        + p.getPort());
                InetAddress direccion = p.getAddress();
                int port = p.getPort();
                if(!lista_servidores.contains(new Nodo(Integer.valueOf(nuevo), direccion)))
                lista_servidores.add(new Nodo(Integer.valueOf(nuevo), direccion));
            }
            displayServidores();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void displayServidores() {
        for (Nodo a : lista_servidores) {
            System.out.println(a);
        }
    }

    public static void main(String... args) {
        new ClienteMulticast(3).start();
    }
}
