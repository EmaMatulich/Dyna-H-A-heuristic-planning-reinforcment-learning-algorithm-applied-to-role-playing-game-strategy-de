package javaapplication2;

/**
 *
 * @author Ema Matulich
 */
import java.util.ArrayList;
import java.util.Random;


public class Qlearning {

    protected static Accion ARRIBA = new Accion("ARRIBA", 0);
    protected static Accion DERECHA = new Accion("DERECHA", 1);
    protected static Accion ABAJO = new Accion("ABAJO", 2);
    protected static Accion IZQUIERDA = new Accion("IZQUIERDA", 3);
    protected static Accion[] acciones = AccionesDisponibles.getTodasLasAcciones();
    protected final double alpha = 0.1;
    protected final double gamma = 0.9;
    protected Estado actual;
    protected Mundo mundo = new Mundo();
    protected double Q[][];
    protected Egreedy egreedy;
    protected int filas;
    protected int columnas;
    protected int episodios;
    protected int totalDeEstados;
    protected Random random;

    public Qlearning(Mundo mundo, Egreedy egreedy, int episodios,Random random) {
        this.random=random;
        this.episodios = episodios;
        this.mundo = mundo;
        this.egreedy = egreedy;
        this.filas = mundo.getFilas();
        this.columnas = mundo.getColumnas();
        this.totalDeEstados = mundo.getTotalEstados();
        this.Q = new double[mundo.getTotalEstados()][acciones.length];
        for (int i = 0; i < this.totalDeEstados; i++) {
            for (int j = 0; j < acciones.length; j++) {
                Q[i][j] = 0;
            }
        }
    }

    public void aprender(GestorAplicacion gestor) {
        for (int i = 0; i < episodios; i++) { // episodios de entrenamiento
            System.out.println("episodio Nº " + i);
            // selecciono un estado aleatoriamente que no se el final ni uno bloqueado
//            do {
//                this.actual = mundo.getEstadoAleatorio();
//            } while (actual.isEsFinal() || actual.isEsObstaculo());
            this.actual = mundo.getEstadoInicial();
            //se usa para dibujar el recorrido del mundo    
            ArrayList<Integer> estadosVisitados= new ArrayList<>();

            int contadorPasos=0;
            while (actual.isEsFinal() == false) // estado objetivo
            {
                //selecciono una accion de todas las disponibles
                contadorPasos++;
                Accion accionElegida = egreedy.getAccion(Q[actual.getNumero()]);
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
                actual = siguiente;
            }            
            this.mundo.dibujarCamino(estadosVisitados);
            gestor.setPasosPromedio(0, mundo.getNumeroDeMundo(), i, contadorPasos);
            }
            
        
    }

    //funcion que devuelve el argmaxQ*
    double maxQ(double[] valores) {
        double maxValor = valores[0];
        for (int i = 0; i < acciones.length; i++) {
            if (valores[i] > maxValor) {
                maxValor = valores[i];
            }
        }
        return maxValor;
    }

    // obtener la politica desde el estado
    //por ejemplo desde el inicial
    void politica() {
        for(int i=0; i<this.totalDeEstados; i++){
            Estado aux=this.mundo.getEstado(i);
            if(!aux.isEsFinal()){
                System.out.println("ESTADO N° "+i+"--"+"FILA: "+aux.getFila()+" COL: "+aux.getColumna()+" OBS: "+aux.isEsObstaculo());
                double maximo=Q[i][0];
                for(int j=0; j<this.acciones.length;j++){
                    if(Q[i][j]>maximo){
                        maximo=Q[i][j];
                    }
                }
                for(int z=0; z<this.acciones.length;z++){
                    if(Q[i][z]==maximo){
                        System.out.println("------"+acciones[z].getNombre());
                    }
                }
            }
        }
    }

    double Q(int estado, int accion) {
        return Q[estado][accion];
    }

    void setQ(int estado, int accion, double valor) {
        Q[estado][accion] = valor;
    }
//
    public void imprimirQ() {
        String toString = "";
        for (int i = 0; i < this.totalDeEstados; i++) {
            for (int j = 0; j < acciones.length; j++) {
                toString = toString + Q[i][j] + " ";
            }
            toString = toString + "\n";
        }
        System.out.println(toString);
    }
    
    public Mundo getMundo(){
        return this.mundo;
    }
}
