package com.eschool.schoolpage.models;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeDifference {
    public static String calcularDiferencia(LocalDateTime fechaPasada, LocalDateTime fechaActual) {
        Duration duration = Duration.between(fechaPasada, fechaActual);

        long segundos = duration.getSeconds();
        long minutos = segundos / 60;
        long horas = minutos / 60;
        long dias = horas / 24;
        long meses = dias / 30;
        long anos = dias / 365;

        if (anos > 0) {
            return anos + " year " + (anos > 1 ? "s" : "") + " ago ";
        } else if (meses > 0) {
            return meses + " month" + (meses > 1 ? "s" : "") + " ago ";
        } else if (dias > 0) {
            return dias + " day" + (dias > 1 ? "s" : "") + " ago ";
        } else if (horas > 0) {
            return horas + " hour" + (horas > 1 ? "s" : "") + " ago ";
        } else if (minutos > 0) {
            return minutos + " min" + (minutos > 1 ? "s" : "") + " ago ";
        } else {
            return "A few seconds ago...";
        }
    }

    public static void main(String[] args) {
        LocalDateTime fechaPasada = LocalDateTime.now().minusMinutes(5);
        LocalDateTime fechaActual = LocalDateTime.now();

        System.out.println(calcularDiferencia(fechaPasada, fechaActual));
    }
}
