/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.FileNotFoundException;
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
    Restricciones restricciones;
    int resultado;    
    
    public BusquedaTabu ( listaTransmisores _transmisores, rangoFrec _frecuencias, Restricciones _restricciones ) 
    {
        frecuencias = _frecuencias.rangoFrecuencias;
        transmisores = _transmisores.transmisores;
        restricciones = _restricciones;
        resultado = Integer.MAX_VALUE;
        Random numero = new Random ();
        for ( int i = 0; i < transmisores.size(); i++ )
        {
            frecuenciasR.add(frecuencias.get(transmisores.get(i)).get(numero.nextInt(frecuencias.get(transmisores.get(i)).size())));
            listaTabu.add(frecuenciasR.get(i)); // Añade en cada posicion la frecuencia asignada a TXi
        }
    }
    
    public void algoritmo () throws FileNotFoundException
    {
        if ( resultado == Integer.MAX_VALUE )
        {
            int result = rDiferencia ( frecuenciasR, restricciones );
            if ( resultado > result )
                resultado = result;
            algoritmo();
        } else {
            
            Random numero = new Random ();
            int token = numero.nextInt (transmisores.size ());
            for ( int i = 0; i < 10; i ++ )
            {
                double sentido = numero.nextDouble ();
                int valorInicial = frecuenciasR.get (token); // Se obtiene la frecuencia del token
                int indiceInicial;
                int nuevoCoste = Integer.MAX_VALUE;

                indiceInicial = frecuencias.get (transmisores.get (token)).indexOf (valorInicial); // Obtiene posicion de la frecuencia asignada al transmisor

                if ( sentido < 0.5 )
                {
                    boolean fin = false;
                    int iteraciones = 0;
                    List<Integer> solucionTabu = new ArrayList<> ();
                    int fact1 = rDiferencia (frecuenciasR, token, restricciones);
                    int minimo = resultado;
                    while ( iteraciones < 20 && !fin )
                    {
                        int posicionesPos = frecuencias.get (transmisores.get(token)).size();
                        indiceInicial = Math.floorMod (indiceInicial-1, frecuencias.get (transmisores.get(token)).size());
                        valorInicial = frecuencias.get (transmisores.get (token)).get (indiceInicial);
                        List<Integer> nuevaSolucion = new ArrayList<> ();
                        nuevaSolucion.addAll (frecuenciasR);
                        nuevaSolucion.set (token, valorInicial);
                        int fact2 = rDiferencia (nuevaSolucion, token, restricciones);
                        nuevoCoste = resultado - fact1 + fact2;

                        if ( nuevoCoste < minimo )
                        {   
                            minimo = nuevoCoste;
                            solucionTabu = nuevaSolucion;
                        }
                        if ( posicionesPos == 0 )
                            fin = true;
                        posicionesPos--;
                        iteraciones++;
                    }
                    
                    if ( minimo < resultado ) 
                    {
                        frecuenciasR = solucionTabu;
                        resultado = minimo;
                    }
                    
                } else
                {
                    boolean fin = false;
                    int iteraciones = 0;
                    List<Integer> solucionTabu = new ArrayList<> ();
                    int fact1 = rDiferencia (frecuenciasR, token, restricciones);
                    int minimo = resultado;
                    while ( iteraciones < 20 && !fin )
                    {
                        int posicionesPos = frecuencias.get (transmisores.get(token)).size();
                        indiceInicial = Math.floorMod (indiceInicial+1, frecuencias.get (transmisores.get(token)).size());
                        valorInicial = frecuencias.get (transmisores.get (token)).get (indiceInicial);
                        List<Integer> nuevaSolucion = new ArrayList<> ();
                        nuevaSolucion.addAll (frecuenciasR);
                        nuevaSolucion.set (token, valorInicial);
                        int fact2 = rDiferencia (nuevaSolucion, token, restricciones);
                        nuevoCoste = resultado - fact1 + fact2;

                        if ( nuevoCoste < minimo )
                        {   
                            minimo = nuevoCoste;
                            solucionTabu = nuevaSolucion;
                        }
                        if ( posicionesPos == 0 )
                            fin = true;
                        posicionesPos--;
                        iteraciones++;
                    }
                    
                    if ( minimo < resultado ) 
                    {
                        frecuenciasR = solucionTabu;
                        resultado = minimo;
                    }
                }
                token = (token + 1) % transmisores.size ();
            }           
        }
    }
    
    public int rDiferencia ( List<Integer> valores, Restricciones rest ) throws FileNotFoundException
    {

        int total = 0;
        for ( int i = 0; i < rest.restricciones.size (); i ++ )
        {
            int tr1 = rest.restricciones.get (i).get (0);
            int tr2 = rest.restricciones.get (i).get (1);
            int diferencia = rest.restricciones.get (i).get (2);
            int result = rest.restricciones.get (i).get (3);

            if ( Math.abs (valores.get (tr1 - 1) - valores.get (tr2 - 1)) > diferencia )
            {
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
    public int rDiferencia ( List<Integer> valores, int cambioTransmisor, Restricciones rest ) throws FileNotFoundException
    {

        int total = 0;
        for ( int i = 0; i < rest.restricciones.size (); i ++ )
        {

            int tr1 = rest.restricciones.get (i).get (0);
            int tr2 = rest.restricciones.get (i).get (1);

            if ( tr1 == cambioTransmisor || tr2 == cambioTransmisor )
            {
                int diferencia = rest.restricciones.get (i).get (2);
                int result = rest.restricciones.get (i).get (3);

                if ( Math.abs (valores.get (tr1 - 1) - valores.get (tr2 - 1)) > diferencia )
                {
                    total += result;
                }

            }

        }

        return total;

    }

    public void resultados ()
    {
        System.out.println ("Coste: " + resultado);
        for ( int i = 0; i < transmisores.size (); i ++ )
        {
            System.out.println ("Transmisor " + (i + 1) + ": " + frecuenciasR.get (i));
        }
    }

}
