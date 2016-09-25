package com.goit.tutor.session;

import java.security.InvalidParameterException;

/**
 * Created by Андрей on 11.09.2016.
 */
public class Settings {

        /*false - not decrementing, true - decrementing*/
        public static final boolean CHANGE_METHOD = false;

        public static final int THRESHOLD = 10;

        private final int perSession;
        /* Easy vs difficult compare words method*/
        private final boolean checkMethod;

        public int getPerSession() {
                return perSession;
        }

        public boolean isCheckMethod() {
                return checkMethod;
        }

        public Settings() {
                this(3, true);
        }

        public Settings(int perSession, boolean checkMethod) {
                if (perSession < 1) throw new InvalidParameterException();
                this.perSession = perSession;
                this.checkMethod = checkMethod;
        }

        @Override
        public String toString() {
                return "Settings{" +
                        "perSession=" + perSession +
                        ", checkMethod=" + checkMethod +
                        '}';
        }
}
