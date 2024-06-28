package kr.co.mostx.japi.excel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ExcelRenderResource {
    private final String excelFileName;
    private final Map<String, String> excelHeaderName;
    private final List<String> dataFieldNames;

    public String getExcelHeaderName(String dataFieldNames) {
        return excelHeaderName.get(dataFieldNames);
    }
}
