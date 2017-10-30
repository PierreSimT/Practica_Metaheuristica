/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import static main.main.NUMERO;

/**
 *
 * @author ptondreau
 */
public class Grasp {

    private static int K = 10;

    private List<List<Integer>> frecuencias = new ArrayList<>();
    private List<Integer> transmisores = new ArrayList<>();
    private List<List<Integer>> listaRestringida = new ArrayList<>();
    private List<List<Integer>> frecuenciasR = new ArrayList<>();
    private List<List<Integer>> vectorCostes = new ArrayList<>(); //Lista vectorCostes; 
    //private List<Integer> frecuenciasRtemp = new ArrayList<>(); //solución temporal
    private Restricciones restricciones;
    private int resultado[] = new int[ 25 ];
    private int idFinal;

    /**
     * Inicialmente elegimos K transmisores aleatoriamente y se le asignan
     * frecuencias también aleatorias.
     *
     * @param _transmisores Lista de transmisores con su rango
     * @param _frecuencias Lista de frecuencias de cada rango
     * @param _restricciones Las restricciones que debe satisfacer la solucion
     */
    public Grasp ( listaTransmisores _transmisores, rangoFrec _frecuencias, Restricciones _restricciones ) throws FileNotFoundException {
        frecuencias = _frecuencias.rangoFrecuencias;
        transmisores = _transmisores.transmisores;
        restricciones = _restricciones;
        Arrays.fill(resultado, Integer.MAX_VALUE);

        for ( int i = 0; i < 25; i ++ ) {
            listaRestringida.add(new ArrayList<>());
            frecuenciasR.add(new ArrayList<>());
            for ( int j = 0; j < transmisores.size(); j ++ ) {
                frecuenciasR.get(i).add(0);
            }
        }

        for ( int k = 0; k < 25; k ++ ) {
            for ( int i = 0; i < K; i ++ ) {
                int transmisor = NUMERO.nextInt(transmisores.size());
                int tama = frecuencias.get(transmisores.get(transmisor)).size();
                int frecuenciaAsig = frecuencias.get(transmisores.get(transmisor)).get(NUMERO.nextInt(tama));

                listaRestringida.get(k).add(transmisor);
                frecuenciasR.get(k).set(transmisor, frecuenciaAsig);
            }
        }
    }

    /**
     * Construcción de la solución, comenzando por la solución incial.
     *
     * @throws FileNotFoundException
     */
    public void algoritmo () throws FileNotFoundException {
        for ( int k = 0; k < 25; k ++ ) {

            Random numero = NUMERO;
            List<List<Integer>> listaRestric = new ArrayList<>();
            int transmisor = 0;
            boolean fin = false;
            while( transmisor < transmisores.size() ) {
                listaRestric = restricciones.restriccionesTransmisor(transmisor);
                if (  ! listaRestringida.get(k).contains(transmisor) && listaRestric.size() > 0 ) {

                    int minimo = Integer.MAX_VALUE;
                    boolean encontrado = false;
                    int frecuenciaR = 0;
                    int frecuencia;
                    int pos = 0;

                    int valor = 0; //Sacado del bucle while

                    while( pos < frecuencias.get(transmisores.get(transmisor)).size() &&  ! encontrado ) {

                        List<Integer> nuevaLista = new ArrayList<>();
                        nuevaLista.addAll(frecuenciasR.get(k));

                        frecuencia = frecuencias.get(transmisores.get(transmisor)).get(pos);
                        nuevaLista.set(transmisor, frecuencia);
                        List<List<Integer>> listaRest = compruebaTransmisores(transmisor, k);

                        if ( listaRest.size() > 0 ) { // Lista no vacía, se selecciona frecuencia que afecte lo menos posible al resultado

                            valor = rDiferencia(nuevaLista, listaRest);
                            if ( valor < minimo ) {
                                minimo = valor;
                                frecuenciaR = frecuencia;
                                if ( valor == 0 ) // Si la suma de todas las restricciones = 0 entonces es el mejor resultado posible
                                {
                                    encontrado = true;
                                }
                            }
                        } else { // En caso de que la lista este vacía no hay restricciones que se puedan satisfacer -> frecuencia aleatoria

                            int tamanio = frecuencias.get(transmisores.get(transmisor)).size();
                            frecuenciaR = frecuencias.get(transmisores.get(transmisor)).get(numero.nextInt(tamanio));
                            valor = 0;
                            encontrado = true;
                        }
                        pos ++;
                    }
                    List<Integer> vCoste = new ArrayList<>();
                    vCoste.add(0, transmisor);
                    vCoste.add(1, frecuenciaR);
                    vCoste.add(2, valor);
                    vectorCostes.add(vCoste);
                    frecuenciasR.get(k).set(transmisor, frecuenciaR);

                }
                transmisor ++;
            }

            resultado[ k ] = rDiferencia(frecuenciasR.get(k), restricciones);

            // Tenemos una solucion temporal, se intenta optimizar
            List<List<Float>> vectorPosicion = asignarPosicion(vectorCostes);
            float sumaSesgo = 0;
            for ( int i = 0; i < vectorPosicion.size(); i ++ ) {
                float sesgos = (1 / vectorPosicion.get(i).get(2));
                sumaSesgo += sesgos;
            }

            //Calculamos el sesgo definitivo
            for ( int i = 0; i < vectorPosicion.size(); i ++ ) {
                float probabilidad = (1 / vectorPosicion.get(i).get(2)) / sumaSesgo;
                vectorPosicion.get(i).add(3, probabilidad);
            }

            //Ahora tenemos en la posicion 3 asignada la probabilidad de cada transmisor.
            //Con tablaSesgoFinal creo una nueva estructura de datos que tenga los transmisores ordenados
            //en función de su valor en posición
            List<List<Float>> vectorFinal = tablaSesgoFinal(vectorPosicion);

            float probabilidadAcumulada = 0;
            for ( int i = 0; i < vectorFinal.size(); i ++ ) {
                probabilidadAcumulada += vectorFinal.get(i).get(3);
                vectorFinal.get(i).add(4, probabilidadAcumulada);
            }

            float aleatorio = numero.nextFloat();
            float distancia = Integer.MAX_VALUE;
            float transmisorElegido = 0;
            float frecuenciaAsignada = 0;

            for ( int i = 0; i < vectorFinal.size(); i ++ ) {
                float nDistancia = Math.abs(vectorFinal.get(i).get(4) - aleatorio);
                if ( nDistancia < distancia ) {
                    distancia = nDistancia;
                    transmisorElegido = i;
                    frecuenciaAsignada = vectorFinal.get(i).get(1);
                }
            }

            busquedaSolucion(transmisorElegido, k);

        }

        int id = 0;
        int minimo = Integer.MAX_VALUE;
        for ( int i = 0; i < 25; i ++ ) {
            int valor = resultado[ i ];
            if ( valor < minimo ) {
                minimo = valor;
                id = i;
            }
        }

        idFinal = id;

    }

    private void busquedaSolucion ( float transmisorElegido, int id ) throws FileNotFoundException {
        Random numero = NUMERO;
        List<List<Integer>> listaRestric = new ArrayList<>();
        int token = numero.nextInt(transmisores.size());
        for ( int i = 0; i < 400; i ++ ) {
            double sentido = numero.nextDouble();
            int valorInicial = frecuenciasR.get(id).get(token); // Se obtiene la frecuencia del token
            int indiceInicial;
            int nuevoCoste = Integer.MAX_VALUE;

            indiceInicial = frecuencias.get(transmisores.get(token)).indexOf(valorInicial); // Mas corto que codigo de abajo
            listaRestric = restricciones.restriccionesTransmisor(token);
            if ( listaRestric.size() > 0 ) {
                if ( sentido < 0.5 ) {
                    boolean encontrado = false;
                    while( indiceInicial >= 0 &&  ! encontrado ) {
                        int fact1 = rDiferencia(frecuenciasR.get(id), token, restricciones);
                        valorInicial = frecuencias.get(transmisores.get(token)).get(indiceInicial);
                        List<Integer> nuevaSolucion = new ArrayList<>();
                        nuevaSolucion.addAll(frecuenciasR.get(id));
                        nuevaSolucion.set(token, valorInicial);
                        int fact2 = rDiferencia(nuevaSolucion, token, restricciones);
                        nuevoCoste = resultado[ id ] + (fact2 - fact1);

                        if ( nuevoCoste < resultado[ id ] ) {
                            frecuenciasR.get(id).set(token, valorInicial);
                            resultado[ id ] = nuevoCoste;
                            encontrado = true;
                        }
                        indiceInicial --;
                    }
                } else {
                    boolean encontrado = false;
                    while( indiceInicial < frecuencias.get(transmisores.get(token)).size() &&  ! encontrado ) {
                        int fact1 = rDiferencia(frecuenciasR.get(id), token, restricciones);
                        valorInicial = frecuencias.get(transmisores.get(token)).get(indiceInicial);
                        List<Integer> nuevaSolucion = new ArrayList<>();
                        nuevaSolucion.addAll(frecuenciasR.get(id));
                        nuevaSolucion.set(token, valorInicial);
                        int fact2 = rDiferencia(nuevaSolucion, token, restricciones);
                        nuevoCoste = resultado[ id ] + (fact2 - fact1);

                        if ( nuevoCoste < resultado[ id ] ) {
                            frecuenciasR.get(id).set(token, valorInicial);
                            resultado[ id ] = nuevoCoste;
                            encontrado = true;
                        }
                        indiceInicial ++;
                    }
                }
            }
            token = Math.floorMod(token + 1, transmisores.size());
        }
    }

    /**
     * Funcion que devuelve una lista con las restricciones que puede satisfacer
     * un transmisor
     *
     * @param transmisor
     * @return
     * @throws FileNotFoundException
     */
    private List<List<Integer>> compruebaTransmisores ( int transmisor, int id ) throws FileNotFoundException {
        int contador = 0;
        List<List<Integer>> listaRest = new ArrayList<>();
        List<List<Integer>> listaT = restricciones.restriccionesTransmisor(transmisor);
        for ( int i = 0; i < listaT.size(); i ++ ) {
            if ( frecuenciasR.get(id).get(listaT.get(i).get(0) - 1) != 0 || frecuenciasR.get(id).get(listaT.get(i).get(1) - 1) != 0 ) {
                listaRest.add(new LinkedList<>());
                listaRest.get(contador ++).addAll(listaT.get(i));
            }
        }

        return listaRest;
    }

    private int rDiferencia ( List<Integer> valores, List<List<Integer>> rest ) {
        int total = 0;
        for ( int i = 0; i < rest.size(); i ++ ) {
            int tr1 = rest.get(i).get(0);
            int tr2 = rest.get(i).get(1);
            int diferencia = rest.get(i).get(2);
            int result = rest.get(i).get(3);

            if ( Math.abs(valores.get(tr1 - 1) - valores.get(tr2 - 1)) > diferencia ) {
                total += result;
            }

        }

        return total;
    }

    public int rDiferencia ( List<Integer> valores, Restricciones rest ) throws FileNotFoundException {

        int total = 0;
        for ( int i = 0; i < rest.restricciones.size(); i ++ ) {
            int tr1 = rest.restricciones.get(i).get(0);
            int tr2 = rest.restricciones.get(i).get(1);
            int diferencia = rest.restricciones.get(i).get(2);
            int result = rest.restricciones.get(i).get(3);

            if ( Math.abs(valores.get(tr1 - 1) - valores.get(tr2 - 1)) > diferencia ) {
                total += result;
            }

        }

        return total;
    }

    /**
     * Calcula el resultado del problema a minimizar
     *
     * @param valores Valores de los transmisores
     * @param cambioTransmisor Transmisor al que se le aplico un cambio de
     * frecuencia
     * @param rest Restricciones a evaluar
     * @return
     * @throws FileNotFoundException
     */
    public int rDiferencia ( List<Integer> valores, int cambioTransmisor, Restricciones rest ) throws FileNotFoundException {

        List<List<Integer>> listaRest = new ArrayList<>();
        listaRest = rest.restriccionesTransmisor(cambioTransmisor);

        int total = 0;
        for ( int i = 0; i < listaRest.size(); i ++ ) {

            int tr1 = listaRest.get(i).get(0);
            int tr2 = listaRest.get(i).get(1);

            if ( tr1 == cambioTransmisor + 1 || tr2 == cambioTransmisor + 1 ) {
                int diferencia = listaRest.get(i).get(2);
                int result = listaRest.get(i).get(3);

                if ( Math.abs(valores.get(tr1 - 1) - valores.get(tr2 - 1)) > diferencia ) {
                    total += result;
                }

            }

        }

        return total;

    }

    /**
     * Asigna una posicion a cada transmisor en función de su vectorCoste. Hasta
     * que no se visiten todos los transmisores que tienen un vectorCoste, se va
     * haciendo un ciclo for que los recorra comprobando si su coste es igual al
     * valor minimo, el cual se va incrementando en uno cada ciclo, y si es asi,
     * lo guarda en la nueva Lista junto a la posición que le corresponda.
     *
     * El coste minimo va aumentando de uno en uno para simplificar un poco el
     * problema, en caso de que ningún transmisor se corresponda con dicho valor
     * iterará toda la lista pero no hará cambios.
     *
     * @param vectorCostes lista con los costes de cada transmisor
     * @return vectorPosicion lista en la que a cada transmisor se le asigna un
     * valor posición según la calidad de su coste
     *
     */
    public List<List<Float>> asignarPosicion ( List<List<Integer>> vectorCostes ) {
        List<List<Float>> vectorPosicion = new ArrayList<>();
        int valorMin = 0;
        int posicion = 1;
        int contador = 0;
        int transmisoresVisitados = 0;
        int i;
        while( transmisoresVisitados != vectorCostes.size() ) {
            contador = 0;
            for ( i = 0; i < vectorCostes.size(); i ++ ) {
                if ( vectorCostes.get(i).get(1) == valorMin ) {
                    List<Float> vPos = new ArrayList<>();
                    vPos.add(0, (float) vectorCostes.get(i).get(0));
                    vPos.add(1, (float) vectorCostes.get(i).get(1));
                    vPos.add(2, (float) posicion);
                    vectorPosicion.add(transmisoresVisitados, vPos);

                    transmisoresVisitados ++;
                    contador ++;
                }
            }
            posicion += contador;
            valorMin ++;
        }

        return vectorPosicion;
    }

    /**
     * Ordena los transmisores para crear la tabla final,aquellos con la mejor
     * posición van al principio
     *
     * @param vectorPosicion lista de lista que guarda el transmisor, su
     * frecuencia, su posición y su probabilidad
     * @return vectorPosFinal lista en la que a cada transmisor se le asigna un
     * valor posición según la calidad de su coste
     *
     */
    public List<List<Float>> tablaSesgoFinal ( List<List<Float>> vectorPosicion ) {
        List<List<Float>> vectorPosFinal = new ArrayList<>();
        int posicionActual = 1;
        int transmisoresVisitados = 0;
        while( transmisoresVisitados != vectorPosicion.size() ) {
            //Busco todos los transmisores que tengan la misma posición y los meto en vectorPosFinal juntos.
            for ( int i = 0; i < vectorPosicion.size(); i ++ ) {

                if ( vectorPosicion.get(i).get(2) == posicionActual ) {
                    vectorPosFinal.add(transmisoresVisitados, vectorPosicion.get(i));
                    transmisoresVisitados ++;
                }
            }
            posicionActual ++;

        }
        return vectorPosFinal;

    }

    public void resultados () {

        List<List<Integer>> listaTrans = new ArrayList<>();
        for ( int i = 0; i < transmisores.size(); i ++ ) {
            listaTrans = restricciones.restriccionesTransmisor(i);
            if ( listaTrans.size() > 0 ) {
                System.out.println("Transmisor " + (i + 1) + ": " + frecuenciasR.get(idFinal).get(i));
            }
        }
        System.out.println("Coste: " + resultado[ idFinal ]);
    }

}
