 
import com.xeiam.xchart.BitmapEncoder;
import com.xeiam.xchart.BitmapEncoder.BitmapFormat;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesMarker;
import com.xeiam.xchart.VectorGraphicsEncoder;
import com.xeiam.xchart.VectorGraphicsEncoder.VectorGraphicsFormat;
 
/**
 * Creates a simple Chart and saves it as a PNG and JPEG image file.
 */
public class XChartSample {
 
  public static void main(String[] args) throws Exception {
 
    double[] yData = new double[] { 2.0, 1.0, 0.0 };
 
    // Create Chart
    Chart chart = new Chart(500, 400);
    chart.setChartTitle("Sample Chart");
    chart.setXAxisTitle("X");
    chart.setYAxisTitle("Y");
    Series series = chart.addSeries("y(x)", null, yData);
    series.setMarker(SeriesMarker.CIRCLE);
 
    BitmapEncoder.saveBitmapWithDPI(chart, "./Sample_Chart_300_DPI", BitmapFormat.JPG, 300);
  }
}