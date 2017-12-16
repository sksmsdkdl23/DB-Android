package ca.bcit.ass3.choi_lowenstein;

import java.io.Serializable;

/**
 * Created by Phili on 10/31/2017.
 */

public class Potluck implements Serializable {
    private String mName;
    private String mDate;
    private String mTime;

    public Potluck(String name, String date, String time) {
        mName = name;
        mDate = date;
        mTime = time;
    }
    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }
}
