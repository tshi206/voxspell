package voxspell.toolbox;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import javax.swing.SwingWorker;

import voxspell.app.User;
import voxspell.gui.Scoreboard;

public class ScoreBoardWorker extends SwingWorker<Void, Void> {

	private Vector<Vector<String>> rowResult = new Vector<Vector<String>>();
	
	private Vector<String> columnNames = new Vector<String>();
	
	public ScoreBoardWorker(){
		columnNames.add("Rank:");
		columnNames.add("Name:");
		columnNames.add("Highest mastered record:");
		columnNames.add("Most frequently played:");
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		
		ArrayList<String> usernames = new ArrayList<String>();
		for (String line : VoxDatabase.getContents().get(1)){
			String[] temp = line.split(" ");
			usernames.add(temp[0]);
		}
		
		ArrayList<User> users = new ArrayList<User>();
		users.add(new User("anonymous", VoxDatabase.sysfilesDirectory));
		
		for (String name : usernames){
			User user = new User(name, VoxDatabase.usrDirectory+name+"/");
			users.add(user);
		}
		
		Collections.sort(users, Collections.reverseOrder());
		
		int count = 1;
		for (User user : users){
			Vector<String> userScores = new Vector<String>();
			if (count == 1){
				userScores.add("SpellingGod");
			}else if (count == 2){
				userScores.add("SpellingKing");
			}else if (count == 3){
				userScores.add("SpellingKnight");
			}else if (count == 4){
				userScores.add("SpellingWarrior");
			}else if (count == 5){
				userScores.add("SpellingNinja");
			}else{
				userScores.add(count+"");
			}
			userScores.add(user.getUsername());
			userScores.add(user.getHighestAmountOfMasteredWords()+"");
			userScores.add(user.getMostPlayedCategory());
			rowResult.add(userScores);
			count++;
		}
		
		return null;
	}

	@Override
	protected void done(){
		
		Scoreboard.getScoreboardWindow().updateTable(rowResult, columnNames);
		
	}
	
}
