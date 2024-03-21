package co.saiyan.common.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * @author larry
 * @createTime 2023/08/13
 * @description Pagination
 */
@Getter
@SuperBuilder
public class Pagination {

    /**
     * 页码(从1开始)
     * -- GETTER --
     *  获取页码(从1开始)
     *
     * @return

     */
    private int pageNo = 1;

    /**
     * 每页的大小
     * -- GETTER --
     *  获取每页的大小
     *
     * @return

     */
    private int pageSize = 10;

    private boolean needTotalCount = false;

    private boolean needPage = true;

    public Pagination() {
    }

    public Pagination(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public void copyFromPagination(Pagination pagination) {
        this.setNeedPage(pagination.isNeedPage());
        this.setNeedTotalCount(pagination.isNeedTotalCount());
        this.setPageNo(pagination.pageNo);
        this.setPageSize(pagination.pageSize);
    }

    /**
     * 根据pageNo和pageSize获取mysql的startRow
     *
     * @return
     */
    public int getStartRow() {
        if (pageNo <= 1 || pageSize <= 0) {
            return 0;
        }
        return (pageNo - 1) * pageSize;
    }

    /**
     * 设置页码(从1开始)
     *
     * @param pageNo
     */
    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    /**
     * 设置每页的大小
     *
     * @param pageSize
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "Pagination{" + "pageNo=" + pageNo + ", pageSize=" + pageSize + '}';
    }

    public void setNeedTotalCount(boolean needTotalCount) {
        this.needTotalCount = needTotalCount;
    }

    public void setNeedPage(boolean needPage) {
        this.needPage = needPage;
    }
}