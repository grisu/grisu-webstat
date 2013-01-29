package grisu.web;

import listeners.CustomValueChangeListener;
import grisu.backend.model.job.JobStat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vo.Users;

import com.github.wolfie.refresher.Refresher;
import com.github.wolfie.refresher.Refresher.RefreshListener;
import com.vaadin.Application;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;


public class GrisuUserApp extends Application {
	
	private static Logger log = LoggerFactory.getLogger(Thread.currentThread().getClass());
	
	@Override
	public void init() {

		log.debug("Entered init()");
		Window mainWindow = new Window("Grisu");

		final Table t2 = new Table("Job Table");

		final UserTable userTab = new UserTable("Users");

		VerticalLayout layout= new VerticalLayout();
		
		HorizontalSplitPanel horiSplitPanel = new HorizontalSplitPanel();
		layout.addComponent(horiSplitPanel);
		mainWindow.setContent(layout);
		horiSplitPanel.addComponent(userTab);
		userTab.setHeight("100%");
		horiSplitPanel.setSizeFull();
		horiSplitPanel.setHeight("100%");
		layout.getComponent(0).setSizeFull();
		layout.setExpandRatio(horiSplitPanel, 1.0f);

		VerticalSplitPanel vertiSplitPanel = new VerticalSplitPanel();

		final JobTable jobTab = new JobTable();
		vertiSplitPanel.addComponent(jobTab);
		final Panel jobTabPan = new Panel();
		jobTabPan.setSizeFull();
		jobTabPan.addComponent(jobTab);
		jobTabPan.setCaption("Jobs for selected user");
		vertiSplitPanel.addComponent(jobTabPan);

		
		horiSplitPanel.addComponent(vertiSplitPanel);
		horiSplitPanel.setHeight("100%");
		horiSplitPanel.getFirstComponent().setSizeFull();
		
		horiSplitPanel.setSplitPosition(30);

		userTab.addListener(new ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				Users selectedUser = (Users) userTab.getSelectedUser();
				if(selectedUser!=null){
					jobTab.populate(selectedUser.getDn(), selectedUser.getActiveJobCount(), selectedUser.getRunningJobCount());
					jobTabPan.setCaption("Jobs for "+selectedUser.getUserName());
				}
			}
		});

		final JobDetailsComponent jobDets = new JobDetailsComponent();

		Panel jobDetPan = new Panel();
		jobDetPan.setSizeFull();
		jobDetPan.setContent(jobDets);
		jobDetPan.setCaption("Job Details");
		vertiSplitPanel.addComponent(jobDetPan);
		
		jobTab.addListener(new CustomValueChangeListener()  {
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				JobStat selectedJob = (JobStat) jobTab.getSelectedJob();
				jobDets.populate(selectedJob);
			}

			public void fireValueChange() {
				JobStat selectedJob = (JobStat) jobTab.getSelectedJob();
				jobDets.populate(selectedJob);
			}
		});
		
		vertiSplitPanel.getSecondComponent().setSizeFull();
		
		mainWindow.getContent().setSizeFull();

		setMainWindow(mainWindow);

		userTab.populate();
		
		final Refresher refresher = new Refresher();
		refresher.setRefreshInterval(5000);
		
		refresher.setHeight("1%");
		
		refresher.addListener(new RefreshListener() {
			
			public void refresh(Refresher source) {
				// TODO Auto-generated method stub
				userTab.refresh();
				//reload the job pane and job-details pane
				Users selectedUser = (Users) userTab.getSelectedUser();
				if(selectedUser!=null){
					jobTab.populate(selectedUser.getDn(), selectedUser.getActiveJobCount(), selectedUser.getRunningJobCount());
				}
			}
		});
		
		mainWindow.addComponent(refresher);
		refresher.setEnabled(false);
		
		log.debug("Exiting init()");
	}

}
