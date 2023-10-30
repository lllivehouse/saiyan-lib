package co.mgentertainment.common.model;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @author larry
 * @createTime 2023/08/13
 * @description Pagination
 */
@Getter
@SuperBuilder
@ToString
public class CursorPage implements Serializable {

    private static final long serialVersionUID = 286378689379757976L;

    /**
     * 每页的大小
     * -- GETTER --
     * 获取每页的大小
     *
     * @return
     */
    private int pageSize = 10;

    /**
     * 最后一条记录的创建时间戳
     */
    private Long lastCreateTimestamp;

    public CursorPage() {
    }

    public CursorPage(int pageSize, Long lastCreateTimestamp) {
        this.pageSize = pageSize;
        this.lastCreateTimestamp = lastCreateTimestamp;
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
     * @param lastCreateTimestamp
     */
    public void setLastCreateTimestamp(Long lastCreateTimestamp) {
        this.lastCreateTimestamp = lastCreateTimestamp;
    }
}