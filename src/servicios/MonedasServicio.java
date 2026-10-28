package servicios;

import modelos.CambioMoneda;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MonedasServicio {

    public static List<CambioMoneda> getDatos(String nombreArchivo) {
        var formatoFecha = DateTimeFormatter.ofPattern("d/M/yyyy");
        try {
            Stream<String> lineasArchivo = Files.lines(Paths.get(nombreArchivo));
            return lineasArchivo.skip(1)
                    .map(linea -> linea.split(","))
                    .map(textos -> new CambioMoneda(textos[0],
                            LocalDate.parse(textos[1], formatoFecha),
                            Double.parseDouble(textos[2])
                    ))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    public static List<String> getMonedas(List<CambioMoneda> cambios) {
        return cambios.stream()
                .map(CambioMoneda::getMoneda) //.map(cambio -> cambio.getMoneda())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public static List<CambioMoneda> filtrar(List<CambioMoneda> cambios,
                                             String moneda,
                                             LocalDate desde, LocalDate hasta) {
        return cambios.stream()
                .filter(cambio -> cambio.getMoneda().equals(moneda) &&
                        !cambio.getFecha().isBefore(desde) &&
                        !cambio.getFecha().isAfter(hasta)
                )
                .collect(Collectors.toList());
    }

    public static Map<LocalDate, Double> getDatosGrafica(List<CambioMoneda> cambios) {
        return cambios.stream()
                .collect(Collectors.toMap(CambioMoneda::getFecha, CambioMoneda::getCambio));
    }

    public static List<Double> getValores(List<CambioMoneda> cambios) {
        return cambios.stream()
                .map(CambioMoneda::getCambio) //.map(cambio -> cambio.getCambio())
                .collect(Collectors.toList());
    }

    public static double getPromedio(List<Double> valores) {
        return valores.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    public static double getDesviacionEstandar(List<Double> valores) {
        var promedio = getPromedio(valores);
        return valores.stream()
                .mapToDouble(valor -> Math.abs(promedio - valor))
                .average()
                .orElse(0.0);
    }

    public static double getMaximo(List<Double> valores) {
        return valores.stream()
                .mapToDouble(Double::doubleValue)
                .max()
                .orElse(0.0);
    }

    public static double getMinimo(List<Double> valores) {
        return valores.stream()
                .mapToDouble(Double::doubleValue)
                .min()
                .orElse(0.0);
    }

    public static double getMediana(List<Double> valores) {
        if (valores.isEmpty()) {
            return 0.0;
        }
        var valoresOrdenados = valores.stream()
                .sorted()
                .collect(Collectors.toList());
        var n = valoresOrdenados.size();
        return n % 2 == 1 ? valoresOrdenados.get(n / 2) :
                (valoresOrdenados.get(n / 2) + valoresOrdenados.get(n / 2 - 1)) / 2;
    }

    public static double getModa(List<Double> valores) {
        return valores.stream()
                .collect(Collectors.groupingBy(valor -> valor, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(0.0);
    }

    public static Map<String, Double> getEstadisticas(List<Double> valores) {
        Map<String, Double> estadisticas = Map.of(
                "Promedio", getPromedio(valores),
                "Desviación Estandar", getDesviacionEstandar(valores),
                "Máximo", getMaximo(valores),
                "Mínimo", getMinimo(valores),
                "Mediana", getMediana(valores),
                "Moda", getModa(valores)
        );
        return estadisticas;
    }
}
