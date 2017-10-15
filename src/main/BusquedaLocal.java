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
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author pierrest
 */
public class BusquedaLocal {

    List<List<Integer>> rangoFrecuencias = new ArrayList<>();
    List<Integer> transmisores = new ArrayList<>();
    List<Integer> solucion = new ArrayList<>();

    int resultado = Integer.MAX_VALUE;

    public BusquedaLocal(listaTransmisores _transmisores, rangoFrec _frecuencias) throws FileNotFoundException {
        rangoFrecuencias = _frecuencias.rangoFrecuencias;
        transmisores = _transmisores.transmisores;

        /*
        
        Solucion inicial
        
        Recorro la lista de transmisores y le asigno a cada uno una frecuencia aleatoria, que se guarda
        en List<Intger> solucion.
        
        Indice=rangoFrec del transmisor.
        numFrec= el número de frecuencias disponibles asociadas a ese rango. Viene determinada
        por el segundo valor en cada fila de dom.txt
        numero= posicion aleatoria de las que existen en cada fila de dom.txt para asociar una frecuencia
        frec= frecuencia asignada
        
        
         */
        for (int i = 0; i < transmisores.size(); i++) {
            int indice = transmisores.get(i);
            int numFrec = rangoFrecuencias.get(indice).get(0);
            int numero = (int) Math.floor(Math.random() * (numFrec) + 1);
            numero=numero -1;
            int frec = rangoFrecuencias.get(indice).get(numero);
            solucion.add(frec);
        }

        resultado = rDiferencia(solucion);

        //Generamos un número aleatorio para calcular el transmisor por el cual vamos a empezar
        int numero = (int) Math.floor(Math.random() * _transmisores.transmisores.size());

        for (int i = 0; i < 100; i++) {
            System.out.println("Ciclo "+i);

            //Genera un random para calcular el sentido en el que nos movemos a la hora de buscar
            //una nueva frecuencia para el transmisor
            //<0.5 izquierda, >=0.5 derecha
            double sentido = Math.random();

            int valorInicial = solucion.get(numero);//La frecuencia asociada al transmisor en la solución actual

            int indiceInicial = 3; //La inicio a cualquier valor, por ejemplo 3

            int frecAsociada = _transmisores.transmisores.get(numero); //El rango de frecuencias a las que puede acceder el transmisor

            int nuevoCoste = 99999; //Nuevo coste al cambiar la frecuencia del transmisor. Iniciado por defecto a 99999.

            //Busco la posición de la frecuencia que tenemos para el transmisor actual en el total
            //de todas las frecuencias que se le podrían asignar
            for (int j = 0; j < _frecuencias.rangoFrecuencias.get(frecAsociada).size(); j++) {
                if (valorInicial == _frecuencias.rangoFrecuencias.get(frecAsociada).get(j)) {
                    indiceInicial = j;
                }

            }

            //Recorrido hacia la izquierda
            if (sentido < 0.5) {
                boolean encontrado = false;

                //El último valor que se puede acceder es 2, ya que 0 es el rango de Frecuencia y 1 el
                //número de frecuencias existentes en ese rango
                while (indiceInicial >= 1 && encontrado == false) {
                    int facto = rDiferenciaFactorizacion(solucion, numero + 1);

                    indiceInicial--;
                    valorInicial = _frecuencias.rangoFrecuencias.get(frecAsociada).get(indiceInicial);
                    List<Integer> nuevaSolucion = new ArrayList<>();
                    nuevaSolucion = solucion;
                    nuevaSolucion.set(numero, valorInicial);
                    int facto2 = rDiferenciaFactorizacion(nuevaSolucion, numero + 1);
                    //Factorizo, elimino el coste de las frecuencias del primer valor y 
                    //sumo el coste de las frecuencias del valor nuevo
                    nuevoCoste = resultado - facto + facto2;

                    if (nuevoCoste < resultado) {
                        solucion = nuevaSolucion;
                        resultado = nuevoCoste;
                        encontrado = true;
                    }

                }
            } else {
                boolean encontrado = false;

                while (indiceInicial < _frecuencias.rangoFrecuencias.get(frecAsociada).size()-1 && encontrado == false) {
                    int facto = rDiferenciaFactorizacion(solucion, numero + 1);

                    indiceInicial++;
                    /*
                    if(indiceInicial==_frecuencias.rangoFrecuencias.get(frecAsociada).get(1)){
                        break;
                    }
                    */
                   
                    valorInicial = _frecuencias.rangoFrecuencias.get(frecAsociada).get(indiceInicial);
                    List<Integer> nuevaSolucion = new ArrayList<>();
                    nuevaSolucion = solucion;
                    nuevaSolucion.set(numero, valorInicial);
                    int facto2 = rDiferenciaFactorizacion(nuevaSolucion, numero + 1);
                    nuevoCoste = resultado - facto + facto2;

                    if (nuevoCoste < resultado) {
                        solucion = nuevaSolucion;
                        resultado = nuevoCoste;
                        encontrado = true;
                    }

                }
            }

            numero = numero+1;
            if(numero==transmisores.size()){
                numero=0;
            }

        }

    }


    /**
     * Lee el fichero ctr.txt y para sacar las diferencias que se han de
     * realizar
     *
     * @param _solucion
     * @param _fichero
     * @throws FileNotFoundException
     */
    public int rDiferencia(List<Integer> _solucion) throws FileNotFoundException {
        File fichero = new File("conjuntos/" + main.DIRECTORIO + "/ctr.txt");
        Scanner lectura = new Scanner(fichero);
        int total = 0;
        while (lectura.hasNextLine()) {
            String linea = lectura.nextLine();
            if (linea.matches("(.* .* C . .* .)")) {
                //System.out.println(linea);
                Scanner sLinea = new Scanner(linea);
                while (sLinea.hasNextInt()) {
                    int tr1 = sLinea.nextInt();
                    int tr2 = sLinea.nextInt();
                    sLinea.next();
                    sLinea.next();
                    int diferencia = sLinea.nextInt();
                    int result = sLinea.nextInt();

                    if (Math.abs(_solucion.get(tr1 - 1) - _solucion.get(tr2 - 1)) > diferencia) {
                        total += result;
                    }
                }
                sLinea.close();
            }
        }
        lectura.close();

        return total;
    }

    public int rDiferenciaFactorizacion(List<Integer> _solucion, int tr) throws FileNotFoundException {
        File fichero = new File("conjuntos/" + main.DIRECTORIO + "/ctr.txt");
        Scanner lectura = new Scanner(fichero);
        int total = 0;
        while (lectura.hasNextLine()) {
            String linea = lectura.nextLine();
            if (linea.matches("(.* .* C . .* .)")) {
                //System.out.println(linea);
                Scanner sLinea = new Scanner(linea);
                while (sLinea.hasNextInt()) {
                    int tr1 = sLinea.nextInt();
                    int tr2 = sLinea.nextInt();
                    sLinea.next();
                    sLinea.next();
                    int diferencia = sLinea.nextInt();
                    int result = sLinea.nextInt();

                    //Si alguno de los dos transmisores es igual a tr, que es el que le pasamos
                    //entonces calcula la diferencia
                    if (tr == tr1 || tr == tr2) {
                        if (Math.abs(_solucion.get(tr1 - 1) - _solucion.get(tr2 - 1)) > diferencia) {
                            total += result;
                        }
                    }
                }
                sLinea.close();
            }
        }
        lectura.close();

        return total;
    }

}

