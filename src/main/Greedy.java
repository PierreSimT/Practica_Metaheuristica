/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import static main.main.NUMERO;

/**
 *
 * @author juan
 */
public class Greedy {

    List<List<Integer>> frecuencias = new ArrayList<>();
    List<Integer> transmisores = new ArrayList<>();
    List<Integer> frecuenciasR = new ArrayList<>(); // Cada posicion es la frecuencia asignada a dicho transmisor
    Restricciones rest;
    int resultado;

    public Greedy ( listaTransmisores _transmisores, rangoFrec _frecuencias, Restricciones _restricciones ) throws FileNotFoundException {
        frecuencias = _frecuencias.rangoFrecuencias;
        transmisores = _transmisores.transmisores;
        rest = _restricciones;

        for ( int i = 0; i < transmisores.size(); i ++ ) {
            frecuenciasR.add(frecuencias.get(transmisores.get(i)).get(NUMERO.nextInt(frecuencias.get(transmisores.get(i)).size())));
        }

        resultado = rDiferencia(frecuenciasR, rest);
    }

    public int rDiferencia ( List<Integer> valores, Restricciones rest ) throws FileNotFoundException {

        int total = 0;
        for ( int i = 0; i < rest.restricciones.size(); i ++ ) {
            int tr1 = rest.restricciones.get(i).get(0);
            int tr2 = rest.restricciones.get(i).get(1);
            int diferencia = rest.restricciones.get(i).get(2);
            int result = rest.restricciones.get(i).get(3);

            if ( Math.abs(valores.get(tr1 - 1) - valores.get(tr2 - 1)) > diferencia ) {
                total += result;
            }

        }

        return total;
    }

    public void resultados () {

        List<List<Integer>> listaTrans = new ArrayList<>();
        for ( int i = 0; i < transmisores.size(); i ++ ) {
            listaTrans = rest.restriccionesTransmisor(i);
            if ( listaTrans.size() > 0 ) {
                System.out.println("Transmisor " + (i + 1) + ": " + frecuenciasR.get(i));
            }
        }
        System.out.println("Coste: " + resultado);
    }

}
