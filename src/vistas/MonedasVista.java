package vistas;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

import javax.swing.*;


import datechooser.beans.DateChooserCombo;
import modelos.CambioMoneda;
import org.jfree.chart.ChartPanel;

public class MonedasVista extends JFrame {

    private JComboBox cmbMoneda;
    private DateChooserCombo dccDesde, dccHasta;
    private JTabbedPane tpCambiosMoneda;
    private JPanel pnlGrafica;
    private JPanel pnlEstadisticas;

    JButton btnGraficar, btnCalcularEstadisticas;

    private List<String> monedas;

    public MonedasVista() {

        setTitle("Cambios de Monedas");
        setSize(700, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JToolBar tb = new JToolBar();

        btnGraficar = new JButton();
        btnGraficar.setIcon(new ImageIcon(getClass().getResource("/iconos/Grafica.png")));
        btnGraficar.setToolTipText("Grafica Cambios vs Fecha");
        tb.add(btnGraficar);

        btnCalcularEstadisticas = new JButton();
        btnCalcularEstadisticas.setIcon(new ImageIcon(getClass().getResource("/iconos/Datos.png")));
        btnCalcularEstadisticas.setToolTipText("Estadísticas de la moneda seleccionada");
        tb.add(btnCalcularEstadisticas);

        // Contenedor con BoxLayout (vertical)
        JPanel pnlCambios = new JPanel();
        pnlCambios.setLayout(new BoxLayout(pnlCambios, BoxLayout.Y_AXIS));

        JPanel pnlDatosProceso = new JPanel();
        pnlDatosProceso.setPreferredSize(new Dimension(pnlDatosProceso.getWidth(), 50)); // Altura fija de 100px
        pnlDatosProceso.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        pnlDatosProceso.setLayout(null);

        JLabel lblMoneda = new JLabel("Moneda");
        lblMoneda.setBounds(10, 10, 100, 25);
        pnlDatosProceso.add(lblMoneda);

        cmbMoneda = new JComboBox();
        cmbMoneda.setBounds(110, 10, 100, 25);
        pnlDatosProceso.add(cmbMoneda);

        dccDesde = new DateChooserCombo();
        dccDesde.setBounds(220, 10, 100, 25);
        pnlDatosProceso.add(dccDesde);

        dccHasta = new DateChooserCombo();
        dccHasta.setBounds(330, 10, 100, 25);
        pnlDatosProceso.add(dccHasta);

        pnlGrafica = new JPanel();
        JScrollPane spGrafica = new JScrollPane(pnlGrafica);

        pnlEstadisticas = new JPanel();

        tpCambiosMoneda = new JTabbedPane();
        tpCambiosMoneda.addTab("Gráfica", spGrafica);
        tpCambiosMoneda.addTab("Estadísticas", pnlEstadisticas);

        // Agregar componentes
        pnlCambios.add(pnlDatosProceso);
        pnlCambios.add(tpCambiosMoneda);

        getContentPane().add(tb, BorderLayout.NORTH);
        getContentPane().add(pnlCambios, BorderLayout.CENTER);

    }

    // getters

    public int getIndiceMonedaSeleccionada() {
        return cmbMoneda.getSelectedIndex();
    }

    public LocalDate getFechaDesde() {
        return dccDesde.getSelectedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public LocalDate getFechaHasta() {
        return dccHasta.getSelectedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    // setters
    public void setGraficarClick(ActionListener evento) {
        btnGraficar.addActionListener(evento);
    }

    public void setCalcularEstadisticasClick(ActionListener evento) {
        btnCalcularEstadisticas.addActionListener(evento);
    }

    public void setMonedas(List<String> monedas) {
        cmbMoneda.setModel(new DefaultComboBoxModel(monedas.toArray()));
    }

    // metodos publicos
    public void activarPestaña(int posicion) {
        if (posicion >= 0 && posicion < tpCambiosMoneda.getTabCount()) {
            tpCambiosMoneda.setSelectedIndex(posicion);
        }
    }

    public void graficar(ChartPanel pnlGraficador) {
        pnlGrafica.removeAll();
        pnlGrafica.setLayout(new BorderLayout());
        pnlGrafica.add(pnlGraficador, BorderLayout.CENTER);
        pnlGrafica.revalidate();
    }

    public void mostrarEstadisticas(Map<String, Double> estadisticas) {
        pnlEstadisticas.removeAll();
        pnlEstadisticas.setLayout(new GridBagLayout());
        var gbc = new GridBagConstraints();
        gbc.gridy = 0;
        estadisticas.forEach((clave, valor) -> {
            gbc.gridx = 0;
            pnlEstadisticas.add(new JLabel(clave), gbc);
            gbc.gridx = 1;
            pnlEstadisticas.add(new JLabel(String.format("%.2f", valor)), gbc);
            gbc.gridy++;
        });

        pnlEstadisticas.revalidate();
    }
}
