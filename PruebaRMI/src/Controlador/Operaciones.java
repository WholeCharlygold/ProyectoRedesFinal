/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 *
 * @author Albarran Cruz Carlos Alberto
 */
public interface Operaciones extends Remote {
    
    int suma(int a,int b)throws RemoteException;
    int multiplicacion(int a,int b)throws RemoteException;
    double division(double a,double b)throws RemoteException;
    Nodo encontrarArchivo(String file) throws RemoteException;
    
    
    
}
