/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;
import java.util.*;


/**
 *
 * @author juan
 */
public class algoritmos {
    
    
    /*
    Algoritmo greedy:
    Asignar un valor al transmisor de forma iterativa e ir calculando uno por uno. Si el resultado mejora
    sustituir la lista de solución
    */
    void greedy(listaTransmisores l, rangoFrec r){
        int menorInterferencia=999999; //Valor a minimizar. 
        List<Integer> solucion= new ArrayList<>(); //Lista donde almaceno la solución
        
        List<Integer> transmisores = new ArrayList<> (); //lista de transmisores
        transmisores=l.transmisores;
        List<List<Integer>> rangoFrecuencias = new ArrayList<> (); //estructura para guardar los rangos de frecuencias
        rangoFrecuencias=r.rangoFrecuencias;
        
        
        for(int i=0;i<transmisores.size();i++){
            for(int j=0;j<rangoFrecuencias.get(i).size();j++){
                
                
                
            }
        }
        
        
        
        
       
        
        
        
        
        
        
        
        
        
        
        
    }
    
}
