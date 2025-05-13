import React from "react";
import ExpenseTable from "./components/ExpenseTable";
import "./App.css";

function App() {
    return (
        <div className="app-container">
            <h1>💰 Expense Tracker</h1>
            <ExpenseTable />
        </div>
    );
}

export default App;
