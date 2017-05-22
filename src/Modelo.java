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
public class Modelo {
    
     private double reward;
    
    private Estado estadoSiguiente;
    
    public Modelo(double reward, Estado estadoSiguiente){ 
        this.reward=reward;
        this.estadoSiguiente=estadoSiguiente;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public Estado getEstado() {
        return estadoSiguiente;
    }

    public void setEstado(Estado estadoSiguiente) {
        this.estadoSiguiente= estadoSiguiente;
    }
    
}
