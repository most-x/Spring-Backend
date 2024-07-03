package kr.co.mostx.japi.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kr.co.mostx.japi.dto.ServeyCryptDto;
import kr.co.mostx.japi.dto.ServeyDto;
import kr.co.mostx.japi.dto.ServeySearchDto;
import kr.co.mostx.japi.entity.Servey;
import kr.co.mostx.japi.excel.ExcelService;
import kr.co.mostx.japi.response.ServeyResponse;
import kr.co.mostx.japi.response.ServeyResponsePage;
import kr.co.mostx.japi.service.ServeyService;
import kr.co.mostx.japi.type.ServeySortType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/servey")
public class ServeyController {
    private final ServeyService serveyService;

    // 데이터 암호화 AES/CBC
    @PostMapping("/encrypt")
    public String encryptData256(@RequestBody String jsonData) throws Exception {
        return serveyService.encryptData(jsonData);
    }

    @GetMapping("/decrypt")
    public ServeyCryptDto decryptDataGet256(@RequestParam String elementData) throws Exception {
        return serveyService.decryptData(elementData);
    }

    @PostMapping("/servey-regist")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ServeyResponse<ServeyDto> createServey(@Valid @RequestBody ServeyDto requestDto) {
        Servey createServey = serveyService.saveServey(requestDto);

        return ServeyResponse.success("성공적으로 등록되었습니다.", null);
    }

    @GetMapping("/list")
    public ServeyResponsePage<List<ServeyDto>> findSearchServey(@ModelAttribute ServeySearchDto serveySearchDto, ServeySortType sortType,
                                                                @RequestParam(required = false, defaultValue = "1") int pageNumber,
                                                                @RequestParam(required = false, defaultValue = "10")int pageSize) {
        return serveyService.findSearchServey(serveySearchDto, sortType, pageNumber, pageSize);
    }

    @GetMapping("/excel/download")
    public void excelDownload(HttpServletResponse response, @ModelAttribute ServeySearchDto serveySearchDto) throws Throwable{
        List<ServeyDto> serveyList = serveyService.findExcelDownload(serveySearchDto);
        ExcelService<ServeyDto> excelService = new ExcelService<>(serveyList, ServeyDto.class);
        excelService.downloadExcel(response);
    }

}

