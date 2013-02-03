package grisu.web;

import grisu.backend.hibernate.JobStatDAO;
import grisu.backend.hibernate.UserDAO;
import grisu.backend.model.job.JobStat;
import grisu.control.JobConstants;
import grisu.jcommons.constants.Constants;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import listeners.CustomValueChangeListener;

import org.apache.naming.java.javaURLContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.DefaultItemSorter;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class JobTable extends CustomComponent {

	@AutoGenerated
	private Panel mainLayout;
	@AutoGenerated
	private TabSheet tabSheet_1;
	@AutoGenerated
	private VerticalLayout verticalLayout_4;
	@AutoGenerated
	private Table tblJobsInactive;
	@AutoGenerated
	private VerticalLayout verticalLayout_3;
	@AutoGenerated
	private Table tblJobs;
	@AutoGenerated
	private Label lblDn;
	@AutoGenerated
	private Label lblClient;
	@AutoGenerated
	private Label lblTotJobs;
	@AutoGenerated
	private VerticalLayout verticallayout;

	private static Logger log = LoggerFactory.getLogger(Thread.currentThread().getClass());

	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	
	public JobTable() {
		
		log.debug("Inside constructor");
		System.out.println("jobtab: Inside constructor"+System.currentTimeMillis());
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
		tblJobs.setSelectable(true);
		tblJobs.setColumnReorderingAllowed(true);
		
		tblJobsInactive.setSelectable(true);
		tblJobsInactive.setColumnReorderingAllowed(true);

		log.debug("Exiting constructor");
	}

	
	public void addListener(final CustomValueChangeListener valueChangeListener) {
		// TODO Auto-generated method stub
		tblJobs.addListener(valueChangeListener);
		tblJobsInactive.addListener(valueChangeListener);
		tabSheet_1.addListener(new SelectedTabChangeListener() {
			
			public void selectedTabChange(SelectedTabChangeEvent event) {
				// TODO Auto-generated method stub
				valueChangeListener.fireValueChange();
			}
		});
	}

	public void setTable(BeanItemContainer<JobStat> jobStatContainer) {
		// TODO Auto-generated method stub
		tblJobs.setContainerDataSource(jobStatContainer);
	}

	public Object getTabcleClickVal() {
		// TODO Auto-generated method stub
		return tblJobs.getValue();
	}

	public void populate(final String dn, String active, String running) {
		// TODO Auto-generated method stub
		log.debug("Inside populate");
		
		JobStatDAO jsDao = new JobStatDAO();
		List<JobStat> jobs=jsDao.findJobByDN(dn, true);
		
		BeanItemContainer<JobStat> jobContainer = new BeanItemContainer<JobStat>(JobStat.class);
		BeanItemContainer<JobStat> inactiveJobContainer = new BeanItemContainer<JobStat>(JobStat.class);
		
		log.debug("No. of jobs: "+jobs.size() +"\ndn: "+dn);
		
		for(JobStat job:jobs){
			if(job.isActive())
				jobContainer.addBean(job);
			else
				inactiveJobContainer.addBean(job);
		}
		
		int totSize=jobs.size();
		tblJobs.setContainerDataSource(jobContainer);
		tblJobsInactive.setContainerDataSource(inactiveJobContainer);
		
		final Set<String> clientSet = new HashSet<String>(); 

		Thread jobTableUpdater = new Thread(){
			public void run(){
				System.out.println("jobtableupdater starts");
				Item tblItem=null;
				String client="";
				
				for(Object id:tblJobs.getItemIds())
				{
					tblItem=tblJobs.getItem(id);
					int status  = (Integer) tblItem.getItemProperty("status").getValue();
					tblItem.getItemProperty("submissionType").setValue(""+JobConstants.translateStatus(status));
					Map<String, String> app= (Map)tblItem.getItemProperty("properties").getValue();
					tblItem.getItemProperty("submittedJobDescription").setValue(app.get(Constants.APPLICATIONNAME_KEY));//appplication key
					
					clientSet.add(app.get("client"));
				}
				
				for(Object id:tblJobsInactive.getItemIds())
				{
					tblItem=tblJobsInactive.getItem(id);
					int status  = (Integer) tblItem.getItemProperty("status").getValue();
					tblItem.getItemProperty("submissionType").setValue(""+JobConstants.translateStatus(status));
					Map<String, String> app= (Map)tblItem.getItemProperty("properties").getValue();
					tblItem.getItemProperty("submittedJobDescription").setValue(app.get(Constants.APPLICATIONNAME_KEY));//appplication key

					clientSet.add(app.get("client"));
				}
				for(String cli:clientSet){
					if(cli!=null)
						client+=cli+", ";
				}
				if(client.length()>0){
					lblClient.setVisible(true);
					lblClient.setValue("Client(s): "+client.substring(0, client.length()-2));
				}
				else
				{
					lblClient.setVisible(false);
				}
				JobStatDAO jsDao = new JobStatDAO();
				lblTotJobs.setValue("Total Jobs: "+jsDao.findJobCount(dn) +"(Active: "+jsDao.findActiveJobCount(dn)+", Running: "+jsDao.findRunningJobCount(dn)+")");
				System.out.println("jobtableupdater ends");
			}
		};
		
		tblJobs.setVisibleColumns(new Object [] {"jobname", "submissionType", "fqan", "submittedJobDescription"});
		tblJobs.setColumnHeaders(new String [] {"Job Name", "Status", "Group", "Application key"});
		tblJobs.select(tblJobs.firstItemId());
		
		tblJobsInactive.setVisibleColumns(new Object [] {"jobname", "submissionType", "fqan", "submittedJobDescription"});
		tblJobsInactive.setColumnHeaders(new String [] {"Job Name", "Status", "Group", "Application key"});
		tblJobsInactive.select(tblJobsInactive.firstItemId());
		
		fireComponentEvent();
		
		lblDn.setValue("DN: "+dn);
		jobContainer.setItemSorter(new DefaultItemSorter(new Comparator<Object>() {
			
			public int compare(Object obj1, Object obj2) {
				// TODO Auto-generated method stub
				if(obj1 instanceof String)
				{
					return ((String)obj1).toLowerCase().compareTo(((String) obj2).toLowerCase());
				}
				else	
				{
					return ((Integer)obj1-(Integer)obj2);
				}
			}
		}));
		
		inactiveJobContainer.setItemSorter(new DefaultItemSorter(new Comparator<Object>() {
			public int compare(Object obj1, Object obj2) {
				if(obj1 instanceof String)
					return ((String)obj1).toLowerCase().compareTo(((String) obj2).toLowerCase());
				else	
					return ((Integer)obj1-(Integer)obj2);
			}
		}));
		
		
		jobTableUpdater.start();
		
		log.debug("Exiting populate");
		System.out.println("Exiting populate");
		System.out.println("jobtab: exit populate"+System.currentTimeMillis());
	}

	public Object getSelectedJob() {
		// TODO Auto-generated method stub
		if(tabSheet_1.getSelectedTab().equals(verticalLayout_3))
			return tblJobs.getValue();
		else
			return tblJobsInactive.getValue();
	}

	public void setTitle(String username) {
		// TODO Auto-generated method stub
		mainLayout.setCaption("Jobs for "+username);
	}

	@AutoGenerated
	private Panel buildMainLayout() {
		// common part: create layout
		mainLayout = new Panel();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		verticallayout = buildVerticalLayout();
		mainLayout.setContent(verticallayout);
		return mainLayout;
	}
	
	@AutoGenerated
	private VerticalLayout buildVerticalLayout()
	{
		verticallayout = new VerticalLayout();
		verticallayout.setImmediate(false);
		verticallayout.setWidth("100%");
		verticallayout.setHeight("100%");
		verticallayout.setSpacing(true);
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");

		
		// lblTotJobs
		lblTotJobs = new Label();
		lblTotJobs.setImmediate(false);
		lblTotJobs.setWidth("-1px");
		lblTotJobs.setHeight("-1px");
		lblTotJobs.setValue("Total Jobs: ");
		verticallayout.addComponent(lblTotJobs);		
		
		// lblDn
		lblDn = new Label();
		lblDn.setImmediate(false);
		lblDn.setWidth("-1px");
		lblDn.setHeight("-1px");
		lblDn.setValue("DN: ");
		verticallayout.addComponent(lblDn);
		
		// lblClient
		lblClient = new Label();
		lblClient.setImmediate(false);
		lblClient.setWidth("-1px");
		lblClient.setHeight("-1px");
		lblClient.setValue("DN: ");
		verticallayout.addComponent(lblClient);

		// tabSheet_1
		tabSheet_1 = buildTabSheet_1();
		verticallayout.addComponent(tabSheet_1);
		return verticallayout;
	}

	@AutoGenerated
	private TabSheet buildTabSheet_1() {
		// common part: create layout
		tabSheet_1 = new TabSheet();
		tabSheet_1.setImmediate(true);
		tabSheet_1.setWidth("-1px");
		tabSheet_1.setHeight("-1px");
		
		// verticalLayout_3
		verticalLayout_3 = buildVerticalLayout_3();
		tabSheet_1.addTab(verticalLayout_3, "Active Jobs", null);
		
		// verticalLayout_4
		verticalLayout_4 = buildVerticalLayout_4();
		tabSheet_1.addTab(verticalLayout_4, "Inactive Jobs", null);
		
		return tabSheet_1;
	}

	@AutoGenerated
	private VerticalLayout buildVerticalLayout_3() {
		// common part: create layout
		verticalLayout_3 = new VerticalLayout();
		verticalLayout_3.setImmediate(false);
		verticalLayout_3.setWidth("-1px");
		verticalLayout_3.setHeight("-1px");
		verticalLayout_3.setMargin(false);
		
		tblJobs = new Table();
		tblJobs.setImmediate(true);
		tblJobs.setWidth("100.0%");
		
		verticalLayout_3.addComponent(tblJobs);
		
		return verticalLayout_3;
	}

	@AutoGenerated
	private VerticalLayout buildVerticalLayout_4() {
		// common part: create layout
		verticalLayout_4 = new VerticalLayout();
		verticalLayout_4.setImmediate(false);
		verticalLayout_4.setWidth("-1px");
		verticalLayout_4.setHeight("-1px");
		verticalLayout_4.setMargin(false);
		
		// table_1
		tblJobsInactive = new Table();
		tblJobsInactive.setImmediate(true);
		tblJobsInactive.setWidth("100.0%");
		tblJobsInactive.setHeight("80.0%");
		verticalLayout_4.addComponent(tblJobsInactive);
		
		return verticalLayout_4;
	}
}
