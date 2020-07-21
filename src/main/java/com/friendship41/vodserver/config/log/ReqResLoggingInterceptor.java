package com.friendship41.vodserver.config.log;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ReqResLoggingInterceptor extends HandlerInterceptorAdapter {
  private final Set<String> clientIpHeaderSet = new HashSet<>();

  @Override
  public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
      throws Exception {
    log.info("====================start====================");
    log.info("Req >> ["+request.getMethod()+"] "+request.getRequestURI() +
        (request.getQueryString() == null ? "" : "?"+request.getQueryString()));
    log.info("from: [ip] "+this.getClientIp(request));
    Stream<String> headerNamesStream = Collections.list(request.getHeaderNames()).stream();
    headerNamesStream.forEach(headerName -> {
      log.info("header: ["+headerName+"] "+request.getHeader(headerName));
    });

    return super.preHandle(request, response, handler);
  }

  @Override
  public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
      final ModelAndView modelAndView) throws Exception {
    log.info("Res >> [status] "+response.getStatus());
    log.info("=====================end=====================");
  }

  private String getClientIp(HttpServletRequest request) {
    Optional<String> opIp = Collections.list(request.getHeaderNames()).stream()
            .filter(Objects::nonNull)
            .findFirst();
    return opIp.isPresent() ? request.getHeader(opIp.get()) : request.getRemoteAddr();
  }

  @PostConstruct
  public void setUpClientIpHeaderSet() {
    clientIpHeaderSet.add("X-Forwarded-For");
    clientIpHeaderSet.add("Proxy-Client-IP");
    clientIpHeaderSet.add("WL-Proxy-Client-IP");
    clientIpHeaderSet.add("HTTP_CLIENT_IP");
    clientIpHeaderSet.add("HTTP_X_FORWARDED_FOR");
  }
}
