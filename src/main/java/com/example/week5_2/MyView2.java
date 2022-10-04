package com.example.week5_2;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.*;
import static com.vaadin.flow.component.notification.Notification.Position.BOTTOM_START;

@Route(value = "index2")
public class MyView2 extends HorizontalLayout {
    private TextField tf1, tf2;
    private Button btnAddGoodWord, btnAddBadWord, btnAddSentence, btnShowSentence;
    private TextArea taGoodSentence, taBadSentence;
    private ComboBox<String> cbGoodWord, cbBadWord;
    private VerticalLayout vl1, vl2;
    private Notification notification;

    public MyView2(){
        tf1 = new TextField("Add Word");
        tf2 = new TextField("Add Sentence");
        btnAddGoodWord = new Button("Add Good Word");
        btnAddBadWord = new Button("Add Bad Word");
        btnAddSentence = new Button("Add Sentence");
        btnShowSentence = new Button("Show Sentence");
        taGoodSentence = new TextArea("Good Sentence");
        taBadSentence = new TextArea("Bad Sentence");
        cbGoodWord = new ComboBox<String>("Good Words");
        cbBadWord = new ComboBox<String>("Bad Words");
        vl1 = new VerticalLayout();
        vl2 = new VerticalLayout();

        tf1.setWidthFull(); tf2.setWidthFull();
        btnAddGoodWord.setWidthFull(); btnAddBadWord.setWidthFull();
        btnAddSentence.setWidthFull(); btnShowSentence.setWidthFull();
        taGoodSentence.setWidthFull(); taBadSentence.setWidthFull();
        cbGoodWord.setWidthFull(); cbBadWord.setWidthFull();
        vl1.setWidthFull(); vl2.setWidthFull();

        cbGoodWord.setItems(new Word().goodWords);
        cbBadWord.setItems(new Word().badWords);

        vl1.add(tf1, btnAddGoodWord, btnAddBadWord, cbGoodWord, cbBadWord);
        vl2.add(tf2,btnAddSentence, taGoodSentence, taBadSentence, btnShowSentence);
        this.add(vl1, vl2);

        btnAddGoodWord.addClickListener(event -> {
            String s = tf1.getValue();
            ArrayList<String> result = WebClient
                    .create()
                    .post()
                    .uri("http://localhost:8080/addGood/"+s)
                    .retrieve()
                    .bodyToMono(ArrayList.class)
                    .block();
            cbGoodWord.setItems(result);
        });

        btnAddBadWord.addClickListener(event -> {
            String s = tf1.getValue();
            ArrayList<String> result = WebClient
                    .create()
                    .post()
                    .uri("http://localhost:8080/addBad/"+s)
                    .retrieve()
                    .bodyToMono(ArrayList.class)
                    .block();
            cbBadWord.setItems(result);
        });

        btnAddSentence.addClickListener(event -> {
            String s = tf2.getValue();
            String result = WebClient
                    .create()
                    .post()
                    .uri("http://localhost:8080/proof/"+s)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            notification = Notification.show(result, 2000, BOTTOM_START);
        });

        btnShowSentence.addClickListener(event -> {
            Sentence result = WebClient
                    .create()
                    .get()
                    .uri("http://localhost:8080/getSentence")
                    .retrieve()
                    .bodyToMono(Sentence.class)
                    .block();
            taGoodSentence.setValue(result.goodSentences+"");
            taBadSentence.setValue(result.badSentences+"");
        });
    }
}
