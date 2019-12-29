package com.fancyfrog.service;

import com.fancyfrog.persistence.model.Expense;
import com.fancyfrog.persistence.repositories.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Ujjwal Gupta on Dec,2019
 */

@Service
public class ExpenseServiceImpl implements ExpenseService{

    private @Autowired ExpenseRepository expenseRepository;

    @Override
    public List<Expense> findAll() {
        return expenseRepository.findAll();
    }

    @Override
    public List<Expense> findByMonthAndYear(String month, int year) {
        return expenseRepository.findByMonthAndYear(month, year);
    }

    @Override
    public List<Expense> findByYear(int year) {
        return expenseRepository.findByYear(year);
    }

    @Override
    public void saveOrUpdateExpense(Expense expense) {
        expenseRepository.save(expense);
    }

    @Override
    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }
}
