/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Pierre Simon
 */
public class BusquedaTabu 
{
    List<List<Integer>> frecuencias = new ArrayList<> ();
    List<Integer> transmisores = new ArrayList<> ();
    List<Integer> frecuenciasR = new ArrayList<> (); // Cada posicion es la frecuencia asignada a dicho transmisor
    List<Integer> listaTabu = new ArrayList<> ();
    int resultado;    
    
    public BusquedaTabu ( listaTransmisores _transmisores, rangoFrec _frecuencias ) 
    {
        frecuencias = _frecuencias.rangoFrecuencias;
        transmisores = _transmisores.transmisores;
        Random numero = new Random ();
        resultado = Integer.MAX_VALUE;
        for ( int i = 0; i < transmisores.size(); i++ )
        {
            frecuenciasR.add(frecuencias.get(transmisores.get(i)).get(numero.nextInt(frecuencias.get(transmisores.get(i)).size())));
        }
    }
    
    
    
}
