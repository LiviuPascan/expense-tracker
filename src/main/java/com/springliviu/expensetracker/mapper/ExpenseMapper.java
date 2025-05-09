package com.springliviu.expensetracker.mapper;

import org.mapstruct.Mapper;
import com.springliviu.expensetracker.model.Expense;
import com.springliviu.expensetracker.dto.ExpenseDto;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {
    ExpenseDto toDto(Expense e);
    Expense toEntity(ExpenseDto dto);
}