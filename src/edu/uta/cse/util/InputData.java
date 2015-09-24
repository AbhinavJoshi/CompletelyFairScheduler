package edu.uta.cse.util;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;

public class InputData {
	public Integer tasksCount, runTime;
	public ArrayList<Task> tasks;

	public InputData(String fileName){
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(fileName));
			String firstLine = br.readLine();
			String[] data = firstLine.split(" ");
			
			this.tasksCount= Integer.parseInt(data[0]);
			this.runTime= Integer.parseInt(data[1]);
			
			String record =null;
			tasks = new ArrayList<Task>();
			while ((record = br.readLine()) != null) {
				tasks.add(new Task(record.split(" ")[0], Integer.parseInt(record.split(" ")[1]), Integer.parseInt(record.split(" ")[2])));
			}
		}catch(Exception e){
			System.out.println("Execption occured while execution :"+ e.getMessage());
			e.printStackTrace();
		}
	}

}
