/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *
 * @author pierrest
 */
public class Algoritmos 
{
    public Algoritmos () {};
    
    public void algGreedy (String _fichero, int transmisores[] ) throws FileNotFoundException 
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
}
