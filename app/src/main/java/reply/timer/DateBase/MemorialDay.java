package reply.timer.DateBase;

import org.litepal.LitePal;
import org.litepal.annotation.Column;

import java.util.Date;

public class MemorialDay extends LitePal {

    @Column
    private String mDetail;
    @Column
    private Date mDate;
    @Column
    private boolean mIsOverdue;
    @Column
    private int id;

    public MemorialDay(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDetail() {
        return mDetail;
    }

    public void setDetail(String detail) {
        mDetail = detail;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean getIsOverdue() {
        return mIsOverdue;
    }

    public void setIsOverdue(boolean overdue) {
        mIsOverdue = overdue;
    }
}
