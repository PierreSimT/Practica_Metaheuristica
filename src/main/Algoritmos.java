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
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *
 * @author pierrest
 */
public class Algoritmos 
{
    public Algoritmos () {};
    
    /**
     * Coge lineas del fichero txt que solo son necesarias para la diferencia
     * @param _fichero fichero ctr.txt
     * @param transmisores vector sobre la frecuencia de transmisores
     * @throws FileNotFoundException si no encuentra el fichero ctr.txt
     */
    public void restricciones (String _fichero, int transmisores[] ) throws FileNotFoundException 
    {
        File fichero = new File ("conjuntos/"+_fichero+"/ctr.txt");
        Scanner lectura = new Scanner (fichero);
        while (lectura.hasNextLine())
        {
            String linea = lectura.nextLine();
            if (linea.matches("(.* .* C . .* .)"))
                System.out.println(linea);
        }
        lectura.close();        
    }
    
    /*
    Algoritmo greedy:
    Asignar un valor al transmisor de forma iterativa e ir calculando uno por uno. Si el resultado mejora
    sustituir la lista de solución
    */
    void greedy ( listaTransmisores l, rangoFrec r )
    {
        int menorInterferencia=999999; //Valor a minimizar. 
        List<Integer> solucion= new ArrayList<>(); //Lista donde almaceno la solución
        
        List<Integer> transmisores = new ArrayList<> (); //lista de transmisores
        transmisores=l.transmisores;
        List<List<Integer>> rangoFrecuencias = new ArrayList<> (); //estructura para guardar los rangos de frecuencias
        rangoFrecuencias=r.rangoFrecuencias;
        
        
        for(int i = 0; i < transmisores.size(); i++)
        {
            for(int j = 0; j < rangoFrecuencias.get(i).size(); j++)
            {
                    
            }
        }      
    }
    
}
