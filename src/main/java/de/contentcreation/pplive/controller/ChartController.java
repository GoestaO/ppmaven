 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.contentcreation.pplive.controller;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;

/**
 *
 * @author gostendorf
 */
@Named
@RequestScoped
public class ChartController implements Serializable {
    
    private HashMap<Integer, HashMap<String, Long>> getChartHashMap(List<Object[]> resultList, boolean singleUser) {
        
        LinkedHashMap<Integer, HashMap<String, Long>> map = new LinkedHashMap<>();
        
        int key;
        String name = "";
        for (Object[] o : resultList) {
            key = Integer.parseInt(o[0].toString());
            if (!singleUser) {
                name = (String) o[1];
            } else {
                name = (String) o[2];
            }
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
    
    public LineChartModel createLinechartModel(List<Object[]> resultList, boolean singleUser, int xMin, String title, String xAxisLabel, String yAxisLabel) {
        if (resultList.size() > 0) {
            HashMap<Integer, HashMap<String, Long>> data = getChartHashMap(resultList, singleUser);
            LineChartModel model = new LineChartModel();
            model = populateData(model, data);
            model.setTitle(title);
            model.setLegendPosition("e");
            model.setShowPointLabels(true);
            model.setExtender("ext");
            Axis xAxis = model.getAxis(AxisType.X);
            xAxis.setLabel(xAxisLabel);
            xAxis.setMin(xMin);
            xAxis.setTickInterval("1");
            Axis yAxis = model.getAxis(AxisType.Y);
            yAxis.setMin(0);
            yAxis.setLabel(yAxisLabel);
            return model;
        }
        return null;
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

    // Methoden für die Charts mit Datum
    private HashMap<String, HashMap<String, Long>> getDateChartHashMap(List<Object[]> resultList, boolean singleUser) {
        
        LinkedHashMap<String, HashMap<String, Long>> map = new LinkedHashMap<>();
        
        String day;
        String name = "";
        for (Object[] o : resultList) {
            day = (String) o[0];
            if (!singleUser) {
                name = (String) o[1];
            } else {
                name = (String) o[2];
            }
            Long anzahl = (Long) o[3];
            
            if (map.containsKey(day)) {
                map.get(day).put(name, anzahl);
            } else {
                map.put(day, new HashMap<String, Long>());
                map.get(day).put(name, anzahl);
            }
        }
        
        return map;
    }
    
    public LineChartModel createDateLinechartModel(List<Object[]> resultList, boolean singleUser, int xMin, String title, String xAxisLabel, String yAxisLabel) {
        if (resultList.size() > 0) {
            HashMap<String, HashMap<String, Long>> data = getDateChartHashMap(resultList, singleUser);
            LineChartModel model = new LineChartModel();
            model = populateDateData(model, data);
            model.setTitle(title);
            model.setLegendPosition("e");
            model.setShowPointLabels(true);
//            model.setExtender("ext");
//            Axis xAxis = model.getAxis(AxisType.X);
//            xAxis.setLabel(xAxisLabel);
//            xAxis.setMin(xMin);            
//            xAxis.setTickInterval("1");
            DateAxis axis = new DateAxis("Tag");
            axis.setTickAngle(-50);
//            axis.setMax("2014-02-01");
            axis.setTickFormat("%d.%m.%y");
//            axis.setTickInterval("1 day");
            model.getAxes().put(AxisType.X, axis);            
            Axis yAxis = model.getAxis(AxisType.Y);
            yAxis.setMin(0);
            yAxis.setLabel(yAxisLabel);
            return model;
        }
        return null;
    }
    
    private LineChartModel populateDateData(LineChartModel model, HashMap<String, HashMap<String, Long>> data) {
        
        Collection<HashMap<String, Long>> nameswerte = data.values();
        
        List<String> namen = new ArrayList<>();
        for (HashMap<String, Long> nameswert : nameswerte) {
            namen.addAll(nameswert.keySet());
        }
        Set<String> namesSet = new HashSet<>(namen);

        // Für jede Station im Set eine ChartSerie erstellen
        for (String name : namesSet) {
            ChartSeries series = createDateChartSeries(name, data);
            model.addSeries(series);
        }
        return model;
    }
    
    private ChartSeries createDateChartSeries(String name, HashMap<String, HashMap<String, Long>> data) {
        ChartSeries series = new ChartSeries();
        series.setLabel(name);
        Set<String> kalenderwochen = data.keySet();
        
        for (String kw : kalenderwochen) {
            Long anzahl = data.get(kw).get(name);
            if (anzahl != null) {
                series.set(kw, anzahl);
            } else {
                series.set(kw, 0);
            }
        }
        return series;
    }
    
//    HashMap<?, ?> hashMap;
    
}
