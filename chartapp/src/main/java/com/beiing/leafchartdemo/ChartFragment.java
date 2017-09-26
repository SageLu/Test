package com.beiing.leafchartdemo;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beiing.leafchart.LeafLineChart;
import com.beiing.leafchart.bean.Axis;
import com.beiing.leafchart.bean.AxisValue;
import com.beiing.leafchart.bean.Line;
import com.beiing.leafchart.bean.PointValue;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChartFragment extends Fragment {

    LeafLineChart lineChart;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public ChartFragment() {
        // Required empty public constructor
    }

    public static ChartFragment newInstance(String param1, String param2) {
        ChartFragment fragment = new ChartFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        lineChart = (LeafLineChart) view.findViewById(R.id.leaf_chart);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initLineChart();
            }
        }, 2000);

    }

    private void initLineChart() {
        //设置X、Y的坐标数字的颜色
        Axis axisX = new Axis(getAxisValuesX());
        axisX.setAxisColor(Color.parseColor("#33B5E5")).setTextColor(Color.RED).setHasLines(true);
        Axis axisY = new Axis(getAxisValuesY());
        axisY.setAxisColor(Color.parseColor("#33B5E5")).setTextColor(Color.BLUE).setHasLines(true).setShowText(true);
        lineChart.setAxisX(axisX);
        lineChart.setAxisY(axisY);
        List<Line> lines = new ArrayList<>();
        lines.add(getFoldLine());
//        lines.add(getCompareLine());
        lineChart.setChartData(lines);

        lineChart.showWithAnimation(3000);

    }

    /**
     * i值可以换成项目中的数字==》    value.setLabel(i + "月" + 1 + "日");
     * @return
     */
    private List<AxisValue> getAxisValuesX() {
        List<AxisValue> axisValues = new ArrayList<>();
        for (int i = 1; i <= 32; i++) {
            AxisValue value = new AxisValue();
            if (i == 1) {
                value.setLabel(i + "月" + 1 + "日");
            }else if (i == 31) {
                value.setLabel(1 + "月" + 30 + "日");
            }else {
                value.setLabel(i + "月");//必须设置 ,,要想不显示调用value.setShowLabel(false);
                value.setShowLabel(false);//设置是否显示坐标数字
            }


            axisValues.add(value);
        }
        return axisValues;
    }

    private List<AxisValue> getAxisValuesY() {
        List<AxisValue> axisValues = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            AxisValue value = new AxisValue();
            value.setLabel(String.valueOf(i * 10000));
            value.setShowLabel(false);//设置是否显示坐标数字
            axisValues.add(value);
        }
        return axisValues;
    }

    private Line getFoldLine() {
        List<PointValue> pointValues = new ArrayList<>();
        //i的最大值表示总共几个点
        for (int i = 1; i <= 3; i++) {
            PointValue pointValue = new PointValue();
            int var = (int) (Math.random() * 1000);

            if(i==1) {
                pointValue.setX((1 - 1) / 30f);
                pointValue.setLabel("15265.5");
                pointValue.setY(15265.5f/70000f);
            }
            if(i==2) {
                pointValue.setX((2 - 1) / 30f);
                pointValue.setLabel("2565.5");
                pointValue.setY(2565.5f/70000f);
            }
            if(i==3) {
                pointValue.setX((3 - 1) / 30f);
                pointValue.setLabel("500.5");
                pointValue.setY(500.5f/70000f);
            }

            pointValues.add(pointValue);
        }

        Line line = new Line(pointValues);
        line.setLineColor(Color.GREEN)
                .setLineWidth(1)
                .setPointColor(Color.GREEN)
                .setCubic(false)
                .setPointRadius(5)
                .setFill(false)
                .setHasLabels(true)
                .setLabelColor(Color.parseColor("#33B5E5"));
        return line;
    }

    private Line getCompareLine() {
        List<PointValue> pointValues = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            PointValue pointValue = new PointValue();
            pointValue.setX((i - 1) / 11f);
            int var = (int) (Math.random() * 100);
            pointValue.setLabel(String.valueOf(var));
            pointValue.setY(var / 100f);
            pointValues.add(pointValue);
        }

        Line line = new Line(pointValues);
        line.setLineColor(Color.BLUE)
                .setLineWidth(1)
                .setPointColor(Color.BLUE)
                .setCubic(false)
                .setPointRadius(5)
                .setFill(false)
                .setHasLabels(true)
                .setLabelColor(Color.BLUE);
        return line;
    }

}
