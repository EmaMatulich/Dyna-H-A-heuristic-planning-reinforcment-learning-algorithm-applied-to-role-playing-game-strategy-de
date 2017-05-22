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
public class DynaH extends DynaQ {

    public DynaH(Mundo mundo, Egreedy egreedy, int episodios, Random random, int N) {
        super(mundo, egreedy, episodios, random, N);
    }

    @Override
    public void aprender(GestorAplicacion gestor) {
        for (int i = 0; i < episodios; i++) { // episodios de entrenamiento
            System.out.println("episodio Nº " + i);
            actual = mundo.getEstadoInicial();
            //permite dibujar el camino
            ArrayList<Integer> estadosVisitados = new ArrayList<>();
            int contadorPasos = 0;
            while (actual.isEsFinal() == false) // estado objetivo
            {
                /*
                qlearning
                 */
                //selecciono una accion de todas las disponibles
                contadorPasos++;
                Accion accionElegida = egreedy.getAccion(Q[actual.getNumero()]);
                //usado para dibujar el camino
                if (!estadosVisitados.contains(actual.getNumero())) {
                    estadosVisitados.add(actual.getNumero());
                }
                Estado siguiente = mundo.proximoEstado(actual, accionElegida);
                double reward = mundo.getReward(actual, accionElegida);
                double maxQSiguiente = this.maxQ(this.Q[siguiente.getNumero()]);
                double qActual = Q[actual.getNumero()][accionElegida.getNumero()];
                double valorQ = qActual + (alpha * (reward + (gamma * maxQSiguiente) - qActual));
                setQ(actual.getNumero(), accionElegida.getNumero(), valorQ);
                // Set the next state as the current state

                /*
                    refactoring modelo
                 */
                if (!estadosVisitadosModelo.contains(actual.getNumero())) {
                    estadosVisitadosModelo.add(actual.getNumero());
                }
                if (!accionesEjecutadasPorEstado[actual.getNumero()].contains(accionElegida.getNumero())) {
                    accionesEjecutadasPorEstado[actual.getNumero()].add(accionElegida.getNumero());
                }
                modelo[actual.getNumero()][accionElegida.getNumero()] = new Prediccion(siguiente, reward);
                //ejecuto el for para el parametro N

                int estadoRandom = this.estadosVisitadosModelo.get(this.random.nextInt(estadosVisitadosModelo.size()));
                for (int j = 0; j < this.N; j++) {
                    int accionRandom = this.Ha(mundo.getEstado(estadoRandom)).getNumero();
                    if (!(modelo[estadoRandom][accionRandom] != null)) {
                        //obtener estado aleatorio 
                        estadoRandom = this.estadosVisitadosModelo.get(this.random.nextInt(estadosVisitadosModelo.size()));
                        //obtener accion alaeatoria                        
                        accionRandom = this.accionesEjecutadasPorEstado[estadoRandom].get(this.random.nextInt((accionesEjecutadasPorEstado[estadoRandom].size())));
                    }
                    Estado prediccionEstado = modelo[estadoRandom][accionRandom].getEstado();
                    double prediccionReward = modelo[estadoRandom][accionRandom].getReward();
                    maxQSiguiente = this.maxQ(this.Q[prediccionEstado.getNumero()]);
                    qActual = Q[estadoRandom][accionRandom];
                    valorQ = qActual + (alpha * (prediccionReward + (gamma * maxQSiguiente) - qActual));
                    setQ(estadoRandom, accionRandom, valorQ);
                    estadoRandom = prediccionEstado.getNumero();
                }
                //seteo el proximo estado
                actual = siguiente;
            }
            this.mundo.dibujarCamino(estadosVisitados);
            gestor.setPasosPromedio(2, mundo.getNumeroDeMundo(), i, contadorPasos);
        }
    }

    private Accion Ha(Estado actual) {
        Double[] valorHa = new Double[4];
        double valorMaximo = 0;
        ArrayList<Accion> peorAccion = new ArrayList<>();
        for (int i = 0; i < acciones.length; i++) {
            valorHa[i] = H(actual, acciones[i]);
            if (valorHa[i] >= 0) {
                if (valorHa[i] > valorMaximo) {
                    valorMaximo = valorHa[i];
                    peorAccion.clear();
                    peorAccion.add(acciones[i]);
                } else if (valorHa[i] == valorMaximo) {
                    peorAccion.add(acciones[i]);
                }
            }
        }
        if(peorAccion.isEmpty()){
            int random=this.random.nextInt(this.acciones.length);
            return this.acciones[random];
        }else{
            int random = this.random.nextInt((peorAccion.size()));
            return peorAccion.get(random);
        }
    }

    private double H(Estado actual, Accion accion) {
        try {
            Estado estadoSiguiente = modelo[actual.getNumero()][accion.getNumero()].getEstado();
            //recuperar estado final
            Estado estadoFinal = mundo.getEstadoFinal();
            //calcular el valor absoluto de estado siguiente - el final al cuadrado
            double valor = Math.pow(Math.abs(Math.sqrt(Math.pow(estadoSiguiente.getFila() - estadoFinal.getFila(), 2) + Math.pow(estadoSiguiente.getColumna() - estadoFinal.getColumna(), 2))), 2);
            return valor;
        } catch (NullPointerException e) {
            return -1;
        }  //recuperar el estado siguiente

    }

}
