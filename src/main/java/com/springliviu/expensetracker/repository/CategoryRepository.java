package com.springliviu.expensetracker.repository;

import com.springliviu.expensetracker.model.Category;
import com.springliviu.expensetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUser(User user);
}