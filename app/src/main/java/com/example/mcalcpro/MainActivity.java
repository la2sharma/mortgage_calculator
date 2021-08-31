// Name: Lakshay Sharma Student No. 217645367
package com.example.mcalcpro;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import ca.roumani.i2c.MPro;


public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, SensorEventListener
{

    private  TextToSpeech tts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.tts = new TextToSpeech(this, this);
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sm.registerListener(this, sm.getDefaultSensor((Sensor.TYPE_ACCELEROMETER)), SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public void onInit(int status)
    {
        this.tts.setLanguage(Locale.US);
    }
    @SuppressLint("DefaultLocale")
    public void analyse (View v)
    {
        EditText cash = (EditText) findViewById(R.id.cashPrice);
        EditText amort = (EditText) findViewById(R.id.amortization);
        EditText interest = (EditText) findViewById(R.id.interest);
        TextView result = (TextView) findViewById(R.id.output);
        String cashS = cash.getText().toString();
        String amortS = amort.getText().toString();
        String interestS = interest.getText().toString();
        try
        {
            MPro mp = new MPro();
            mp.setPrinciple(cashS);
            mp.setAmortization(amortS);
            mp.setInterest(interestS);
            String s = "Monthly Payment = " + mp.computePayment("%,.2f");

            s += "\n\n";s += "By making this payments for " + amortS + " years, the mortgage on its nth " +
                "anniversary, the balance still owing depends on n as show below";
            s += "\n\n"; s += String.format("%8s" , "n") + String.format("%16s" , "Balance");
            s += "\n\n"; s += String.format("%8d" , 0) + mp.outstandingAfter(0, "%,16.0f");
            s += "\n\n"; s += String.format("%8d" , 1) + mp.outstandingAfter(1, "%,16.0f");
            s += "\n\n"; s += String.format("%8d" , 2) + mp.outstandingAfter(2, "%,16.0f");
            s += "\n\n"; s += String.format("%8d" , 3) + mp.outstandingAfter(3, "%,16.0f");
            s += "\n\n"; s += String.format("%8d" , 4) + mp.outstandingAfter(4, "%,16.0f");
            s += "\n\n"; s += String.format("%8d" , 5) + mp.outstandingAfter(5, "%,16.0f");
            s += "\n\n"; s += String.format("%8d" , 10) + mp.outstandingAfter(10, "%,16.0f");
            s += "\n\n"; s += String.format("%8d" , 15) + mp.outstandingAfter(15, "%,16.0f");
            s += "\n\n"; s += String.format("%8d" , 20) + mp.outstandingAfter(20, "%,16.0f");

            result.setText(s);
            tts.speak( mp.computePayment("%.2f") , TextToSpeech.QUEUE_FLUSH , null);
        }

        catch (Exception e)
        {
            Toast label = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            label.show();
        }
    }
    public void onAccuracyChanged(Sensor arg0, int arg1)
    {
    }

    public void onSensorChanged(SensorEvent event)
    {
        double ax = event.values[0];
        double ay = event.values[1];
        double az = event.values[2];
        double a = Math.sqrt(ax*ax + ay*ay + az*az);
        if(a > 10)
        {
            ((EditText) findViewById(R.id.cashPrice)).setText("");
            ((EditText) findViewById(R.id.amortization)).setText("");
            ((EditText) findViewById(R.id.interest)).setText("");
            ((TextView) findViewById(R.id.output)).setText("");
        }
    }
}
