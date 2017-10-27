/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Scanner;

/**
 *
 * @author alumno
 */
public class main {
    
    public static String DIRECTORIO;

    //Variables para el menu
    static Scanner scanner = new Scanner(System.in);
    static int select = -1;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        
        System.out.println("Directorio donde se encuentran los archivos: ");
        Scanner reader = new Scanner(System.in);
        DIRECTORIO = reader.next();

        rangoFrec frecuencias = new rangoFrec();
        listaTransmisores transmisores = new listaTransmisores();
        restricciones rest = new restricciones();   
        
        busquedaTabu2 bt2=new busquedaTabu2(transmisores, frecuencias, rest);
        bt2.algoritmo();
        bt2.resultados();
        
        

//        System.out.println("Directorio donde se encuentran los archivos: ");
//        Scanner reader = new Scanner(System.in);
//        DIRECTORIO = reader.next();
//
//        rangoFrec frecuencias = new rangoFrec();
//        listaTransmisores transmisores = new listaTransmisores();
//        restricciones rest = new restricciones();
//
//        while (select != 0) {
//
//            try {
//                System.out.println("Elige opción:\n1.- Greedy"
//                        + "\n2.- Búsqueda Local\n"
//                        + "3.- Búsqueda Tabú\n"
//                        + "4.- GRASP\n"
//                        + "5.- Cambiar directorio\n"
//                        + "0.- Salir");
//
//                select = Integer.parseInt(scanner.nextLine());
//
//                switch (select) {
//                    case 1:
//                        Greedy greedy = new Greedy(transmisores, frecuencias, rest);
//                        greedy.resultados();
//                        break;
//                    case 2:
//                        BusquedaLocal busquedaLocal = new BusquedaLocal(transmisores, frecuencias, rest);
//                        busquedaLocal.algoritmo();
//                        busquedaLocal.resultados();
//                        
//                        break;
//                    case 3:
//                        BusquedaTabu busquedaTabu = new BusquedaTabu(transmisores, frecuencias, rest);
//                        busquedaTabu.algoritmo();
//                        busquedaTabu.resultados();
//
//                        break;
//                    case 4:
//
//                        break;
//                    case 5:
//
//                        System.out.println("Directorio donde se encuentran los archivos: ");
//
//                        DIRECTORIO = reader.next();
//
//                        frecuencias = new rangoFrec();
//                        transmisores = new listaTransmisores();
//                        rest = new restricciones();
//                    case 0:
//                        System.out.println("Fin");
//                        break;
//                    default:
//                        System.out.println("Número no reconocido");
//                        break;
//                }
//
//                System.out.println("\n"); //Mostrar un salto de línea en Java
//
//            } catch (Exception e) {
//                System.out.println("Uoop! Error!");
//            }
//        }
        /*
        BusquedaLocal busquedaLocal = new BusquedaLocal(transmisores, frecuencias, rest);

        busquedaLocal.algoritmo();
        System.out.println("El coste es: " + busquedaLocal.resultado);
        System.out.println("Las frecuencias asignadas son: ");

        for (int i = 0; i < busquedaLocal.frecuenciasR.size(); i++) {
            System.out.println("Transmisor " + (i + 1) + ": " + busquedaLocal.frecuenciasR.get(i));
        }
*/
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
