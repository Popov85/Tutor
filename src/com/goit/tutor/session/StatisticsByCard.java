package com.goit.tutor.session;

/* Keeps details of the use Cards during a startSmallSession
 * 
 */
public final class StatisticsByCard {

        private int cardId;
        // A word where a user made a mistake
        private String question;
        // How much time did a user spend on a card
        //private Date timeSpent = new Date();
        private String usersIncorrectAnswer;
        // One or several first letters of a word
        // Two or more prompt letters is highly discouraged
        private String promptGiven;

        private int directionOfTranslation;

        public String getQuestion() {
                return question;
        }

        public String getUsersIncorrectAnswer() {
                return usersIncorrectAnswer;
        }

        public String getPromptGiven() {
                return promptGiven;
        }

        public int getDirectionOfTranslation() {
                return directionOfTranslation;
        }

        public StatisticsByCard(int cardId, String question, String usersIncorrectAnswer, String promptGiven) {
                this.cardId = cardId;
                this.question = question;
                this.usersIncorrectAnswer = usersIncorrectAnswer;
                this.promptGiven = promptGiven;
        }
}
