package com.rmc.thienle.jedi;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    ListView lv, mDeviceListView;
    private TextView mResultView;
    ArrayList<String> arrList=null;
    ArrayList<Entry> entryList=null;
    ArrayList<String> mDeviceArrayList=null;
    MyArrayAdapter adapter=null;
    MyArrayAdapter mDevicesAdapter=null;
    SharedPreferences sharedPrefs;
    private BluetoothAdapter mBluetoothAdapter;
    private MenuItem connectItem = null;
    private MenuItem synctItem = null;
    DBHelper mydb;
    ProgressDialog progress;
    private Handler progressBarHandler ;
    int entry_Index=0;

    /**
     * Member object for the chat services
     */
    private BluetoothService mBtService = null;
    public static final String ENTRY_PREFERENCES = "Thienkyo_switch_Shared_Preferences";
    public static final String ENTRY_KEY = "Entry_List_Shared_KEY";
    public static final String LAST_BLUDEVICE_KEY = "Last_Device_Addr_Shared_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle(R.string.app_name);
       // toolbar.setSubtitle("Sub");
        setSupportActionBar(toolbar);
        lv=(ListView) findViewById(R.id.entry_list);
        mResultView =(TextView) findViewById(R.id.temp_contain);

        if (savedInstanceState == null) {
            mBluetoothAdapter = ((BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
            // Is Bluetooth supported on this device?
            if (mBluetoothAdapter != null) {
                // Is Bluetooth turned on?
                if (!mBluetoothAdapter.isEnabled()) {
                    // Prompt user to turn on Bluetooth (logic continues in onActivityResult()).
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, Utility.REQUEST_ENABLE_BT);
                }
            } else {
                // Bluetooth is not supported.
                Toast.makeText(this, R.string.bt_not_supported, Toast.LENGTH_SHORT).show();
                //showErrorText(R.string.bt_not_supported);
            }
        }
        mydb = new DBHelper(this);

        //1. Tạo ArrayList object: entry
        arrList=new ArrayList();
        entryList = mydb.getFullEntries();
//        for (Entry item : entryList) {
//            Log.d(TAG, "item: "+ item.entry_name +" "+ item.toString());
//            arrList.add(item.toString());
//        }

        mDeviceArrayList = new ArrayList<>();
        //2. Gán Data Source (ArrayList object) vào ArrayAdapter
        adapter=new MyArrayAdapter(this, android.R.layout.simple_list_item_1, arrList);
        for (Entry item : entryList) {
            Log.d(TAG, "item: "+ item.entry_name +" "+ item.toString());
            adapter.add(item.toString());
        }

       // pairedDevices = mBluetoothAdapter.getBondedDevices();
        mDevicesAdapter = new MyArrayAdapter(this, android.R.layout.simple_list_item_1, mDeviceArrayList);


        //3. gán Adapter vào ListView
        lv.setAdapter(adapter);

        //4. Xử lý sự kiện nhấn nút Nhập
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                if(entryList.size() <=20) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", 0);
                    bundle.putString("val", "");
                    Intent myIntent = new Intent(MainActivity.this, ThirdActivity.class);
                    myIntent.putExtra("MyPackage", bundle);
                    startActivity(myIntent);
                }else{
                    Snackbar.make(view, R.string.max_number_entry, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
        //5. Xử lý sự kiện chọn một phần tử trong ListView
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                mResultView.setText("position : " + arg2 + "; value =" + arrList.get(arg2));
                Bundle bundle=new Bundle();
                bundle.putInt("id",entryList.get(arg2).getEntry_id());
                bundle.putString("val", entryList.get(arg2).toString());
                Intent myIntent = new Intent(MainActivity.this, ThirdActivity.class);
                myIntent.putExtra("MyPackage", bundle);
                startActivity(myIntent);
            }
        });
        //6. xử lý sự kiện Long click
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Log.d(TAG, "entry : "+ entryList.get(arg2).getEntry_id());
                mydb.deleteEntry(entryList.get(arg2).getEntry_id());
                loadDataFromPref();
               // arrList.remove(arg2);//xóa phần tử thứ arg2

               // adapter.notifyDataSetChanged();
                return true;
            }
        });
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
      //  IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(bReciever, filter);
        mBtService = new BluetoothService(getApplicationContext(), mHandler);
       // loadDataFromPref();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
        loadDataFromPref();
        mBluetoothAdapter = ((BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
        // Is Bluetooth supported on this device?
        if (mBluetoothAdapter != null) {
            // Is Bluetooth turned on?
            if (!mBluetoothAdapter.isEnabled()) {
                // Prompt user to turn on Bluetooth (logic continues in onActivityResult()).
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, Utility.REQUEST_ENABLE_BT);
            }
        } else {
            // Bluetooth is not supported.
            Toast.makeText(this, R.string.bt_not_supported, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(bReciever);
        mBtService.stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        connectItem = menu.findItem(R.id.action_connect);
        synctItem   = menu.findItem(R.id.action_sync);
        Log.d(TAG, "getState in onCreateOptionsMenu() :"+ mBtService.getState());
        setStatus(mBtService.getState());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_check_system) {
            chksys();
            return true;
        }
        if (id == R.id.action_sync) {

            progressBarHandler = new Handler();
            entry_Index = 0;
            progress = new ProgressDialog(this); // this = YourActivity
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setMessage("Syncing. Please wait...");
            progress.setProgress(0);
            progress.setMax(entryList.size());
            progress.setIndeterminate(false);
            progress.setCanceledOnTouchOutside(false);
            progress.show();

            new Thread(new Runnable() {
                @Override
                public void run()
                {
                    // do the thing that takes a long time
                    try {
                        mBtService.sync(entryList);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.e(TAG, "write data to bluetooth device in MainActivity 1234");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {
                            progress.dismiss();
                        }
                    });
                }
            }).start();
            return true;
        }
        if (id == R.id.action_sync_time) {
            syncTime();

            return true;
        }
        if (id == R.id.action_connect) {
           // mBtService.stop();
            String temp_addr = getLastBludeviceAddr();
            Log.d(TAG, "address:"+temp_addr);
            if(mBtService.getState() != BluetoothService.STATE_CONNECTED) {
                if (temp_addr == "") { // last device : no and paired devices : no
                    mBluetoothAdapter.startDiscovery();
                    showBluetoothDialog();
                    //item.setIcon(R.drawable.ic_bluetooth_enable);
                } else { //last device : yes
                    connect(temp_addr, true);
                }
            }
            return true;
        }
        if (id == R.id.action_delete_last_device) {
            manageLastDevice("");
            return true;
        }
        if (id == R.id.action_add_fake_last_device) {
            manageLastDevice("20:16:01:06:79:99");
            return true;
        }
        if (id == R.id.action_delete_all) {
            deleteDataPref();
            return true;
        }
        if (id == R.id.action_add_item) {
            addDataPref();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void showBluetoothDialog() {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View contentView = layoutInflater.inflate(R.layout.scan_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(contentView);
        alertDialogBuilder.setTitle("Device list");

        mDeviceListView=(ListView) contentView.findViewById(R.id.devicels);
        mDevicesAdapter.clear();
        mDevicesAdapter.add(getString(R.string.string_no_device));
        mDeviceListView.setAdapter(mDevicesAdapter);

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mBluetoothAdapter.cancelDiscovery();
                                mDevicesAdapter.clear();
                                dialog.cancel();
                            }
                        });
        // create an alert dialog
        final AlertDialog alert = alertDialogBuilder.create();
        alert.show();

        mDeviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // String[] array = mDeviceArrayList.get(arg2).split("\\n");
                // mResultView.setText("Pos: "+arg2+", value: "+mDeviceArrayList.get(arg2));
                //mResultView.setText("Pos: " + arg2 + ", device : " + array[0] + "; addr =" + array[1]);
                String address = mDeviceArrayList.get(arg2).substring(mDeviceArrayList.get(arg2).length() - 17);
                mResultView.setText("string: " + mDeviceArrayList.get(arg2) + " addr: " + address);
                connect(address, true);
                if (mBtService.getState() != BluetoothService.STATE_NONE) {
                    manageLastDevice(address);
                    setStatus(BluetoothService.STATE_CONNECTED);
                } else {
                    setStatus(BluetoothService.STATE_NONE);
                }
                alert.cancel();
            }
        });
    }

    private final BroadcastReceiver bReciever = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("bReciever", "bReciever action to ... " + action);
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Create a new device item
                // DeviceItem newDevice = new DeviceItem(device.getName(), device.getAddress(), "false");
                // Add it to our adapter
                mDevicesAdapter.clear();
                mDevicesAdapter.add(device.getName() + "\n" + device.getAddress());
                mDevicesAdapter.notifyDataSetChanged();
            }else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                //disconnected, do what you want to notify user here, toast, or dialog, etc.
                setStatus(BluetoothService.STATE_NONE);
                connect(getLastBludeviceAddr(), true);
                if(mBtService.getState() == BluetoothService.STATE_CONNECTED){
                    Toast.makeText(getApplicationContext(), R.string.bt_device_connected, Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    /**
     * Establish connection with other device
     *
     * @param address   device address.
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    private void connect(String address, boolean secure) {
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, Utility.REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        }
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mBtService.connect(device, secure);
    }

    /**
     * Write data .
     *
     * @param data A string of text to send.
     */
    private void writeData(String data) throws InterruptedException {
        // Check that we're actually connected before trying anything
        Log.d(TAG, "getState in writeData(): "+ mBtService.getState());
        if (mBtService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(getApplicationContext(), R.string.bt_device_not_connect, Toast.LENGTH_SHORT).show();
            return;
        }
        // Check that there's actually something to send
        if (data.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = data.getBytes();
            mBtService.write2(send, 1);
        }
    }

    /**
     * check system .
     */
    private void chksys() {
        // Check that we're actually connected before trying anything
        Log.d(TAG, "getState in chksys(): "+ mBtService.getState());
//        if (mBtService.getState() != BluetoothService.STATE_CONNECTED) {
//            Toast.makeText(getApplicationContext(), R.string.bt_lost_connection, Toast.LENGTH_SHORT).show();
//            return;
//        }
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        String data = "C:"+year+","+entryList.size()+":O";
        // Get the message bytes and tell the BluetoothChatService to write
        byte[] send = data.getBytes();
        mBtService.write(send);
    }

    private  void syncTime(){
        progressBarHandler = new Handler();
        progress = new ProgressDialog(this); // this = YourActivity
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setMessage("Syncing time. Please wait...");
        progress.setIndeterminate(true);
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        new Thread(new Runnable() {
            @Override
            public void run()
            {
                // do the thing that takes a long time
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH) + 1;
                int day = c.get(Calendar.DAY_OF_MONTH);
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int min = c.get(Calendar.MINUTE);
                int second = c.get(Calendar.SECOND);

                try {
                    writeData("B:" + year + "," + month + "," + day + "," + hour + "," + min + "," + second + ":O");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "write data to bluetooth device in MainActivity 1234");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        progress.dismiss();
                    }
                });
            }
        }).start();

    }

    private void manageLastDevice(String addr){
        sharedPrefs = getSharedPreferences(ENTRY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sharedPrefs.edit();
        // This will remove all entries of the specific SharedPreferences
        e.remove(LAST_BLUDEVICE_KEY);
        mResultView.setText("kyo");
        if(addr != "" & addr != null) {
            e.putString(LAST_BLUDEVICE_KEY, addr);
        }
        e.commit();
        mResultView.setText(sharedPrefs.getString(LAST_BLUDEVICE_KEY, ""));
    }

    private String getLastBludeviceAddr(){
        sharedPrefs = getSharedPreferences(ENTRY_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPrefs.getString(LAST_BLUDEVICE_KEY, "");
    }

    private void loadDataFromPref(){
        adapter.clear();
        entryList.clear();
        entryList = mydb.getFullEntries();
        for (Entry item : entryList) {
            Log.d(TAG, "item: "+ item);
            adapter.add(item.toString());
        }
        adapter.notifyDataSetChanged();
    }

    private void deleteDataPref(){
        mydb.deleteEntry(0);
        adapter.clear();
    }

    private void addDataPref(){
        mydb.insertEntry("W1", 1, "sang2", 6, 15, 6, 20,1,1,1,1,1,1,1,25,6);
        mydb.insertEntry("W1", 1, "chieu2", 18, 15, 5, 0, 1, 1, 1, 1, 1, 1, 1, 25, 6);
        loadDataFromPref();
    }

    /**
     * Updates the status of bluetooth connection.
     *
     * @param status status
     */
    private void setStatus(int status) {
        Log.e(TAG, "in status :" + status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        connectItem.setIcon(R.drawable.ic_bluetooth_disable);
        if(status == BluetoothService.STATE_NONE){
            toolbar.setSubtitle(R.string.status_not_connect);
            connectItem.setIcon(R.drawable.ic_bluetooth_disable);
            synctItem.setEnabled(false);
        }
        if(status == BluetoothService.STATE_CONNECTING){
            toolbar.setSubtitle(R.string.status_connecting);
            connectItem.setIcon(R.drawable.ic_bluetooth_disable);
            synctItem.setEnabled(false);
        }
        if(status == BluetoothService.STATE_CONNECTED){
            toolbar.setSubtitle(R.string.status_connected);
            connectItem.setIcon(R.drawable.ic_bluetooth_enable);
            synctItem.setEnabled(true);
        }
    }

    protected void showSwitchTimeDialog(String time) {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.switch_time, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);

        final TextView switch_time_tv = (TextView) promptView.findViewById(R.id.switch_time_tv);
        if(time !="") {
            alertDialogBuilder.setTitle("Time on the switch :");
            switch_time_tv.setText(time);
            // setup a dialog window
            alertDialogBuilder.setCancelable(false)
                    .setPositiveButton("Sync time.", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            syncTime();
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
        }else{
            alertDialogBuilder.setTitle("Check system :");
            switch_time_tv.setText("OK");
            // setup a dialog window
            alertDialogBuilder.setCancelable(false)
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
        }
        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    /**
     * The Handler that gets information back from the BluetoothService
     */

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            AppCompatActivity activity = MainActivity.this;
            switch (msg.what) {
                case Utility.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            setStatus(BluetoothService.STATE_CONNECTED);
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            setStatus(BluetoothService.STATE_CONNECTING);
                            break;
                        case BluetoothService.STATE_NONE:
                            setStatus(BluetoothService.STATE_NONE);
                            break;
                    }
                    break;
                case Utility.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    entry_Index++;
                    String writeMessage = new String(writeBuf);
                    Log.e(TAG, "write data to bluetooth device kyo:" + writeMessage);
                    mResultView.append(writeMessage + ":" + entry_Index + "\n");
                    if(!writeMessage.equals("B")) {
                        progressBarHandler.post(new Runnable() {
                            public void run() {
                                progress.setProgress(entry_Index);
                            }
                        });
                    }
                    break;
                case Utility.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Log.e(TAG, "read data to bluetooth device: " + readMessage);
                    Log.e(TAG, "read data to bluetooth device indexOf rC: " + readMessage.indexOf("rC"));

                    mResultView.append(readMessage+"\n");
                    if(readMessage.indexOf("rC") != -1 && readMessage.indexOf("ok") == -1 ){
                        String[] temp_arr = readMessage.split(":");
                        String[] temp_arr2 = temp_arr[1].split(",");
                        String[] temp_arr3 = {"0","0","0","0","0","0"};

                        for(int i = 0; i < temp_arr2.length; i++){
                            temp_arr3[i]= temp_arr2[i];
                        }
                        String time = temp_arr3[0]+"/"+temp_arr3[1]+"/"+temp_arr3[2]+"   "+temp_arr3[3]+"/"+temp_arr3[4]+"/"+temp_arr3[5];
                        showSwitchTimeDialog(time);
                    }
                    if(readMessage.indexOf("rC") != -1 && readMessage.indexOf("ok") != -1 ){
                        showSwitchTimeDialog("");
                    }
                    break;
                case Utility.MESSAGE_TOAST:
                    if (null != activity) {
                        setStatus(BluetoothService.STATE_NONE);
                        Toast.makeText(activity, msg.getData().getString(Utility.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

}
