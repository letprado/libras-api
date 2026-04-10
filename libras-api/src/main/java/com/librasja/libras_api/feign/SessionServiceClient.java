package com.librasja.libras_api.feign;

import com.librasja.libras_api.dto.SessionResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "sessionServiceFeignClient", url = "${feign.session-service.url:http://localhost:8080}")
public interface SessionServiceClient {

    @GetMapping("/sessions/{id}")
    SessionResponseDto getSession(@PathVariable Long id);

    @GetMapping("/sessions")
    List<SessionResponseDto> getAllSessions();
}
