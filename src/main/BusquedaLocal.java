/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author pierrest
 */

public class BusquedaLocal 
{
    List<List<Integer>> frecuencias = new ArrayList<> ();
    List<Integer> transmisores = new ArrayList<> ();
    List<Integer> frecuenciasR = new ArrayList<> (); // Cada posicion es la frecuencia asignada a dicho transmisor
    int resultado;
    
    public BusquedaLocal( listaTransmisores _transmisores, rangoFrec _frecuencias ) 
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

    /**
     * Algoritmo greedy:
     * Asignar un valor al transmisor de forma iterativa e ir calculando uno por uno. Si el resultado mejora
     * sustituir la lista de solución
     * @param l 
     * @param r 
     */
    public void algoritmo( ) throws FileNotFoundException
    {
        if ( resultado == Integer.MAX_VALUE ) 
        {
            int result = rDiferencia(frecuenciasR); // Da lugar a la solucion inicial
            if ( resultado > result )
                resultado = result;
            algoritmo ();
        } else {
            Random numero = new Random ();
            for ( int i = 0; i < 1000; i++ ) 
            {
                int token = numero.nextInt(transmisores.size());
                double sentido = numero.nextDouble();
                int valorInicial = frecuenciasR.get(token); // Se obtiene la frecuencia del token
                int indiceInicial;
                int nuevoCoste = Integer.MAX_VALUE;
                
                indiceInicial = frecuencias.get(transmisores.get(token)).indexOf(valorInicial); // Mas corto que codigo de abajo
                
                if ( sentido < 0.5 ) 
                {
                    boolean encontrado = false;
                    while ( indiceInicial >= 0 && !encontrado ) 
                    {
                        int fact1 = rDiferencia(frecuenciasR, token); 
                        valorInicial = frecuencias.get(transmisores.get(token)).get(indiceInicial);
                        List<Integer> nuevaSolucion = new ArrayList <> ();
                        nuevaSolucion.addAll(frecuenciasR);
                        nuevaSolucion.set(token, valorInicial);
                        int fact2 = rDiferencia(nuevaSolucion, token);
                        nuevoCoste = resultado - fact1 + fact2;
                        
                        if ( nuevoCoste < resultado ) {
                            frecuenciasR = nuevaSolucion;
                            resultado = nuevoCoste;
                            encontrado = true;
                        }
                        indiceInicial--;
                    }
                } else {
                    boolean encontrado = false;
                    while ( indiceInicial < frecuencias.get(transmisores.get(token)).size() && !encontrado ) 
                    {
                        int fact1 = rDiferencia(frecuenciasR, token);                       
                        valorInicial = frecuencias.get(transmisores.get(token)).get(indiceInicial);
                        List<Integer> nuevaSolucion = new ArrayList <> ();
                        nuevaSolucion.addAll(frecuenciasR);
                        nuevaSolucion.set(token, valorInicial);
                        int fact2 = rDiferencia(nuevaSolucion, token);
                        nuevoCoste = resultado - fact1 + fact2;
                        
                        
                        if ( nuevoCoste < resultado ) {
                            frecuenciasR = nuevaSolucion;
                            resultado = nuevoCoste;
                            encontrado = true;
                        }
                        indiceInicial++;
                    }
                }
                System.out.println( i+" : Resultado actual: "+resultado);
                token = (token+1)%transmisores.size();
            }
        }
    }
    
    /**
     * Lee el fichero ctr.txt y para sacar las diferencias que se han de 
     * realizar
     * @param _fichero
     * @throws FileNotFoundException 
     */
    public int rDiferencia( List<Integer> valores ) throws FileNotFoundException 
    {
        File fichero = new File("conjuntos/" + main.DIRECTORIO + "/ctr.txt");
        Scanner lectura = new Scanner(fichero);
        int total = 0;
        while (lectura.hasNextLine()) 
        {
            String linea = lectura.nextLine();
            if (linea.matches("(.*C.*)")) 
            {
//                System.out.println(linea);
                Scanner sLinea = new Scanner (linea);
                while ( sLinea.hasNextInt() )
                {
                    int tr1 = sLinea.nextInt();
                    int tr2 = sLinea.nextInt();
                    sLinea.next(); sLinea.next();
                    int diferencia = sLinea.nextInt();
                    int result = sLinea.nextInt();
                    
                    if ( Math.abs(valores.get(tr1-1)) - valores.get(tr2-1) > diferencia )
                        total += result;
                }
                sLinea.close();
            }
        }
        lectura.close();
        
        return total;
    }
    
    /**
     * Calcula el resultado del problema a minimizar
     * @param valores Valores de los transmisores
     * @param cambioTransmisor Transmisor al que se le aplico un cambio de frecuencia
     * @return
     * @throws FileNotFoundException 
     */
    public int rDiferencia( List<Integer> valores, int cambioTransmisor) throws FileNotFoundException 
    {
        File fichero = new File("conjuntos/" + main.DIRECTORIO + "/ctr.txt");
        Scanner lectura = new Scanner(fichero);
        int total = 0;
        while (lectura.hasNextLine()) 
        {
            String linea = lectura.nextLine();
            if (linea.matches("(\\s*"+cambioTransmisor+"\\s+.*C.*)") || linea.matches("(.*\\s+"+cambioTransmisor+"\\s+.*C.*)")) 
            {
//                System.out.println(linea);
                Scanner sLinea = new Scanner (linea);
                while ( sLinea.hasNextInt() )
                {
                    int tr1 = sLinea.nextInt();
                    int tr2 = sLinea.nextInt();
                    sLinea.next(); sLinea.next();
                    int diferencia = sLinea.nextInt();
                    int result = sLinea.nextInt();
                    
                    if ( Math.abs(valores.get(tr1-1) - valores.get(tr2-1)) < diferencia )
                        total += result;
                }
                sLinea.close();
            }
        }
        lectura.close();
        
        return total;
    }
    
}
