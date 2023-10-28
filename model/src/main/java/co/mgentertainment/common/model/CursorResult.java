package co.mgentertainment.common.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author larry
 * @createTime 2023/10/28
 * @description CursorResult
 */
@Data
public class CursorResult<T> implements Serializable {

    private static final long serialVersionUID = -5983847187357226981L;

    private final boolean hasNext;
    private final List<T> records;
    private final int size;

    public CursorResult(int pageSize, List<T> rows) {
        this.size = pageSize;
        this.records = rows;
        this.hasNext = rows.size() == pageSize;
    }

    public static <T> CursorResult<T> createCursorResult(int pageSize, List<T> rows) {
        return new CursorResult<>(pageSize, rows);
    }

    public static <T> CursorResult<T> emptyPageResult(int pageSize) {
        return new CursorResult<>(pageSize, Collections.emptyList());
    }

    @Override
    public String toString() {
        return "CursorResult [hashNext=" + this.hasNext + ", size=" + size + ", records=" + this.records + "]";
    }
}
