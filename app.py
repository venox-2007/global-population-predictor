# ==========================================
# 🌍 POPULATION PREDICTOR APP (Streamlit + ML)
# ==========================================

# ---------- PART 1: IMPORTS & ML LOGIC ----------
import streamlit as st
import pandas as pd
import matplotlib.pyplot as plt
from sklearn.linear_model import LinearRegression
from sklearn.preprocessing import PolynomialFeatures

# ----- PAGE SETUP -----
st.set_page_config(page_title="Population Forecast", page_icon="🌍", layout="centered")

# ----- DARK UI THEME -----
st.markdown("""
    <style>
    .stApp {background-color: #0E1117; color: #FAFAFA;}
    h1, h2, h3, h4 {color: #00B4D8 !important;}
    label, .css-10trblm, .css-1d391kg {color: #CCCCCC !important;}
    div[data-baseweb="select"] > div {
        background-color: #1E1E1E !important; color: #FAFAFA !important; border-radius: 8px !important;
    }
    input[type="number"] {
        background-color: #1E1E1E !important; color: #FAFAFA !important;
        border-radius: 6px !important; border: 1px solid #333333 !important;
    }
    button[kind="primary"] {
        background-color: #00B4D8 !important; color: white !important; border-radius: 10px !important; font-weight: 600 !important;
    }
    button[kind="primary"]:hover {background-color: #0096C7 !important;}
    .stAlert {
        background-color: #003049 !important; color: #FAFAFA !important; border-left: 4px solid #00B4D8 !important;
    }
    hr {border: 1px solid #333333;}
    </style>
""", unsafe_allow_html=True)

# ---------- PART 2: LOAD DATA ----------
@st.cache_data
def load_data():
    df = pd.read_csv("cleaned_population_data.csv")
    return df

df = load_data()

# ---------- PART 3: MODEL FUNCTIONS ----------
def get_countries():
    return sorted(df['Country/Territory'].unique())

def train_model(country, degree=2):
    country_data = df[df['Country/Territory'] == country]
    X = country_data[['Year']]
    y = country_data['Population']

    poly = PolynomialFeatures(degree=degree)
    X_poly = poly.fit_transform(X)
    model = LinearRegression()
    model.fit(X_poly, y)
    return model, poly, country_data

def predict_population(country, year):
    model, poly, country_data = train_model(country, degree=2)
    X_future = pd.DataFrame({'Year': [year]})
    X_future_poly = poly.transform(X_future)
    predicted_pop = int(model.predict(X_future_poly)[0])
    return predicted_pop, country_data

# ---------- PART 4: STREAMLIT UI ----------
st.title("🌍 Global Population Predictor")
st.markdown("""
### 👋 Welcome to the Global Population Predictor  
Forecast the population of any country based on historical data from **1970–2022**,  
using **Polynomial Regression (Degree = 2)** for realistic growth trends.
""")
st.divider()

# --- Inputs ---
countries = get_countries()
country = st.selectbox("🌎 Select Country", countries, index=countries.index("India") if "India" in countries else 0)
year_input = st.number_input("📅 Enter a year to predict (e.g. 2035)", min_value=1970, max_value=2100, value=2035, step=1)

# --- Prediction ---
predicted_pop, past_data = predict_population(country, year_input)

# --- Visualization ---
fig, ax = plt.subplots(figsize=(8,5))
ax.plot(past_data['Year'], past_data['Population'], 'bo-', label="Actual (1970–2022)")
ax.scatter(year_input, predicted_pop, color='red', s=100, label=f"Predicted {year_input}")

years_extended = pd.DataFrame({'Year': range(1970, year_input + 1)})
poly = PolynomialFeatures(degree=2)
X_poly = poly.fit_transform(past_data[['Year']])
model = LinearRegression()
model.fit(X_poly, past_data['Population'])
ax.plot(years_extended['Year'], model.predict(poly.transform(years_extended)), 'r--', label="Predicted Trend")

ax.set_title(f"{country} Population Forecast up to {year_input}")
ax.set_xlabel("Year")
ax.set_ylabel("Population")
ax.legend()
ax.grid(True)
st.pyplot(fig)

# --- Output Section ---
st.subheader("📊 Predicted Population")
st.success(f"**Estimated {country} population in {year_input}: {predicted_pop:,}**")

# ---------- PART 5: METRICS SUMMARY ----------
base_year = 1970
base_population = int(past_data[past_data["Year"] == base_year]["Population"].values[0])
absolute_increase = predicted_pop - base_population
growth_percent = ((predicted_pop - base_population) / base_population) * 100
years_diff = year_input - base_year
cagr = ((predicted_pop / base_population) ** (1 / years_diff) - 1) * 100 if years_diff > 0 else 0

col1, col2, col3 = st.columns(3)
col1.metric(label="📈 Total Growth Since 1970", value=f"{growth_percent:.2f}%")
col2.metric(label="🔁 CAGR (1970–{})".format(year_input), value=f"{cagr:.2f}% per year")
col3.metric(label="👥 Population Increase Since 1970", value=f"{absolute_increase:,}")
st.markdown("---")