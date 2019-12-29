package com.fancyfrog.persistence.repositories;

import com.fancyfrog.persistence.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Ujjwal Gupta on Dec,2019
 */
@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByMonthAndYear(String month, int year);

    List<Expense> findByYear(int year);

}
