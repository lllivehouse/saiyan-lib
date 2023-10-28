package co.mgentertainment.common.model;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * @author larry
 * @createTime 2023/08/13
 * @description Pagination
 */
@Getter
@SuperBuilder
@ToString
public class CursorPage {

    /**
     * 每页的大小
     * -- GETTER --
     * 获取每页的大小
     *
     * @return
     */
    private int pageSize = 10;

    /**
     * 最后一条记录的创建时间
     */
    private Date lastCreateTime;

    public CursorPage() {
    }

    public CursorPage(int pageSize, Date lastCreateTime) {
        this.pageSize = pageSize;
        this.lastCreateTime = lastCreateTime;
    }

    /**
     * 设置每页的大小
     *
     * @param pageSize
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 获取最后一条记录的创建时间
     *
     * @param lastCreateTime
     */
    public void setLastCreateTime(Date lastCreateTime) {
        this.lastCreateTime = lastCreateTime;
    }
}