/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author alumno
 */
public class rangoFrec 
{
    int [][] rangoFrecuencias = new int [100][100]; 
    Scanner archivo;
    
    public rangoFrec ( ) throws FileNotFoundException
    {
        int contX = 0;
        File file = new File("conjuntos/graph06/dom.txt");
        archivo = new Scanner (file);
        while ( archivo.hasNextLine() )
        {
            int contY = 0;
            String line = archivo.nextLine();
            Scanner lineScanner = new Scanner (line);
            lineScanner.nextInt();
            while ( lineScanner.hasNext() )
            {
                int token = lineScanner.nextInt();
                rangoFrecuencias[contX][contY++] = token;
            }
            lineScanner.close();
            contX++;
        }
        archivo.close();
    }
}
