package com.roy.encrypo.controller;

import com.roy.encrypo.dto.Payload;
import com.roy.encrypo.service.CryptographyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/cryptography")
public class CryptographyControllerImpl implements CryptographyController {
    @Autowired
    CryptographyService cryptographyService;

    @PostMapping("/encrypt")
    @Override
    public Payload encrypt(@RequestBody Payload payload) throws Exception {
        return new Payload(cryptographyService.encrypt(payload.getPayload()));
    }

    @PostMapping("/decrypt")
    @Override
    public Payload decrypt(@RequestBody Payload payload) throws Exception {
        return new Payload(cryptographyService.decrypt(payload.getPayload()));
    }
}
