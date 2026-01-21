package com.financeiro.service;

public class CalculadoraFinanceira {

    public static double calcularVariacaoPercentual(double valorInicial, double valorFinal) {
        if (valorInicial == 0) return 0;
        return ((valorFinal - valorInicial) / valorInicial) * 100;
    }

    public static double calcularJurosSimples(double principal, double taxa, int periodos) {
        return principal * taxa * periodos;
    }

    public static double calcularJurosCompostos(double principal, double taxa, int periodos) {
        return principal * Math.pow(1 + taxa, periodos);
    }

    public static double calcularValorFuturo(double aporteMensal, double taxaMensal, int meses) {
        if (taxaMensal == 0) return aporteMensal * meses;
        
        return aporteMensal * ((Math.pow(1 + taxaMensal, meses) - 1) / taxaMensal);
    }

    public static double calcularTaxaEquivalente(double taxaAnual, int periodosPorAno) {
        return Math.pow(1 + taxaAnual, 1.0 / periodosPorAno) - 1;
    }

    public static double calcularPMT(double valorPresente, double taxa, int periodos) {
        if (taxa == 0) return valorPresente / periodos;
        
        return valorPresente * (taxa * Math.pow(1 + taxa, periodos)) / (Math.pow(1 + taxa, periodos) - 1);
    }

    public static double calcularVP(double valorFuturo, double taxa, int periodos) {
        return valorFuturo / Math.pow(1 + taxa, periodos);
    }

    public static double calcularVF(double valorPresente, double taxa, int periodos) {
        return valorPresente * Math.pow(1 + taxa, periodos);
    }

    public static double calcularTaxaInternaRetorno(double[] fluxos) {
        double taxa = 0.1;
        double precisao = 0.0001;
        int maxIteracoes = 1000;
        
        for (int i = 0; i < maxIteracoes; i++) {
            double vpl = calcularVPL(fluxos, taxa);
            double vplDerivada = calcularVPLDerivada(fluxos, taxa);
            
            if (Math.abs(vpl) < precisao) {
                return taxa;
            }
            
            if (Math.abs(vplDerivada) < 0.0001) {
                break;
            }
            
            taxa = taxa - vpl / vplDerivada;
        }
        
        return taxa;
    }

    private static double calcularVPL(double[] fluxos, double taxa) {
        double vpl = 0;
        for (int t = 0; t < fluxos.length; t++) {
            vpl += fluxos[t] / Math.pow(1 + taxa, t);
        }
        return vpl;
    }

    private static double calcularVPLDerivada(double[] fluxos, double taxa) {
        double derivada = 0;
        for (int t = 1; t < fluxos.length; t++) {
            derivada -= t * fluxos[t] / Math.pow(1 + taxa, t + 1);
        }
        return derivada;
    }

    public static double calcularMedia(double[] valores) {
        if (valores.length == 0) return 0;
        
        double soma = 0;
        for (double v : valores) {
            soma += v;
        }
        return soma / valores.length;
    }

    public static double calcularDesvioPadrao(double[] valores) {
        if (valores.length <= 1) return 0;
        
        double media = calcularMedia(valores);
        double somaQuadrados = 0;
        
        for (double v : valores) {
            somaQuadrados += Math.pow(v - media, 2);
        }
        
        return Math.sqrt(somaQuadrados / (valores.length - 1));
    }
}
