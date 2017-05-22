/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.util.ArrayList;
import java.util.Random;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javax.swing.JFrame;

/**
 *
 * @author Ema Matulich
 */
public class GestorAplicacion {

    Random random;
    Egreedy egreedy;
    final int N_PLANNING_STEPS = 10;
    final int EPISODIOS = 100;
    Mundo[] mundos = new Mundo[30];
    /*
            primera dimension es el algoritmo:
                0-qlearning
                1-dynaq
                2-dynah
            segunda dimension son los mundos
            tercera dimension son los episodios;
     */
    private int[][] pasosQlearning;
    private int[][] pasosDynaQ;
    private int[][] pasosDynaH;

    public GestorAplicacion() {
        this.random = new Random();
        this.egreedy = new Egreedy(random);
        for (int i = 0; i < 30; i++) {
            mundos[i] = new Mundo(39, 36, 1, 4, 28, 34, random, i);
        }
        pasosQlearning = new int[mundos.length + 1][EPISODIOS];
        pasosDynaQ = new int[mundos.length + 1][EPISODIOS];
        pasosDynaH = new int[mundos.length + 1][EPISODIOS];
    }

    public void resolverLaberintos() {
        for (int j = 0; j < mundos.length; j++) {
            System.out.println("MUNDO "+j+" ORIGINAL");
            System.out.println(mundos[j].grilla());
            System.out.println("REWARD DE LOS ESTADOS DEL MUNDO "+j);
            System.out.println(mundos[j].toString());
            System.out.println("MUNDO " + j);
            System.out.println("Q LEARNING");
            Qlearning agente1 = new Qlearning(mundos[j], egreedy, EPISODIOS, random);
            agente1.aprender(this);
//            agente1.politica();
//                System.out.println("FUNCION DE VALOR RESULTANTE");
//                agente1.imprimirQ();
            System.out.println("MUNDO " + j);
            System.out.println("DYNA Q");
            DynaQ agente2 = new DynaQ(mundos[j], egreedy, EPISODIOS, random, N_PLANNING_STEPS);
            agente2.aprender(this);
//                  agente2.politica();
//                System.out.println("FUNCION DE VALOR RESULTANTE");
//                agente2.imprimirQ();
            System.out.println("MUNDO " + j);
            System.out.println("DYNA H");
            DynaH agente3= new DynaH(mundos[j], egreedy, EPISODIOS, random, N_PLANNING_STEPS);
            agente3.aprender(this);
//                agente3.politica();
//                System.out.println("FUNCION DE VALOR RESULTANTE");
//                agente3.imprimirQ();
        }
        this.calcularPromedioFinal();
        this.imprimirPasosPromedio();
        this.graficarPasosPromedioQlearning();
        this.graficarPasosPromedioDynaQ();
        this.graficarPasosPromedioDynaH();
        this.graficarPasosPromedioComparacion();
    }

    public void calcularPromedioFinal() {
        for (int z = 0; z < this.EPISODIOS; z++) {
            int pasosPromedioQlearning = 0;
            int pasosPromedioDynaQ = 0;
            int pasosPromedioDynaH = 0;
            for (int j = 0; j < this.mundos.length; j++) {
                pasosPromedioQlearning = pasosPromedioQlearning + pasosQlearning[j][z];
                pasosPromedioDynaQ = pasosPromedioDynaQ + pasosDynaQ[j][z];
                pasosPromedioDynaH = pasosPromedioDynaH + pasosDynaH[j][z];
            }
            pasosQlearning[mundos.length][z] = pasosPromedioQlearning / this.mundos.length;
            pasosDynaQ[mundos.length][z] = pasosPromedioDynaQ / this.mundos.length;
            pasosDynaH[mundos.length][z] = pasosPromedioDynaH / this.mundos.length;
        }
    }

    public void setPasosPromedio(int algoritmo, int mundo, int episodio, int pasos) {
        switch (algoritmo) {
            case 0:
                this.pasosQlearning[mundo][episodio] = pasos;
                break;
            case 1:
                this.pasosDynaQ[mundo][episodio] = pasos;
                break;
            case 2:
                this.pasosDynaH[mundo][episodio] = pasos;
                break;
        }
    }

    public void imprimirPasosPromedio() {
        for (int i = 0; i < 3; i++) {
            String matrix = "";
            for (int j = 0; j < mundos.length+1; j++) {
                for (int z = 0; z < EPISODIOS; z++) {
                    switch (i) {
                        case 0:
                            matrix = matrix + "--" + this.pasosQlearning[j][z] + "--";
                            break;
                        case 1:
                            matrix = matrix + "--" + this.pasosDynaQ[j][z] + "--";
                            break;
                        case 2:
                            matrix = matrix + "--" + this.pasosDynaH[j][z] + "--";
                            break;
                    }
                }
                matrix = matrix + "\n";
            }
            switch (i) {
                case 0:
                    System.out.println("PASOS PROMEDIO Q LEARNING");
                    break;
                case 1:
                    System.out.println("PASOS PROMEDIO DYNA Q");
                    break;
                case 2:
                    System.out.println("PASOS PROMEDIO DYNA H");
                    break;
            }
            System.out.println(matrix);
        }
    }

    public void graficarPasosPromedioQlearning() {
        JFXPanel fxPanel = new JFXPanel();
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("EPISODIOS");
        yAxis.setLabel("PASOS");
        //creating the chart
        final LineChart<Number, Number> lineChart
                = new LineChart<Number, Number>(xAxis, yAxis);
        //defining a series
        ArrayList<XYChart.Series<Number, Number>> series = new ArrayList<>();
        
        for(int i=0; i<this.mundos.length;i++){
            XYChart.Series aux=new XYChart.Series();
            aux.setName("MUNDO "+i);
            for(int j=0; j<this.EPISODIOS;j++){
                aux.getData().add(new XYChart.Data(j,this.pasosQlearning[i][j]));
            }
            
            series.add(aux);
        }
        XYChart.Series pasosPromedio=new XYChart.Series();
        pasosPromedio.setName("PASOS PROMEDIO");
        for(int j=0; j<this.EPISODIOS;j++){
            pasosPromedio.getData().add(new XYChart.Data(j,this.pasosQlearning[this.mundos.length][j]));
        }
        series.add(pasosPromedio);
        Scene scene  = new Scene(lineChart,800,600);        
        lineChart.getData().addAll(series);        

        fxPanel.setScene(scene);
        JFrame frame = new JFrame("PASOS PROMEDIO Q LEARNING");
        frame.add(fxPanel);
        frame.setSize(600, 600);
        frame.setVisible(true);

    }
    
    public void graficarPasosPromedioDynaQ(){
     JFXPanel fxPanel = new JFXPanel();
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("EPISODIOS");
        yAxis.setLabel("PASOS");
        //creating the chart
        final LineChart<Number, Number> lineChart
                = new LineChart<Number, Number>(xAxis, yAxis);
        //defining a series
        ArrayList<XYChart.Series<Number, Number>> series = new ArrayList<>();
        
        for(int i=0; i<this.mundos.length;i++){
            XYChart.Series aux=new XYChart.Series();
            aux.setName("MUNDO "+i);
            for(int j=0; j<this.EPISODIOS;j++){
                aux.getData().add(new XYChart.Data(j,this.pasosDynaQ[i][j]));
            }
            
            series.add(aux);
        }
        XYChart.Series pasosPromedio=new XYChart.Series();
        pasosPromedio.setName("PASOS PROMEDIO");
        for(int j=0; j<this.EPISODIOS;j++){
            pasosPromedio.getData().add(new XYChart.Data(j,this.pasosDynaQ[this.mundos.length][j]));
        }
        series.add(pasosPromedio);
        Scene scene  = new Scene(lineChart,800,600);        
        lineChart.getData().addAll(series);        

        fxPanel.setScene(scene);
        JFrame frame = new JFrame("PASOS PROMEDIO DYNA Q");
        frame.add(fxPanel);
        frame.setSize(600, 600);
        frame.setVisible(true);
    }
    public void graficarPasosPromedioDynaH(){
    JFXPanel fxPanel = new JFXPanel();
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("EPISODIOS");
        yAxis.setLabel("PASOS");
        //creating the chart
        final LineChart<Number, Number> lineChart
                = new LineChart<Number, Number>(xAxis, yAxis);
        //defining a series
        ArrayList<XYChart.Series<Number, Number>> series = new ArrayList<>();
        
        for(int i=0; i<this.mundos.length;i++){
            XYChart.Series aux=new XYChart.Series();
            aux.setName("MUNDO "+i);
            for(int j=0; j<this.EPISODIOS;j++){
                aux.getData().add(new XYChart.Data(j,this.pasosDynaH[i][j]));
            }
            
            series.add(aux);
        }
        XYChart.Series pasosPromedio=new XYChart.Series();
        pasosPromedio.setName("PASOS PROMEDIO");
        for(int j=0; j<this.EPISODIOS;j++){
            pasosPromedio.getData().add(new XYChart.Data(j,this.pasosDynaH[this.mundos.length][j]));
        }
        series.add(pasosPromedio);
        Scene scene  = new Scene(lineChart,800,600);        
        lineChart.getData().addAll(series);        

        fxPanel.setScene(scene);
        JFrame frame = new JFrame("PASOS PROMEDIO DYNA H");
        frame.add(fxPanel);
        frame.setSize(600, 600);
        frame.setVisible(true);
    }
    
        public void graficarPasosPromedioComparacion(){
    JFXPanel fxPanel = new JFXPanel();
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("EPISODIOS");
        yAxis.setLabel("PASOS");
        //creating the chart
        final LineChart<Number, Number> lineChart
                = new LineChart<Number, Number>(xAxis, yAxis);
        //defining a series
        ArrayList<XYChart.Series<Number, Number>> series = new ArrayList<>();
        
        XYChart.Series pasosPromedioQlearning=new XYChart.Series();
        pasosPromedioQlearning.setName("Q LEARNING");
        for(int j=0; j<this.EPISODIOS;j++){
            pasosPromedioQlearning.getData().add(new XYChart.Data(j,this.pasosQlearning[this.mundos.length][j]));
        }
        series.add(pasosPromedioQlearning);
        
        XYChart.Series pasosPromedioDynaQ=new XYChart.Series();
        pasosPromedioDynaQ.setName("DYNA Q");
        for(int j=0; j<this.EPISODIOS;j++){
            pasosPromedioDynaQ.getData().add(new XYChart.Data(j,this.pasosDynaQ[this.mundos.length][j]));
        }
        series.add(pasosPromedioDynaQ);
        
        XYChart.Series pasosPromedioDynaH=new XYChart.Series();
        pasosPromedioDynaH.setName("DYNA H");
        for(int j=0; j<this.EPISODIOS;j++){
            pasosPromedioDynaH.getData().add(new XYChart.Data(j,this.pasosDynaH[this.mundos.length][j]));
        }
        series.add(pasosPromedioDynaH);
        Scene scene  = new Scene(lineChart,800,600);        
        lineChart.getData().addAll(series);        

        fxPanel.setScene(scene);
        JFrame frame = new JFrame("PASOS PROMEDIO 3 ALGORITMOS");
        frame.add(fxPanel);
        frame.setSize(600, 600);
        frame.setVisible(true);
    }
}
