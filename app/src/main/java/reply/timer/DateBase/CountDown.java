package reply.timer.DateBase;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Date;
import java.util.UUID;

public class CountDown extends LitePalSupport{

    @Column
    private String mName;
    @Column
    private String mDate;
    @Column
    private boolean mIsOverdue;
    @Column
    private int id;

    public CountDown() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public boolean getIsOverdue() {
        return mIsOverdue;
    }

    public void setIsOverdue(boolean overdue) {
        mIsOverdue = overdue;
    }
}
