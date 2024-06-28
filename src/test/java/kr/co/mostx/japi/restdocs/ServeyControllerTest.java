package kr.co.mostx.japi.restdocs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.LinkedHashMap;
import java.util.Map;

import static kr.co.mostx.japi.restdocs.RestDocsConfiguration.field;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ServeyControllerTest extends AbstractRestDocsTests {
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void ServeyCreateTest() throws Exception {
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("serveyOne", 1);
        input.put("serveyTwo", 2);
        input.put("serveyThree", 3);
        input.put("serveyFour", 4);
        input.put("serveyFive", 5);
        input.put("userName", "조동현");
        input.put("userPhone", "01000000000");
        input.put("consultantName", "강성민");
        input.put("platform", "WRMS");
        input.put("serveyNumber", "CO0000000016");

        ResultActions result = mockMvc.perform(post("/api/servey/servey-regist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)));

        result.andExpect(status().isCreated())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("serveyOne").description("만족도조사 1번 항목"),
                                fieldWithPath("serveyTwo").description("만족도조사 2번 항목"),
                                fieldWithPath("serveyThree").description("만족도조사 3번 항목"),
                                fieldWithPath("serveyFour").description("만족도조사 4번 항목"),
                                fieldWithPath("serveyFive").description("만족도조사 5번 항목"),
                                fieldWithPath("userName").description("고객명"),
                                fieldWithPath("userPhone").description("고객 휴대폰번호"),
                                fieldWithPath("consultantName").description("상담원명"),
                                fieldWithPath("platform").description("인입경로").attributes(field("constraints", "WRMS," + "\n" + "ILSANG")),
                                fieldWithPath("serveyNumber").description("상담번호").attributes(field("constraints", "Unique key: 중복불가"))
                        )
                ));
    }

    @Test
    void DecryptTest() throws Exception {
        mockMvc.perform(get("/api/servey/decrypt")
                        .param("encryptedData", "EmyBaXv4ISguu2Unh03mwE%2BhqbGWzFvfPnsypAjCqK0SOiAFVQfWBGPCrTl8sKQeFHRZMnj1MCUZJfbeWKFT%2F8f4iN4O2nHkST4wfvqET9FYqCn5OaehirgmnV7KBMNQ76nGeeq6yaUW%2F51WreFzY3mEuFr3Gov8bqgUKk1c0jU6UkQfXVrUSmOWZGbx2VUZm6Tzdy6qU1icWPDFyykGig%3D%3D")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                queryParameters(
                                        parameterWithName("encryptedData").description("암호화된 데이터")
                                ),
                                responseFields(
                                        fieldWithPath("decryptData").type(JsonFieldType.OBJECT).description("복호환된 데이터(JSON)"),
                                        fieldWithPath("decryptData.userName").type(JsonFieldType.STRING).description("고객명"),
                                        fieldWithPath("decryptData.userPhone").type(JsonFieldType.STRING).description("고객 휴대폰번호"),
                                        fieldWithPath("decryptData.consultantName").type(JsonFieldType.STRING).description("상담원명"),
//                                        fieldWithPath("decryptData.platform").type(JsonFieldType.STRING).description("인입경로"),
                                        fieldWithPath("decryptData.serveyNumber").type(JsonFieldType.STRING).description("상담번호"),
                                        fieldWithPath("decryptData.messageTime").type(JsonFieldType.STRING).description("알림톡 전송시간"),
                                        fieldWithPath("registStatus").description("만족도조사 등록여부 (Y:등록불가, N:등록가능)")
                                ))
                );
    }
    @Test
    void ServeySearchFindTest() throws Exception {
        mockMvc.perform(get("/api/servey/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("startDate", "2024-05-20")
                        .param("endDate", "2024-06-11")
                        .param("searchType", "serveyNumber")
                        .param("searchWord", "CO0000")
                        .param("platform", "WRMS")
                        .param("scoreType", "avgScore")
                        .param("minScore", "1")
                        .param("maxScore", "5")
                        .param("pageNumber", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("startDate").optional().description("검색시작일"),
                                parameterWithName("endDate").optional().description("검색종료일"),
                                parameterWithName("searchType").optional().description("검색유형").attributes(field("constraints", "serveyNumber: 상담번호" + "\n" + "userName: 상담자명 " + "\n" + " userPhone: 상담자번호")),
                                parameterWithName("searchWord").optional().description("검색어"),
                                parameterWithName("platform").optional().description("인입경로").attributes(field("constraints", "WRMS, ILSANG")),
                                parameterWithName("scoreType").optional().description("점수유형").attributes(field("contraints", "totalScore: 총점" + "\n" + "avgScore: 평균")),
                                parameterWithName("minScore").optional().description("최소점수"),
                                parameterWithName("maxScore").optional().description("최대점수"),
                                parameterWithName("pageNumber").optional().description("페이지 번호").attributes(field("contranints", "Default Number : 1")),
                                parameterWithName("pageSize").optional().description("페이지 사이즈").attributes(field("contranints", "Default Size : 10"))
                        )
                ));
    }
}
