package com.example.springbatchapp.config;

import com.example.springbatchapp.batch.listener.JobResultListener;
import com.example.springbatchapp.batch.utils.FileVerificationSkipper;
import com.example.springbatchapp.model.Person;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.net.MalformedURLException;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Value("${inputFile}")
    private Resource resource;

    @Value("${inputFiles}")
    private Resource[] resources;

    @Autowired
    private ItemReader<Person> itemReader;

    @Autowired
    private MultiResourceItemReader<Person> multiResourceItemReader;

    @Autowired
    private ItemWriter<Person> itemWriter;

    @Autowired
    private ItemProcessor<Person, Person> itemProcessor;


    // Definition du Reader

    // le Reader lit une source de données et est appelé successivement au sein
    // d'une étape et retourne des objets pour lequel il est défini (Personne dans
    // notre cas).

    // Reader sur un seul fichier .csv

    //@Bean()
    //public FlatFileItemReader<Person> reader(@Value("${inputFile}") Resource resource) {
    //return new FlatFileItemReaderBuilder<Person>().name("personItemReader")
    //.resource(resource)
    //.linesToSkip(1).delimited()
    //.names("firstName", "lastName", "salary")
    //.fieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {
    //{
    // setTargetType(Person.class);
    //}
    //}).build();
    //}
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean()
    @Primary
    public FlatFileItemReader<Person> reader() {
        return new FlatFileItemReaderBuilder<Person>().name("personItemReader").linesToSkip(1).delimited()
                .names(new String[] { "num", "firstName", "LastName", "salary" })
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {
                    {
                        setTargetType(Person.class);
                    }
                }).build();
    }

    @Bean()
    public MultiResourceItemReader<Person> multiResourceItemReader(){
        MultiResourceItemReader<Person> resourceItemReader = new MultiResourceItemReader<>();
        resourceItemReader.setResources(resources);
        resourceItemReader.setDelegate(reader());
        return resourceItemReader;
    }

    // Writers
    @Bean
    public FlatFileItemWriter<Person> writer() {
        FlatFileItemWriter<Person> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("output/outputData.csv"));
        // All job repetitions should "append" to same output file
        writer.setAppendAllowed(true);
        writer.setLineAggregator(new DelimitedLineAggregator<Person>() {
            {
                setDelimiter(",");
                setFieldExtractor(new BeanWrapperFieldExtractor<Person>() {
                    {
                        setNames(new String[] { "num", "firstName", "LastName", "salary" });
                    }
                });
            }
        });
        return writer;
    }

    // La première méthode définit le job

    // Les jobs sont créés à partir des steps, où chaque step peut impliquer un
    // reader, un processor et un writer.
    @Bean(name = "jobStoreToDb")
    @Primary
    public Job jobStoreToDb() throws MalformedURLException {
        return jobBuilderFactory.get("Job Store DB").incrementer(new RunIdIncrementer()).start(stepOne()).build();
    }

    @Bean(name = "jobManyFilesToDb")
    public Job jobManyFilesToDb(JobResultListener listener) throws MalformedURLException {
        return jobBuilderFactory.get("Job Many Files DB").incrementer(new RunIdIncrementer()).listener(listener).start(stepTwo()).build();
    }

    // Charge data.csv et insere ds mysql database
    @Bean
    public Step stepOne(){
        return stepBuilderFactory.get("First Step : Loading data from .csv to db").<Person, Person>chunk(2)
                .reader(itemReader).processor(itemProcessor).writer(itemWriter).build();
    }

    @Bean
    public Step stepTwo(){
        return stepBuilderFactory.get("First Step : Loading data from *.csv to db").<Person, Person>chunk(2)
                .reader(multiResourceItemReader)
                .faultTolerant().skipPolicy(fileVerificationSkipper())
                .processor(itemProcessor)
                .writer(itemWriter).build();
    }








    @Bean
    public SkipPolicy fileVerificationSkipper() {
        return new FileVerificationSkipper();
    }


}
