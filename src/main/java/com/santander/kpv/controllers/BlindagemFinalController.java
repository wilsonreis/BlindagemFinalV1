package com.santander.kpv.controllers;

import com.santander.kpv.services.sender.BlindagemFinalSendSyncReplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.jms.JmsException;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class BlindagemFinalController {

    private final BlindagemFinalSendSyncReplyService enviaMensagem;

    public BlindagemFinalController(BlindagemFinalSendSyncReplyService enviaMensagem) {
        this.enviaMensagem = enviaMensagem;
    }

    @PostMapping(value = "/blindagemFinalV1", consumes = MediaType.TEXT_PLAIN_VALUE)
    public String blindagem(@RequestBody String message) {
        try {
            return enviaMensagem.sendSyncReply(message);
        } catch (JmsException ex) {
            log.info("public String blindagem(@RequestBody String message)  [{}]", ex.getMessage());
            throw new RuntimeException("public String blindagem(@RequestBody String message)  [{}]", ex);
        }
    }
}
