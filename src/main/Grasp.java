/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author ptondreau
 */
public class Grasp
{
    private static int K = 10;
    
    private List<List<Integer>> frecuencias = new ArrayList<> ();
    private List<Integer> transmisores = new ArrayList<>();
    private List<Integer> listaRestringida = new ArrayList<> ();
    private List<Integer> frecuenciasR = new ArrayList<>();
    private double vectorCostes [];
    private Restricciones restricciones;
    private int resultado;
    
    /**
     * Inicialmente elegimos K transmisores aleatoriamente y se le asignan 
     * frecuencias también aleatorias.
     * @param _transmisores Lista de transmisores con su rango
     * @param _frecuencias Lista de frecuencias de cada rango
     * @param _restricciones Las restricciones que debe satisfacer la solucion
     */
    public Grasp ( listaTransmisores _transmisores, rangoFrec _frecuencias, Restricciones _restricciones ) throws FileNotFoundException {
        frecuencias = _frecuencias.rangoFrecuencias;
        transmisores = _transmisores.transmisores;
        restricciones = _restricciones;
        Random numero = new Random ();
        resultado = Integer.MAX_VALUE;
        for ( int i = 0; i < 400; i++ ) {
            frecuenciasR.add(0);
        }
        
        for ( int i = 0; i < K; i++ ) {
            int transmisor = numero.nextInt (transmisores.size ());
            int tama = frecuencias.get(transmisores.get (transmisor)).size();
            int frecuenciaAsig = frecuencias.get(transmisores.get(transmisor)).get(numero.nextInt(tama));           
            listaRestringida.add (transmisor);
            frecuenciasR.set(transmisor, frecuenciaAsig);
        }
        solucionInicial ();
        resultado = rDiferencia(frecuenciasR, restricciones);
    }
    
    private void solucionInicial () throws FileNotFoundException {
        Random numero = new Random ();
        int transmisor = 0;
        boolean fin = false;
        while ( transmisor < 400 ) {
        
            if ( !listaRestringida.contains(transmisor) ) {
                
                int minimo = Integer.MAX_VALUE;
                boolean encontrado = false;
                int frecuenciaR = 0;
                int frecuencia;
                int pos = 0;
    
                while ( pos < frecuencias.get(transmisores.get(transmisor)).size () &&  ! encontrado ) {
                    
                    int valor;
                    
                    List<Integer> nuevaLista = new ArrayList<>();
                    nuevaLista.addAll(frecuenciasR);
                    
                    frecuencia = frecuencias.get(transmisores.get(transmisor)).get(pos++);
                    nuevaLista.set(transmisor, frecuencia);
                    List<List<Integer>> listaRest = compruebaTransmisores(transmisor);
                    
                    if ( listaRest.size() > 0 ) { // Lista no vacía, se selecciona frecuencia que afecte lo menos posible al resultado
                        
                        valor = rDiferencia(nuevaLista, listaRest);
                        if ( valor < minimo ) {
                            minimo = valor;
                            frecuenciaR = frecuencia;
                            if ( valor == 0 ) // Si la suma de todas las restricciones = 0 entonces es el mejor resultado posible
                                encontrado = true;
                        }
                    } else { // En caso de que la lista este vacía no hay restricciones que se puedan satisfacer -> frecuencia aleatoria
                        
                        int tamanio = frecuencias.get(transmisores.get(transmisor)).size();
                        frecuenciaR = frecuencias.get(transmisores.get(transmisor)).get(numero.nextInt(tamanio));
                        encontrado = true;
                    }
                }
                frecuenciasR.set(transmisor, frecuenciaR);
                System.out.println(transmisor+" : "+frecuenciaR);
            }
            transmisor++;           
        }
    }
    
    /**
     * Funcion que devuelve una lista con las restricciones que puede satisfacer
     * un transmisor
     * @param transmisor
     * @return
     * @throws FileNotFoundException 
     */
    private List<List<Integer>> compruebaTransmisores ( int transmisor ) throws FileNotFoundException {
        int contador = 0;
        List<List<Integer>> listaRest = new ArrayList<>();
        List<List<Integer>> listaT = restricciones.restriccionesTransmisor(transmisor);
        for ( int i = 0; i < listaT.size()-1; i++ )
        {
            if ( frecuenciasR.get(listaT.get(i).get(0)) != 0 || frecuenciasR.get(listaT.get(i).get(1)) != 0 ) {
                    listaRest.add(new LinkedList<>() );
                    listaRest.get(contador++).addAll(listaT.get(i));
            }
        }
        
        return listaRest;
    }
    
    private int rDiferencia ( List<Integer> valores, List<List<Integer>> rest ) {
        int total = 0;
        for ( int i = 0; i < rest.size (); i ++ )
        {
            int tr1 = rest.get(i).get (0);
            int tr2 = rest.get(i).get (1);
            int diferencia = rest.get(i).get (2);
            int result = rest.get(i).get (3);

            if ( Math.abs (valores.get (tr1 - 1) - valores.get (tr2 - 1)) > diferencia )
            {
                total += result;
            }

        }

        return total;
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
