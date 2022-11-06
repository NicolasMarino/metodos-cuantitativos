package com.infra;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class Main {

    private static final int FIN_SIMULACION = 2000; // Cantidad de simulaciones
    private static final int PROB_MAL_CLIMA = 40;
    private static final int TOPE_MONTECARLO = 5;
    private static final Map<Integer, TablaCota> malClimaMap = new HashMap<>();
    private static final Map<Integer, TablaCota> buenClimaMap = new HashMap<>();
    private static final int costoProducirRevista = 40;
    private static final int precioVentaRevista = 250;
    private static final int costosFijosQuioscoDiario = 1000; // (30.000 * 1/30) 1000 para calcular 1 dia, y luego hacemos esto x 2000.
    private static final AtomicLong contadorDiasMalos = new AtomicLong(0);
    private static final AtomicLong contadorDiasBuenos = new AtomicLong(0);
    private static final AtomicLong mayorCantidadDeDiasMalos = new AtomicLong(0);
    private static final AtomicLong mayorCantidadDeDiasBuenos = new AtomicLong(0);

    private static final Map<Integer, List<ResultadoUno>> mapEjercicioUno = new HashMap<>();
    private static final List<Integer> cantidadImpresionesRevistas = Arrays.asList(2500, 2600,2700); // 2600,2700
    private static Map<Integer, Integer> mapaDiasMalosConsecutivos = new HashMap<>();
    private static Map<Integer, Integer> relacionTipoClimaVenta = new HashMap<>();

    public static void main(String[] args) {

        cargarMapasTablas(malClimaMap, buenClimaMap);

        cantidadImpresionesRevistas.forEach(revistasImpresas -> {

            contadorDiasMalos.set(0);
            mayorCantidadDeDiasMalos.set(0);
            contadorDiasBuenos.set(0);
            mayorCantidadDeDiasBuenos.set(0);
            mapaDiasMalosConsecutivos = new HashMap<>();
            relacionTipoClimaVenta = new HashMap<>();

            for(int nroCli = 0; nroCli < FIN_SIMULACION; nroCli++) {
                puntoUno(revistasImpresas);

                if(contadorDiasMalos.get() > mayorCantidadDeDiasMalos.get()){
                    mayorCantidadDeDiasMalos.set(contadorDiasMalos.get());
                }

                if(contadorDiasMalos.get() > 1){
                    mapaDiasMalosConsecutivos.computeIfPresent((int) contadorDiasMalos.get(), (k, v) -> v = v + 1);
                    mapaDiasMalosConsecutivos.computeIfAbsent((int) contadorDiasMalos.get(), value -> 1);
                }
            }

            resultadoPuntoDos(revistasImpresas);
            System.out.println("=============================================");
            System.out.println("==================[Parte 3]=======================");

            System.out.println("Mayor cantidad de dias consecutivos con mal clima: "+ mayorCantidadDeDiasMalos.get());
            System.out.println("=============================================");
            System.out.println("==================[Parte 4]=======================");

            System.out.println("--- Incidencia de días consecutivos con mal clima ----");
            mapaDiasMalosConsecutivos.forEach((key, value)
                    -> System.out.println("Dias consecutivos: " + key + " Cantidad: " + value));
            System.out.println("=============================================");
            System.out.println("==================[Parte 5]=======================");
            System.out.println("--- Relacion venta tipo clima ----");
            relacionTipoClimaVenta.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEachOrdered((key) ->
                            System.out.println("Cantidad revistas vendidas: " + key.getKey() + " Cantidad ocurrencias: " + key.getValue()));

            System.out.println("=============================================");
        });
    }

    private static void puntoUno(int cantidadRevistasImpresas){
        int simulacionRevistasVendidas = obtenerCantidadDeRevistasVendidas();
        long cantidadRealVendidos = (cantidadRevistasImpresas - simulacionRevistasVendidas < 0) ? cantidadRevistasImpresas : simulacionRevistasVendidas;
        long cantidadPerdidaOportunidad = (cantidadRevistasImpresas - simulacionRevistasVendidas < 0) ? (cantidadRevistasImpresas - simulacionRevistasVendidas)*(-1) : 0;
        long cantidadDesperdicio =  (cantidadRevistasImpresas - simulacionRevistasVendidas < 0) ? 0 : cantidadRevistasImpresas - simulacionRevistasVendidas;
        String tiempo = simulacionRevistasVendidas >= 2300 ? "Clima malo" : "Clima bueno";
        if (tiempo.equals("Clima malo")){
            contadorDiasMalos.getAndAdd(1);
            contadorDiasBuenos.set(0);
        }else{
            contadorDiasBuenos.getAndAdd(1);
            contadorDiasMalos.set(0);
        }
        relacionTipoClimaVenta.computeIfPresent(simulacionRevistasVendidas, (k,v) -> v= v+1);
        relacionTipoClimaVenta.putIfAbsent(simulacionRevistasVendidas, 1);

        mapEjercicioUno.get(cantidadRevistasImpresas).add( new ResultadoUno(simulacionRevistasVendidas, cantidadRealVendidos, cantidadPerdidaOportunidad, cantidadDesperdicio, tiempo));
    }

    private static void resultadoPuntoDos(Integer cantRevistasImpresas){

        System.out.println("================= Parte 2 ===================");
            DecimalFormat formatea =new DecimalFormat("###,###,###,###,###,###,###.##");

            System.out.println(" Para "+ formatea.format(cantRevistasImpresas) + " revistas impresas por día: ");

            double costos = costoProducirRevista * (cantRevistasImpresas*FIN_SIMULACION);
            double ventas = precioVentaRevista * mapEjercicioUno.get(cantRevistasImpresas).stream().mapToLong(ResultadoUno::getCantidadRealVendidos).sum();
            double utilidad = (ventas - costos) - (costosFijosQuioscoDiario * FIN_SIMULACION);

            double cantidadRevistasDesperdiciadas = mapEjercicioUno.get(cantRevistasImpresas).stream().mapToLong(ResultadoUno::getDesperdicio).sum();
            double revistasDesperdiciadasEnPesos = cantidadRevistasDesperdiciadas * (precioVentaRevista - costoProducirRevista);

            double cantidadPerdidaOportunidad = mapEjercicioUno.get(cantRevistasImpresas).stream().mapToLong(ResultadoUno::getPerdidaOportunidad).sum();
            double perdidaOportunidadEnPesos = cantidadPerdidaOportunidad * (precioVentaRevista - costoProducirRevista);

            System.out.println(" Total de revistas impresas "+ (formatea.format(cantRevistasImpresas*FIN_SIMULACION)));
            System.out.println(" Total revistas vendidas (simuladas): " + formatea.format(mapEjercicioUno.get(cantRevistasImpresas).stream().mapToLong(ResultadoUno::getCantidadSimulacionVendidos).sum()));
            System.out.println(" Total revistas vendidas (reales): "+ formatea.format(mapEjercicioUno.get(cantRevistasImpresas).stream().mapToLong(ResultadoUno::getCantidadRealVendidos).sum()));
            System.out.println(" Utilidad (en $UYU): "+ formatea.format(utilidad));
            System.out.println(" Total de perdida de oportunidad (en cantidad de revistas): " + formatea.format(cantidadPerdidaOportunidad));
            System.out.println(" Total de perdida de oportunidad (en $UYU): " + formatea.format(perdidaOportunidadEnPesos));
            System.out.println(" Desperdicio (en cantidad de revistas): " + formatea.format(cantidadRevistasDesperdiciadas));
            System.out.println(" Desperdicio (en $UYU): " + formatea.format(revistasDesperdiciadasEnPesos));
    }

  private static void cargarMapasTablas(Map<Integer, TablaCota> climaMaloMap, Map<Integer, TablaCota> climaBuenoMap){
        climaMaloMap.put(0, new TablaCota(2300,1, 15));
        climaMaloMap.put(1, new TablaCota(2400,16, 37));
        climaMaloMap.put(2, new TablaCota(2500,38, 61));
        climaMaloMap.put(3, new TablaCota(2600,62, 82));
        climaMaloMap.put(4, new TablaCota(2700,83, 100));

        climaBuenoMap.put(0, new TablaCota( 1200,1, 25));
        climaBuenoMap.put(1, new TablaCota(1300,26, 49));
        climaBuenoMap.put(2, new TablaCota(1400,50, 68));
        climaBuenoMap.put(3, new TablaCota(1500,69, 85));
        climaBuenoMap.put(4, new TablaCota(1600,86, 100));

        mapEjercicioUno.put(2500, new ArrayList<>());
        mapEjercicioUno.put(2600, new ArrayList<>());
        mapEjercicioUno.put(2700, new ArrayList<>());
    }

    private static int Montecarlo (Map<Integer, TablaCota> tablaCotaMap, int tope){
        int numeroRandomEntre1y100 = (int) Math.floor(Math.random() * 100) + 1;

        for (int i=0; i < tope; i++){
            if(estaComprendidoEn(numeroRandomEntre1y100, tablaCotaMap.get(i).getInf(), tablaCotaMap.get(i).getSup())){
                return tablaCotaMap.get(i).getValor();
            }
        }
        return -1; // Esto no debería pasar nunca.
    }

    private static boolean estaComprendidoEn(int numeroAEvaluar, int cotaInferior, int cotaSuperior){
        return numeroAEvaluar >= cotaInferior && numeroAEvaluar <= cotaSuperior;
    }

    private static int obtenerCantidadDeRevistasVendidas(){
        int numeroRandomEntre1y100 = (int) Math.floor(Math.random() * 100) + 1;

        if( numeroRandomEntre1y100 > PROB_MAL_CLIMA){
            return Montecarlo(buenClimaMap, TOPE_MONTECARLO);
        }

        return Montecarlo(malClimaMap, TOPE_MONTECARLO);
    }
}