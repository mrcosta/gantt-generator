package org.gantt.generator.mrcpsp;

import java.awt.Color;
import java.io.IOException;
import java.util.Calendar;

import org.swiftgantt.Config;
import org.swiftgantt.GanttChart;
import org.swiftgantt.common.Time;
import org.swiftgantt.model.GanttModel;
import org.swiftgantt.model.Task;
import org.swiftgantt.ui.TimeUnit;

public class Gantt {
	
	public void execute() {
		GanttChart gantt = new GanttChart();
		
		gantt.setTimeUnit(TimeUnit.Year);
		//gantt.setShowTreeView(false);
		
		Config config = gantt.getConfig();		
		config.setTimeUnitWidth(25);
		config.setBlankStepsToDeadline(0);
		config.setBlankStepsToKickoffTime(0);		
		config.setDeadlineBackColor(Color.WHITE);
		config.setKickoffTimeBackColor(Color.WHITE);
		
		config.setAllowAccurateTaskBar(false);
		config.setFillInvalidArea(true);
		config.setShowTaskInfoBehindTaskBar(true);
		
		
		
		GanttModel model = new GanttModel();
		
		model.setKickoffTime(new Time(1000));
		model.setDeadline(new Time(1020)); // put makespan here (18 for this case)  and more 2 units just to leave some space
		
		Task task1 = new Task("1", new Time(1000), new Time(1001));
		Task task2 = new Task("2", new Time(1013), new Time(1018));
		
		task2.addPredecessor(task1);
		
		model.addTask(new Task[]{task1, task2});
		gantt.setModel(model);		
		
		String filePath = "lib/xxx.png";
		try {
			gantt.generateImageFile(filePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
