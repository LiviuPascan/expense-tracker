package com.springliviu.expensetracker.mapper;

import org.mapstruct.Mapper;
import com.springliviu.expensetracker.model.Expense;
import com.springliviu.expensetracker.dto.ExpenseDto;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    ExpenseDto toDto(Expense e);
    Expense toEntity(ExpenseDto dto);
}