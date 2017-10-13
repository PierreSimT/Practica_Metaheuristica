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
    List<Integer> solucion=new ArrayList<>();
   
    int resultado = Integer.MAX_VALUE;

    
    public BusquedaLocal( listaTransmisores _transmisores, rangoFrec _frecuencias ) 
    {
        rangoFrecuencias = _frecuencias.rangoFrecuencias;
        transmisores = _transmisores.transmisores;
        
        /*
        
        Solucion inicial
        
        Recorro la lista de transmisores y le asigno a cada uno una frecuencia aleatoria, que se guarda
        en List<Intger> solucion.
        
        Indice=rangoFrec del transmisor.
        numFrec= el número de frecuencias disponibles asociadas a ese rango. Viene determinada
        por el segundo valor en cada fila de dom.txt
        numero= posicion aleatoria de las que existen en cada fila de dom.txt para asociar una frecuencia
        frec= frecuencia asignada
        
        
        */
        
        
       
        
        for(int i=0;i<_transmisores.transmisores.size();i++){
            int indice=_transmisores.transmisores.get(i);
            int numFrec=_frecuencias.rangoFrecuencias.get(indice).get(1);
            numFrec+=2;
            int numero = (int) Math.floor(Math.random()*numFrec+2);
            int frec=_frecuencias.rangoFrecuencias.get(indice-1).get(numero);
            solucion.add(frec);
        }
     
        
 
        for(int i=0;i<1000;i++){
            
            //Generamos un número aleatorio para calcular el transmisor por el cual vamos a empezar
            
             int numero = (int) Math.floor(Math.random()*_transmisores.transmisores.size());
             
             //Genera un random para calcular el sentido en el que nos movemos a la hora de buscar
             //una nueva frecuencia para el transmisor
             //<0.5 izquierda, >0.5 derecha
             double sentido=Math.random();
             
           
                 int valorInicial=solucion.get(numero);
                 int indiceInicial=0;
                 int frecAsociada=_transmisores.transmisores.get(numero);
                 
                 //Buscamos el indice correspondiente al valor de la frecuencia en el rango de frecuencias total
                 
                 for(int j=0;j<_frecuencias.rangoFrecuencias.get(frecAsociada).size() || valorInicial==0;j++){
               
                     if(valorInicial==_frecuencias.rangoFrecuencias.get(frecAsociada).get(j)){
                         indiceInicial=j;
                         if(j>2 && sentido<0.5){
                             valorInicial=_frecuencias.rangoFrecuencias.get(frecAsociada).get(j--);
                         }
                         else if(j!=_frecuencias.rangoFrecuencias.size()){
                             valorInicial=_frecuencias.rangoFrecuencias.get(frecAsociada).get(j++);
                         }
                     }
                 }
                 
                 
                 //Valor inicial ahora contiene la frecuencia a la izquierda o la derecha de la frecuencia
                 //original de nuestra primera solución
                 
                 
                 List<Integer> nuevaSolucion=new ArrayList<>();
                 nuevaSolucion=solucion;
                 nuevaSolucion.set(numero, valorInicial);
                 
                 //Ahora calculamos el coste de la nuevaSolucion.
                 //Si mejora la establecemos como la solución por defecto
                 //Si no, seguimos buscando.
                 
                 
                 
           
             
                 
            
            
            
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
        
        int result = rDiferencia();
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
    public int rDiferencia() throws FileNotFoundException 
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
                    
                    if ( Math.abs(solucion.get(tr1-1) - solucion.get(tr2-1)) > diferencia )
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

