package com.example.springbatchapp.repository;

import com.example.springbatchapp.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
