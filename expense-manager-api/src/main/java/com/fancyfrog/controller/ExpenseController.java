package com.fancyfrog.controller;

import com.fancyfrog.persistence.model.Expense;
import com.fancyfrog.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ujjwal Gupta on Dec,2019
 */

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/expense")
public class ExpenseController {

    private @Autowired ExpenseService expenseService;

    @GetMapping
    public ResponseEntity<?> findAll(){
        List<Expense> expenses = expenseService.findAll();
        return new ResponseEntity(expenses, HttpStatus.OK);
    }

    @GetMapping("/{year}/{month}")
    public ResponseEntity<?> getByMonthYear(@PathVariable("year") int year, @PathVariable("month") String month){
        List<Expense> result = new ArrayList<>();
        if("ALL".equalsIgnoreCase(month)){
            result = expenseService.findByYear(year);
        }else{
            result = expenseService.findByMonthAndYear(month, year);
        }
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addOrUpdateExpense(@RequestBody Expense expense){
        expenseService.saveOrUpdateExpense(expense);
        return new ResponseEntity("Expense added succcessfully", HttpStatus.OK);
    }

    @DeleteMapping
    public void deleteExpense(@RequestParam("id") Long id) {
        expenseService.deleteExpense(id);
    }

    @GetMapping("/api/hello")
    public String hello() {
        return "Hello, the time at the server is now " + new Date() + "\n";
    }
}
