package com.imooc.mall.util;

import org.apache.poi.ss.usermodel.Cell;

//处理Excel
public class ExcelUtil {
    public static Object getCellValue(Cell cell){
        switch (cell.getCellTypeEnum()){
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case NUMERIC://NUMERIC表示数值
                return cell.getNumericCellValue();
        }
        return null;
    }
}
