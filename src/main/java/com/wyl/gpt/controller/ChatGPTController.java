package com.wyl.gpt.controller;

import com.alibaba.fastjson.JSON;
import com.wyl.gpt.Utils.R;
import com.wyl.gpt.entity.vo.gptVo;
import com.wyl.gpt.entity.vo.messageVo;
import com.wyl.gpt.service.ChatGPTService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "chatgpt")
@RestController
@RequestMapping("/chat")
public class ChatGPTController {


    @Autowired
    private ChatGPTService chatGPTService;

    @ApiOperation("chat")
    @ApiResponses({
            @ApiResponse(code = 0, message = "操作成功", response = String.class),
            @ApiResponse(code = 500, message = "操作失败", response = String.class)
    })
    @CrossOrigin(origins = "*")//配置跨域
    @GetMapping("/")
    public String welcome() {
        return "welcome!";
    }

    @ApiOperation("chat")
    @ApiResponses({
            @ApiResponse(code = 0, message = "操作成功", response = String.class),
            @ApiResponse(code = 500, message = "操作失败", response = String.class)
    })
    @CrossOrigin(origins = "*")//配置跨域
    @PostMapping("/gpt")
    public String gpt(@RequestBody gptVo prompt) {
        String text = "遇到问题，请耐心重试。导致的原因可能为：1.当前使用人数过多被阻止访问;2.服务器被较多人使用，回答功能受限(即便直接访问官网，也会经常出现该问题)。3.您使用的网络禁止访问本服务。4.网站每日总问题量超出限额。";

        try {
            text = chatGPTService.getCompletion(prompt.getPreMsg() + prompt.getMsg());
        } catch (Exception e) {
            System.out.println(e);
            return text;
        }
        System.out.println(prompt.getMsg());
        String returnLog = text.replaceAll("\r|\n|\t|\n\n", "");
        returnLog = returnLog.substring(0, returnLog.length() > 40 ? 40 : returnLog.length());
        System.out.println(returnLog);
        return text;
    }

    @ApiOperation("chat")
    @ApiResponses({
            @ApiResponse(code = 0, message = "操作成功", response = String.class),
            @ApiResponse(code = 500, message = "操作失败", response = String.class)
    })
    @CrossOrigin(origins = "*")//配置跨域
    @PostMapping("/gpt3.5")
    public String Chat(@RequestBody messageVo[] messages) {
        String text = "遇到问题，请耐心重试。导致的原因可能为：1.当前使用人数过多被阻止访问;2.服务器被较多人使用，回答功能受限(即便直接访问官网，也会经常出现该问题)。3.您使用的网络禁止访问本服务。4.网站每日总问题量超出限额。";
        text = chatGPTService.getChat(messages);
        System.out.println(messages[messages.length-1].getContent());
        System.out.println(text.substring(0,text.length()>30?30:text.length()));
        return text;
    }


}