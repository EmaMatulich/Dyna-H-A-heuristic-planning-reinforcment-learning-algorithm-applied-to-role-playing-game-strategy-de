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
class EstadoAccionEjecutada {
    private Estado estado;
    private Accion accion;

    public EstadoAccionEjecutada(Estado estado, Accion accion) {
        this.estado = estado;
        this.accion = accion;
    }

    public EstadoAccionEjecutada() {
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Accion getAccion() {
        return accion;
    }

    public void setAccion(Accion accion) {
        this.accion = accion;
    }
    
    
}
