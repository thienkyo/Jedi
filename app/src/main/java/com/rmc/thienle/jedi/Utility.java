package com.rmc.thienle.jedi;

/**
 * Created by thienle on 3/20/16.
 */
public class Utility {
    public static final int REQUEST_ENABLE_BT = 1;
    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
   // public static String EXTRA_DEVICE_ADDRESS = "device_address";
}
