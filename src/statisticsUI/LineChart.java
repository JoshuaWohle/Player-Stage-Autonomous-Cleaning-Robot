package statisticsUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.text.SimpleDateFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.*;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

/**
 * This class is used to create the line chart to visually represent the statistics
 * @author Team Cyan
 *
 */

public class LineChart extends JPanel {

	
	private TimeSeriesCollection dataset = null;
	private JFreeChart chart = null;
	private ChartPanel chartpanel = null;
	private String xaxis = "";
	private String yaxis = "";
	private Color bgChartColor = new Color(03,20,27);
	private XYLineAndShapeRenderer renderer;
	
	
	/**
	 * 
	 * This is the Constructor used to create the line chart visually
	 * 
	 * @param chartTitle - specifying the name of the chart
	 * @param xaxis - the name of the values represented in the x-axis
	 * @param yaxis - the name of the value represented in the y-axis
	 */
	public LineChart(String chartTitle, String xaxis,String yaxis){
		super(new BorderLayout());
		this.dataset = new TimeSeriesCollection();
		this.xaxis = xaxis;
		this.yaxis = yaxis;
		chartpanel = new ChartPanel(createChart(this.dataset));
		this.setBackground(bgChartColor);
		this.add(chartpanel, BorderLayout.CENTER);
		JLabel jlTitle = new JLabel(chartTitle);
		jlTitle.setForeground(Color.CYAN);
		jlTitle.setFont(new Font("", Font.BOLD, 16));
		jlTitle.setHorizontalAlignment((int) JLabel.CENTER_ALIGNMENT);
		this.add(jlTitle, BorderLayout.NORTH);
		this.setPreferredSize(new Dimension(300,400));
		
	}	
	
	/**
	 * Method used to add data to the series
	 * 
	 * @param series - the series type e.g. Time,Data,Percentage
	 * @param sec - The representation of Data in the x-axis represented here using milliseconds
	 * @param y - The value to represent at that specific time
	 */
	public void addToSeries(TimeSeries series, FixedMillisecond sec, double y){
		
		try{
			series.add(sec, y);
		}
		catch(Exception e){}
	}
	
	
	/**
	 * Method is used to add the series to the chart and the line Colour represented 
	 * @param newSeries
	 * @param colour
	 */
	public void addSeries(TimeSeries newSeries, Color colour){
		dataset.addSeries(newSeries);
		renderer.setSeriesPaint(0, Color.CYAN);
	}
	
	/**
	 * Remove TimeSeries
	 * @param series
	 */
	public void removeSeries(TimeSeries series){
		dataset.removeSeries(series);
	}
	
	
	/**
	 * Method to modify the chart adding colours and labels
	 * @param dataset2
	 * @return
	 */
	private JFreeChart createChart(TimeSeriesCollection dataset2) {
		chart = ChartFactory.createTimeSeriesChart("", xaxis, yaxis, dataset2, true, true, false);
		chart.setBackgroundPaint(bgChartColor);
		chart.removeLegend();
		
		XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(bgChartColor);
        plot.setDomainGridlinesVisible(false);
        plot.setRangeGridlinesVisible(false);
        
        
        renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(1, false);
        renderer.setSeriesShapesVisible(0, false);
        
        plot.setRenderer(renderer);
        
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setTickLabelPaint(Color.CYAN);
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setLabelPaint(Color.CYAN);
        rangeAxis.setAxisLinePaint(Color.CYAN);
        
        
        DateAxis valueAxis = (DateAxis)(plot.getDomainAxis());
        valueAxis.setDateFormatOverride(new SimpleDateFormat("H:mm:ss"));
        valueAxis.setTickLabelPaint(Color.CYAN);
        valueAxis.setStandardTickUnits(DateAxis.createStandardDateTickUnits());
        valueAxis.setLabelPaint(Color.CYAN);
        valueAxis.setAxisLinePaint(Color.CYAN);
                
		return chart;
		
	}

}
	
	
