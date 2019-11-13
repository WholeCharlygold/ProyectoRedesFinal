/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;
import java.io.Serializable;
import java.net.*;
import java.util.Comparator;
import java.util.Objects;

/**
 *
 * @author Albarran Cruz Carlos Alberto
 */
public class Nodo extends Thread implements Serializable{
    
    private int puertoRMI;
    private InetAddress direccion;
    private int puertoD;
    private boolean por_aqui_pasaron;
    
    
    public Nodo(int puertoD,InetAddress direccion){
        this.puertoRMI=puertoD-100;
        this.puertoD=puertoD;
        this.direccion=direccion;
        this.por_aqui_pasaron=false;
       // servidorRMI=new ServidorRMI(this.puertoRMI);
     

    }

    public boolean isPor_aqui_pasaron() {
        return por_aqui_pasaron;
    }

    public void setPor_aqui_pasaron(boolean por_aqui_pasaron) {
        this.por_aqui_pasaron = por_aqui_pasaron;
    }
    
    
    public void setPuerto(int puerto){
        this.puertoD=puerto;
    }
    
    public void setDireccion(InetAddress direccion){
        this.direccion=direccion;
    }
    public int getPuerto(){
        return this.puertoD;
    }
    
    public int getPuertoRMI(){
        return this.puertoRMI;
    }
    public InetAddress getDireccion(){
        return this.direccion;
    }
    
   @Override
    public String toString() {
        return "Nodo {" + "Direccion = " + direccion+ ", PuertoD = " + puertoD +", PuertoRMI = "+puertoRMI+'}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + this.puertoRMI;
        hash = 79 * hash + Objects.hashCode(this.direccion);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Nodo other = (Nodo) obj;
        if (this.puertoRMI != other.puertoRMI) {
            return false;
        }
        if (this.puertoD != other.puertoD) {
            return false;
        }
        return true;
    }
    
    public void run(){
        
    }

    
    
    
}

class MyCompNode implements Comparator<Nodo> {


    @Override
    public int compare(Nodo o1, Nodo o2) {
          if(o1.hashCode()< o2.hashCode()){
            return -1;
        } else {
            return 1;
        }
    }
}
