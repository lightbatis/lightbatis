package titan.lightbatis.dataset.impl;

import lombok.Data;

import java.io.Serializable;

@Data
public class ExcelOption implements Serializable {
    /**
     * 名称列的开始行
     */
    private int columnNameStartRow = 0;
}
