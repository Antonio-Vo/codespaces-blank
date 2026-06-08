
import pandas as pd
import numpy as np
import sklearn
import matplotlib.pyplot as plt
from sklearn.linear_model import LinearRegression, LogisticRegression
from sklearn.cluster import KMeans
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score
import os
import kagglehub
# Download latest version
path = kagglehub.dataset_download("yasserh/titanic-dataset")
print("Path to dataset files:", path)
# Load the dataset
file_path = os.path.join(path, "Titanic-Dataset.csv")
df = pd.read_csv(file_path)
print(df.columns)

model = LinearRegression()
def survival_by_age(df):
    survivors = df[df["Survived"] == 1]
    ages = survivors["Age"].dropna().value_counts().sort_index()
    X = np.array(ages.index.tolist()).reshape(-1, 1)
    y = np.array(ages.values.tolist())
    return X, y

X, y = survival_by_age(df)

model.fit(X, y)
plt.figure()
plt.plot(X, y, 'ro')
plt.figure()
plt.plot(X, y, 'ro', label='Actual')
plt.plot(X, model.predict(X), 'b-', label='Linear Fit')
plt.title("Survival Count by Age")
plt.xlabel("Age")
plt.ylabel("Survivor Count")
plt.legend()
plt.savefig('titanic_plot.png', dpi=150, bbox_inches='tight')

#Question 1
ages = np.array(df["Age"].dropna())
print(ages.mean())

#Question 2
survivors = np.array(df["Survived"].dropna())
print(survivors.mean())

#Question 3
#counts the amount of every value
people = (df["Survived"].value_counts().dropna())
#making the bar chart
plt.figure()
plt.bar(["Survivors", "Non-Survivors"], [people[1], people[0]], color="red", edgecolor="black", linewidth=2)
plt.title("Survival Probability")
plt.ylabel("Amount")
plt.xlabel("Survivors vs Non Survivors")
plt.savefig("survival_probability.png", dpi=150, bbox_inches='tight')

#Question 4
#find the amount fo males survivors and females survivors
def F_to_M_survival(df):
    male_survivors = 0
    female_survivors = 0
    for x in range(len(df)):
        person = df.iloc[x]
        if person["Sex"] == "male" and person["Survived"] == 1:
            male_survivors += 1
        elif person["Sex"] == "female" and person["Survived"] == 1:
            female_survivors += 1
    return male_survivors, female_survivors

males = F_to_M_survival(df)[0]
females = F_to_M_survival(df)[1]

#making the graph
plt.title("Male and Female Survival Chances")
plt.ylabel("Survival Probability")
plt.xlabel("Sex")
plt.bar(["Males", "Females"], [males/len(df.dropna()), females/len(df.dropna())], color="red", edgecolor="black", linewidth=2)
plt.savefig('F_to_M_survival.png', dpi=150, bbox_inches='tight')

#Question 5
#returns the percent of surviving passengers from each class
def class_to_class_survival(df):
    class_1_survivors = 0
    class_2_survivors = 0
    class_3_survivors = 0
    for x in range(len(df)):
        person = df.iloc[x]
        if person["Survived"] == 1:
            if person["Pclass"] == 1:
                class_1_survivors += 1
            elif person["Pclass"] == 2:
                class_2_survivors += 1
            elif person["Pclass"] == 3:
                class_3_survivors += 1
    return class_1_survivors/len(df), class_2_survivors/len(df), class_3_survivors/len(df)
#made a bar chart
a_class = class_to_class_survival(df)
class_1 = a_class[0]
class_2 = a_class[1]
class_3 = a_class[2]
plt.figure()
plt.title("Class Survival Probability")
plt.ylabel("Survival Probability")
plt.xlabel("Classes")
plt.bar(["Class 1", "Class 2", "Class 3"], [class_1, class_2, class_3], color="red", edgecolor="black", linewidth=2)
plt.savefig('class_survival.png', dpi=150, bbox_inches='tight')

#Question 6
#the amount of kids  that survived
print(len(df[(df["Age"] < 18) & (df["Survived"] == 1)]))

# Question 7
model = RandomForestClassifier(n_estimators=100, random_state=42)
# Step 1: Select and clean relevant columns
cols = ["Pclass", "Sex", "Age", "SibSp", "Parch", "Fare", "Embarked"]

df_clean = df[cols + ["Survived"]].dropna()

# Step 2: Encode categorical variables
df_clean["Sex"] = df_clean["Sex"].map({"male": 0, "female": 1})
df_clean["Embarked"] = df_clean["Embarked"].map({"S": 0, "C": 1, "Q": 2})

# Step 3: Define features and target
X = df_clean[cols]
y = df_clean["Survived"]

# Step 4: Train/test split
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# Step 5: Fit and evaluate
model.fit(X_train, y_train)
accuracy = model.score(X_test, y_test)
print("Accuracy:", accuracy)

importances = model.feature_importances_  # built-in for RandomForest

print(importances)

#Age was the most important, and Sex right after

#Question 8
model = KMeans(n_clusters=3, random_state=42)
X = df[["Age", "Fare"]].dropna().values

model.fit(X)
labels = model.labels_

plt.figure()
plt.scatter(X[:, 0], X[:, 1], c=labels, cmap="viridis", alpha=0.6)
plt.savefig("kmeans_plot.png", dpi=150, bbox_inches="tight")

#This told me if your were around thirty your tickets would be 50 dollars
#If you had an expensive fare your would be probable be 20

#Question 9
clean_df = df[["Pclass", "Age", "SibSp", "Fare", "Parch"]].dropna()
y = clean_df[["Fare"]].values

#setting up model
model_class = LinearRegression()
X_Class = clean_df[["Pclass"]].values
model_class.fit(X_Class, y)
class_predictions = model_class.predict(X_Class)

#making the graph
plt.figure()
plt.title("Class to Fare predictions")
plt.xlabel("Classes")
plt.ylabel("Fare")
plt.plot(X_Class, class_predictions, label="Class", color="blue")
plt.savefig("class_fare_predictions.png", dpi=150, bbox_inches="tight")

#setting up model
model_age = LinearRegression()
X_Age = clean_df[["Age"]].values
model_age.fit(X_Age, y)
age_predictions = model_age.predict(X_Age)

#making graph
plt.figure()
plt.title("Class to Fare predictions")
plt.xlabel("Age")
plt.ylabel("Fare")
plt.plot(X_Age, age_predictions, label="Age", color="red")
plt.savefig("age_fare_predictions.png", dpi=150, bbox_inches="tight")


#making the model
model_family_size = LinearRegression()
df["FamilySize"] = clean_df["SibSp"] + clean_df["Parch"] + 1
X_SibSp = df[["FamilySize"]].dropna().values
model_family_size.fit(X_SibSp, y)
family_size_predictions = model_family_size.predict(X_SibSp)

#making the graph
plt.figure()
plt.title("Family Size to Fare predictions")
plt.xlabel("Family Size")
plt.ylabel("Fare")
plt.plot(X_SibSp, family_size_predictions, label="SibSp", color="green")
plt.savefig("family_size_predictions.png", dpi=150, bbox_inches="tight")


#From this I learned that the bigger the family, the older younger, and the higher the class, you would have a higher priced fare.

#Question 10
model = LogisticRegression()

clean_df = df[["Survived", "Pclass", "Age", "SibSp", "Parch", "Fare"]].dropna()
y = np.array(clean_df["Survived"])
X = np.array(clean_df[["Age", "SibSp", "Parch", "Fare", "Pclass"]])

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.1, random_state=42)
model.fit(X_train, y_train)

accuracy = model.score(X_test, y_test)
print("Accuracy:", accuracy)


#getting information
print("Will you survive the Titanic?")
Sex = input("(0) for male (1) for female: ")
Age = int(input("Enter your age (whole number: "))
Class = int(input("Class 1, 2, or 3: "))
Fare = float(input("Enter your fare price: "))
Siblings_and_Spouses = int(input("Enter number of SibSp: "))
Parents_and_Children = int(input("Enter number of Parch: "))

predict = np.array([Age, Siblings_and_Spouses, Parents_and_Children, Fare, Class]).reshape(1, -1)
prediction = model.predict(predict)
print(prediction, "if 1 you live and if 0 you died")




