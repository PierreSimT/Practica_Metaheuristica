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
 * @author ptondreau
 */
public class Grasp
{
    private static int K = 10;
    
    private List<List<Integer>> frecuencias = new ArrayList<> ();
    private List<Integer> transmisores = new ArrayList<>();
    private List<Integer> listaRestringida = new ArrayList<> ();
    private List<Integer> frecuenciasR = new ArrayList<> (400);
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
    public Grasp ( listaTransmisores _transmisores, rangoFrec _frecuencias, Restricciones _restricciones ) {
        frecuencias = _frecuencias.rangoFrecuencias;
        transmisores = _transmisores.transmisores;
        restricciones = _restricciones;
        Random numero = new Random ();
        resultado = Integer.MAX_VALUE;
        for ( int i = 0; i < K; i++ ) {
            int transmisor = numero.nextInt (transmisores.size ());
            int tama = frecuencias.get(transmisores.get (transmisor)).size();
            int frecuenciaAsig = frecuencias.get(transmisores.get(transmisor)).get(numero.nextInt(tama));           
            listaRestringida.add (transmisor);
            
        }
        solucionInicial ();
    }
    
    private void solucionInicial () {
        
        int transmisor = 0;
        int frecuencia;
        boolean fin = false;
        while ( !fin ) {
            int pos = 0;
            boolean encontrado = false;
            if ( !listaRestringida.contains(transmisor) ) {
                while ( pos < frecuencias.get (transmisores.get (transmisor)).size () &&  ! encontrado ) {
                     
                }
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
    
    private int calculaCoste () {
        return 0;
    }
}
