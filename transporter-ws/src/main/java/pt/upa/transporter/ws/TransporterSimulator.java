package pt.upa.transporter.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

//Timer t = new Timer();
//t.schedule(new TransporterSimuladore(j), 5000);

public class TransporterSimulator extends TimerTask{
	public Timer timer;	
	private JobView job;
	private JobView accepted = new JobView();
	private JobView heading = new JobView();
	private JobView ongoing = new JobView();
	private JobView completed = new JobView();
	
	public TransporterSimulator(JobView j){ 
		job = j;
		accepted.setJobState(JobStateView.ACCEPTED);
		heading.setJobState(JobStateView.HEADING);
		ongoing.setJobState(JobStateView.ONGOING);
		completed.setJobState(JobStateView.COMPLETED);
	}
	
	public void run(){
		if(job.getJobState() == accepted.getJobState()){
			job.setJobState(JobStateView.HEADING);
		}
		else if(job.getJobState() == heading.getJobState()){
			job.setJobState(JobStateView.ONGOING);
		}
		else if(job.getJobState() == ongoing.getJobState()){
			job.setJobState(JobStateView.COMPLETED);
		}
	}
}

