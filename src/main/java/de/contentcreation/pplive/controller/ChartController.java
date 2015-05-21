 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.contentcreation.pplive.controller;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

/**
 *
 * @author gostendorf
 */
@Named
@RequestScoped
public class ChartController implements Serializable {

    private HashMap<Integer, HashMap<String, Integer>> getChartHashMap(List<Object[]> resultList) {
        LinkedHashMap<Integer, HashMap<String, Integer>> map = new LinkedHashMap<>();
        int key;
        for (Object[] o : resultList) {
            key = Integer.parseInt(o[0].toString());
            String name = (String) o[2];
            Integer anzahl = (Integer) o[5];

            if (map.containsKey(key)) {
                map.get(key).put(name, anzahl);
            } else {
                map.put(key, new HashMap<String, Integer>());
                map.get(key).put(name, anzahl);
            }
        }
        return map;
    }

    public LineChartModel createBarModel(List<Object[]> resultList, String title, String xAxisLabel, String yAxisLabel) {
        HashMap<Integer, HashMap<String, Integer>> data = getChartHashMap(resultList);
        LineChartModel model = new LineChartModel();
        model = populateData(model, data);
        model.setTitle(title);
        model.setLegendPosition("e");
        model.setShowPointLabels(true);
        model.setShowDatatip(false);
        Axis xAxis = model.getAxis(AxisType.X);
        xAxis.setLabel(xAxisLabel);
        Axis yAxis = model.getAxis(AxisType.Y);
        yAxis.setLabel(yAxisLabel);
        yAxis.setMin(0.00);
        yAxis.setMax(80.00);
        return model;
    }

    private LineChartModel populateData(LineChartModel model, HashMap<Integer, HashMap<String, Integer>> data) {

        Collection<HashMap<String, Integer>> nameswerte = data.values();

        List<String> nameen = new ArrayList<>();
        for (HashMap<String, Integer> nameswert : nameswerte) {
            nameen.addAll(nameswert.keySet());
        }
        Set<String> namesSet = new HashSet<>(nameen);

        // Für jede Station im Set eine ChartSerie erstellen
        for (String name : namesSet) {
            ChartSeries series = createChartSeries(name, data);
            model.addSeries(series);
        }
        return model;
    }

    private ChartSeries createChartSeries(String name, HashMap<Integer, HashMap<String, Integer>> data) {
        ChartSeries series = new ChartSeries();
        series.setLabel(name);
        Set<Integer> kalenderwochen = data.keySet();

        for (int kw : kalenderwochen) {
            Integer anzahl = data.get(kw).get(name);
            if (anzahl != null) {
                series.set(kw, anzahl);
            } else {
                series.set(kw, 0);
            }
        }
        return series;
    }

}
