package com.sparta.springplus.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j // Lombok을 사용하여 로그를 사용하도록 설정한다.
@Aspect // 해당 클래스가 AspectJ를 사용한 AOP 클래스임을 나타낸다. AOP 구현을 위한 Proxy 생성 등을 자동으로 해준다.
@Component // Spring에서 관리하는 Bean으로 설정한다. 
public class RequestLogAop {

    // @PointCut : 실행 시점을 설정하는 어노테이션
    // 첫번째 * : 메서드의 반환 타입을 지정한다, 반환 타입에 상관없이 포인트컷을 적용한다는 뜻
    // com.thesun4sky.todoparty : 패키지 경로를 의미한다.
    // .. : 하위 디렉토리를 뜻한다.
    // *Controller : 클래스 이름을 지정한다. Controller로 끝나는 모든 클래스를 의미한다.
    // . : 클래스 내의 메서드를 뜻한다.
    // * : 메서드 이름을 지정한다. 즉, 모든 메서드 이름을 뜻한다.
    // (..) : 메서드의 매개변수를 지정한다. ".."은 0개 이상의 모든 매개변수를 의미한다.
    
    // com.thesun4sky.todoparty 패키지 및 하위 패키지에 있는, 이름이 Controller로 끝나는 모든 클래스의 모든 메서드(매개변수와 상관없이)를 대상으로 한다
    @Pointcut("execution(* com.sparta.springplus..*Controller.*(..))")
    private void controller() {}

    // 포인트컷으로 설정된 메서드 실행 전후에 로그를 출력하는 Around Advice
    @Around("controller()")
    public Object loggingBefore(ProceedingJoinPoint joinPoint) throws Throwable {
        
        // 현재 HTTP 요청을 가져온다
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

				// null 일 경우를 대비하여 null인 경우 경고 로그를 출력한다.
        if (requestAttributes == null) {
            log.warn("RequestAttributes가 null 입니다!");
            return joinPoint.proceed();
        }

				// HttpServletRequest 객체를 가져온다.
        HttpServletRequest request = requestAttributes.getRequest();
        
        // 현재 실행 중인 메서드 이름을 가져온다.
        String methodName = joinPoint.getSignature().getName();
        
	      // 요청 URI를 UTF-8로 디코딩하여 가져온다.
        String requestUri = URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8);
        
        // HTTP 메서드(GET, POST 등)를 가져온다.
        String httpMethod = request.getMethod();
        
        // 요청 파라미터를 가져와 문자열로 변환한다.
        String params = getParams(request); 

        log.info("[{}] {}", httpMethod, requestUri);
        log.info("method: {}", methodName);
        log.info("params: {}", params);

				// 메서드 실행 시점 이전에 한번만 원래 호출되어야할 메서드를 실행한다.
				// proceed()가 호출되기 전에는 실행 전 로직이 호출된다.
				// proceed()가 호출된 후에는 실행 후 로직이 호출된다.
//         return joinPoint.proceed();
        
        // 메서드 실행 후에도 로그가 찍히도록 만드는 방법
        // proceed() 호출 이전 코드, 메서드 실행 시점 이전에 호출된다.
         Object result = joinPoint.proceed();
        
         log.info("[{}] {}", httpMethod, requestUri);
         log.info("method: {}", methodName);
         log.info("params: {}", params);
        
         return result;
    }

    // HTTP 요청의 파라미터를 문자열로 변환하여 반환한다
    private static String getParams(HttpServletRequest request) {
				
				// parameter를 Map 형태로 가져온다.
        Map<String, String[]> parameterMap = request.getParameterMap();
        
        // 맵의 Entry들을 Stream으로 변환한다.
        return parameterMap.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + Arrays.toString(entry.getValue())) // 각 엔트리를 "key=[value]" 형태의 문자열로 변환한다.
                .collect(Collectors.joining(", ")); // 변환된 문자열들을 ", "로 구분하여 하나의 문자열로 합친다.
    }
}