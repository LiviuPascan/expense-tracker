import React, { useEffect, useState } from "react";
import { getExpenses } from "../services/api";
import "./ExpenseTable.css";

function ExpenseTable() {
    const [expenses, setExpenses] = useState([]);

    useEffect(() => {
        getExpenses()
            .then((res) => setExpenses(res.content)) // получаем именно массив из поля content
            .catch((err) => console.error("Ошибка загрузки:", err));
    }, []);

    return (
        <div className="expense-table-wrapper">
            <h2>📊 Список расходов</h2>
            <table className="expense-table">
                <thead>
                <tr>
                    <th>Название</th>
                    <th>Сумма</th>
                    <th>Категория</th>
                    <th>Дата</th>
                </tr>
                </thead>
                <tbody>
                {expenses.map((e) => (
                    <tr key={e.id}>
                        <td>{e.description || "—"}</td>
                        <td>{e.amount}₸</td>
                        <td>{e.categoryName}</td>
                        <td>{e.date ? new Date(e.date).toLocaleDateString() : "—"}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default ExpenseTable;
