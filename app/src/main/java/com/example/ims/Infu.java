package com.example.ims;

import java.io.Serializable;
import java.util.Date;
class Infu {
    Date gototime;
    Date offtime;
    Infu(){
        gototime= null;
        offtime = null;

    }
    Infu(Date gototime,Date offtime){
        this.gototime=gototime;
        this.offtime=offtime;
    }
}
