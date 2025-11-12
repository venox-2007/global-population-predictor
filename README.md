# 🌍 Global Population Predictor

An interactive Machine Learning dashboard built with **Python** and **Streamlit**, designed to forecast the population of any country using historical data from **1970–2022**.

---

## 📘 Overview

This project predicts future population values using **Polynomial Regression (Degree = 2)**, trained on global demographic data.  
The model is deployed through a Streamlit dashboard that allows users to:

- Select any **country**
- Input a **target year** (up to 2100)
- View the **predicted population**
- Visualize **historical vs. forecasted population trends**
- Analyze key statistics such as:
  - 📈 Total Growth (%) since 1970  
  - 🔁 CAGR (Compound Annual Growth Rate)  
  - 👥 Absolute Population Increase  

---

## 🧠 Tech Stack

- **Programming Language:** Python  
- **Libraries:** Pandas, Scikit-learn, Matplotlib, Streamlit  
- **Model:** Polynomial Regression (Degree = 2)  
- **Data:** Global population dataset (1970–2022)  

---

## 📊 Dataset Details

The dataset contains data for **234 countries and territories** with columns such as:
- Country/Territory  
- Continent  
- Yearly population (1970–2022)  
- Area, Density, and Growth Rate  

The raw dataset was cleaned and reshaped into a long time-series format:  
