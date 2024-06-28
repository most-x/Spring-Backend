package kr.co.mostx.japi.excel;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ExcelService<T> {
    private final Workbook workbook;
    private final Sheet sheet;
    private final ExcelRenderResource resource;
    private final List<T> dataList;
    private int rowIndex = 0;

    public ExcelService(List<T> dataList, Class<T> type) {
        this.workbook = new SXSSFWorkbook();
        this.sheet = workbook.createSheet();
        this.resource = ExcelRenderResourceFactory.prepareRenderResource(type);
        this.dataList = dataList;
    }

    public void downloadExcel(HttpServletResponse response) throws NoSuchFieldException, IllegalAccessException, IOException {
        createHead();
        createBody();
        writeExcel(response);
    }

    private void createHead() {
        Row row = sheet.createRow(rowIndex++);
        int columnIndex = 0;
        for (String dataFieldName : resource.getDataFieldNames()) {
            Cell cell = row.createCell(columnIndex++);
            String value = resource.getExcelHeaderName(dataFieldName);
            cell.setCellValue(value);
        }
    }

    private void createBody() throws NoSuchFieldException, IllegalAccessException {
        for (T data : dataList) {
            Row row = sheet.createRow(rowIndex++);
            int columnIndex = 0;
            for (String dataFieldName : resource.getDataFieldNames()) {
                Cell cell = row.createCell(columnIndex++);
                Field field = SuperClassReflectionUtil.getField(data.getClass(), (dataFieldName));
                field.setAccessible(true);
                Object cellValue = field.get(data);
                field.setAccessible(false);
                setCallValue(cell, cellValue);
            }
        }
    }

    private void writeExcel(HttpServletResponse response) throws IOException {
        String fileName = new String(resource.getExcelFileName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        response.setContentType("ms-vnd/excel");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s.xlsx\"", fileName));
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    private void setCallValue(Cell cell, Object cellValue) {
        if (cellValue instanceof Number) {
            Number numberValue = (Number) cellValue;
            cell.setCellValue(numberValue.doubleValue());
            return;
        }

        cell.setCellValue(ObjectUtils.isEmpty(cellValue) ? "" : String.valueOf(cellValue));
    }
}
