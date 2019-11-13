/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.net.*;
import java.io.*;

/**
 *
 * @author Albarran Cruz Carlos Alberto
 */
public class ClienteFlujo extends Thread {

    public int puertoD;
    public String file;
    Socket cl;
    DataInputStream dis;
    DataOutputStream doos;

    public ClienteFlujo(int puertoD, String file) {
        this.puertoD = puertoD;
        this.file = file;

        try {

            String host = "127.0.0.1";

            cl = new Socket(host, this.puertoD);

            dis = new DataInputStream(cl.getInputStream());
            doos = new DataOutputStream(cl.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void run() {
        try {
            doos.writeUTF(file);
            int size_buffer = dis.readInt();
            byte[] b = new byte[size_buffer];
            
            System.out.println("Size of buffer: " + size_buffer);
        
            for (int i = 0; i < 1; i++) {
                // System.out.println(num_archivos);
                String nombre = dis.readUTF();
                System.out.println("Recibimos el archivo: " + nombre);
                long tam = dis.readLong();
                DataOutputStream dos = new DataOutputStream(new FileOutputStream(nombre));
                long recibidos = 0;
                int n1, porcentaje;
                while (tam > 0 && (n1 = dis.read(b, 0, (int) Math.min(b.length, tam))) != -1) {
                    dos.write(b, 0, n1);
                    dos.flush();
                    recibidos = recibidos + n1;
                    porcentaje = (int) (recibidos * 100 / tam);
                    //  System.out.print("Recibido: " + porcentaje + "%\r");
                    tam -= n1;
                }

                System.out.print("\n\n Archivo recibido\n");
                this.notifyAll();
                
            }

            // for
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ClienteFlujo(9101, "hola.txt").start();
    }// main
}
