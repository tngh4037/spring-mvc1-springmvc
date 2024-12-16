package hello.springmvc.basic.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
public class RequestBodyStringController {

    @PostMapping("/request-body-string-v1")
    public void requestBodyString(HttpServletRequest request,
                                  HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody = {}", messageBody);
        response.getWriter().write("ok");
    }

    /**
     * InputStream(Reader): HTTP 요청 메시지 바디의 내용을 직접 조회
     * OutputStream(Writer): HTTP 응답 메시지의 바디에 직접 결과 출력
     */
    @PostMapping("/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream,
                                    Writer responseWriter) throws IOException {
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody = {}", messageBody);

        responseWriter.write("ok");
    }

    /**
     * HttpEntity: HTTP header, body 정보를 편리하게 조회
     * - 메시지 바디 정보를 직접 조회(@RequestParam X, @ModelAttribute X)
     * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
     * <p>
     * 응답에서도 HttpEntity 사용 가능
     * - 메시지 바디 정보 직접 반환(view 조회X)
     * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
     */
    @PostMapping("/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) { // HttpEntity<String> httpEntity : 스프링이 Http 바디에 있는 내용이 문자라고 인지하고, 문자로 변환해서 주입해준다. (HttpMessageConverter 동작)
        String messageBody = httpEntity.getBody(); // 변환된 메시지 바디를 조회한다.
        // HttpHeaders headers = httpEntity.getHeaders(); // 헤더 정보도 얻을 수 있다.
        log.info("messageBody = {}", messageBody);

        return new HttpEntity<>("ok"); // 첫 번째 파라미터로 바디 메시지를 넣을 수 있다.
    }

    /**
     * RequestEntity & ResponseEntity 참고
     */
    @PostMapping("/request-body-string-v3-v1")
    public HttpEntity<String> requestBodyStringV3V1(RequestEntity<String> httpEntity) {
        String messageBody = httpEntity.getBody();
        log.info("messageBody = {}", messageBody);
        return new ResponseEntity<>("ok", HttpStatus.CREATED);
    }

    /**
     * @RequestBody
     * - 메시지 바디 정보를 직접 조회(@RequestParam X, @ModelAttribute X)
     * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
     *
     * @ResponseBody
     * - 메시지 바디 정보 직접 반환(view 조회X)
     * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
     */
    @PostMapping("/request-body-string-v4")
    @ResponseBody
    public String requestBodyStringV4(@RequestBody String messageBody) { // @RequestBody: HTTP 요청 메시지 바디 정보를 읽어준다.
        log.info("messageBody = {}", messageBody);
        return "ok";
    }

    // 결국, HTTP 메시지 바디 처리를 위해, 최초에 스트림 등으로 처리했던 작업들을 스프링이 자동으로 해주는것이다.
    // => 그 자동으로 해주는 것 : HttpMessageConverter
}
