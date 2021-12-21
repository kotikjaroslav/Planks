package com.github.bc29ea3c101054baa1429ffc2edba4ae.planks;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class StatsActivity extends AppCompatActivity {

    // DECLARATION
    BarChart barChart;

    final Thread threadBuild = new Thread() {
        public void run() {
            String title = "time(sec)";
            List<Record> records;
            RecordDao recordDAO;
            ArrayList<Long> valueList = new ArrayList<>();
            ArrayList<BarEntry> entries = new ArrayList<>();
            ArrayList<String> xVals = new ArrayList<>();

            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "Record").build();
            recordDAO = db.recordDao();
            records = recordDAO.getAll();

            // farby stlpcov
            int zelenyStlpec = rgb("#8CC176");
            int cervenyStlpec = rgb("#D24B44");
            List<Integer> colors = new ArrayList<>();
            long predoslaHodnotaGrafu = 0;

            // hodnoty do grafu
            for (int i = 0; i < records.size(); i++) {
                // hodnoty v sec
                long rec = records.get(i).rScore / 1000;
                if (rec > predoslaHodnotaGrafu) {
                    colors.add(zelenyStlpec);
                } else {
                    colors.add(cervenyStlpec);
                }
                predoslaHodnotaGrafu = rec;
                valueList.add(rec);
                // ziskany cas konvertnem do formatu dd.mm pokial je viac ako 24 hodin stary inak zobrazim
                // cas vo formate hh:mm
                xVals.add(formatDatum(records.get(i).rDate));

            }

            // vytvorime stlpce
            for (int j = 0; j < records.size(); j++) {
                BarEntry barEntry = new BarEntry(j, valueList.get(j));
                entries.add(barEntry);
            }

            BarDataSet barDataSet = new BarDataSet(entries, title);
            barDataSet.setColors(colors);
            barDataSet.setHighlightEnabled(false);
            barDataSet.setValueTextColor(rgb("#bdc3c9"));
            // format textu
            MDecimalFormatter formatter = new MDecimalFormatter();
            barDataSet.setValueFormatter(formatter);
            BarData data = new BarData(barDataSet);

            // disable description text in chart
            Description description = new Description();
            description.setEnabled(true);
            description.setText("time(sec)");
            description.setTextSize(10);
            description.setTextColor(rgb("#bdc3c9"));

            // legenda
            barChart.getLegend().setEnabled(false);

            // X axis
            XAxis xAxis = barChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            xAxis.setTextColor(rgb("#bdc3c9"));
            xAxis.setDrawGridLines(false);
            xAxis.setGranularityEnabled(true);
            xAxis.setGranularity(1f);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));

            // Y axis
            YAxis rightAxis = barChart.getAxisRight();
            rightAxis.setTextColor(rgb("#bdc3c9"));
            rightAxis.setDrawAxisLine(false);

            YAxis leftAxis = barChart.getAxisLeft();
            leftAxis.setTextColor(rgb("#bdc3c9"));
            leftAxis.setDrawAxisLine(false);

            barChart.setDescription(description);
            barChart.setData(data);
            barChart.setVisibleXRangeMaximum(20);
            barChart.moveViewToX(records.size());

        }
    };

    public String formatDatum(long timestamp){
        // ak je timestamp starsi ako 24 hodin vrat format [cislo v mesiaci].[mesiac]
        if (timestamp + 24*60*60*1000 > System.currentTimeMillis()){
            return (String) android.text.format.DateFormat.format("HH:mm", timestamp);
        } else {
            return (String) android.text.format.DateFormat.format("dd.MM HH:mm", timestamp);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        barChart = findViewById(R.id.chart);
        threadBuild.start();
    }
}