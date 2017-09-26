package com.beiing.leafchartdemo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.beiing.leafchart.OutsideLineChart;
import com.beiing.leafchart.bean.Axis;
import com.beiing.leafchart.bean.AxisValue;
import com.beiing.leafchart.bean.Line;
import com.beiing.leafchart.bean.PointValue;

import java.util.ArrayList;
import java.util.List;

public class OutsideLineChartActivity extends AppCompatActivity {
    OutsideLineChart outsideLineChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outside_line_chart);

        outsideLineChart = (OutsideLineChart) findViewById(R.id.outside_linechart);

        initLineChart();
    }


    private void initLineChart() {
        Axis axisX = new Axis(getAxisValuesX());
        axisX.setAxisColor(Color.parseColor("#33B5E5")).setTextColor(Color.DKGRAY).setHasLines(true);
        Axis axisY = new Axis(getAxisValuesY());
        axisY.setAxisColor(Color.parseColor("#33B5E5")).setTextColor(Color.DKGRAY).setHasLines(true).setShowText(true);
        outsideLineChart.setAxisX(axisX);
        outsideLineChart.setAxisY(axisY);

        outsideLineChart.setChartData(getFoldLine());

        outsideLineChart.showWithAnimation(1000);

    }

    private List<AxisValue> getAxisValuesX(){
        List<AxisValue> axisValues = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            AxisValue value = new AxisValue();
            value.setLabel(i + "月");
            axisValues.add(value);
        }
        return axisValues;
    }

    private List<AxisValue> getAxisValuesY(){
        List<AxisValue> axisValues = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            AxisValue value = new AxisValue();
            value.setLabel(String.valueOf(i * 10));
            axisValues.add(value);
        }
        return axisValues;
    }

    private Line getFoldLine(){
        List<PointValue> pointValues = new ArrayList<>();

        PointValue p = new PointValue();
        p.setX( (1 - 1) / 11f);
        p.setLabel(String.valueOf(90));
        p.setY(90 / 100f);
        pointValues.add(p);
        //=========
        PointValue p2 = new PointValue();
        p2.setX( (2 - 1) / 11f);
        p2.setLabel(String.valueOf(8));
        p2.setY(8 / 100f);
        pointValues.add(p2);
        for (int i = 3; i <= 12; i++) {
            PointValue pointValue = new PointValue();
            pointValue.setX( (i - 1) / 11f);
//            int var = (int) (Math.random() * 100);
            int var = 25;
            pointValue.setLabel(String.valueOf(var));
            pointValue.setY(var / 100f);
            pointValues.add(pointValue);
        }


        Line line = new Line(pointValues);
        line.setLineColor(Color.parseColor("#33B5E5"))
                .setLineWidth(3)
                .setPointColor(Color.YELLOW)
                .setCubic(true)
                .setPointRadius(3)
                .setFill(true)
                .setFillColor(Color.parseColor("#33B5E5"))
                .setHasLabels(true)
                .setLabelColor(Color.parseColor("#33B5E5"));
        return line;
    }

}
