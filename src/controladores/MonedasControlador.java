package controladores;

import modelos.CambioMoneda;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import servicios.MonedasServicio;
import vistas.MonedasVista;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class MonedasControlador {

    private final MonedasVista vista;
    private List<String> monedas;
    private List<CambioMoneda> cambiosMoneda;

    public MonedasControlador(MonedasVista vista) {
        this.vista = vista;

        this.vista.setGraficarClick(evento -> graficar());
        this.vista.setCalcularEstadisticasClick(evento -> calcularEstadisticas());
        cargarDatos();
    }

    private void cargarDatos() {
        String nombreArchivo = System.getProperty("user.dir") + "/src/datos/Cambios Monedas.csv";
        cambiosMoneda = MonedasServicio.getDatos(nombreArchivo);
        monedas = MonedasServicio.getMonedas(cambiosMoneda);
        vista.setMonedas(monedas);
    }

    private void graficar() {
        if (vista.getIndiceMonedaSeleccionada() >= 0) {
            LocalDate desde = vista.getFechaDesde();
            LocalDate hasta = vista.getFechaHasta();
            var moneda = monedas.get(vista.getIndiceMonedaSeleccionada());

            var datosFiltrados = MonedasServicio.filtrar(cambiosMoneda, moneda, desde, hasta);
            var datosGrafica = MonedasServicio.getDatosGrafica(datosFiltrados);

            TimeSeries serie = new TimeSeries("Cambio en USD de " + moneda);

            for (var dato : datosGrafica.entrySet()) {
                var fecha = dato.getKey();
                var fechaSerie = new Day(fecha.getDayOfMonth(), fecha.getMonthValue(), fecha.getYear());
                var valorCambio = dato.getValue();
                serie.add(fechaSerie, valorCambio);
            }

            TimeSeriesCollection series = new TimeSeriesCollection();
            series.addSeries(serie);

            JFreeChart graficador = ChartFactory.createTimeSeriesChart(
                    "Graáfica de cambio en USD de " + moneda + " vs Fecha",
                    "Fecha",
                    "Cambio en USD",
                    series
            );

            ChartPanel pnlGraficador = new ChartPanel(graficador);
            pnlGraficador.setPreferredSize(new Dimension(500, 270));
            vista.graficar(pnlGraficador);
        }
        // Cambiar a la pestaña de Grafica
        vista.activarPestaña(0);
    }

    private void calcularEstadisticas() {
        if (vista.getIndiceMonedaSeleccionada() >= 0) {
            LocalDate desde = vista.getFechaDesde();
            LocalDate hasta = vista.getFechaHasta();
            var moneda = monedas.get(vista.getIndiceMonedaSeleccionada());
            var datosFiltrados = MonedasServicio.filtrar(cambiosMoneda, moneda, desde, hasta);
            var valoresCambios = MonedasServicio.getValores(datosFiltrados);
            var estadisticas = MonedasServicio.getEstadisticas(valoresCambios);

            vista.mostrarEstadisticas(estadisticas);
        }
        // Cambiar a la pestaña de estadísticas
        vista.activarPestaña(1);

    }


}
