/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.util.ArrayList;

/**
 *
 * @author Ema Matulich
 */
public class AccionesDisponibles {

    private static Accion ARRIBA = new Accion("ARRIBA", 0);
    private static Accion DERECHA = new Accion("DERECHA", 1);
    private static Accion ABAJO = new Accion("ABAJO", 2);
    private static Accion IZQUIERDA = new Accion("IZQUIERDA", 3);

    public AccionesDisponibles(){}

    public static Accion[] getTodasLasAcciones() {
        return new Accion[]{ARRIBA, DERECHA, ABAJO, IZQUIERDA};
    }

}
