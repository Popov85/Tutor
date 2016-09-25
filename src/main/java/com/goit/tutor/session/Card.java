package com.goit.tutor.session;

import java.util.ArrayList;

public class Card {

	public int wordId;
	public String engWord;
	public String trsp;
	public String rusWord;

	//Status 1 - deferred, 2 - to learn (-), 3 - to learn (n), 4 - learned)*
	public int status;
	//Current status of the word +
	public int answered;
	//All the variants of translation
	public ArrayList<String> translations =new ArrayList<String>();
	//A bunch of examples
	public ArrayList<String> examples =new ArrayList<String>();

	@Override
	public String toString() {
		String trans="";
		for (String temp : translations) {
			trans=trans+temp+" ; ";
		}
		String examp = "";
		for (String temp : examples) {
			examp = examp+temp+" ; ";
		}
		return "Card [wordId=" + wordId + ", engWord=" + engWord + ", trsp="
				+ trsp + ", status=" + status
				+ ", answered=" + answered + ", translations=" + trans
				+ ", examples="+examp+" ]";
	}
}
