package com.goit.tutor.session;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.Date;
import java.util.List;

public final class StatisticsBySession {

        private String userId;
        private String dictionary;
        /*Date&Time stamp*/
        private Date dateTime;

        private int incorrect;
        private int correct;
        private int prompts;

        private int cardsChangedStatus;
        private List<StatisticsByCard> mistakes;

        private long timeSpent;

        public StatisticsBySession(Date dateTime, int incorrect, int prompts,
                                   int correct, int cardsChangedStatus, long timeSpent,
                                   List<StatisticsByCard> mistakes) {
                this.userId = System.getProperty("user.name");
                this.dictionary = "Default_Dictionary";
                this.dateTime = dateTime;
                this.incorrect = incorrect;
                this.correct = correct;
                this.prompts = prompts;
                this.cardsChangedStatus = cardsChangedStatus;
                this.timeSpent = timeSpent;
                this.mistakes = mistakes;
        }

        public void saveToJSON() {
                try {
                        FileOutputStream os=new FileOutputStream("stat.json",true);
                        BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(os));
                        Gson gson=new GsonBuilder().create();
                        String temp=gson.toJson(this);
                        bw.append("\n"+temp);
                        bw.close();
                } catch (FileNotFoundException e) {
                        e.printStackTrace();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

}
