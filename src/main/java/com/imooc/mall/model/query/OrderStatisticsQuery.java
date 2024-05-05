package com.imooc.mall.model.query;

import java.util.Date;

//订单的量的统计Query
public class OrderStatisticsQuery {
    private Date start;
    private Date end;

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}
