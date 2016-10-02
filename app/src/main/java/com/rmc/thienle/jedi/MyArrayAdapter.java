package com.rmc.thienle.jedi;

import android.app.Activity;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by thienle on 3/20/16.
 */
public class MyArrayAdapter extends ArrayAdapter<String> {
    Activity context=null;
    ArrayList<String> myArray=null;
    String arr[];
    int layoutId;

    public MyArrayAdapter(Activity context, int layoutId, ArrayList<String>arr){
        super(context, layoutId, arr);
        this.context=context;
        this.layoutId=layoutId;
        this.myArray=arr;
    }

    public MyArrayAdapter(Activity context, int layoutId, String[] arr){
        super(context, layoutId, arr);
        this.context=context;
        this.layoutId=layoutId;
        this.arr=arr;
    }

//    @Override
//    public String toString(){
//        String data = "";
//        for (int i = 0; i < getCount(); i++) {
//            data += getItem(i).toString() ;
//            if( i + 1 < getCount()){
//                data += "|";
//            }
//        }
//        return  data;
//    }

}
