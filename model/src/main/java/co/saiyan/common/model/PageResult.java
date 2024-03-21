package co.saiyan.common.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author larry
 * @createTime 2023/08/13
 * @description PageResult
 */
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = -5983847187357226981L;

    private final long total;
    private final List<T> records;
    private final int current;
    private final int size;
    private int pages;

    public PageResult(int pageNo, int pageSize, long totalCount, List<T> rows) {
        this.current = pageNo;
        this.size = pageSize;
        this.total = totalCount;
        this.records = rows;
        if (pageSize != 0) {
            this.pages = (int) ((totalCount / pageSize) + (totalCount % pageSize > 0 ? 1 : 0));
        }
    }

    public PageResult(Pagination pagination, long totalCount, List<T> rows) {
        this(pagination.getPageNo(), pagination.getPageSize(), totalCount, rows);
    }

    public static <T> PageResult<T> createPageResult(int pageNo, int pageSize, long count, List<T> rows) {
        return new PageResult<>(pageNo, pageSize, count, rows);
    }

    public static <T> PageResult<T> createPageResult(Pagination pagination, long count, List<T> rows) {
        return new PageResult<>(pagination.getPageNo(), pagination.getPageSize(), count, rows);
    }

    public static <T> PageResult<T> emptyPageResult(int pageNo, int pageSize) {
        return new PageResult<>(pageNo, pageSize, 0, Collections.emptyList());
    }

    public static <T> PageResult<T> emptyPageResult(Pagination pagination) {
        return new PageResult<>(pagination.getPageNo(), pagination.getPageSize(), 0, Collections.emptyList());
    }

    @Override
    public String toString() {
        return "PageResult [current=" + current + ", size=" + size + ", total=" + total + ", pages="
                + pages + ", records=" + this.records + "]";
    }
}
