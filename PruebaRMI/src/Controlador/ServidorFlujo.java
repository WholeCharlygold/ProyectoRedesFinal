/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JFileChooser;

/**
 *
 * @author Albarran Cruz Carlos Alberto
 */
public class ServidorFlujo extends Thread {

    ServerSocketChannel s;
    Socket cl;
    DataInputStream diis;
    Selector selector;
    int puerto;

    public ServidorFlujo(int puerto) {
        this.puerto = puerto;
        try {
            selector = Selector.open();
            s = ServerSocketChannel.open();
            s.configureBlocking(false);
            InetSocketAddress hostAddress = new InetSocketAddress(puerto);
            s.bind(hostAddress);
            s.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            s.register(selector, SelectionKey.OP_ACCEPT);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public synchronized void run() {
        try {

            while (true) {
                int readyCount = selector.select();
                if (readyCount == 0) {
                    continue;
                }
                Set<SelectionKey> readyKeys = selector.selectedKeys();
                Iterator iterator = readyKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = (SelectionKey) iterator.next();
                    iterator.remove();

                    if (key.isAcceptable()) {
                        
                        ServerSocketChannel server=(ServerSocketChannel)key.channel();
                            ServerSocket ss = server.socket();
                        cl = ss.accept();
                        diis = new DataInputStream(cl.getInputStream());
                        DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
                        
                        String file = diis.readUTF();
                        System.out.println(file);
                         byte[] b = new byte[1024];
                            dos.writeInt(1024);
                        int puertoRMI = puerto-100;

                        File f = new File(System.getProperty("user.dir") + "/" + puertoRMI + "/" + file);
                        String archivo = f.getAbsolutePath();
                        String nombre = f.getName();
                        long tam = f.length();

                        DataInputStream dis = new DataInputStream(new FileInputStream(archivo));
                        dos.writeUTF(nombre);
                        dos.flush();
                        dos.writeLong(tam);
                        long enviados = 0;
                    int porcentaje, n;
                    System.out.println("Enviando el archivo: " + f.getName());
                    while (tam > 0 && (n = dis.read(b, 0, (int) Math.min(b.length, tam))) != -1) {
                        dos.write(b, 0, n);
                        dos.flush();
                        enviados = enviados + n;
                        porcentaje = (int) (enviados * 100 / tam);
                        //  System.out.println("Enviado: " + porcentaje + "%\r");
                        tam -= n;
                    } // while
                    //contador++;
                    System.out.print("\n\n");
                    break;
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new ServidorFlujo(9101).start();
    }// main

}
