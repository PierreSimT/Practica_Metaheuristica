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

/**
 *
 * @author pierre
 */
public class listaTransmisores 
{
    List<Integer> transmisores = new ArrayList<> ();
    
    public listaTransmisores() throws FileNotFoundException 
    {
        int contX = 0;
        File file = new File (main.TRABAJO+"/conjuntos/"+main.DIRECTORIO+"/var.txt");
        Scanner archivo = new Scanner (file);
        while ( archivo.hasNextLine() )
        {
            contX = 0;
            String line = archivo.nextLine();
            Scanner linea = new Scanner(line);
            linea.nextInt();
            while ( linea.hasNext() && contX < 1 )
            {
                int token = linea.nextInt();
                transmisores.add(token);
                contX++;
            }  
            linea.close();
        }
        archivo.close();
    }
}
