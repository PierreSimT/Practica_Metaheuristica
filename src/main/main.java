/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.FileNotFoundException;

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
        // TODO code application logic here
        rangoFrec nuevo = new rangoFrec();
        for ( int x = 0; x < nuevo.rangoFrecuencias.size(); x++ )
        {
            System.out.print("[");
            for ( int y = 0; y < nuevo.rangoFrecuencias.get(x).size(); y++ )
            {
                System.out.print(nuevo.rangoFrecuencias.get(x).get(y)+", ");
            }
            System.out.print("]");
            System.out.println();
        }
    }
    
}
