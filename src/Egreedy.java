/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Ema Matulich
 */
public class Egreedy {

    private Accion[] acciones;
    private final double epsilon = 0.1;
    private final Random random;

    public Egreedy(Random random) {
        this.acciones = AccionesDisponibles.getTodasLasAcciones();
        this.random=random;
    }

    //como parametro recibe el vector para un estado particular de la uncion Q
    //explotar elegir la opcion que tenga el menor valor del vector que se paso como parametro 
    //ya que el algotirmo explota las rutas de menor
    //COSTO es decir menores trayectorias
    public Accion getAccion(double[] valoresQ) {
        double probabilidad = random.nextDouble();
        Accion retornada = null;
        if (epsilon>probabilidad) {
            retornada = this.acciones[this.random.nextInt(this.acciones.length)];
        } else {
            double valorMaximo=valoresQ[0];
            for (int i =0;i < valoresQ.length; i++) {
              if(valoresQ[i]>valorMaximo){
                  valorMaximo=valoresQ[i];
              }
            }
            ArrayList<Integer> accionesOptimas= new ArrayList();
            for (int j=0;j < valoresQ.length; j++) {
              if(valoresQ[j]==valorMaximo){
                  accionesOptimas.add(j);
              }
            }
            retornada= this.acciones[accionesOptimas.get(this.random.nextInt(accionesOptimas.size()))];
        }
        return retornada;
    }

}
