package com.app.listner;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class MyJobListner implements JobExecutionListener{

	@Override
	public void beforeJob(JobExecution jobExecution) {
		System.out.println("Before job : "+jobExecution.getStartTime());
		System.out.println("Before job status : "+jobExecution.getStatus());
		
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		System.out.println("after job : "+jobExecution.getStartTime());
		System.out.println("after job status : "+jobExecution.getStatus());
		
	}

}
