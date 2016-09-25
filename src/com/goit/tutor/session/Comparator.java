package com.goit.tutor.session;

/**
 * Created by Андрей on 11.09.2016.
 */
public class Comparator {

        private final Prompt prompt;
        private boolean checkMethod;

        public Comparator(Prompt prompt, boolean checkMethod) {
                this.prompt = prompt;
                this.checkMethod = checkMethod;
        }

        // Compares two words and determine if a card has been correctly answered
        // There are two cases: 1 - checkMethod=true OR false
        // true - quickly enter 2-3 letters, if correct - count it as correct
        // false - only 100% matching, there should be a word like this, if not - count it as incorrect
        public boolean compareWords(String usersWords, String hiddenWords) {
                boolean result=false;
                if (checkMethod) {// Lighter mode
                        result=method1Easier(usersWords, hiddenWords);
                }
                else {// Harder mode
                        result=method2Harder(usersWords, hiddenWords);
                }
                return result;
        }

        private boolean method1Easier(String usersWords, String hiddenWords) {
                boolean toReturn;
                if (usersWords.equals("")|| usersWords.equals(prompt.getCurrentPrompt())) {
                        toReturn=false;
                }
                else {
                        String case1=" "+usersWords;
                        String case2=usersWords;
                        if (containChecker(case1, hiddenWords)||
                                (startChecker(case2, hiddenWords))) {
                                toReturn=true;
                        }
                        else {
                                toReturn=false;
                        }
                }
                return toReturn;
        }

        private boolean method2Harder(String usersWords, String hiddenWords) {
                return hiddenWords.equals(usersWords)? true:false;
        }

        private boolean containChecker(String word, String sentence) {
                return sentence.toLowerCase().contains(word.toLowerCase());
        }

        private boolean startChecker(String word, String sentence) {
                return sentence.startsWith(word);
        }
}
