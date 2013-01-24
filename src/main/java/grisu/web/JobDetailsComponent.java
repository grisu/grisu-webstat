package grisu.web;

import grisu.backend.model.job.JobStat;
import grisu.jcommons.constants.Constants;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class JobDetailsComponent extends CustomComponent{

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private Panel jobDetPanel;
	@AutoGenerated
	private VerticalLayout jobDetLayout;
	@AutoGenerated
	private Table tblDets;
	@AutoGenerated
	private Table tblProps;

	private static Logger log = LoggerFactory.getLogger(Thread.currentThread().getClass());

	public JobDetailsComponent() {
		// TODO Auto-generated constructor stub
		
		log.debug("Inside constructor");
		
		buildMainLayout();
		setCompositionRoot(mainLayout);
		
		
		tblDets.addContainerProperty("property", String.class, null);
		tblDets.addContainerProperty("value", String.class, null);
		
		tblDets.setVisibleColumns(new String [] {"property", "value"});
		tblDets.setColumnHeaders(new String [] {"Property", "Value"});
		
		tblProps.addContainerProperty("property", String.class, null);
		tblProps.addContainerProperty("value", String.class, null);
		
		tblProps.setVisibleColumns(new String[] {"property", "value"});
		tblProps.setColumnHeaders(new String [] {"Property",  "value"});
				
		tblDets.setCaption("Job Details");
		tblProps.setCaption("Job Properties");
		tblDets.setPageLength(7);
		tblProps.setPageLength(9);
		
		log.debug("Exiting constructor");
	}

	public void populate(JobStat job) {
		// TODO Auto-generated method stub

		log.debug("Inside populate()");
		
		tblDets.removeAllItems();
		tblProps.removeAllItems();
		
		if(job!=null){
			tblDets.addItem(new Object [] {"Job Name",  job.getJobname()},null);
			tblDets.addItem(new Object [] {"DN",  job.getDn()}, null);
			tblDets.addItem(new Object [] {"Group",  job.getFqan()}, null);
			tblDets.addItem(new Object [] {"Status",  job.getStatus()}, null);
			tblDets.addItem(new Object [] {"Submission Type",  job.getSubmissionType()}, null);
			tblDets.addItem(new Object [] {"Active",  job.isActive()}, null);
			tblDets.addItem(new Object [] {"Batch job",  job.isBatchJob()}, null);
			
			Map<String, String> propertyMap = job.getProperties();
			
			tblProps.addItem(new Object [] {"Job Name",  propertyMap.get(Constants.JOBNAME_KEY)},null);
			tblProps.addItem(new Object [] {"Commandline",  propertyMap.get(Constants.COMMANDLINE_KEY)},null);
			tblProps.addItem(new Object [] {"Number of CPUs",  propertyMap.get(Constants.NO_CPUS_KEY)},null);
			tblProps.addItem(new Object [] {"Memory in Bytes",  propertyMap.get(Constants.MEMORY_IN_B_KEY)},null);
			tblProps.addItem(new Object [] {"Walltime in minutes",  propertyMap.get(Constants.WALLTIME_IN_MINUTES_KEY)},null);
			tblProps.addItem(new Object [] {"Submission location",  propertyMap.get(Constants.SUBMISSIONLOCATION_KEY)},null);
			tblProps.addItem(new Object [] {"Submission time",  propertyMap.get(Constants.SUBMISSION_TIME_KEY)},null);
			tblProps.addItem(new Object [] {"Application name",  propertyMap.get(Constants.APPLICATIONNAME_KEY)},null);
			tblProps.addItem(new Object [] {"Application version",  propertyMap.get(Constants.APPLICATIONVERSION_KEY)},null);
		}
		
		log.debug("Exiting populate()");
	}

	@AutoGenerated
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("-1px");
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("-1px");
		
		// jobDetPanel
		jobDetPanel = buildJobDetPanel();
		mainLayout.addComponent(jobDetPanel);
		mainLayout.setExpandRatio(jobDetPanel, 1.0f);
		
		return mainLayout;
	}

	@AutoGenerated
	private Panel buildJobDetPanel() {
		// common part: create layout
		jobDetPanel = new Panel();
		jobDetPanel.setImmediate(false);
		jobDetPanel.setWidth("100.0%");
		jobDetPanel.setHeight("-1px");
		
		// jobDetLayout
		jobDetLayout = buildJobDetLayout();
		jobDetPanel.setContent(jobDetLayout);
		
		return jobDetPanel;
	}

	@AutoGenerated
	private VerticalLayout buildJobDetLayout() {
		// common part: create layout
		jobDetLayout = new VerticalLayout();
		jobDetLayout.setImmediate(false);
		jobDetLayout.setWidth("100.0%");
		jobDetLayout.setHeight("-1px");
		jobDetLayout.setMargin(false);
		jobDetLayout.setSpacing(true);
		
		// tblProps
		tblProps = new Table();
		tblProps.setImmediate(false);
		tblProps.setWidth("100.0%");
		tblProps.setHeight("-1px");
		jobDetLayout.addComponent(tblProps);
		
		// tblDets
		tblDets = new Table();
		tblDets.setImmediate(false);
		tblDets.setWidth("100.0%");
		tblDets.setHeight("-1px");
		jobDetLayout.addComponent(tblDets);
		
		return jobDetLayout;
	}
}
