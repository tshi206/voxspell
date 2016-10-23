package voxspell.app;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import voxspell.toolbox.VoxDatabase;

public class User implements Comparable<User>{
	
	private String username;
	private String userDir;
	
	private int highestAmountOfMasteredWords = 0;
	

	public User(String username, String userDir){
		this.username = username;
		this.userDir = userDir;
		this.highestAmountOfMasteredWords = calculateHighestAmountOfMasteredWords(userDir);
	}
	
	public String getUsername() {
		return username;
	}

	public String getMostPlayedCategory(){
		ArrayList<String> words = new ArrayList<String>();
		String[] filenames = {".masteredhistory", ".faultedhistory", ".failedhistory"};
		Scanner scanner;
		for (String s : filenames){
			try {
				scanner = new Scanner(new FileReader(userDir+s));
				while (scanner.hasNext()){
					String line = scanner.nextLine();
					words.add(line);
				}
				scanner.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		ArrayList<Category> cats = new ArrayList<Category>();
		for (String categoryName : VoxDatabase.getCategories()){
			Category cate = new Category(categoryName);
			for (String s : words){
				if (VoxDatabase.getLevelContents().get(VoxDatabase.getCategories().indexOf(categoryName)).contains(s)){
					cate.wordsCount++;
				}
			}
			cats.add(cate);
		}
		Collections.sort(cats);
		
		if (cats.get(cats.size()-1).wordsCount==0){
			return "N/A";
		}
		return cats.get(cats.size()-1).getName();
	}

	private int calculateHighestAmountOfMasteredWords(String userDir){
		
		int count = 0;
		Scanner scanner;
		try {
			scanner = new Scanner(new FileReader(userDir+".records"));
			while (scanner.hasNext()){
				scanner.nextLine();
				count++;
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return count;
		
	}
	
	
	public boolean equalsTo(User u){
		if (highestAmountOfMasteredWords == u.highestAmountOfMasteredWords){
			return true;
		}
		return false;
	}
	
	public int getHighestAmountOfMasteredWords() {
		return highestAmountOfMasteredWords;
	}

	@Override
	public int compareTo(User u) {
		if (this.equalsTo(u)){
			return 0;
		}else{
			if (highestAmountOfMasteredWords>u.highestAmountOfMasteredWords){
				return 1;
			}else {
				return -1;
			}
		}
	}
	
}

class Category implements Comparable<Category>{
	
	protected int wordsCount = 0;
	private String name;
	
	public Category(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean equalsTo(Category c){
		if (wordsCount == c.wordsCount){
			return true;
		}
		return false;
	}
	
	@Override
	public int compareTo(Category c) {
		if (this.equalsTo(c)){
			return 0;
		}else{
			if (wordsCount>c.wordsCount){
				return 1;
			}else {
				return -1;
			}
		}
	}
	
}
