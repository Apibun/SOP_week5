package com.example.week5_2;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
public class WordPublisher {
    protected Word words = new Word();
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @RequestMapping(value = "/addBad/{word}", method = RequestMethod.POST)
    public ArrayList<String> addBadWord(@PathVariable("word") String s){
        words.badWords.add(s);
        return words.badWords;
    }
    @RequestMapping(value = "/delBad/{word}", method = RequestMethod.GET)
    public ArrayList<String> deleteBadWord(@PathVariable("word") String s){
        words.badWords.remove(s);
        return words.badWords;
    }
    @RequestMapping(value = "/addGood/{word}", method = RequestMethod.POST)
    public ArrayList<String> addGoodWord(@PathVariable("word") String s){
        words.goodWords.add(s);
        return words.goodWords;
    }
    @RequestMapping(value = "/delGood/{word}", method = RequestMethod.GET)
    public ArrayList<String> deleteGoodWord(@PathVariable("word") String s){
        words.goodWords.remove(s);
        return words.goodWords;
    }
    @RequestMapping(value = "/proof/{sentence}", method = RequestMethod.POST)
    public String proofSentence(@PathVariable("sentence") String s){
        String checkWord = "";
        for(String word: words.badWords){
            if(s.contains(word)){
                checkWord += "bad";
                break;
            }
        }
        for(String word: words.goodWords){
            if(s.contains(word)){
                checkWord += "good";
                break;
            }
        }
        if (checkWord.equals("goodbad") || checkWord.equals("badgood")){
            rabbitTemplate.convertAndSend("Fanout", "", s);
            return "Found Good Word and Bad Word";
        }
        else if(checkWord.equals("good")){
            rabbitTemplate.convertAndSend("Direct", "good", s);
            return "Found Good Word";
        }
        else if (checkWord.equals("bad")){
            rabbitTemplate.convertAndSend("Direct", "bad", s);
            return "Found Bad Word";
        }
        return "";
    }
    @RequestMapping(value = "/getSentence", method = RequestMethod.GET)
    public Sentence getSentence(){
        Object sentences = rabbitTemplate.convertSendAndReceive("Direct", "queue", "");
        return ((Sentence) sentences);
    }
}
