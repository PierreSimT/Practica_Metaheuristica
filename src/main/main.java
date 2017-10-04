/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author alumno
 */
public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException 
    {
        // Pide al usuario el directorio donde se encuentran los archivos a leer
        System.out.println("Directorio donde se encuentran los archivos: ");
        Scanner reader = new Scanner(System.in);
        String directorio = reader.next();
        
        rangoFrec frecuencias = new rangoFrec(directorio);
        listaTransmisores transmisores = new listaTransmisores(directorio);
        
//        for ( int x = 0; x < nuevo.rangoFrecuencias.size(); x++ )
//        {
//            System.out.print("[");
//            for ( int y = 0; y < nuevo.rangoFrecuencias.get(x).size(); y++ )
//            {
//                System.out.print(nuevo.rangoFrecuencias.get(x).get(y)+", ");
//            }
//            System.out.print("]");
//            System.out.println();
//        }
    }
    
}
