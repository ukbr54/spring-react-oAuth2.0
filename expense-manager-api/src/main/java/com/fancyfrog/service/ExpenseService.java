package com.fancyfrog.service;

import com.fancyfrog.persistence.model.Expense;

import java.util.List;

/**
 * Created by Ujjwal Gupta on Dec,2019
 */
public interface ExpenseService {

    List<Expense> findAll();

    List<Expense> findByMonthAndYear(String month, int year);

    List<Expense> findByYear(int year);

    void saveOrUpdateExpense(Expense expense);

    void deleteExpense(Long id);
}
