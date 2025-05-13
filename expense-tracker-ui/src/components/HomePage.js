import React from "react";
import "./HomePage.css";

function HomePage() {
    return (
        <div className="container">
            <h1>✅ Expense Tracker Backend работает</h1>
            <p>Добро пожаловать в API для отслеживания расходов.</p>
            <a className="button" href="http://localhost:8080/swagger-ui/index.html" target="_blank" rel="noreferrer">
                Перейти в Swagger UI
            </a>
            <footer>
                <p>© 2025 ExpenseTracker API</p>
            </footer>
        </div>
    );
}

export default HomePage;
