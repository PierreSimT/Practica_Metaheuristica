/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author pierrest
 */
public class busquedaTabu2 {

    List<List<Integer>> frecuencias = new ArrayList<>();
    List<Integer> transmisores = new ArrayList<>();
    List<Integer> frecuenciasR = new ArrayList<>(); // Cada posicion es la frecuencia asignada a dicho transmisor
    restricciones rest;
    int resultado;

    //Variables de control para la reinicialización en caso de estancamiento
    int contadorEstancamiento = 0;
    int costeCicloPrevio = Integer.MAX_VALUE;

    //BT
    List<List<Integer>> listaTabu = new ArrayList<>(); //Lista Tabu
    int tamaFisicolt;                                //Tamaño fisico de la listaTabu
    int tamalogicolt = 0;                              //Tamaño logico de la listaTabu
    int indicelt = 0;                                   
    List<List<Integer>> matrizFrecuencias = new ArrayList<>();

    public busquedaTabu2(listaTransmisores _transmisores, rangoFrec _frecuencias, restricciones _restricciones) {
        frecuencias = _frecuencias.rangoFrecuencias;
        transmisores = _transmisores.transmisores;
        rest = _restricciones;

        tamaFisicolt = (int) transmisores.size() / 6;

        Random numero = new Random();
        resultado = Integer.MAX_VALUE;
        for (int i = 0; i < transmisores.size(); i++) {
            frecuenciasR.add(frecuencias.get(transmisores.get(i)).get(numero.nextInt(frecuencias.get(transmisores.get(i)).size())));
        }
        
        
    }

    /**
     * Algoritmo greedy: Asignar un valor al transmisor de forma iterativa e ir
     * calculando uno por uno. Si el resultado mejora sustituir la lista de
     * solución
     *
     * @param l
     * @param r
     */
    public void algoritmo() throws FileNotFoundException {
        if (resultado == Integer.MAX_VALUE) {
            int result = rDiferencia(frecuenciasR, rest); // Da lugar a la solucion inicial
            if (resultado > result) {
                resultado = result;
            }
            algoritmo();
        } else {
            Random numero = new Random();
            for (int i = 0; i < 10000; i++) {

                boolean tabuActualizado = false;
                int token = numero.nextInt(transmisores.size());
                double sentido = numero.nextDouble();
                int valorInicial = frecuenciasR.get(token); // Se obtiene la frecuencia del token
                int indiceInicial;
                int nuevoCoste = Integer.MAX_VALUE;

                indiceInicial = frecuencias.get(transmisores.get(token)).indexOf(valorInicial); // Mas corto que codigo de abajo

                if (sentido < 0.5) {
                    int iteraciones = 0;
                    boolean fin=false;
                    while (!fin && iteraciones < 20) {
                        int posicionesPos = frecuencias.get(transmisores.get(token)).size();
                        indiceInicial = Math.floorMod(indiceInicial - 1, frecuencias.get(transmisores.get(token)).size());
                        
                        int fact1 = rDiferencia(frecuenciasR, token, rest);
                        valorInicial = frecuencias.get(transmisores.get(token)).get(indiceInicial);
                        boolean esTabu = comprobarTabu(token, valorInicial);

                        if (esTabu == false) {
                            List<Integer> nuevaSolucion = new ArrayList<>();
                            nuevaSolucion.addAll(frecuenciasR);
                            nuevaSolucion.set(token, valorInicial);
                            int fact2 = rDiferencia(nuevaSolucion, token, rest);
                            nuevoCoste = resultado - fact1 + fact2;

                            if (nuevoCoste < resultado) {
                                frecuenciasR = nuevaSolucion;
                                resultado = nuevoCoste;
                                insertarTabu(token, valorInicial);
                                tabuActualizado = true;

                            }
                        }
                        if(posicionesPos==0){
                            fin=true;
                        }

                        iteraciones++;
                        posicionesPos--;

                    }
                } else {
                    int iteraciones = 0;
                    boolean fin=false;

                    while (!fin && iteraciones < 20) {
                        int posicionesPos = frecuencias.get(transmisores.get(token)).size();
                        indiceInicial = Math.floorMod(indiceInicial + 1, frecuencias.get(transmisores.get(token)).size());
                        int fact1 = rDiferencia(frecuenciasR, token, rest);
                        valorInicial = frecuencias.get(transmisores.get(token)).get(indiceInicial);
                        boolean esTabu = comprobarTabu(token, valorInicial);

                        if (esTabu == false) {
                            List<Integer> nuevaSolucion = new ArrayList<>();
                            nuevaSolucion.addAll(frecuenciasR);
                            nuevaSolucion.set(token, valorInicial);
                            int fact2 = rDiferencia(nuevaSolucion, token, rest);
                            nuevoCoste = resultado - fact1 + fact2;

                            if (nuevoCoste < resultado) {
                                frecuenciasR = nuevaSolucion;
                                resultado = nuevoCoste;
                                insertarTabu(token, valorInicial);
                                tabuActualizado = true;

                            }
                        }
                        if (posicionesPos == 0) {
                            fin = true;
                        }
                        posicionesPos--;
                        iteraciones++;
                    }
                }
                System.out.println(i + " : Resultado actual: " + resultado);

                /*Añadir a matriz de frecuencias*/
                boolean encontrado = false;
                for (int j = 0; j < matrizFrecuencias.size(); j++) {

                    //Si ya existe la combinación transmisor-frecuencia en la matriz, aumentar el número de frecuencias
                    if (matrizFrecuencias.get(j).get(0) == token && matrizFrecuencias.get(j).get(1) == valorInicial) {
                        int var = matrizFrecuencias.get(j).get(2);
                        matrizFrecuencias.get(j).set(2, var);
                        encontrado = true;
                    }
                }

                if (encontrado == false) {
                    List<Integer> l1 = new ArrayList<>();
                    l1.add(0, token);
                    l1.add(1, valorInicial);
                    l1.add(2, 1);
                    matrizFrecuencias.add(l1);
                }

                if (costeCicloPrevio == resultado) {
                    contadorEstancamiento++;
                } else {
                    contadorEstancamiento = 0;
                }
                costeCicloPrevio = resultado;

                //Comprobar estancamiento
                if (contadorEstancamiento == 2000) {
                    System.out.println("Estancado!");
                    pasarMatrizFrecuencias();

                }

                //Cambiar indice lista Tabu si se ha añadido algun valor
                if (tabuActualizado == true) {
                    if(tamalogicolt!=tamaFisicolt){
                    tamalogicolt++;
                    }
                    indicelt++;
                    //Si tamalogico y tamafisico iguales, poner el tamalogico a 0 para que sobreescriba
                    //el valor más antiguo
                    if (indicelt == tamaFisicolt) {
                        indicelt=0;
                    }
                }

                token = (token + 1) % transmisores.size();
            }
        }
    }

    /**
     *
     */
    public int rDiferencia(List<Integer> valores, restricciones rest) throws FileNotFoundException {

        int total = 0;
        for (int i = 0; i < rest.restricciones.size(); i++) {
            int tr1 = rest.restricciones.get(i).get(0);
            int tr2 = rest.restricciones.get(i).get(1);
            int diferencia = rest.restricciones.get(i).get(2);
            int result = rest.restricciones.get(i).get(3);

            if (Math.abs(valores.get(tr1 - 1) - valores.get(tr2 - 1)) > diferencia) {
                total += result;
            }

        }

        return total;
    }

    /**
     * Calcula el resultado del problema a minimizar
     *
     * @param valores Valores de los transmisores
     * @param cambioTransmisor Transmisor al que se le aplico un cambio de
     * frecuencia
     * @param rest Restricciones a evaluar
     * @return
     * @throws FileNotFoundException
     */
    public int rDiferencia(List<Integer> valores, int cambioTransmisor, restricciones rest) throws FileNotFoundException {

        int total = 0;
        for (int i = 0; i < rest.restricciones.size(); i++) {

            int tr1 = rest.restricciones.get(i).get(0);
            int tr2 = rest.restricciones.get(i).get(1);

            if (tr1 == cambioTransmisor || tr2 == cambioTransmisor) {
                int diferencia = rest.restricciones.get(i).get(2);
                int result = rest.restricciones.get(i).get(3);

                if (Math.abs(valores.get(tr1 - 1) - valores.get(tr2 - 1)) > diferencia) {
                    total += result;
                }

            }

        }

        return total;

    }

    /*
   Método para crear una nueva solución en caso de estancamiento
   A cada transmisor le asocio la frecuencia registrada en matrizFrecuencias que tenga el mayor
   número de repeticiones
     */
    public void pasarMatrizFrecuencias() {
        List<Integer> nFrecR = new ArrayList<>();
        int frec;
        int numRepeticiones;
        for (int i = 0; i < transmisores.size(); i++) {
            frec = 0;
            numRepeticiones = 0;
            for (int j = 0; j < matrizFrecuencias.size(); j++) {
                if (matrizFrecuencias.get(j).get(0) == i && matrizFrecuencias.get(j).get(2) > numRepeticiones) {
                    frec = matrizFrecuencias.get(j).get(1);
                    numRepeticiones = matrizFrecuencias.get(j).get(2);
                }
            }
            nFrecR.add(i, frec);
        }

        frecuenciasR = nFrecR;

    }

    public void insertarTabu(int transmisor, int frecuencia) {
        List<Integer> tabu = new ArrayList<>();
        tabu.add(0, transmisor);
        tabu.add(1, frecuencia);

        listaTabu.add(indicelt, tabu);

    }

    public boolean comprobarTabu(int transmisor, int frecuencia) {
        boolean solucion = false;
        for (int i = 0; i < tamalogicolt; i++) {
            if (listaTabu.get(i).get(0) == transmisor && listaTabu.get(i).get(1) == frecuencia) {
                solucion = true;
            }
        }

        return solucion;
    }

    public void resultados() {
        System.out.println("Coste: " + resultado);
        for (int i = 0; i < transmisores.size(); i++) {
            System.out.println("Transmisor " + (i + 1) + ": " + frecuenciasR.get(i));
        }
    }

}
