package com.it.common.message;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhaowq
 * @description 分页集
 * @create 2017-08-25 上午 11:22
 **/
public class QueryResult<T> implements Serializable {
    private static final long serialVersionUID = 461900815434592315L;
    private List<T> list;
    private long total;

    public QueryResult() {
    }

    public QueryResult(List<T> list, long total) {
        this.list = list;
        this.total = total;
    }

    public List<T> getlist() {
        return this.list;
    }

    public void setlist(List<T> list) {
        this.list = list;
    }

    public long getTotal() {
        return this.total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
