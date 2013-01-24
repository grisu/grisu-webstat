package grisu.web;

import grisu.backend.hibernate.JobStatDAO;
import grisu.backend.model.job.JobStat;
import grisu.control.JobConstants;
import grisu.jcommons.constants.Constants;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.DefaultItemSorter;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;

public class JobTable extends CustomComponent {

	@AutoGenerated
	private Panel mainLayout;
	@AutoGenerated
	private Table tblJobs;
	@AutoGenerated
	private Label lblInactive;
	@AutoGenerated
	private Label lblActive;
	@AutoGenerated
	private Label lblTotJobs;

	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	
	
	public JobTable(String caption) {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here

		tblJobs.setSelectable(true);
		tblJobs.setColumnReorderingAllowed(true);
		mainLayout.setCaption(caption);
		//mainLayout.getContent().setSizeFull();

	}

	public void addListener(ValueChangeListener valueChangeListener) {
		// TODO Auto-generated method stub
		tblJobs.addListener(valueChangeListener);
	}

	public void setTable(BeanItemContainer<JobStat> jobStatContainer) {
		// TODO Auto-generated method stub
		tblJobs.setContainerDataSource(jobStatContainer);
	}



	public Object getTabcleClickVal() {
		// TODO Auto-generated method stub
		return tblJobs.getValue();
	}

	public void populate(String dn, String active) {
		// TODO Auto-generated method stub
		
		JobStatDAO jsDao = new JobStatDAO();
		List<JobStat> jobs=jsDao.findJobByDN(dn, true);
		BeanItemContainer<JobStat> jobContainer = new BeanItemContainer<JobStat>(JobStat.class);
		System.out.println(jobs.size()+"..."+dn);
		
		jobContainer.addAll(jobs);

		int totSize=jobs.size();
		int inactive = totSize-Integer.parseInt(active);
		tblJobs.setContainerDataSource(jobContainer);
		
		Item tblItem=null;
		for(Object id:tblJobs.getItemIds())
		{
			tblItem=tblJobs.getItem(id);
			int status  = (Integer) tblItem.getItemProperty("status").getValue();
			tblItem.getItemProperty("submissionType").setValue(""+JobConstants.translateStatus(status));
			Map<String, String> app= (Map)tblItem.getItemProperty("properties").getValue();
			tblItem.getItemProperty("submittedJobDescription").setValue(app.get(Constants.APPLICATIONNAME_KEY));//appplication key
		}

		tblJobs.setVisibleColumns(new Object [] {"jobname","active", "submissionType", "fqan", "submittedJobDescription"});
		tblJobs.setColumnHeaders(new String [] {"Job Name","Active", "Status", "Group", "Application key"});
		tblJobs.select(tblJobs.firstItemId());
		fireComponentEvent();
		
		lblTotJobs.setValue("Total Jobs: "+totSize);
		lblActive.setValue("Active Jobs: "+active);
		lblInactive.setValue("Inactive Jobs: " +inactive);
		
		
		//Custom sorter (as the default sorting doesn't work properly for the columns with generated values)
		jobContainer.setItemSorter(new DefaultItemSorter(new Comparator<Object>() {
			
			public int compare(Object obj1, Object obj2) {
				// TODO Auto-generated method stub
				if(obj1 instanceof String)
				{
					return ((String)obj1).toLowerCase().compareTo(((String) obj2).toLowerCase());
				}
				else if(obj1 instanceof Boolean){
					return ((Boolean)obj1).compareTo((Boolean)obj2);
				}
				else	
				{
					return ((Integer)obj1-(Integer)obj2);
				}
			}
		}));
	}

	public Object getSelectedJob() {
		// TODO Auto-generated method stub
		return tblJobs.getValue();
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
		
		// lblTotJobs
		lblTotJobs = new Label();
		lblTotJobs.setImmediate(false);
		lblTotJobs.setWidth("50.0%");
		lblTotJobs.setHeight("10.0%");
		lblTotJobs.setValue("Total Jobs: ");
		mainLayout.addComponent(lblTotJobs);
		
		// lblActive
		lblActive = new Label();
		lblActive.setImmediate(false);
		lblActive.setWidth("-1px");
		lblActive.setHeight("10.0%");
		lblActive.setValue("Active Jobs: ");
		mainLayout.addComponent(lblActive);
		
		// label_1
		lblInactive = new Label();
		lblInactive.setImmediate(false);
		lblInactive.setWidth("-1px");
		lblInactive.setHeight("-1px");
		lblInactive.setValue("Inactive Jobs");
		mainLayout.addComponent(lblInactive);
		
		// tblJobs
		tblJobs = new Table();
		tblJobs.setImmediate(true);
		tblJobs.setWidth("100.0%");
		tblJobs.setHeight("80.0%");
		mainLayout.addComponent(tblJobs);
		
		return mainLayout;
	}

	public void setTitle(String username) {
		// TODO Auto-generated method stub
		mainLayout.setCaption("Jobs for "+username);
	}

}
