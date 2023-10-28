package co.mgentertainment.common.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author larry
 * @createTime 2023/10/28
 * @description CursorResult
 */
@Data
@ToString
public class CursorResult<T> implements Serializable {

    private static final long serialVersionUID = 7319206916713429407L;

    private final List<T> records;
    private final int size;
    private final Date lastCreateTime;
    private final boolean hasNext;

    public CursorResult(int pageSize, Date lastCreateTime, List<T> rows) {
        this.size = pageSize;
        this.lastCreateTime = lastCreateTime;
        this.records = rows;
        this.hasNext = rows.size() == pageSize;
    }

    public static <T> CursorResult<T> createCursorResult(int pageSize, Date lastCreateTime, List<T> rows) {
        return new CursorResult<>(pageSize, lastCreateTime, rows);
    }

    public static <T> CursorResult<T> emptyPageResult() {
        return new CursorResult<>(0, null, Collections.emptyList());
    }
}
