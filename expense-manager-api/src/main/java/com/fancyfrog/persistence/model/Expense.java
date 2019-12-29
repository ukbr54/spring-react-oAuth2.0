package com.fancyfrog.persistence.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by Ujjwal Gupta on Dec,2019
 */

@Setter
@Getter
@Entity(name = "Expense")
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private Integer amount;

    private String month;

    private int year;
}
