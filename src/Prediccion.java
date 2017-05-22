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
public class Prediccion {
    
    private Estado estado;
    
    private double reward;

    
    public Prediccion(Estado estado, double reward) {
        this.estado = estado;
        this.reward = reward;
    }
    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
    
    
    
}
