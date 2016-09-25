package com.goit.tutor.session;

import com.goit.tutor.xml.XMLUpdater;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Андрей on 11.09.2016.
 */
public class UtilitySession {

        public ArrayList<Card> shuffleCards(ArrayList<Card> cards, int perSession) {
                int pointer=1;
                int reverse= cards.size() -1;
                for (int i = 0; i < perSession; i++) {
                        Random randomGenerator = new Random();
                        int randomInt = randomGenerator.nextInt(cards.size() - pointer);
                        Collections.swap(cards, randomInt, reverse);
                        reverse--;
                        pointer++;
                }
                return cards;
        }

        /*Gets a word to hide from a user*/
        public String getHiddenPart(Card card, int i) {
                return getPart(i, getStringFromArray(card.translations), card.engWord);
        }

        /*Gets a word to show to a user*/
        public String getShownPart(Card card, int i) {
                return getPart(i, card.engWord, getStringFromArray(card.translations));
        }

        private String getPart(int i, String one, String two) {
                String toReturn;
                // Mixed by default. I need to decide what to hide based on i value (even OR uneven)
                // If i is even - hide Russian translation, otherwise - hide English word
                if ((i%2) == 0) {
                        toReturn=one;
                }
                else {
                        toReturn=two;
                }
                return toReturn;
        }

        /*Watch, if answered attribute becomes greater than a threshold - change status attribute to "4"*/
        public boolean incrementCardsAttributes(Card card) {
                card.answered++;
                if (card.answered >= Settings.THRESHOLD) {
                        card.status = 4;
                        card.answered = 0;
                        return true;
                }
                return false;
        }

        public void decrementCardsAttributes(Card card) {
                if (Settings.CHANGE_METHOD) {
                        if (card.answered != 0) {
                                card.answered--;
                        }
                }
        }

        /* Converts an array of string into a long String*/
        private String getStringFromArray(ArrayList<String> myArray) {
                StringBuilder sb = new StringBuilder();
                for (String s : myArray) {
                        sb.append(s);
                        sb.append(" ; ");
                }
                return sb.toString();
        }

        public String convertEffectiveness(int correct, int counter) {
                float result = (float) correct/counter;
                return Float.toString(round(result*100,1)) +" %";
        }

        private float round(float value, int precision) {
                int scale = (int) Math.pow(10, precision);
                return (float) Math.round(value * scale) / scale;
        }

        public String convertTime(long millis){
                return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                        TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                        TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
        }

        public void saveSession(HashMap<Integer, Card> cards, String path) {
                XMLUpdater anUpdater=new XMLUpdater(cards, path);
                anUpdater.update();
        }
        // TODO Save statistics
        public void saveStatistics(int incorrect, int prompts, int correct,
                                   int changed, List<StatisticsByCard> mistakes, long time) {
                StatisticsBySession stat = new StatisticsBySession(new Date(),
                        incorrect, prompts, correct, changed, time, mistakes);
                stat.saveToJSON();
        }
}
