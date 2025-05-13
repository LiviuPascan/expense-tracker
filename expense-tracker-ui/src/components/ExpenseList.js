import React, { useEffect, useState } from "react";
import { getExpenses } from "../services/api";
import AddExpenseForm from "./AddExpenseForm";

function ExpenseList() {
    const [expenses, setExpenses] = useState([]);

    const loadExpenses = () => {
        getExpenses()
            .then(res => setExpenses(res.data))
            .catch(err => console.error("Ошибка загрузки расходов:", err));
    };

    useEffect(() => {
        loadExpenses();
    }, []);

    return (
        <div>
            <AddExpenseForm onAdd={loadExpenses} />
            <h2>📋 Список расходов</h2>
            <ul>
                {expenses.map(exp => (
                    <li key={exp.id}>
                        {exp.name} — {exp.amount}₸ ({exp.category})
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default ExpenseList;
