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

    private Task tasks[];
    private String fileName;

    public Gantt(Task[] tasks, String fileName) {
        this.tasks = tasks;
        this.fileName = fileName;
    }

    public Gantt() {
    }

    public void execute() {
		GanttChart gantt = new GanttChart();
		
		gantt.setTimeUnit(TimeUnit.Year);

		Config config = gantt.getConfig();		
		config.setTimeUnitWidth(24);
		config.setBlankStepsToDeadline(0);
		config.setBlankStepsToKickoffTime(0);
		config.setDeadlineBackColor(Color.WHITE);
		config.setKickoffTimeBackColor(Color.WHITE);
        config.setGanttChartBackColor(Color.WHITE);// ---- change the century column color
        config.setTaskTreeViewBackColor(Color.WHITE);
        config.setTaskBarBackColor(Color.GRAY);

		config.setAllowAccurateTaskBar(false);
		config.setFillInvalidArea(true);
		config.setShowTaskInfoBehindTaskBar(true);

		/*Task task1 = new Task("1", new Time(1000), new Time(1001));
		Task task2 = new Task("2", new Time(1013), new Time(1018));
        Task task3 = new Task("3", new Time(1005), new Time(1008));

		task2.addPredecessor(task1);

        fileName = "xxx";
        tasks = new Task[]{task1, task2, task3};*/

        GanttModel model = new GanttModel();
        model.addTask(tasks);

        model.setKickoffTime(new Time(1000));
        model.setDeadline(new Time(model.getTaskTreeModel().getLatestTask().getEnd().getYear() + 2)); // put makespan here (18 for this case)  and more 2 units just to leave some space

		gantt.setModel(model);

		String filePath = "gantt/" + fileName + ".png";
		try {
			gantt.generateImageFile(filePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
