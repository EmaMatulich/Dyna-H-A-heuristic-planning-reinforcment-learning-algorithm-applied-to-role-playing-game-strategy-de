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
public class Estado {
    private int numero;
    private int fila;
    private int columna;
    private boolean esObstaculo;
    private boolean esFinal;
    private boolean esInicial;

    
    public Estado(){}
    
    public Estado(int fila, int columna, boolean esObstaculo, boolean esFinal,boolean esInicial) {
        this.fila = fila;
        this.columna = columna;
        this.esObstaculo = esObstaculo;
        this.esFinal = esFinal;
        this.esInicial=esInicial;
    }

    public void setEsInicial(boolean esInicial) {
        this.esInicial = esInicial;
    }

    public boolean isEsInicial() {
        return esInicial;
    }
    
    
    public boolean isEsFinal() {
        return esFinal;
    }
    
    public void setEsFinal(boolean esFinal) {
        this.esFinal = esFinal;
    }

    

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public boolean isEsObstaculo() {
        return esObstaculo;
    }

    public void setEsObstaculo(boolean esObstaculo) {
        this.esObstaculo = esObstaculo;
    }
    
    public String toString(){
        return this.numero +" "+this.fila+" "+this.columna;
    }
    
}