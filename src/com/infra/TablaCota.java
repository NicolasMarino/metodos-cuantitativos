package com.infra;

public class TablaCota {

    private int valor;

    private int inf;

    private int sup;

    public TablaCota(int valor, int inf, int sup) {
        this.valor = valor;
        this.inf = inf;
        this.sup = sup;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public int getInf() {
        return inf;
    }

    public void setInf(int inf) {
        this.inf = inf;
    }

    public int getSup() {
        return sup;
    }

    public void setSup(int sup) {
        this.sup = sup;
    }
}
