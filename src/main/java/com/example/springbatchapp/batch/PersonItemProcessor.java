package com.example.springbatchapp.batch;

import com.example.springbatchapp.model.Person;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

//Definition du Processor
//Dans notre exemple, on a écrit un simple processor qui ne fait que convertir quelques attributs de notre objet Person
//en majuscules.
@Component
public class PersonItemProcessor implements ItemProcessor<Person, Person> {

    @Override
    public Person process(final Person personne){
        String firstName = personne.getFirstName().toUpperCase();
        personne.setFirstName(firstName);
        String lastName = personne.getLastName();
        lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase();
        personne.setLastName(lastName);
        return personne;
    }
}
