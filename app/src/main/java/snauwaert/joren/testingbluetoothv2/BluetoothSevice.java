package snauwaert.joren.testingbluetoothv2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


// repertoire

public class BluetoothSevice extends AppCompatActivity {
    private static final String TAG = "MainActivity"; //defining tag to use for logging

    //Defining global variables
    LinearLayout MainActivityLayout;
    PopupWindow pairedDevicesWindow;
    BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting the bluetoothadapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // defining the ONNOFF button
        final Button btnONNOFF = findViewById(R.id.btnONOFF);
        btnONNOFF.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EnableBluetooth();// Do something in response to button click
            }
        });

        //Defining the btnpaired list
        final Button btnPairedList = findViewById(R.id.btnPairedList);

        MainActivityLayout = findViewById(R.id.MainActivityLayout);

        btnPairedList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConDevPopup();


            }
        });

    }

    private void ConDevPopup (){

        //instantiate the popup.xml layout file
        LayoutInflater layoutInflater = (LayoutInflater) BluetoothSevice.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.paired_devices,null);

        final ListView PairedDebListView = customView.findViewById(R.id.PairedDevlist);

        // Initializing a new String Array
        String[] devices = new String[] {};

        // Create a List from String Array elements
        final List<String> PairedDevList = new ArrayList(Arrays.asList(devices));

        // Create an ArrayAdapter from List
        final ArrayAdapter<String> ConDevArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, PairedDevList);

        // DataBind ListView with items from ArrayAdapter
        PairedDebListView.setAdapter(ConDevArrayAdapter);

        GetPairedBluetooth(ConDevArrayAdapter,PairedDevList);

        //instantiate popup window
        pairedDevicesWindow = new PopupWindow(customView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //display the popup window
        pairedDevicesWindow.showAtLocation(MainActivityLayout, Gravity.CENTER, 0, 0);


    }

    // checking if the device has a bluetooth adapter and if it is turned on
    private void EnableBluetooth(){

        int REQUEST_ENABLE_BT =1; // placeholder for the request enabled


        if (mBluetoothAdapter == null) {
            Log.i(TAG, "EnableBluetooth: no adapter found"); // Device doesn't support Bluetooth
        }

        if (!mBluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT); // the REQUEST_ENABLE_BT is the answer of the app if the addaptor works
            //TODO: integrate the REQUEST_ENABLE_BT feedback
        }
    }

    //getting the paired devices
    private void GetPairedBluetooth(ArrayAdapter<String> ConDevArrayAdapter,List<String> PairedDevList){
        Set <BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                Log.i(TAG, "GetPairedBluetooth: devicename:"+ deviceName);
                Log.i(TAG, "GetPairedBluetooth: devicehardware adress:"+ deviceHardwareAddress);
                PairedDevList.add(deviceName);
                ConDevArrayAdapter.notifyDataSetChanged();
            }
        }
    }

}