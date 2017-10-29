/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author ptondreau
 */
public class Grasp {

    private static int K = 10;

    private List<List<Integer>> frecuencias = new ArrayList<>();
    private List<Integer> transmisores = new ArrayList<>();
    private List<Integer> listaRestringida = new ArrayList<>();
    private List<Integer> frecuenciasR = new ArrayList<>();
    //private double vectorCostes [];
    private List<List<Integer>> vectorCostes = new ArrayList<>(); //Lista vectorCostes; 

    private List<Integer> frecuenciasRtemp = new ArrayList<>(); //solución temporal
    private Restricciones restricciones;
    private int resultado;

    /**
     * Inicialmente elegimos K transmisores aleatoriamente y se le asignan
     * frecuencias también aleatorias.
     *
     * @param _transmisores Lista de transmisores con su rango
     * @param _frecuencias Lista de frecuencias de cada rango
     * @param _restricciones Las restricciones que debe satisfacer la solucion
     */
    public Grasp(listaTransmisores _transmisores, rangoFrec _frecuencias, Restricciones _restricciones) throws FileNotFoundException {
        frecuencias = _frecuencias.rangoFrecuencias;
        transmisores = _transmisores.transmisores;
        restricciones = _restricciones;
        Random numero = new Random();
        resultado = Integer.MAX_VALUE;
        for (int i = 0; i < 400; i++) {
            frecuenciasR.add(0);
        }

        for (int i = 0; i < K; i++) {
            int transmisor = numero.nextInt(transmisores.size());
            int tama = frecuencias.get(transmisores.get(transmisor)).size();
            int frecuenciaAsig = frecuencias.get(transmisores.get(transmisor)).get(numero.nextInt(tama));
//            List<Integer> vCoste = new ArrayList<>();
//            vCoste.add(0, transmisor);
//            vCoste.add(1, frecuenciaAsig);
//            vCoste.add(2, )
            listaRestringida.add(transmisor);
            frecuenciasR.set(transmisor, frecuenciaAsig);
        }

        frecuenciasRtemp = frecuenciasR;
        solucionInicial();
        resultado = rDiferencia(frecuenciasR, restricciones);
    }

    private void solucionInicial() throws FileNotFoundException {
        Random numero = new Random();
        int transmisor = 0;
        boolean fin = false;
        while (transmisor < 400) {

            if (!listaRestringida.contains(transmisor)) {

                int minimo = Integer.MAX_VALUE;
                boolean encontrado = false;
                int frecuenciaR = 0;
                int frecuencia;
                int pos = 0;

                int valor = 0; //Sacado del bucle while

                while (pos < frecuencias.get(transmisores.get(transmisor)).size() && !encontrado) {

                    List<Integer> nuevaLista = new ArrayList<>();
                    nuevaLista.addAll(frecuenciasR);

                    frecuencia = frecuencias.get(transmisores.get(transmisor)).get(pos++);
                    nuevaLista.set(transmisor, frecuencia);
                    List<List<Integer>> listaRest = compruebaTransmisores(transmisor);

                    if (listaRest.size() > 0) { // Lista no vacía, se selecciona frecuencia que afecte lo menos posible al resultado

                        valor = rDiferencia(nuevaLista, listaRest);
                        if (valor < minimo) {
                            minimo = valor;
                            frecuenciaR = frecuencia;
                            if (valor == 0) // Si la suma de todas las restricciones = 0 entonces es el mejor resultado posible
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
                }
                List<Integer> vCoste = new ArrayList<>();
                vCoste.add(0, transmisor);
                vCoste.add(1, frecuenciaR);
                vCoste.add(2, valor);
                vectorCostes.add(vCoste);
                frecuenciasR.set(transmisor, frecuenciaR);
            }
            transmisor++;
        }

        //Ahora mismo tenemos una solución "temporal" generada aleatoriamente y un vectorCostes relleno
        List<List<Double>> vectorPosicion = asignarPosicion(vectorCostes);
        int sumaPorcentajes = 0;
        for (int i = 0; i < vectorPosicion.size(); i++) {
            double porcentaje = (1 / vectorPosicion.get(i).get(2));
            sumaPorcentajes += porcentaje;
        }

        //Calculamos el sesgo definitivo
        for (int i = 0; i < vectorPosicion.size(); i++) {
            double probabilidad = (1 / vectorPosicion.get(i).get(1)) / sumaPorcentajes;
            vectorPosicion.get(i).add(3, probabilidad);
        }

        //Ahora tenemos en la posicion 3 asignada la probabilidad de cada transmisor.
        //Con tablaSesgoFinal creo una nueva estructura de datos que tenga los transmisores ordenados
        //en función de su valor en posición
        List<List<Double>> vectorFinal = tablaSesgoFinal(vectorPosicion);

        double probabilidadAcumulada = 0;
        for (int i = 0; i < vectorFinal.size(); i++) {
            probabilidadAcumulada += vectorFinal.get(i).get(3);
            vectorFinal.get(i).add(4, probabilidadAcumulada);

        }

        double aleatorio = numero.nextDouble();
        double transmisorElegido = 0;
        double frecuenciaAsignada = 0;

        for (int i = 0; i < vectorFinal.size(); i++) {
            if (i == 0) {
                if (aleatorio > 0 && aleatorio < vectorFinal.get(i).get(4)) {
                    transmisorElegido = vectorFinal.get(i).get(0);
                    frecuenciaAsignada = vectorFinal.get(i).get(1);
                }
            } else {
                if (aleatorio > vectorFinal.get(i - 1).get(4) && aleatorio < vectorFinal.get(i).get(4)) {
                    transmisorElegido = vectorFinal.get(i).get(0);
                    frecuenciaAsignada = vectorFinal.get(i).get(1);
                }
            }
        }
        System.out.println("Transmisor: "+transmisorElegido);
        System.out.println("Frecuencia: "+frecuenciaAsignada);
        

    }

    /**
     * Funcion que devuelve una lista con las restricciones que puede satisfacer
     * un transmisor
     *
     * @param transmisor
     * @return
     * @throws FileNotFoundException
     */
    private List<List<Integer>> compruebaTransmisores(int transmisor) throws FileNotFoundException {
        int contador = 0;
        List<List<Integer>> listaRest = new ArrayList<>();
        List<List<Integer>> listaT = restricciones.restriccionesTransmisor(transmisor);
        for (int i = 0; i < listaT.size(); i++) {
            if (frecuenciasR.get(listaT.get(i).get(0)-1) != 0 || frecuenciasR.get(listaT.get(i).get(1)-1) != 0) {
                listaRest.add(new LinkedList<>());
                listaRest.get(contador++).addAll(listaT.get(i));
            }
        }

        return listaRest;
    }

    private int rDiferencia(List<Integer> valores, List<List<Integer>> rest) {
        int total = 0;
        for (int i = 0; i < rest.size(); i++) {
            int tr1 = rest.get(i).get(0);
            int tr2 = rest.get(i).get(1);
            int diferencia = rest.get(i).get(2);
            int result = rest.get(i).get(3);

            if (Math.abs(valores.get(tr1 - 1) - valores.get(tr2 - 1)) > diferencia) {
                total += result;
            }

        }

        return total;
    }

    public int rDiferencia(List<Integer> valores, Restricciones rest) throws FileNotFoundException {

        int total = 0;
        for (int i = 0; i < rest.restricciones.size(); i++) {
            int tr1 = rest.restricciones.get(i).get(0);
            int tr2 = rest.restricciones.get(i).get(1);
            int diferencia = rest.restricciones.get(i).get(2);
            int result = rest.restricciones.get(i).get(3);

            if (Math.abs(valores.get(tr1 - 1) - valores.get(tr2 - 1)) > diferencia) {
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
    public int rDiferencia(List<Integer> valores, int cambioTransmisor, Restricciones rest) throws FileNotFoundException {

        List<List<Integer>> listaRest = new ArrayList<>();
        listaRest = rest.restriccionesTransmisor(cambioTransmisor);

        int total = 0;
        for (int i = 0; i < listaRest.size(); i++) {

            int tr1 = listaRest.get(i).get(0);
            int tr2 = listaRest.get(i).get(1);

            if (tr1 == cambioTransmisor || tr2 == cambioTransmisor) {
                int diferencia = listaRest.get(i).get(2);
                int result = listaRest.get(i).get(3);

                if (Math.abs(valores.get(tr1 - 1) - valores.get(tr2 - 1)) > diferencia) {
                    total += result;
                }

            }

        }

        return total;

    }

    /**
     * asigna una posicion a cada transmisor en función de su vectorCoste. Hasta
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
    public List<List<Double>> asignarPosicion(List<List<Integer>> vectorCostes) {
        List<List<Double>> vectorPosicion = new ArrayList<>();
        int valorMin = 0;
        int posicion = 1;
        int contador = 0;
        int transmisoresVisitados = 0;
        int i;
        while (transmisoresVisitados != vectorCostes.size()) {
            contador = 0;
            for ( i = 0; i < vectorCostes.size(); i++) {
                if (vectorCostes.get(i).get(1) == valorMin) {
                    List<Double> vPos = new ArrayList<>();
                    vPos.add(0, (double) vectorCostes.get(i).get(0));
                    vPos.add(1, (double) vectorCostes.get(i).get(1));
                    vPos.add(2, (double) posicion);
                    vectorPosicion.add(transmisoresVisitados, vPos);

                    transmisoresVisitados++;
                    contador++;
                }
            }
            posicion += contador;
            valorMin++;
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
    public List<List<Double>> tablaSesgoFinal(List<List<Double>> vectorPosicion) {
        List<List<Double>> vectorPosFinal = new ArrayList<>();
        int posicionActual = 1;
        int transmisoresVisitados = 0;
        while (transmisoresVisitados != vectorPosicion.size()) {
            //Busco todos los transmisores que tengan la misma posición y los meto en vectorPosFinal juntos.
            for (int i = 0; i < vectorPosicion.size(); i++) {

                if (vectorPosicion.get(i).get(2) == posicionActual) {
                    vectorPosFinal.add(transmisoresVisitados, vectorPosicion.get(i));
                    transmisoresVisitados++;
                }
            }
            posicionActual++;

        }
        return vectorPosFinal;

    }

    public void resultados() {
        System.out.println("Coste: " + resultado);
        for (int i = 0; i < transmisores.size(); i++) {
            System.out.println("Transmisor " + (i + 1) + ": " + frecuenciasR.get(i));
        }
    }

}