package com.cuyesgyg.appcuy;

/**
 * Created by Yonathan on 22/11/2018.
 */

public class Consulta_control_nacimientos {

    private String poza;
    private String fecha_control;
    private String observacion;
    private String cantidad;
    private String estado;

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


    public String getPoza() {
        return poza;
    }

    public void setPoza(String poza) {
        this.poza = poza;
    }



    public String getFecha_control() {
        return fecha_control;
    }

    public void setFecha_control(String fecha_control) {
        this.fecha_control = fecha_control;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }


}
