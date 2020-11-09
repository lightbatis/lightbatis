package titan.lightbatis.common;

import lombok.Data;
import titan.lightbatis.result.PageList;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageResult {
    private int totalSize = 0;
    private List<?> records = new ArrayList<>();
    public PageResult(PageList pageList) {
        this.totalSize = pageList.getTotalSize();
        this.records = pageList.getRecords();
    }
}
