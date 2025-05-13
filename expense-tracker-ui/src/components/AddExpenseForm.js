import React, { useState } from "react";
import { addExpense } from "../services/api";

function AddExpenseForm({ onAdd }) {
    const [form, setForm] = useState({
        name: "",
        amount: "",
        category: ""
    });

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        const newExpense = {
            name: form.name,
            amount: parseFloat(form.amount),
            category: form.category
        };

        addExpense(newExpense)
            .then(() => {
                onAdd(); // обновить список
                setForm({ name: "", amount: "", category: "" });
            })
            .catch((err) => {
                console.error("Ошибка при добавлении:", err);
            });
    };

    return (
        <form onSubmit={handleSubmit} style={{ marginBottom: "2rem" }}>
            <h3>➕ Добавить расход</h3>
            <input
                type="text"
                name="name"
                placeholder="Название"
                value={form.name}
                onChange={handleChange}
                required
            />
            <input
                type="number"
                name="amount"
                placeholder="Сумма"
                value={form.amount}
                onChange={handleChange}
                required
            />
            <input
                type="text"
                name="category"
                placeholder="Категория"
                value={form.category}
                onChange={handleChange}
                required
            />
            <button type="submit">Добавить</button>
        </form>
    );
}

export default AddExpenseForm;
