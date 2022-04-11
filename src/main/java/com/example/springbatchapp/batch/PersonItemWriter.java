package com.example.springbatchapp.batch;

import com.example.springbatchapp.model.Person;
import com.example.springbatchapp.repository.PersonRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

// Definition du Writer

// Le Writer écrit les données provenant du processor (ou directement lues par le Reader).
// Dans notre cas, Il reçoit du processor les objets transformés et chaque objet sera par la suite persisté dans notre base de données.
@Component
public class PersonItemWriter implements ItemWriter<Person> {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public void write(List<? extends Person> list) throws Exception {
        System.out.println(list);
        personRepository.saveAll(list);
    }
}
