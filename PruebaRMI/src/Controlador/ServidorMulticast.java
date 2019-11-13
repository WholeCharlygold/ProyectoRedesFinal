/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.net.*;

/**
 *
 * @author Albarran Cruz Carlos Alberto
 */
public class ServidorMulticast extends Thread {

    InetAddress gpo;
    MulticastSocket s;
    String msj;
    byte[] b;

    public ServidorMulticast(String msj) {

        try {
            s = new MulticastSocket();
            s.setReuseAddress(true);
            s.setTimeToLive(1);
            this.msj = msj;
            b = msj.getBytes();
            gpo = InetAddress.getByName("228.1.1.1");
            s.joinGroup(gpo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void run() {
        try {
            for (;;) {
                DatagramPacket p = new DatagramPacket(b, msj.length(), gpo, 4000);
                s.send(p);
                System.out.println("Enviando mensaje: " + msj + " con un TTL=" + s.getTimeToLive());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ia) {
                    ia.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String... args) {

        new ServidorMulticast("2000").start();

    }
}
