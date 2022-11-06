package com.infra;

public class ResultadoUno {

    private long cantidadSimulacionVendidos; // simulo montecarlo

    private long cantidadRealVendidos; // vendimos efectivamente

    private long perdidaOportunidad; // Nos perdimos de vender (no imprimimos tantas revistas)

    private long desperdicio; // imprimimos pero no vendimos

    private String tiempo;

    public ResultadoUno(long cantidadSimulacionVendidos, long cantidadRealVendidos, long perdidaOportunidad, long desperdicio, String tiempo) {
        this.cantidadSimulacionVendidos = cantidadSimulacionVendidos;
        this.cantidadRealVendidos = cantidadRealVendidos;
        this.perdidaOportunidad = perdidaOportunidad;
        this.desperdicio = desperdicio;
        this.tiempo = tiempo;
    }

    public long getCantidadSimulacionVendidos() {
        return cantidadSimulacionVendidos;
    }

    public void setCantidadSimulacionVendidos(long cantidadSimulacionVendidos) {
        this.cantidadSimulacionVendidos = cantidadSimulacionVendidos;
    }

    public long getCantidadRealVendidos() {
        return cantidadRealVendidos;
    }

    public void setCantidadRealVendidos(long cantidadRealVendidos) {
        this.cantidadRealVendidos = cantidadRealVendidos;
    }

    public long getPerdidaOportunidad() {
        return perdidaOportunidad;
    }

    public void setPerdidaOportunidad(long perdidaOportunidad) {
        this.perdidaOportunidad = perdidaOportunidad;
    }

    public long getDesperdicio() {
        return desperdicio;
    }

    public void setDesperdicio(long desperdicio) {
        this.desperdicio = desperdicio;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }
}
