package grisu.web;

import listeners.CustomValueChangeListener;
import grisu.backend.model.job.JobStat;
import grisu.settings.ServerPropertiesManager;
//import grisu.settings.ServerPropertiesManager;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vo.Users;

import com.github.wolfie.refresher.Refresher;
import com.github.wolfie.refresher.Refresher.RefreshListener;
import com.vaadin.Application;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;


public class GrisuUserApp extends Application {
	
	private static Logger log = LoggerFactory.getLogger(Thread.currentThread().getClass());
	
	@Override
	public void init() {

		log.debug("Entered init()");
		System.out.println("Entered init()");
		
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
		//vertiSplitPanel.addComponent(jobTab);
		final Panel jobTabPan = new Panel();
		
		Button btnJobRefresh = new Button();
		btnJobRefresh.setCaption("Refresh");
		btnJobRefresh.addListener(new Button.ClickListener() {
			
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
				Users selectedUser = (Users) userTab.getSelectedUser();
				if(selectedUser!=null){
					jobTab.populate(selectedUser.getDn(), selectedUser.getActiveJobCount(), selectedUser.getRunningJobCount());
					jobTabPan.setCaption("Jobs for "+selectedUser.getUserName());
				}
			}
		});
		
		jobTabPan.addComponent(btnJobRefresh);
		//btnJobRefresh.
		jobTabPan.setSizeFull();
		jobTabPan.addComponent(jobTab);
		jobTabPan.setCaption("Jobs for selected user");
		vertiSplitPanel.addComponent(jobTabPan);

		
		horiSplitPanel.addComponent(vertiSplitPanel);
		horiSplitPanel.setHeight("100%");
		horiSplitPanel.getFirstComponent().setSizeFull();
		
		horiSplitPanel.setSplitPosition(50);

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
		refresher.setEnabled(false);
		refresher.setRefreshInterval(ServerPropertiesManager.getDatabaseRefresh());
	//	refresher.setRefreshInterval(30000);
		
		
		refresher.setHeight("1%");
		
		refresher.addListener(new RefreshListener() {
			
			public void refresh(Refresher source) {
				
//				refresher.setEnabled(false);
		//		System.out.println("refresh starts "+System.currentTimeMillis());
				// TODO Auto-generated method stub
				
				Thread refresherThread = new Thread(){
					
					public void run(){
						refresher.setEnabled(false);
						System.out.println("refresh starts "+System.currentTimeMillis());
						System.out.println(refresher.isEnabled());
						userTab.refresh();
						
//						Users selectedUser = (Users) userTab.getSelectedUser();
//						if(selectedUser!=null){
//							jobTab.populate(selectedUser.getDn(), selectedUser.getActiveJobCount(), selectedUser.getRunningJobCount());
//						}						
						System.out.println("refresh ends "+System.currentTimeMillis());
						refresher.setEnabled(true);
					}
				};
				refresherThread.start();
				
				//userTab.refresh();
				//reload the job pane and job-details pane
/*new
				Users selectedUser = (Users) userTab.getSelectedUser();
				if(selectedUser!=null){
					jobTab.populate(selectedUser.getDn(), selectedUser.getActiveJobCount(), selectedUser.getRunningJobCount());
				}
				refresher.setEnabled(true);
**/				
			//	System.out.println("refresh ends "+System.currentTimeMillis());
			}
		});
		
		
		mainWindow.addComponent(refresher);
	//	refresher.setEnabled(false);
		refresher.setEnabled(true);
		
		log.debug("Exiting init()");
		System.out.println("Exiting init()");
	}

}
