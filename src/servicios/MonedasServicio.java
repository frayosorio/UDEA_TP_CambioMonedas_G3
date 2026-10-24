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
}
