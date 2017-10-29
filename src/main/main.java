/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author alumno
 */
public class main {

    public static String DIRECTORIO;
    public static Integer LINEAS;
    public static Random NUMERO;
    
    //Variables para el menu
    static Scanner scanner = new Scanner(System.in);
    static int select = -1;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        NUMERO = new Random ();
        NUMERO.setSeed(3181827);
        
        System.out.println("Directorio donde se encuentran los archivos: ");
        Scanner reader = new Scanner(System.in);
        DIRECTORIO = reader.next();
        LINEAS = countLines(DIRECTORIO)+1;
                
        rangoFrec frecuencias = new rangoFrec();
        listaTransmisores transmisores = new listaTransmisores();
        Restricciones rest = new Restricciones();

        while (select != 0) {

//            try {
                System.out.print("Elige opción:\n1.- Greedy"
                        + "\n2.- Búsqueda Local\n"
                        + "3.- Búsqueda Tabúr\n"
                        + "4.- GRASP\n"
                        + "5.- Cambiar directorio\n"
                        + "0.- Salir"
                        + "\n: ");

                select = Integer.parseInt(scanner.nextLine());

                switch (select) {
                    case 1:
                        Greedy greedy = new Greedy(transmisores, frecuencias, rest);
                        greedy.resultados();
                        break;
                    case 2:
                        BusquedaLocal busquedaLocal = new BusquedaLocal(transmisores, frecuencias, rest);
                        busquedaLocal.algoritmo();
                        busquedaLocal.resultados();
                        break;
                    case 3:
                        BusquedaTabu busquedaTabu = new BusquedaTabu(transmisores, frecuencias, rest);
                        busquedaTabu.algoritmo();
                        busquedaTabu.resultados();
                        break;
                    case 4:
                        Grasp grasp = new Grasp(transmisores, frecuencias, rest);
                        grasp.resultados();
                        break;
                    case 5:

                        System.out.println("Directorio donde se encuentran los archivos: ");

                        DIRECTORIO = reader.next();
                        LINEAS = countLines(DIRECTORIO)+1;
                        
                        frecuencias = new rangoFrec();
                        transmisores = new listaTransmisores();
                        rest = new Restricciones();
                    case 0:
                        System.out.println("Fin");
                        break;
                    default:
                        System.out.println("Número no reconocido");
                        break;
                }

                System.out.println("\n"); //Mostrar un salto de línea en Java

//            } catch (Exception e) {
//                System.out.println("Uoop! Error! " + e.toString());
//            }
        }
    }
    
    public static int countLines(String filename) throws IOException {
    InputStream is = new BufferedInputStream(new FileInputStream("conjuntos/"+filename+"/var.txt"));
    try {
        byte[] c = new byte[1024];
        int count = 0;
        int readChars = 0;
        boolean empty = true;
        while ((readChars = is.read(c)) != -1) {
            empty = false;
            for (int i = 0; i < readChars; ++i) {
                if (c[i] == '\n') {
                    ++count;
                }
            }
        }
        return (count == 0 && !empty) ? 1 : count;
    } finally {
        is.close();
    }
}

}
