package Kelas;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

public class XAxisValueFormatter implements IAxisValueFormatter {

    private String[] labels;
    private String namaBulan;

    public XAxisValueFormatter(String[] values) {
        labels = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        String nilaiIndex =  (String.valueOf(value));
        String[] separated = nilaiIndex.split(".");
        Log.d("nilaiIndex:",""+nilaiIndex);
        Log.d("separeted:",separated[0]);

        for (int c=0;c<this.labels.length;c++){

            namaBulan = labels[c].toString();
        }
         //return this.labels[(int) value];
        return namaBulan;
    }
}
