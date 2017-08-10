package com.mostlabs.app.lowpassmagneto;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.text.DecimalFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

public class MainActivity extends AppCompatActivity
    implements SensorEventListener, View.OnClickListener, AdapterView.OnItemSelectedListener {

    private SensorManager sensorManager;
    private Sensor magnetoSensor, accelSensor, gyroSensor;

    private LineChart lineChart;

    private TextView xText, yText, zText;
    private TextView xFilter, yFilter, zFilter;
    private Button playButton;
    private Spinner sensorList;

    private float[] sensorFiltered = new float[3];
    private float[] temp = new float[3];
    private int sensorIndex = 0;

    private boolean sensorState = false;

    DecimalFormat df = new DecimalFormat("#.#####");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        magnetoSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        xText = (TextView) findViewById(R.id.xText);
        yText = (TextView) findViewById(R.id.yText);
        zText = (TextView) findViewById(R.id.zText);
        xFilter = (TextView) findViewById(R.id.xFilText);
        yFilter = (TextView) findViewById(R.id.yFilText);
        zFilter = (TextView) findViewById(R.id.zFilText);

        //
        // Konfigurasi dasar untuk LineChart
        //
        lineChart = (LineChart) findViewById(R.id.chart);
//        lineChart.setOnChartValueSelectedListener(this);

        lineChart.setBackgroundColor(Color.BLACK);
        lineChart.setDrawGridBackground(false);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);
        lineChart.setData(data);

        // Mengatur legend pada grafik
        Legend l = lineChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        // Mengatur axis X pada grafik
        XAxis xl = lineChart.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(true);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        // Mengatur axis Y pada grafik
        YAxis leftAxis = lineChart.getAxisLeft();;
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMaxValue(50f);
        leftAxis.setAxisMinValue(-50f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);

        sensorList = (Spinner)findViewById(R.id.sensorSpinner);
        sensorList.setOnItemSelectedListener(this);

        playButton = (Button) findViewById(R.id.playButton);

        playButton.setOnClickListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = 0.97f;

        switch (event.sensor.getType()) {
            case Sensor.TYPE_MAGNETIC_FIELD:
                temp = event.values.clone();

                xText.setText(String.format("%.5f",temp[0]));
                yText.setText(String.format("%.5f",temp[1]));
                zText.setText(String.format("%.5f",temp[2]));
                addEntr(temp);

                sensorFiltered[0] = alpha * sensorFiltered[0] + (1 - alpha)
                        * event.values[0];
                sensorFiltered[1] = alpha * sensorFiltered[1] + (1 - alpha)
                        * event.values[1];
                sensorFiltered[2] = alpha * sensorFiltered[2] + (1 - alpha)
                        * event.values[2];

                xFilter.setText(String.format("%.5f",sensorFiltered[0]));
                yFilter.setText(String.format("%.5f",sensorFiltered[1]));
                zFilter.setText(String.format("%.5f",sensorFiltered[2]));
                break;

            case Sensor.TYPE_ACCELEROMETER:
                temp = event.values.clone();

                xText.setText(String.format("%.5f",temp[0]));
                yText.setText(String.format("%.5f",temp[1]));
                zText.setText(String.format("%.5f",temp[2]));
                addEntr(temp);

                sensorFiltered[0] = alpha * sensorFiltered[0] + (1 - alpha)
                        * event.values[0];
                sensorFiltered[1] = alpha * sensorFiltered[1] + (1 - alpha)
                        * event.values[1];
                sensorFiltered[2] = alpha * sensorFiltered[2] + (1 - alpha)
                        * event.values[2];

                xFilter.setText(String.format("%.5f",sensorFiltered[0]));
                yFilter.setText(String.format("%.5f",sensorFiltered[1]));
                zFilter.setText(String.format("%.5f",sensorFiltered[2]));
                break;

            case Sensor.TYPE_GYROSCOPE:
                temp = event.values.clone();

                xText.setText(String.format("%.5f",temp[0]));
                yText.setText(String.format("%.5f",temp[1]));
                zText.setText(String.format("%.5f",temp[2]));
                addEntr(temp);

                sensorFiltered[0] = alpha * sensorFiltered[0] + (1 - alpha)
                        * event.values[0];
                sensorFiltered[1] = alpha * sensorFiltered[1] + (1 - alpha)
                        * event.values[1];
                sensorFiltered[2] = alpha * sensorFiltered[2] + (1 - alpha)
                        * event.values[2];

                xFilter.setText(String.format("%.5f",sensorFiltered[0]));
                yFilter.setText(String.format("%.5f",sensorFiltered[1]));
                zFilter.setText(String.format("%.5f",sensorFiltered[2]));
                break;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.playButton:
                if (!sensorState) {
                    switch (sensorIndex) {
                        case 0:
                            sensorManager.registerListener(this, accelSensor,
                                    SensorManager.SENSOR_DELAY_GAME);
                            break;
                        case 1:
                            sensorManager.registerListener(this, gyroSensor,
                                    SensorManager.SENSOR_DELAY_GAME);
                            break;
                        case 2:
                            sensorManager.registerListener(this, magnetoSensor,
                                    SensorManager.SENSOR_DELAY_GAME);
                            break;
                    }

                    playButton.setText("Pause the sensor");
                    sensorState = true;
                }
                else {
                    sensorManager.unregisterListener(this);
                    playButton.setText("Run the sensor");
                    sensorState = false;
                }
        }
    }

    private void addEntr(float[] values) {
        LineData data = lineChart.getData();

        if (data != null) {
            ILineDataSet set = data.getDataSetByIndex(0);
            ILineDataSet set1 = data.getDataSetByIndex(1);
            ILineDataSet set2 = data.getDataSetByIndex(2);

            if (set == null) {
                set = xSet();
                set1 = ySet();
                set2 = zSet();

                data.addDataSet(set);
                data.addDataSet(set1);
                data.addDataSet(set2);
            }

            data.addXValue("");

            data.addEntry(new Entry(values[0], set.getEntryCount()), 0);
            data.addEntry(new Entry(values[1], set1.getEntryCount()), 1);
            data.addEntry(new Entry(values[2], set2.getEntryCount()), 2);

            data.setDrawValues(false);
            data.setHighlightEnabled(false);

            // memberitahukan pada kontrol grafik bahwa data telah berubah
            lineChart.notifyDataSetChanged();

            // Membatasi data yang terlihat pada grafik
            lineChart.setVisibleXRange(0, 100);

            // Untuk memunculkan grafik terbaru
            lineChart.moveViewToX(data.getXValCount() - (100 + 1));
        }
    }

    private LineDataSet xSet() {

        LineDataSet set = new LineDataSet(null, "X Value");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.RED);
        set.setCircleColor(Color.RED);
        set.setLineWidth(1f);
        set.setCircleRadius(1f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.RED);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }

    private LineDataSet ySet() {

        LineDataSet set = new LineDataSet(null, "Y Value");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.GREEN);
        set.setCircleColor(Color.GREEN);
        set.setLineWidth(1f);
        set.setCircleRadius(1f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.GREEN);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }

    private LineDataSet zSet() {

        LineDataSet set = new LineDataSet(null, "Z Value");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.BLUE);
        set.setCircleColor(Color.BLUE);
        set.setLineWidth(1f);
        set.setCircleRadius(1f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.BLUE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        sensorManager.unregisterListener(this);

        sensorIndex = position;
        sensorState = false;

        playButton.setText("Run the sensor");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
