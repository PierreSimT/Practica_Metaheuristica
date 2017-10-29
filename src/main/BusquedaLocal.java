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
public class BusquedaLocal {

    List<List<Integer>> frecuencias = new ArrayList<>();
    List<Integer> transmisores = new ArrayList<>();
    List<Integer> frecuenciasR = new ArrayList<>(); // Cada posicion es la frecuencia asignada a dicho transmisor
    Restricciones rest;
    int resultado;

    public BusquedaLocal(listaTransmisores _transmisores, rangoFrec _frecuencias, Restricciones _restricciones) {
        frecuencias = _frecuencias.rangoFrecuencias;
        transmisores = _transmisores.transmisores;
        rest = _restricciones;
        Random numero = new Random();
        numero.setSeed(3181827);
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
            int token = numero.nextInt(transmisores.size());
            for (int i = 0; i < 10000; i++) {
                double sentido = numero.nextDouble();
                int valorInicial = frecuenciasR.get(token); // Se obtiene la frecuencia del token
                int indiceInicial;
                int nuevoCoste = Integer.MAX_VALUE;

                indiceInicial = frecuencias.get(transmisores.get(token)).indexOf(valorInicial); // Mas corto que codigo de abajo

                if (sentido < 0.5) {
                    boolean encontrado = false;
                    while (indiceInicial >= 0 && !encontrado) {
                        int fact1 = rDiferencia(frecuenciasR, token, rest);
                        valorInicial = frecuencias.get(transmisores.get(token)).get(indiceInicial);
                        List<Integer> nuevaSolucion = new ArrayList<>();
                        nuevaSolucion.addAll(frecuenciasR);
                        nuevaSolucion.set(token, valorInicial);
                        int fact2 = rDiferencia(nuevaSolucion, token, rest);
                        nuevoCoste = resultado + (fact2 - fact1);

                        if (nuevoCoste < resultado) {
                            frecuenciasR.set(token, valorInicial);
                            resultado = nuevoCoste;
                            encontrado = true;
                        }
                        indiceInicial--;
                    }
                } else {
                    boolean encontrado = false;
                    while (indiceInicial < frecuencias.get(transmisores.get(token)).size() && !encontrado) {
                        int fact1 = rDiferencia(frecuenciasR, token, rest);
                        valorInicial = frecuencias.get(transmisores.get(token)).get(indiceInicial);
                        List<Integer> nuevaSolucion = new ArrayList<>();
                        nuevaSolucion.addAll(frecuenciasR);
                        nuevaSolucion.set(token, valorInicial);
                        int fact2 = rDiferencia(nuevaSolucion, token, rest);
                        nuevoCoste = resultado + (fact2 - fact1);

                        if (nuevoCoste < resultado) {
                            frecuenciasR.set(token, valorInicial);
                            resultado = nuevoCoste;
                            encontrado = true;
                        }
                        indiceInicial++;
                    }
                }
                token = Math.floorMod(token + 1, transmisores.size());
            }
        }
    }

    /**
     *
     */
    public int rDiferencia(List<Integer> valores, Restricciones rest) throws FileNotFoundException {

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
    public int rDiferencia(List<Integer> valores, int cambioTransmisor, Restricciones rest) throws FileNotFoundException {

        List<List<Integer>> listaRest = new ArrayList<>();
        listaRest = rest.restriccionesTransmisor(cambioTransmisor);

        int total = 0;
        for (int i = 0; i < listaRest.size(); i++) {

            int tr1 = listaRest.get(i).get(0);
            int tr2 = listaRest.get(i).get(1);

            if (tr1 == cambioTransmisor+1 || tr2 == cambioTransmisor+1 ) {
                int diferencia = listaRest.get(i).get(2);
                int result = listaRest.get(i).get(3);

                if (Math.abs(valores.get(tr1 - 1) - valores.get(tr2 - 1)) > diferencia) {
                    total += result;
                }

            }

        }

        return total;

    }

    public void resultados() {
        
        for (int i = 0; i < transmisores.size(); i++) {
            System.out.println("Transmisor " + (i + 1) + ": " + frecuenciasR.get(i));
        }
        System.out.println("Coste: " + resultado);
    }

}
