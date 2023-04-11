package com.wyl.gpt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wyl.gpt.dao.TokenTimesDao;
import com.wyl.gpt.dao.VisiteDao;
import com.wyl.gpt.entity.TokenTimesEntity;
import com.wyl.gpt.entity.VisiteEntity;
import com.wyl.gpt.entity.vo.gpt4ResponseVo;
import com.wyl.gpt.entity.vo.gptResponseVo;
import com.wyl.gpt.entity.vo.messageVo;
import com.wyl.gpt.service.ChatGPTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.alibaba.fastjson.JSON;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class ChatGPTServiceImpl implements ChatGPTService {

    @Autowired
    private TokenTimesDao tokenTimesDao;
    @Autowired
    private VisiteDao visiteDao;
    private TokenTimesEntity apiKey = new TokenTimesEntity();

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getCompletion(String prompt) {

        //过滤prompt字符串，去除字符串中的"、\t、
        prompt = prompt.replaceAll("\"", "")
                .replaceAll("\t", "")
                .replaceAll("\r", "")
                .replaceAll("\\\\", "")
                .replaceAll("\n", "");
        //prompt = "\""+ prompt + "\"";

        //设置请求头
        //String url = "https://api.openai.com/v1/chat/completions";
        String url = "https://api.openai.com/v1/completions";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //动态获取被使用次数最少的key
        apiKey = detectTimes();
        if(apiKey.getId() == -1)return "管理员:今日总次数已用完，请歇一会儿明天再问";
        //apiKey.setToken("");
        headers.setBearerAuth(apiKey.getToken());

        //"messages": [{"role": "user", "content": "Hello!"}]
        //设置请求体
        String body = String.format("{\"model\": \"text-davinci-003\",\"prompt\": \"%s\", \"max_tokens\": 1666}", prompt);
        //String body = String.format("{\"model\": \"gpt-3.5-turbo\",\"messages\": \"%s\", \"max_tokens\": 2044}", prompt);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class, Collections.emptyMap());

        //处理返回请求
        if (response.getStatusCode() == HttpStatus.OK) {

            //解析chatgpt的回答
            String rspRaw = response.getBody().replace('[', ' ');
            rspRaw = rspRaw.replace(']', ' ');
            gptResponseVo rsp = JSON.parseObject(rspRaw, gptResponseVo.class);

            //记录每个token当天的回答次数
            addTokenTimes();

            //记录每天总访问次数，总访问key
            countDailyVisite(rsp.getUsage().getPrompt_tokens(), rsp.getUsage().getCompletion_tokens());

            return rsp.getChoices().getText();

        } else {
            throw new RuntimeException("Failed to get completion: " + response.getStatusCode());
        }
    }


    @Override
    public String getChat(messageVo[] prompt) {

        //过滤prompt字符串，去除字符串中的"、\t、
//        prompt = prompt.replaceAll("\"", "")
//                .replaceAll("\t", "")
//                .replaceAll("\r", "")
//                .replaceAll("\\\\", "")
//                .replaceAll("\n", "");
        //prompt = "\""+ prompt + "\"";

        //设置请求头
        String url = "https://api.openai.com/v1/chat/completions";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //动态获取被使用次数最少的key
        apiKey = detectTimes();
        if(apiKey.getId() == -1)return "管理员:今日总次数已用完，请歇一会儿明天再问";
        //apiKey.setToken("");
        headers.setBearerAuth(apiKey.getToken());

        //将实体转化成JSON
        String msg = JSON.toJSONString(prompt);

        //设置请求体
        String body = String.format("{\"model\": \"gpt-3.5-turbo\",\"messages\": %s, \"max_tokens\": 2000}", msg);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class, Collections.emptyMap());

        //处理返回请求
        if (response.getStatusCode() == HttpStatus.OK) {

            //解析chatgpt的回答
            gpt4ResponseVo resp = JSON.parseObject(response.getBody(), gpt4ResponseVo.class);
            messageVo respMsg = resp.getChoices()[0].getMessage();

            //记录每个token当天的回答次数
            addTokenTimes();

            //记录每天总访问次数，总访问key
            countDailyVisite(resp.getUsage().getPrompt_tokens(),resp.getUsage().getCompletion_tokens());

            return respMsg.getContent();

        } else {
            throw new RuntimeException("Failed to get completion: " + response.getStatusCode());
        }
    }

    /**
     * 设置单日token使用次数限制
     *
     * @return
     */
    private TokenTimesEntity detectTimes() {
        List<TokenTimesEntity> list = tokenTimesDao.selectList(
                new LambdaQueryWrapper<TokenTimesEntity>()
                        .orderByAsc(TokenTimesEntity::getTotalTimes)
        );
//        if (list.get(0).getTimes() > 40) {
//            TokenTimesEntity errorEntity = new TokenTimesEntity();
//            errorEntity.setId(-1);
//            return errorEntity;
//        }
        return list.get(0);
    }

    /**
     * token使用次数计数器
     */
    private void addTokenTimes() {
        Date date = new Date();
        SimpleDateFormat dateToString = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateToString.format(date);
        //单个token总数计数器
        apiKey.setTotalTimes(apiKey.getTotalTimes() + 1);
        //单日token使用次数
        //如果是当天，次数+1;如果是下一天，次数归0
        if (dateString.equals(apiKey.getDate())) {
            apiKey.setTimes(apiKey.getTimes() + 1);
            tokenTimesDao.updateById(apiKey);
        } else {
            apiKey.setDate(dateString);
            apiKey.setTimes(1);
            tokenTimesDao.updateById(apiKey);
        }
    }

    /**
     * 记录每天总访问次数
     */
    private void countDailyVisite(Long promptTokens, Long completionTokens) {
        Date date = new Date();
        SimpleDateFormat dateToString = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateToString.format(date);
        VisiteEntity visiteEntity = visiteDao.selectOne(
                new LambdaQueryWrapper<VisiteEntity>()
                        .eq(VisiteEntity::getDate, dateString)
        );
        if (visiteEntity == null) {
            visiteEntity = new VisiteEntity();
            visiteEntity.setDate(dateString);
            visiteEntity.setTimes(1);
            visiteEntity.setPromptTokens(promptTokens);
            visiteEntity.setCompletionTokens(completionTokens);
            visiteDao.insert(visiteEntity);
        } else {
            visiteEntity.setTimes(visiteEntity.getTimes() + 1);
            visiteEntity.setPromptTokens(visiteEntity.getPromptTokens() + promptTokens);
            visiteEntity.setCompletionTokens(visiteEntity.getCompletionTokens() + (completionTokens==null?0:completionTokens));
            visiteDao.update(visiteEntity, new LambdaQueryWrapper<VisiteEntity>()
                    .eq(VisiteEntity::getDate, dateString)
            );
        }


    }
}

