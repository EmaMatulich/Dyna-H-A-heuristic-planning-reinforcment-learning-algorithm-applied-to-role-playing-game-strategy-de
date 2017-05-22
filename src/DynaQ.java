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
public class DynaQ  extends Qlearning{
    
    //este vector va a contener el valor del reward y el estado siguiente observado en caso de que
    //el par estado accion se encuentre en el set de abajo
    protected Prediccion[][] modelo;
    //array que contiene los estados visitados
    protected ArrayList<Integer> estadosVisitadosModelo;
    //array que contiene un array con las acciones ejecutadas por cada estado
    protected ArrayList<Integer>[] accionesEjecutadasPorEstado;
    
    //parametro del algoritmo
    protected int N;
    public DynaQ(Mundo mundo, Egreedy egreedy, int episodios,Random random,int N) {
        super(mundo, egreedy, episodios,random);
        modelo= new Prediccion[mundo.getTotalEstados()][acciones.length];
        this.estadosVisitadosModelo= new ArrayList<>();
        this.accionesEjecutadasPorEstado= (ArrayList<Integer>[])new ArrayList[this.mundo.getTotalEstados()];
        for (int i = 0; i < accionesEjecutadasPorEstado.length; i++) {
            accionesEjecutadasPorEstado[i] = new ArrayList<Integer>();
        }
        this.N=N;
    }
    
    @Override
    public void aprender(GestorAplicacion gestor) {
        for (int i = 0; i < episodios; i++) { // episodios de entrenamiento
            System.out.println("episodio Nº " + i);
            actual = mundo.getEstadoInicial();
            //permite dibujar el camino
            ArrayList<Integer> estadosVisitados= new ArrayList<>();
            int contadorPasos=0;
            while (actual.isEsFinal() == false) // estado objetivo
            {
                /*
                qlearning
                */
                //selecciono una accion de todas las disponibles
                contadorPasos++;
                Accion accionElegida = egreedy.getAccion(Q[actual.getNumero()]);
                //usado para dibujar el camino
                if(!estadosVisitados.contains(actual.getNumero())){
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
                if(!estadosVisitadosModelo.contains(actual.getNumero())){
                    estadosVisitadosModelo.add(actual.getNumero());
                }
                if(!accionesEjecutadasPorEstado[actual.getNumero()].contains(accionElegida.getNumero())){
                   accionesEjecutadasPorEstado[actual.getNumero()].add(accionElegida.getNumero());
                }
                modelo[actual.getNumero()][accionElegida.getNumero()]= new Prediccion(siguiente, reward);
                //ejecuto el for para el parametro N
                for(int j=0; j<this.N;j++){
                  //obtener estado aleatorio 
                  int estadoRandom = this.estadosVisitadosModelo.get(this.random.nextInt(estadosVisitadosModelo.size()));
                  //obtener accion alaeatoria                        
                  int accionRandom = this.accionesEjecutadasPorEstado[estadoRandom].get(this.random.nextInt((accionesEjecutadasPorEstado[estadoRandom].size())));
                  Estado prediccionEstado = modelo[estadoRandom][accionRandom].getEstado();
                  double prediccionReward = modelo[estadoRandom][accionRandom].getReward();
                  maxQSiguiente = this.maxQ(this.Q[prediccionEstado.getNumero()]);
                  qActual = Q[estadoRandom][accionRandom];
                  valorQ = qActual + (alpha * (prediccionReward + (gamma * maxQSiguiente) - qActual));
                  setQ(estadoRandom, accionRandom, valorQ);
                }
                //seteo el proximo estado
                actual = siguiente;
            }
            this.mundo.dibujarCamino(estadosVisitados);
            gestor.setPasosPromedio(1, mundo.getNumeroDeMundo(), i, contadorPasos);

        }
    }
    
}
