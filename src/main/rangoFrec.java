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
import java.util.Random;

/**
 *
 * @author alumno
 */
public class rangoFrec 
{
    List<List<Integer>> rangoFrecuencias = new ArrayList<> (); 
    
    
    public rangoFrec () throws FileNotFoundException
    {
        int contX = 0;
        File file = new File("conjuntos/"+main.DIRECTORIO+"/dom.txt");
        Scanner archivo = new Scanner (file);
        while ( archivo.hasNextLine() )
        {
            int contY = 0;
            rangoFrecuencias.add(new ArrayList<Integer>());
            String line = archivo.nextLine();
            Scanner lineScanner = new Scanner (line);
            lineScanner.nextInt();
            while ( lineScanner.hasNext() )
            {
                int token = lineScanner.nextInt();
                rangoFrecuencias.get(contX).add(token);
            }
            lineScanner.close();
            contX++;
        }
        archivo.close();
    }
    
}
