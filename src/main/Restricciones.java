/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

/**
 *
 * @author juan
 */
public class Restricciones {

    LinkedList<LinkedList<Integer>> restricciones = new LinkedList<>();

    public Restricciones() throws FileNotFoundException {
        int contador = 0;
        File fichero = new File("conjuntos/" + main.DIRECTORIO + "/ctr.txt");
        Scanner lectura = new Scanner(fichero);

        while (lectura.hasNextLine()) {
            String linea = lectura.nextLine();
            if (linea.matches("(.*C.*)")) {
//                System.out.println(linea);
                Scanner sLinea = new Scanner(linea);
                while (sLinea.hasNextInt()) {

                    int tr1 = sLinea.nextInt();
                    int tr2 = sLinea.nextInt();
                    sLinea.next();
                    sLinea.next();
                    int diferencia = sLinea.nextInt();
                    int result = sLinea.nextInt();
                    
                    LinkedList<Integer> datos=new LinkedList<>();
                    datos.add(0,tr1);
                    datos.add(1,tr2);
                    datos.add(2,diferencia);
                    datos.add(3,result);
                    
                    restricciones.add(contador, datos);
                    contador++;
                }
                sLinea.close();
                

            }
        }
        lectura.close();

    }

    public void leerResultados() {
        for (int i = 0; i < restricciones.size(); i++) {
           System.out.println(restricciones.get(i).get(0)+" "+restricciones.get(i).get(1)+" "+
                   restricciones.get(i).get(2)+" "+restricciones.get(i).get(3));
            }
        }
    }

