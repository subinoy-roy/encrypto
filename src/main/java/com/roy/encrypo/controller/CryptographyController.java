package com.roy.encrypo.controller;

import com.roy.encrypo.dto.Payload;
import org.springframework.web.bind.annotation.PostMapping;

public interface CryptographyController {
    @PostMapping("/encrypt")
    Payload encrypt(Payload payload) throws Exception;

    @PostMapping("/decrypt")
    Payload decrypt(Payload payload) throws Exception;
}
