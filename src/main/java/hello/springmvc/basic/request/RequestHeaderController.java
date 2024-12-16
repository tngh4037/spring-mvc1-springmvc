package hello.springmvc.basic.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;

@Slf4j
@RestController
public class RequestHeaderController {

    @RequestMapping("/headers")
    public String headers(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpMethod httpMethod,
                          Locale locale,                                                        // 가장 우선 순위가 높은 locale이 조회됨.
                          @RequestHeader MultiValueMap<String, String> headerMap,               // 헤더를 한번에 다 받는 경우
                          @RequestHeader("host") String host,                                   // 헤더를 하나만 받는 경우 (괄호안에 헤더 네임을 작성한다.)
                          @CookieValue(value = "myCookie", required = false) String cookie      // value: cookie name, required = (default) true
    ) {
        log.info("request={}", request);
        log.info("response={}", response);
        log.info("httpMethod={}", httpMethod);
        log.info("locale={}", locale);
        log.info("headerMap={}", headerMap);
        log.info("header host={}", host);
        log.info("myCookie={}", cookie);

        MultiValueMap<String, String> map = new LinkedMultiValueMap();
        map.add("keyA", "value1");
        map.add("keyA", "value2");

        // [value1,value2]
        List<String> values = map.get("keyA");

        return "ok";
    }
}


