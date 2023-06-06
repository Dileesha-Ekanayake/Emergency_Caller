package com.example.emergency_caller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    SensorManager sm = null;
    EditText txtX = null;
    EditText txtY = null;
    EditText txtZ = null;
    List list;
    float[] value;
    SensorEventListener sel;
    String number;
    Intent call;
    Button btnReset;
    EditText txtNumber;
    Button btnAdd;
    AlertDialog.Builder alert1;
    AlertDialog.Builder alert2;
    AlertDialog.Builder alert3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sm = (SensorManager)getSystemService(SENSOR_SERVICE);
        list = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);

        txtX = this.findViewById(R.id.txtX);
        txtY = this.findViewById(R.id.txtY);
        txtZ = this.findViewById(R.id.txtZ);

        txtNumber = this.findViewById(R.id.txtNumber);
        btnAdd = this.findViewById(R.id.btnAdd);
        btnReset = this.findViewById(R.id.btnReset);
        alert1 = new AlertDialog.Builder(this);
        alert2 = new AlertDialog.Builder(this);
        alert3 = new AlertDialog.Builder(this);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number = txtNumber.getText().toString();
                if(number.isEmpty()){
                    alert1.setMessage("Please Enter a Emergency Number..");
                    alert1.setPositiveButton("OK",null);
                    alert1.show();
                }else {
                    txtNumber.setEnabled(false);
                    btnAdd.setEnabled(false);
                    btnReset.setEnabled(true);
                    call = new Intent();
                    call.setAction(Intent.ACTION_CALL);
                    call.setData(Uri.parse("tel:" + number));
                }
            }
        });

        sel = new SensorEventListener(){
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
            @SuppressLint("SetTextI18n")
            public void onSensorChanged(SensorEvent event) {
                value = event.values;
                txtX.setText(": " + value[0]);
                txtY.setText(": " + value[1]);
                txtZ.setText(": " + value[2]);

                if (number != null && event.values[2] > 20){
                    startActivity(call);
                }
            }
        };

        sm.registerListener(sel, (Sensor) list.get(0), SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void reset(View v){
        if(number != null){
            alert2.setMessage("Are You Sure To Reset..?");
            alert2.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    number = null;
                    txtNumber.setText("");
                    txtNumber.setEnabled(true);
                    btnAdd.setEnabled(true);
                    btnReset.setEnabled(false);
                }
            });
//            alert2.setNegativeButton("NO", (dialogInterface, i) -> {
//                btnAdd.setEnabled(false);
//            });
            alert2.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    btnAdd.setEnabled(false);
                }
            });
            alert2.show();
        }

    }
}