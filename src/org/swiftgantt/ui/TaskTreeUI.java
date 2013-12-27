package org.swiftgantt.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

import org.swiftgantt.TaskTreeView;
import org.swiftgantt.common.EventLogger;
import org.swiftgantt.model.Task;
import org.apache.log4j.LogManager;

/**
 * 
 * @author Yuxing Wang
 * 
 */
public class TaskTreeUI extends BaseUI {
	protected TaskTreeView taskTreeView = null;

	private final int NODE_WIDTH = 9;

	int fontHeight = 0;
	int rowHeight = 0;
	int cellWidth = 0;

	int hScrollOffset = 5;//Horizontal scroll offset

	public TaskTreeUI() {
		logger = LogManager.getLogger(TaskTreeUI.class);
	}

	public static ComponentUI createUI(JComponent c) {
		return new TaskTreeUI();
	}

	@Override
	public void installUI(JComponent c) {
		taskTreeView = (TaskTreeView) c;
		super.installUI(c);
	}

	@Override
	public void uninstallUI(JComponent c) {
		super.uninstallUI(c);
	}

	@Override
	public void paint(Graphics g, JComponent c) {
		super.paint(g, c);
		logger.info("Paint TaskTreeUI");
		rowHeight = super.ganttChart.getConfig().getGanttChartRowHeight();
		cellWidth = rowHeight / 2;
		fontHeight = g.getFontMetrics().getHeight();
		//
		int width = super.clientWidth;
		int height = super.clientHeight;
		g.setColor(super.ganttChart.getConfig().getTaskTreeViewBackColor());
		g.fillRect(-hScrollOffset, 0, width, height);
		// Draw task tree.
		g.setColor(Color.black);
		drawTasks(g);
		g.setColor(Color.black);
		g.drawRect(0, 0, width, height - 1);
	}

	/*
	 * Draw all tasks in tree model.
	 */
	private void drawTasks(Graphics g) {
		if(super.ganttChart == null || super.ganttChart.getModel() == null
				|| super.ganttChart.getModel().getTaskTreeModel() == null){
			return;
		}
		Task root = (Task) super.ganttChart.getModel().getTaskTreeModel().getRoot();
		int rowNum = 0;
		for (int i = 0; i < root.getChildCount(); i++) {
			Task task = (Task) root.getChildAt(i);
			Task nextTask = ((i + 1) < root.getChildCount()) ? (Task) root.getChildAt(i + 1) : null;
			rowNum = drawTask(g, task, nextTask, rowNum);//drawTask() knows what's the next task row num.
		}
	}

	/*
	 * return currrent row number after drawn this task and it's sub-tasks.
	 */
	private int drawTask(Graphics g, Task task, Task nextSibling, int rowNum) {
		String next = nextSibling != null ? nextSibling.getName() : "";
		if (logger.isDebugEnabled()) {
			logger.debug("Paint task " + task + " at row " + rowNum + " to next " + next + " for task tree view.");
		}

		int offset = 0;
		int x1 = task.getLevel() * cellWidth - hScrollOffset;
		int y1 = rowNum * rowHeight;
		// Draw connector line from this task to first child task.
		g.drawLine(x1, y1 - cellWidth, x1, y1 + cellWidth);
		// Draw connector line as background first, from this task to next sibling task.
		if (nextSibling != null) { // If next sibling is existed, we need lind to it. 
			logger.debug("To next sibling: " + nextSibling.getName());
			offset += task.getTasksCount();
			int y2 = (rowNum + offset + 1) * rowHeight;
			g.drawLine(x1, y1 + cellWidth, x1, y2 + cellWidth);
		}

		// The rectangle for each tasks's area.
		Rectangle taskRect = new Rectangle(x1, y1, taskTreeView.getWidth(), rowHeight);
		//Draw connection.
		this.drawConnection(g, taskRect);
		// Draw all children recursively.
		if (!task.isLeaf()) {// Recursive to children.
			this.drawNode(g, taskRect);
			for (int i = 0; i < task.getChildCount(); i++) {
				Task curTask = (Task) task.getChildAt(i);
				Task nextTask = ((i + 1) < task.getChildCount()) ? (Task) task.getChildAt(i + 1) : null;
				rowNum = drawTask(g, curTask, nextTask, ++rowNum);
			}
		}
		this.drawLabel(g, task.getName(), taskRect);
		return rowNum;
	}

	/*
	 * Draw connection from left center tree node to label.
	 */
	private void drawConnection(Graphics g, Rectangle rect) {
		int x1 = rect.x - hScrollOffset;
		int x2 = rect.x + cellWidth - hScrollOffset;
		int y = rect.y + cellWidth;
		g.drawLine(x1, y, x2, y);
	}

	/*
	 * Draw the expanded node on the top of left border center.
	 */
	private void drawNode(Graphics g, Rectangle rect) {
		int x1 = rect.x - NODE_WIDTH / 2;
		int x2 = x1 + NODE_WIDTH;
		int y1 = (rowHeight - NODE_WIDTH) / 2 + rect.y;
		int y2 = y1 + NODE_WIDTH / 2 + 1;
		//		g.drawLine(rect.x, rect.y - rowHeight/2, rect.x, y1); //Draw the connector from node above to current node. 
		g.setColor(taskTreeView.getBackground());
		g.fillRect(x1, y1, NODE_WIDTH, NODE_WIDTH);
		g.setColor(Color.black);
		g.drawRect(x1, y1, NODE_WIDTH, NODE_WIDTH); //Draw the expanded node.
		g.drawLine(x1, y2, x2, y2); // Draw the connector from expanded node to the label.
	}

	/*
	 * 
	 */
	private void drawLabel(Graphics g, String label, Rectangle rect) {
		if (label == null) {
			return;
		}
		int x = rect.x + cellWidth;
		int y = rect.y + rowHeight - fontHeight / 2;
		int width = 1000; // Temporary 100 for width.
		int height = fontHeight;
		g.setColor(super.ganttChart.getConfig().getTaskTreeViewBackColor());
		g.fillRect(x, rect.y, width, height);
		g.setColor(Color.black);
		//		g.drawRect(x, rect.y, width, height);
		g.drawChars(label.toCharArray(), 0, label.length(), x, y);
	}

	public int getHorizonScrollOffset() {
		return hScrollOffset;
	}

	public void setHorizonScrollOffset(int horizonScrollOffset) {
		this.hScrollOffset = horizonScrollOffset;
	}

}
