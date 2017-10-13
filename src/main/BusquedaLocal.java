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
    List<List<Integer>> rangoFrecuencias = new ArrayList<> ();
    List<Integer> transmisores = new ArrayList<> ();
    List<Integer> frecuenciasR = new ArrayList<> ();
    int resultado;
    
    public BusquedaLocal( listaTransmisores _transmisores, rangoFrec _frecuencias ) 
    {
        rangoFrecuencias = _frecuencias.rangoFrecuencias;
        transmisores = _transmisores.transmisores;
        Random numero = new Random ();
        resultado = Integer.MAX_VALUE;
        for ( int i = 0; i < transmisores.size(); i++ )
        {
            frecuenciasR.add(rangoFrecuencias.get(transmisores.get(i)).get(numero.nextInt(rangoFrecuencias.get(transmisores.get(i)).size())));
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
        int menorInterferencia=999999; //Valor a minimizar. 
        
        int result = rDiferencia(frecuenciasR, 1);
        if ( resultado > result )
            resultado = result;
        
        System.out.print(resultado);
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
            if (linea.matches("(.* .* C . .* .)")) 
            {
                System.out.println(linea);
                Scanner sLinea = new Scanner (linea);
                while ( sLinea.hasNextInt() )
                {
                    int tr1 = sLinea.nextInt();
                    int tr2 = sLinea.nextInt();
                    sLinea.next(); sLinea.next();
                    int diferencia = sLinea.nextInt();
                    int result = sLinea.nextInt();
                    
                    if ( Math.abs(valores.get(transmisores.get(tr1-1)) - valores.get(transmisores.get(tr2-1))) > diferencia )
                    {
                        total += result;
                    }
                }
                sLinea.close();
            }
        }
        lectura.close();
        
        return total;
    }
    
    public int rDiferencia( List<Integer> valores, int cambio) throws FileNotFoundException 
    {
        File fichero = new File("conjuntos/" + main.DIRECTORIO + "/ctr.txt");
        Scanner lectura = new Scanner(fichero);
        int total = 0;
        while (lectura.hasNextLine()) 
        {
            String linea = lectura.nextLine();
            if (linea.matches("((\\s*"+cambio+")(.*)(C . .* .))") || linea.matches("((.*)(\\s*"+cambio+")(C . .* .))")) 
            {
                System.out.println(linea);
                Scanner sLinea = new Scanner (linea);
                while ( sLinea.hasNextInt() )
                {
                    int tr1 = sLinea.nextInt();
                    int tr2 = sLinea.nextInt();
                    sLinea.next(); sLinea.next();
                    int diferencia = sLinea.nextInt();
                    int result = sLinea.nextInt();
                    
                    if ( Math.abs(valores.get(transmisores.get(tr1-1)) - valores.get(transmisores.get(tr2-1))) > diferencia )
                    {
                        total += result;
                    }
                }
                sLinea.close();
            }
        }
        lectura.close();
        
        return total;
    }
    
}
