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
    
    public HashMap<Integer, HashMap<String, Long>> getChartHashMap(List<Object[]> resultList) {
        LinkedHashMap<Integer, HashMap<String, Long>> map = new LinkedHashMap<>();
        int key;
        for (Object[] o : resultList) {
            key = Integer.parseInt(o[0].toString());
            String name = (String) o[1];
            Long anzahl = (Long) o[3];
            
            if (map.containsKey(key)) {
                map.get(key).put(name, anzahl);
            } else {
                map.put(key, new HashMap<String, Long>());
                map.get(key).put(name, anzahl);
            }
        }
        return map;
    }
    
    public LineChartModel createLinechartModel(List<Object[]> resultList, String title, String xAxisLabel, String yAxisLabel) {
        HashMap<Integer, HashMap<String, Long>> data = getChartHashMap(resultList);
        LineChartModel model = new LineChartModel();
        model = populateData(model, data);
        model.setTitle(title);
        model.setLegendPosition("e");
        model.setShowPointLabels(true);
        model.setShowDatatip(false);
        Axis xAxis = model.getAxis(AxisType.X);
        xAxis.setLabel(xAxisLabel);
//        xAxis.setTickInterval("10");
//        xAxis.setTickFormat("%b");
//        System.out.println(xAxis.getTickInterval());
        Axis yAxis = model.getAxis(AxisType.Y);
        yAxis.setLabel(yAxisLabel);
//        yAxis.setMin(0);
//        yAxis.setMax(80.00);
        return model;
    }
    
    private LineChartModel populateData(LineChartModel model, HashMap<Integer, HashMap<String, Long>> data) {
        
        Collection<HashMap<String, Long>> nameswerte = data.values();
        
        List<String> namen = new ArrayList<>();
        for (HashMap<String, Long> nameswert : nameswerte) {
            namen.addAll(nameswert.keySet());
        }
        Set<String> namesSet = new HashSet<>(namen);

        // Für jede Station im Set eine ChartSerie erstellen
        for (String name : namesSet) {
            ChartSeries series = createChartSeries(name, data);
            model.addSeries(series);
        }
        return model;
    }
    
    private ChartSeries createChartSeries(String name, HashMap<Integer, HashMap<String, Long>> data) {
        ChartSeries series = new ChartSeries();
        series.setLabel(name);
        Set<Integer> kalenderwochen = data.keySet();
        
        for (int kw : kalenderwochen) {
            Long anzahl = data.get(kw).get(name);
            if (anzahl != null) {
                series.set(kw, anzahl);
            } else {
                series.set(kw, 0);
            }
        }
        return series;
    }
    
}
