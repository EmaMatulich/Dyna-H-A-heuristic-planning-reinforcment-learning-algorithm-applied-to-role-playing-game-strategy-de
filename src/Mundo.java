/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.math3.distribution.NormalDistribution;

/**
 *
 * @author Ema Matulich
 */
public class Mundo {

    private Accion ARRIBA = new Accion("ARRIBA", 0);
    private Accion DERECHA = new Accion("DERECHA", 1);
    private Accion ABAJO = new Accion("ABAJO", 2);
    private Accion IZQUIERDA = new Accion("IZQUIERDA", 3);
    private Random random;
    private int filas;
    private int columnas;
    private Estado[][] estados;
    private int totalEstados;
    private Accion[] acciones = AccionesDisponibles.getTodasLasAcciones();
    private double[][] reward;
    private Estado estadoFinal;
    private Estado estadoInicial;
    private int numeroDeMundo;

    private NormalDistribution normal = new NormalDistribution(0, 0.3);

    public Mundo() {

    }

    public Mundo(int filas, int columnas, int filaEstadoInicial, int columnaEstadoInicial, int filaEstadoFinal, int columnaEstadoFinal, Random random,int numeroDeMundo) {
        this.random=random;
        this.filas = filas;
        this.columnas = columnas;
        this.totalEstados = filas * columnas;
        int contador = 0;
        int auxFila = 0;
        int auxColumna = 0;
        this.estados = this.generarGrillaDeEstados(filaEstadoInicial, columnaEstadoInicial, filaEstadoFinal, columnaEstadoFinal);
        this.estadoInicial = estados[filaEstadoInicial][columnaEstadoInicial];
        this.estadoFinal = estados[filaEstadoFinal][columnaEstadoFinal];
        reward = new double[this.totalEstados][acciones.length];
        this.numeroDeMundo=numeroDeMundo;
        //INICIALIZO LA FUNCION DE REWARD DEL MUNDO SEGUN
        //LA GRAFICA DIBUJADA EN PANTALLA
        //recorro las filas del mundo
        for (int i = 0; i < filas; i++) {
            //recorro las columnas del mundo
            for (int j = 0; j < columnas; j++) {
                //recorro todas las acciones para el estado actual en el 
                //que me encuentro segun los 2 fors exteriores
                estados[i][j].setNumero(contador);
                contador++;
                auxFila = estados[i][j].getFila();
                auxColumna = estados[i][j].getColumna();
                //El experimento dice que todos los reward son menos 1 si no llevan a un estado final
                reward[estados[i][j].getNumero()][acciones[0].getNumero()] = -1;
                reward[estados[i][j].getNumero()][acciones[1].getNumero()] = -1;
                reward[estados[i][j].getNumero()][acciones[2].getNumero()] = -1;
                reward[estados[i][j].getNumero()][acciones[3].getNumero()] = -1;

                //comprobar si puede ir hacia arriba y hay estado final
                auxFila--;
                if (((auxFila >= 0) && (auxFila < filas)) && ((auxColumna >= 0) && (auxColumna < columnas))) {
                    //si se puede ir hacia arriba ver si el estado al que se llega es final
                    if (estados[auxFila][auxColumna].isEsFinal()) {
                        reward[estados[i][j].getNumero()][acciones[0].getNumero()] = 0;
                    }
                }
//                else {
//                    reward[estados[i][j].getNumero()][acciones[0].getNumero()] = -1;
//                }
                auxFila = estados[i][j].getFila();
                auxColumna = estados[i][j].getColumna();
                //comprobar si puede ir hacia la derecha y hay estado final
                auxColumna++;
                if (((auxFila >= 0) && (auxFila < filas)) && ((auxColumna >= 0) && (auxColumna < columnas))) {
                    //si se puede ir hacia la derecha ver si el estado al que se llega es final
                    if (estados[auxFila][auxColumna].isEsFinal()) {
                        reward[estados[i][j].getNumero()][acciones[1].getNumero()] = 0;
                    }
                }
//                else {
//                    reward[estados[i][j].getNumero()][acciones[1].getNumero()] = -1;
//                }
                auxFila = estados[i][j].getFila();
                auxColumna = estados[i][j].getColumna();
                //comprobar si puede ir hacia abajo y hay estado final
                auxFila++;
                if (((auxFila >= 0) && (auxFila < filas)) && ((auxColumna >= 0) && (auxColumna < columnas))) {
                    //si se puede ir hacia abajo ver si el estado al que se llega es final
                    if (estados[auxFila][auxColumna].isEsFinal()) {
                        reward[estados[i][j].getNumero()][acciones[2].getNumero()] = 0;
                    }
                } 
//                else {
//                    reward[estados[i][j].getNumero()][acciones[2].getNumero()] = -1;
//                }
                auxFila = estados[i][j].getFila();
                auxColumna = estados[i][j].getColumna();
                //comprobar si puede ir hacia la izquierda y hay estado final
                auxColumna--;
                if (((auxFila >= 0) && (auxFila < filas)) && ((auxColumna >= 0) && (auxColumna < columnas))) {
                    //si se puede ir hacia la izquierda ver si el estado al que se llega es final
                    if (estados[auxFila][auxColumna].isEsFinal()) {
                        reward[estados[i][j].getNumero()][acciones[3].getNumero()] = 0;
                    }
                } 
//                else {
//                    reward[estados[i][j].getNumero()][acciones[3].getNumero()] = -1;
//                }
            }
        }
    }

    public Estado getEstado(int fila, int columna) {
        return this.estados[fila][columna];
    }

    public Estado proximoEstado(Estado actual, Accion accion) {
        //PASOS:
        //calcular nueva fila y columna segun la  accion
        //ver si esta en el rango
        //si no estan en el rango devolver el mismo estado
        //si esta en el rango y el siguiente es obstaculo devolver el mismo estado
        //si esta en el rango y el siguiente es final o no devolver ese estado
        int auxFila = actual.getFila();
        int auxColumna = actual.getColumna();
        Estado proximo = null;
        switch (accion.getNumero()) {
            //se mueve hacia ARRIBA
            case 0:
                auxFila--;
                if (((auxFila >= 0) && (auxFila < this.filas)) && ((auxColumna >= 0) && (auxColumna < this.columnas))) {
                    //si se puede ir hacia arriba ver si el estado al que se llega es final
                    if (estados[auxFila][auxColumna].isEsObstaculo()) {
                        proximo = actual;
                    } else {
                        proximo = estados[auxFila][auxColumna];
                    }
                } else {
                    proximo = actual;
                }
                break;
            //se mueve a la DERECHA
            case 1:
                auxColumna++;
                if (((auxFila >= 0) && (auxFila < this.filas)) && ((auxColumna >= 0) && (auxColumna < this.columnas))) {
                    //si se puede ir hacia arriba ver si el estado al que se llega es final
                    if (estados[auxFila][auxColumna].isEsObstaculo()) {
                        proximo = actual;
                    } else {
                        proximo = estados[auxFila][auxColumna];
                    }
                } else {
                    proximo = actual;
                }
                break;
            //se mueve hacia ABAJO
            case 2:
                auxFila++;
                if (((auxFila >= 0) && (auxFila < this.filas)) && ((auxColumna >= 0) && (auxColumna < this.columnas))) {
                    //si se puede ir hacia arriba ver si el estado al que se llega es final
                    if (estados[auxFila][auxColumna].isEsObstaculo()) {
                        proximo = actual;
                    } else {
                        proximo = estados[auxFila][auxColumna];
                    }
                } else {
                    proximo = actual;
                }
                break;
            //se mueve hacia la IZQUIERDA
            case 3:
                auxColumna--;
                if (((auxFila >= 0) && (auxFila < this.filas)) && ((auxColumna >= 0) && (auxColumna < this.columnas))) {
                    //si se puede ir hacia arriba ver si el estado al que se llega es final
                    if (estados[auxFila][auxColumna].isEsObstaculo()) {
                        proximo = actual;
                    } else {
                        proximo = estados[auxFila][auxColumna];
                    }
                } else {
                    proximo = actual;
                }
                break;
            default:
                break;
        }
        return proximo;
    }

    public double getReward(Estado actual, Accion accion) {
        return this.reward[actual.getNumero()][accion.getNumero()];
    }

    public Estado getEstadoAleatorio() {
        int filaEstado=this.random.nextInt(this.filas);
        int columnaEstado=this.random.nextInt(this.columnas);
        return estados[filaEstado][columnaEstado];
    }

    public String toString() {
        String toString = "";
        for (int i = 0; i < this.totalEstados; i++) {
            for (int j = 0; j < this.acciones.length; j++) {
                toString = toString + reward[i][j] + " ";
            }
            toString = toString + "\n";
        }
        return toString;
    }

    public String grilla() {
        String toString = "";
        for (int i = 0; i < this.filas; i++) {
            for (int j = 0; j < this.columnas; j++) {
                if (estados[i][j].isEsObstaculo()) {
                    toString = toString + "\u2248" + " ";
                } else if (estados[i][j].isEsFinal()) {
                    toString = toString + "F" + " ";
                } else if (estados[i][j].isEsInicial()) {
                    toString = toString + "I" + " ";
                } else {
                    toString = toString + "\u25A1" + " ";
                }
            }
            toString = toString + "\n";
        }
        return toString;
    }

    private Estado[][] generarGrillaDeEstados(int filaEstadoInicial, int columnaEstadoInicial, int filaEstadoFinal, int columnaEstadoFinal) {
        Estado[][] estadosCreados = new Estado[this.filas][this.columnas];
        for (int i = 0; i < this.filas; i++) {
            for (int j = 0; j < this.columnas; j++) {
                estadosCreados[i][j] = new Estado();
                estadosCreados[i][j].setFila(i);
                estadosCreados[i][j].setColumna(j);
                estadosCreados[i][j].setEsFinal(false);
                estadosCreados[i][j].setEsInicial(false);
                estadosCreados[i][j].setEsObstaculo(false);
                //ver si es el inicial o final
                //sino ver si le toca ser obstaculo
                if (((estadosCreados[i][j].getFila() == filaEstadoInicial) && (estadosCreados[i][j].getColumna() == columnaEstadoInicial)) || ((estadosCreados[i][j].getFila() == filaEstadoFinal) && (estadosCreados[i][j].getColumna() == columnaEstadoFinal))) {
                    if ((estadosCreados[i][j].getFila() == filaEstadoInicial) && (estadosCreados[i][j].getColumna() == columnaEstadoInicial)) {
                        estadosCreados[i][j].setEsInicial(true);
                    }
                    if ((estadosCreados[i][j].getFila() == filaEstadoFinal) && (estadosCreados[i][j].getColumna() == columnaEstadoFinal)) {
                        estadosCreados[i][j].setEsFinal(true);
                    }
                } else {
                    //ver si se obstaculisa o no el estado
                    double valorDistribucion = normal.inverseCumulativeProbability(random.nextDouble());
                    // normal.density(random.nextDouble());

                    long tileType = Long.signum(Math.abs(Math.round(valorDistribucion)));
//                    System.out.println(tileType);
                    if (tileType == 1) {
                        estadosCreados[i][j].setEsObstaculo(true);
                    }
                }
            }
        }
        return estadosCreados;
    }


    public int getFilas() {
        return filas;
    }

    public void setFilas(int filas) {
        this.filas = filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public void setColumnas(int columnas) {
        this.columnas = columnas;
    }

    public int getTotalEstados() {
        return totalEstados;
    }

    public void setTotalEstados(int totalEstados) {
        this.totalEstados = totalEstados;
    }

    public Estado getEstadoInicial() {
        return estadoInicial;
    }

    public void setEstadoInicial(Estado estadoInicial) {
        this.estadoInicial = estadoInicial;
    }

    public Estado getEstadoFinal() {
        return estadoFinal;
    }

    public void setEstadoFinal(Estado estadoFinal) {
        this.estadoFinal = estadoFinal;
    }

    public int getNumeroDeMundo() {
        return numeroDeMundo;
    }

    public void setNumeroDeMundo(int numeroDeMundo) {
        this.numeroDeMundo = numeroDeMundo;
    }
    
    
    public void dibujarCamino(ArrayList<Accion> ejecutadas, ArrayList<String> filas, ArrayList<String> columnas) {
        String[][] camino = new String[this.filas][this.columnas];
        for (int i = 0; i < this.filas; i++) {
            for (int j = 0; j < this.columnas; j++) {
                if (estados[i][j].isEsObstaculo()) {
                    camino[i][j] = "O";
                } else if (estados[i][j].isEsFinal()) {
                    camino[i][j] = "F";
                } else if (estados[i][j].isEsInicial()) {
                    camino[i][j] = "I";
                } else {
                    camino[i][j] = "*";
                }
            }
        }
        for (int i = 0; i < ejecutadas.size(); i++) {
            if (ejecutadas.get(i).getNumero()==ARRIBA.getNumero()) {
                camino[Integer.parseInt(filas.get(i))][Integer.parseInt(columnas.get(i))] ="^";
                continue;
            }
            if (ejecutadas.get(i).getNumero()==ABAJO.getNumero()) {
                camino[Integer.parseInt(filas.get(i))][Integer.parseInt(columnas.get(i))] ="V";
                continue;
            }
            if (ejecutadas.get(i).getNumero()==DERECHA.getNumero()) {
                camino[Integer.parseInt(filas.get(i))][Integer.parseInt(columnas.get(i))] =">";
                continue;
            }
            if (ejecutadas.get(i).getNumero()==IZQUIERDA.getNumero()) {
                camino[Integer.parseInt(filas.get(i))][Integer.parseInt(columnas.get(i))] ="<";
            }
        }
        String toString = "";
        for (int i = 0; i < this.filas; i++) {
            for (int j = 0; j < this.columnas; j++) {
               
                    toString = toString + camino[i][j] + " ";
              
            }
            toString = toString + "\n";
        }
        System.out.println(toString);
    }
    public void dibujarCamino(ArrayList<Integer> estadosVisitados) {
        String[][] camino = new String[this.filas][this.columnas];    
        for (int i = 0; i < this.filas; i++) {
            for (int j = 0; j < this.columnas; j++) {
                if (estados[i][j].isEsObstaculo()) {
                    camino[i][j] = "\u2248";  //≈
                } else if (estados[i][j].isEsFinal()) {
                    camino[i][j] = "F";
                } else if (estados[i][j].isEsInicial()) {
                    camino[i][j] = "I";
                } else {
                    camino[i][j] = "\u25A1";
                }
                if(estadosVisitados.contains(this.estados[i][j].getNumero()) && !estados[i][j].isEsInicial() && !estados[i][j].isEsFinal()){
                    camino[i][j] = "\u25A0";
                }
            }
        }
        String toString = "";
        for (int i = 0; i < this.filas; i++) {
            for (int j = 0; j < this.columnas; j++) {
               
                    toString = toString + camino[i][j] + " ";
              
            }
            toString = toString + "\n";
        }
        System.out.println(toString);
    }

    Estado getEstado(int numero) {
          for(int i=0; i<this.filas;i++){
              for(int j=0; j<columnas;j++){
                  if(this.estados[i][j].getNumero()==numero){
                      return estados[i][j];
                  }
              }
          }
          return null;
    }

}
