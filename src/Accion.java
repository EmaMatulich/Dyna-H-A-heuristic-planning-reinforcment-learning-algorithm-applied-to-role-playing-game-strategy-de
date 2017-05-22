/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

/**
 *
 * @author Ema Matulich
 */
public class Accion {
    
    
    private String nombre;
    private int numero;

    public Accion(String nombre, int numero) {
        this.nombre = nombre;
        this.numero=numero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void setNumero(int numero){
        this.numero=numero;
    }
    
    public int getNumero(){
        return this.numero;
    }
    	
    public String toString() {
	StringBuilder result = new StringBuilder();
	result.append("[Accion: ");
	result.append(this.nombre);
	result.append("]");
	return result.toString();
    }
    
    
    
}
