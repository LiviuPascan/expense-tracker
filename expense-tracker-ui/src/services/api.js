import axios from "axios";

const API_URL = "http://localhost:8080/api/expenses";

export const getExpenses = async () => {
    return axios.get(API_URL);
};
export const addExpense = async (expense) => {
    return res.data.content;
};