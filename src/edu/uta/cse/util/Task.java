package edu.uta.cse.util;
public class Task implements Comparable<Task> {
	public static boolean inScheduler = false;
	public Integer arrivalTime, cpuTime, runTime, trunTime, actualStartTime,
			completedTime;
	public String taskName;

	public Task(String name, int arrivalTime, int cpuTime) {
		this.taskName = name;
		this.arrivalTime = arrivalTime;
		this.cpuTime = cpuTime;
	}

	@Override
	public int compareTo(Task o) {
		if (!inScheduler) {
			if (this.arrivalTime < o.arrivalTime)
				return -1;
			else if (this.arrivalTime == o.arrivalTime)
				return 0;
			else
				return 1;
		} else {
			if (this.runTime < o.runTime)
				return -1;
			else if (this.runTime == o.runTime)
				return 0;
			else
				return 1;
		}
	}

	@Override
	public String toString() {
		return taskName + "(" + runTime.toString() + ")";
	}
}
