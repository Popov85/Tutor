package com.goit.tutor.session;

/**
 * Created by Андрей on 29.07.2016.
 */
public class User {

        private String nickname;

        /*Take into account RunTime (sec), Number of mistakes (quantity) and Number of Prompts taken (quantity)*/
        // TODO
        private String personalBest = "";

        public User(String nickname) {
                this.nickname = nickname;
        }

        public String getNickname() {
                return nickname;
        }
}
