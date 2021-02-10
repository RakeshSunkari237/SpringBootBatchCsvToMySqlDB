package com.app.runner;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyJobLanucher implements CommandLineRunner{

	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private Job job;
	
	
	
	public void run(String... args) throws Exception {
		jobLauncher.run(job,new JobParametersBuilder()
				.addLong("time",System.currentTimeMillis())
				.toJobParameters());
	}
}
