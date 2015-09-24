package edu.uta.cse.heap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import edu.uta.cse.util.*;



public class HeapScheduler {

	public static void main(String[] args) {

		InputData data = new InputData(
				//"C:/Users/Sandeep/Desktop/TestFiles/input1.txt");
				"C:/Users/Sandeep/Desktop/TestFiles/newTestFiles/1000.txt");
		Collections.sort(data.tasks);
		Scanner scan = new Scanner(System.in);
		System.out.println("Total number of Tasks : "+data.tasksCount);
		System.out.println("Total duration : "+data.runTime+"\n");
		
		System.out.println("Please enter the duration for which you want to execute : ");
		int inputTime = scan.nextInt();
		
		scan.close();
		final long executionStartTime;
		executionStartTime = System.nanoTime();
		HeapMinPriorityQueue<Task> heap = new HeapMinPriorityQueue<Task>();
		ArrayList<TimeData> runResults = schedule(heap, data, inputTime);
		String taskList = "<";
		for (TimeData td : runResults) {
			if (td.runningTask != null) {
				System.out.println("-------------------\nCPU Time: "
						+ td.curTime);
				System.out.println("Number of Tasks in Execution: "
						+ td.numberOfTasks);
				System.out.println("running Task: " + td.runningTask.taskName+", current runtime: "+td.runningTask.runTime);
				taskList = taskList + td.runningTask.taskName + ", ";
			}
			if (td.completedTask != null)
				System.out.println("completed Task: " + td.runningTask.taskName
						+ ", finsih time: " + td.runningTask.completedTime);
			if (td.runningTask != null)
				System.out.println("Tree State: " + td.treeState);
		}
		taskList = taskList.substring(0, taskList.length() - 2);
		System.out.println("Task List : " + taskList + ">");
		System.out.println("Total time :"
				+ (System.nanoTime() - executionStartTime)/1000000
				+ " milliseconds");
	}

	public static ArrayList<TimeData> schedule(HeapMinPriorityQueue<Task> heap,
			InputData data, int inputTime) {

		int taskIndex = 0;
		Task.inScheduler = true;

		// min_vruntime is set to the smallest vruntime of tasks on the
		// timeline
		int min_vruntime = 0;

		// current running task or null if no tasks are running.
		Task running_task = null;

		// Initialize statistics gathering
		ArrayList<TimeData> results = new ArrayList<TimeData>();
		// Revisit binarytree.RESET_STATS();

		// Loop from time/tick 0 through the total time/ticks specified
		for (int curTime = 0; curTime < inputTime; curTime++) {
			// Results data for this time unit/tick
			TimeData tresults = new TimeData();
			tresults.curTime = curTime;

			// Check tasks at the beginning of the task queue. Add any to
			// the timeline structure when the start_time for those tasks
			// has arrived.
			while (taskIndex < data.tasksCount
					&& (curTime >= data.tasks.get(taskIndex).arrivalTime)) {
				Task new_task = (Task)data.tasks.get(taskIndex++);
				// new tasks get their vruntime set to the current
				// min_vruntime
				new_task.runTime = min_vruntime;
				new_task.trunTime = 0;
				new_task.actualStartTime = curTime;
				heap.insert(new_task);
				
				tresults.numberOfTasks = heap.size()
						+ (running_task != null ? 1 : 0);
			}

			// If there is a task running and its vruntime exceeds
			// min_vruntime then add it back to the timeline. Since
			// vruntime is greater it won't change min_vruntime when it's
			// added back to the timeline.
			if (running_task != null && (running_task.runTime >= min_vruntime)) {
				heap.insert(running_task);
				running_task = null;
			}

			// If there is no running task (which may happen right after
			// the running_task is added back to the timeline above), find
			// the task with the smallest vruntime on the timeline, remove
			// it and set it as the running_task and determine the new
			// min_vruntime.
			if (running_task == null && heap.size() > 0) {
				Task min_node = heap.extractMin();
				running_task = min_node;
				if (heap.size() > 1) {
					min_vruntime = heap.minimum().runTime;
				}
			}

			tresults.runningTask = running_task;

			// Update the running_task (if any) by increasing the vruntime
			// and the truntime. If the running task has run for it's full
			// duration then report it as completed and set running_task
			// to null.
			boolean task_done = false;
			if (running_task != null) {
				running_task.runTime++;
				running_task.trunTime++;
				// console.log(curTime + ": " + running_task.id);
				if (running_task.trunTime >= running_task.cpuTime) {
					running_task.completedTime = curTime;
					tresults.completedTask = running_task;
					task_done = true; // Set running_task to null later
					// console.log("Completed task:", running_task.id);
				}
			}
			tresults.treeState = heap.printTree();
			results.add(tresults);

			if (task_done) {
				running_task = null;
			}

		}
		return results;
	}

}
