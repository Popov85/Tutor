package com.goit.tutor.session;

/**
 * Created by Андрей on 11.09.2016.
 */
public class Prompt {

        private String lexeme;

        private String currentPrompt = "";
        private int counterOfPromptLetters = 1;
        private int counterOfPrompts = 0;

        public Prompt(String lexeme) {
                this.lexeme = cutOutBracketPrefix(lexeme);
        }

        public String getCurrentPrompt() {
                return currentPrompt;
        }

        public int getCounterOfPromptLetters() {
                return counterOfPromptLetters;
        }

        public int getCounterOfPrompts() {
                return counterOfPrompts;
        }

        private String cutOutBracketPrefix(String lexeme) {
                if (lexeme.startsWith("(")) {
                        int beginIndex = identifyBeginIndex(lexeme);
                        lexeme = lexeme.substring(beginIndex, lexeme.length());
                }
                return lexeme;
        }

        // Returns a position of ")" symbol
        private int identifyBeginIndex(String lexeme) {
                int position=0;
                for (int i = 0; i < lexeme.length(); i++){
                        char c = lexeme.charAt(i);
                        if (c==')') {
                                position=i;
                                break;
                        }
                }
                return position+2;
        }

        public boolean showPrefix() {
                if (currentPrompt.length()!=lexeme.length()) {
                        String ltr = getPrompt(lexeme, this.counterOfPromptLetters);
                        currentPrompt = ltr;
                        counterOfPromptLetters++;
                        counterOfPrompts++;
                        return true;
                } else {// Prompt has grown into the exact phrase
                        return false;
                }
        }

        // () - is omitted, space is ignored
        private String getPrompt(String aWord, int letterInOrderToShow) {
                String nextLetter = aWord.substring(0, letterInOrderToShow);
                return nextLetter;
        }
}
