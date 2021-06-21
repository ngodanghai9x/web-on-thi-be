package vn.actvn.onthionline.common.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public class OptimizedPage<T> {
    private List<T> content;
    boolean isFirst;
    boolean isLast;
    boolean isEmpty;
    int totalPages;
    int pageSize;
    int currentPage;
    long totalElements;
    Sort sort;

    public static <T> OptimizedPage convert(Page<T> page) {
        OptimizedPage optimizedPage = new OptimizedPage();
        optimizedPage.setContent(page.getContent());
        optimizedPage.setFirst(page.isFirst());
        optimizedPage.setLast(page.isLast());
        optimizedPage.setEmpty(page.isEmpty());
        optimizedPage.setTotalPages(page.getTotalPages());
        optimizedPage.setPageSize(page.getSize());
        optimizedPage.setCurrentPage(page.getNumber() + 1);
        optimizedPage.setTotalElements(page.getTotalElements());
        optimizedPage.setSort(page.getSort());
        return optimizedPage;
    }

    public List<T> getContent() {
        return this.content;
    }

    public boolean isFirst() {
        return this.isFirst;
    }

    public boolean isLast() {
        return this.isLast;
    }

    public boolean isEmpty() {
        return this.isEmpty;
    }

    public int getTotalPages() {
        return this.totalPages;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public long getTotalElements() {
        return this.totalElements;
    }

    public Sort getSort() {
        return this.sort;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public void setFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }

    public void setLast(boolean isLast) {
        this.isLast = isLast;
    }

    public void setEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof OptimizedPage)) {
            return false;
        } else {
            OptimizedPage<?> other = (OptimizedPage)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label63: {
                    Object this$content = this.getContent();
                    Object other$content = other.getContent();
                    if (this$content == null) {
                        if (other$content == null) {
                            break label63;
                        }
                    } else if (this$content.equals(other$content)) {
                        break label63;
                    }

                    return false;
                }

                if (this.isFirst() != other.isFirst()) {
                    return false;
                } else if (this.isLast() != other.isLast()) {
                    return false;
                } else if (this.isEmpty() != other.isEmpty()) {
                    return false;
                } else if (this.getTotalPages() != other.getTotalPages()) {
                    return false;
                } else if (this.getPageSize() != other.getPageSize()) {
                    return false;
                } else if (this.getCurrentPage() != other.getCurrentPage()) {
                    return false;
                } else if (this.getTotalElements() != other.getTotalElements()) {
                    return false;
                } else {
                    Object this$sort = this.getSort();
                    Object other$sort = other.getSort();
                    if (this$sort == null) {
                        if (other$sort != null) {
                            return false;
                        }
                    } else if (!this$sort.equals(other$sort)) {
                        return false;
                    }

                    return true;
                }
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof OptimizedPage;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $content = this.getContent();
        result = result * 59 + ($content == null ? 43 : $content.hashCode());
        result = result * 59 + (this.isFirst() ? 79 : 97);
        result = result * 59 + (this.isLast() ? 79 : 97);
        result = result * 59 + (this.isEmpty() ? 79 : 97);
        result = result * 59 + this.getTotalPages();
        result = result * 59 + this.getPageSize();
        result = result * 59 + this.getCurrentPage();
        long $totalElements = this.getTotalElements();
        result = result * 59 + (int)($totalElements >>> 32 ^ $totalElements);
        Object $sort = this.getSort();
        result = result * 59 + ($sort == null ? 43 : $sort.hashCode());
        return result;
    }

    public String toString() {
        return "OptimizedPage(content=" + this.getContent() + ", isFirst=" + this.isFirst() + ", isLast=" + this.isLast() + ", isEmpty=" + this.isEmpty() + ", totalPages=" + this.getTotalPages() + ", pageSize=" + this.getPageSize() + ", currentPage=" + this.getCurrentPage() + ", totalElements=" + this.getTotalElements() + ", sort=" + this.getSort() + ")";
    }

    public OptimizedPage() {
    }

    public OptimizedPage(List<T> content, boolean isFirst, boolean isLast, boolean isEmpty, int totalPages, int pageSize, int currentPage, long totalElements, Sort sort) {
        this.content = content;
        this.isFirst = isFirst;
        this.isLast = isLast;
        this.isEmpty = isEmpty;
        this.totalPages = totalPages;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.totalElements = totalElements;
        this.sort = sort;
    }
}
