package co.saiyan.common.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author larry
 * @createTime 2023/10/28
 * @description CursorResult
 */
@Data
@ToString
public class CursorResult<T> implements Serializable {

    private static final long serialVersionUID = 4189964944909648114L;

    private final List<T> records;
    private final int size;
    private final Long lastCreateTimestamp;
    private final boolean hasNext;

    public CursorResult(int pageSize, Long lastCreateTimestamp, List<T> rows) {
        this.size = pageSize;
        this.records = rows;
        this.lastCreateTimestamp = lastCreateTimestamp;
        this.hasNext = rows.size() == pageSize;
    }

    public static <T> CursorResult<T> createCursorResult(int pageSize, Long lastCreateTimestamp, List<T> rows) {
        return new CursorResult<>(pageSize, lastCreateTimestamp, rows);
    }

    public static <T> CursorResult<T> emptyPageResult() {
        return new CursorResult<>(0, null, Collections.emptyList());
    }
}
