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
        for ( int x = 0; x < 100; x++ )
        {
            for ( int y = 0; y < 100; y++ )
            {
                System.out.print(nuevo.rangoFrecuencias[x][y]+", ");
            }
            System.out.println();
        }
    }
    
}
