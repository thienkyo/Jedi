package com.rmc.thienle.jedi;

/**
 * Created by thienle on 4/17/16.
 */
public class Entry {
    public String entry_name;
    public int isActive;
    public int start_hr;
    public int start_min;
    public int end_hr;
    public int end_min;
    public int mon;
    public int tue;
    public int wen;
    public int thu;
    public int fri;
    public int sat;
    public int sun;
    public int date;
    public int relayid;
    public int entry_id;

    public Entry(String entry_name, int start_hr, int start_min,int end_hr, int end_min, int entry_id) {
        this.entry_name = entry_name;
        this.start_hr = start_hr;
        this.start_min = start_min;
        this.start_hr = start_hr;
        this.start_min = start_min;
        this.end_hr = end_hr;
        this.end_min = end_min;
        this.entry_id = entry_id;
    }

    public Entry(String entry_name, int isActive, int start_hr, int start_min,int end_hr, int end_min, int mon, int tue, int wen, int thu,
                 int fri, int sat, int sun, int date, int relayid, int entry_id) {
        this.entry_name = entry_name;
        this.isActive = isActive;
        this.start_hr = start_hr;
        this.start_min = start_min;
        this.start_hr = start_hr;
        this.start_min = start_min;
        this.end_hr = end_hr;
        this.end_min = end_min;
        this.mon = mon;
        this.tue = tue;
        this.wen = wen;
        this.thu = thu;
        this.fri = fri;
        this.sat = sat;
        this.sun = sun;
        this.date = date;
        this.relayid = relayid;
        this.entry_id = entry_id;
    }

    public int getEntry_id() {
        return entry_id;
    }

    public String toString(){
        return entry_name +" - "+ (start_hr >9 ? start_hr+"" : "0"+start_hr) +":"+ (start_min > 9 ? start_min+"" : "0"+start_min)+ " -> "+
                                  (end_hr >9 ? end_hr+"" : "0"+end_hr) +":"+ (end_min > 9 ? end_min+"" : "0"+end_min)
                ;
    }

    public String toFullString(){  //1,18,0,18,30,1,1,1,1,1,1,1,25,6:
        return isActive+","+start_hr+","+start_min+","+end_hr+","+end_min+","+
                mon+","+tue+","+wen+","+thu+","+fri+","+sat+","+sun+","+date+","+relayid;
    }
}
