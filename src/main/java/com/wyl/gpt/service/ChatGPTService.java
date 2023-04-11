package com.wyl.gpt.service;

import com.wyl.gpt.entity.vo.messageVo;


public interface ChatGPTService {

    String getCompletion(String prompt);

    String getChat(messageVo[] chat);
}
