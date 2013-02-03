package grisu.web;

import grisu.backend.hibernate.JobDAO;
import grisu.backend.hibernate.JobStatDAO;
import grisu.backend.model.job.JobStat;
import grisu.control.exceptions.NoSuchJobException;

import java.sql.SQLClientInfoException;
import java.sql.Statement;
import java.util.List;

import com.vaadin.Application;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.ui.*;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Driver;

//import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;

public class GrisuApp extends Application {
	@Override
	public void init() {
		Window mainWindow = new Window("Grisu");
		Label label = new Label("Hello Vaadin user. hello");
		mainWindow.addComponent(label);

			
			final Table t2 = new Table("Job Table");
			JobStatDAO jobStatDAO = new JobStatDAO();
			
			//List<JobStat> jobs = jobStatDAO.findAllJobs();
			List<JobStat> jobs=null;
			try {
				jobs = jobStatDAO.getSimilarJobNamesByDN("DC=nz,DC=org,DC=bestgrid,DC=slcs,O=The University of Auckland,CN=Markus Binsteiner _bK32o4Lh58A3vo9kKBcoKrJ7ZY", "gromacs%");
			} catch (NoSuchJobException e) {
				e.printStackTrace();
			}
			System.out.println("job list size:"+jobs.size());
			BeanItemContainer<JobStat> jobStatContainer = new BeanItemContainer<JobStat>(JobStat.class);
			jobStatContainer.addAll(jobs);
			t2.setContainerDataSource(jobStatContainer);
			t2.setVisibleColumns(new Object[] {"jobname"});
			
			t2.setSelectable(true);
			t2.setColumnCollapsingAllowed(true);
			t2.setColumnReorderingAllowed(true);
			t2.setImmediate(true);
			
			Panel p = new Panel();
			p.setCaption("job panel");
			p.setScrollable(true);
			p.addComponent(t2);
			p.getContent().setSizeUndefined();
			
			mainWindow.addComponent(p);
			
			//final 
			final Panel jobDetailsPanel = new Panel("Job Details");
			mainWindow.addComponent(jobDetailsPanel);

			// Shows feedback from selection.
			final Label current = new Label("Selected: -");
			jobDetailsPanel.addComponent(current);

			final JobDetComponent jobDetComp = new JobDetComponent();
			jobDetailsPanel.addComponent(jobDetComp);
			
			final UserTable userTab = new UserTable();
			jobDetailsPanel.addComponent(userTab);
			
			userTab.setTable(jobStatContainer);

			userTab.addListener(new ValueChangeListener() {
				
				public void valueChange(ValueChangeEvent event) {
					// TODO Auto-generated method stub
				//	tmp.setVal();
					current.setValue((JobStat)userTab.getTabcleClickVal());
					jobDetComp.setJobDetails((JobStat)userTab.getTabcleClickVal());
				}
			});
			
			// Handle selection change.
			t2.addListener(new Property.ValueChangeListener() {
			    public void valueChange(ValueChangeEvent event) {
			        JobStat currJob = (JobStat) t2.getValue();
			        jobDetComp.setJobDetails(currJob);
			    }
			});
			
		setMainWindow(mainWindow);
	}

	public static void main(String[] args){
		GrisuApp g = new GrisuApp();
		g.init();
	}
}
