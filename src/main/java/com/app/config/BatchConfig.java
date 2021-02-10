package com.app.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.app.dao.ProductRepository;
import com.app.entity.Product;
import com.app.listner.MyJobListner;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private ProductRepository productRepo;
	
	@Bean
	public JobExecutionListener listner() {
		return new MyJobListner();
	}

	@Bean
	public Step stepOne() {
		return stepBuilderFactory.get("stepA").
				<Product, Product>chunk(3)
				.reader(reader())
				.processor(processor())
				.writer(products->{
					System.out.println("----------From Item Writer---------------------------");
					productRepo.saveAll(products);
				}).build();
				
	}

	@Bean
	public Job convertCsvToDb() {
		return jobBuilderFactory.get("jobA")
								.incrementer(new RunIdIncrementer())
								.listener(listner())
								.start(stepOne())
								.build();
	}

		
	// 2. Item Processor
	@Bean
	public ItemProcessor<Product, Product> processor() {
		// return new ProductProcessor();
		return (prod) -> {
			System.out.println("--------Item Processor----------------------------");
			prod.setProdDiscount(prod.getProdCost() * 3 / 100.0);
			prod.setProdGst(prod.getProdCost() * 12 / 100.0);
			return prod;
		};
	}

	// 1. Item Reader from CSV file
	@Bean
	public ItemReader<Product> reader() {
		System.out.println("-----------------From Item Reader-------------------------------");
		FlatFileItemReader<Product> reader = new FlatFileItemReader<Product>();

		// --loading file/Reading file
		reader.setResource(new ClassPathResource("/data/MyProducts.csv"));

		// --read data line by line
		reader.setLineMapper(new DefaultLineMapper<Product>() {
			{
				// --make one into multiple part
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						// -- Store as variables with names
						setNames("prodId", "prodName", "prodCost");
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<Product>() {
					{
						// --Convert to model class object
						setTargetType(Product.class);
					}
				});
			}
		});
		return reader;
	}
}
