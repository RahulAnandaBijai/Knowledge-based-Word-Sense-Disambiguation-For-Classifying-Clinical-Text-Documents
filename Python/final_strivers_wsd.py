# -*- coding: utf-8 -*-
"""Final_Strivers_WSD.ipynb

Automatically generated by Colaboratory.

Original file is located at
    https://colab.research.google.com/drive/1bDs3ycGc-79hh55RCnQ5z6EA1Ya-pkjX
"""

# Authors - Sahil Aneja, Rahul Ananda Bijai, Harsh Jasani
# Team - STRIVERS
# This python program is used to perform multi-class classification on medical specialty.
# The dataset(customMedicalDataset.csv) was obtained using cTAKES and Java program.


#Importing all the required packages
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
import gc
from collections import Counter


from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.decomposition import PCA
from sklearn.linear_model import LogisticRegression
from sklearn.model_selection import train_test_split
from imblearn.over_sampling import SMOTE

from sklearn.metrics import confusion_matrix
from sklearn.metrics import classification_report
from sklearn.manifold import TSNE

import string
import re
import nltk
from nltk.tokenize import word_tokenize
from nltk.tokenize import sent_tokenize
from nltk.stem import WordNetLemmatizer 
nltk.download('punkt')
nltk.download('wordnet')

# Creating a data frame for mtsamples
mtSamplesDf = pd.read_csv("/content/mtsamples.csv")
mtSamplesDf.head(n=10)

# Creating a data frame from our generated dataset
umlsDf = pd.read_csv("/content/customMedicalDataset.csv")
umlsDf.head(n=10)

# Removing Duplicate Transcriptions from mtsamples
mtSamplesDf = mtSamplesDf.drop_duplicates(subset='transcription', keep="first")
mtSamplesDf.head(n=10)

# Removing Duplicate Transcriptions from completeDataset
umlsDf = umlsDf.drop_duplicates(subset='transcription', keep="first")
umlsDf.head(n=10)

# Merging the mtsamples on our completeDataset with transcription
mergedDf = pd.merge(mtSamplesDf, umlsDf, on='transcription', how='left', indicator=True)
mergedDf.head(n=10)

# Dropping the missing transcriptions from the merged data frame
data = mergedDf.drop(mergedDf[mergedDf['transcription'].isna()].index)

# Selecting the relevant features
selectedData = data[['symptoms','diseases','procedures','medical_specialty']]
selectedData.head(n=10)

# Checking the missing values for the data frame
selectedData.isnull().sum()

# Dropping the missing values
selectedData = selectedData.dropna(how = 'any')
selectedData.shape

# Checking the count of each Medical Specialty
print('Total Unique Medical Specialty in the Dataframe : ',selectedData['medical_specialty'].nunique())
print('Medcial Specialty                Count')
count = selectedData['medical_specialty'].value_counts()
count

# Selecting medical specialty having count more than 40 only. Returns top 10 medical specialties
selectedData = selectedData[selectedData['medical_specialty'].isin(count[count > 40].index)]

# 10 Medical Specialty is considered for the machine learning process
print('Medical Specialty having count more tha 40:')
print('Medcial Specialty                Count')
selectedData['medical_specialty'].value_counts()

# Function to Clean the Texts
def textCleaning(words): 
    words = words.translate(str.maketrans('', '', string.punctuation))
    word = ''.join([i for i in words if not i.isdigit()]) 
    REPLACE_BY_SPACE_RE = re.compile('[/(){}\[\]\|@,;]')
    
    lWord = word.lower()
    lWord = REPLACE_BY_SPACE_RE.sub('', lWord) 
    return lWord

# Function to Lemmatize the Texts
def textLemmatizing(words):
    wordList=[]
    lemmatization = WordNetLemmatizer() 
    sentence=sent_tokenize(words)
    
    intialSentence= sentence[0:1]
    finalSentence = sentence[len(sentence)-2: len(sentence)-1]
    
    for sentence in intialSentence:
        words=word_tokenize(sentence)
        for word in words:
            wordList.append(lemmatization.lemmatize(word))
    for sentence in finalSentence:
        words=word_tokenize(sentence)
        for word in words:
            wordList.append(lemmatization.lemmatize(word))       
    return ' '.join(wordList)

# Chaning the Tpye to String for Further process
selectedData['symptoms']  = selectedData['symptoms'] .astype(str)
selectedData['diseases'] = selectedData['diseases'].astype(str)
selectedData['procedures'] = selectedData['procedures'].astype(str)

# Cleaning and Lemmatizing Symptoms
selectedData['symptoms'] = selectedData['symptoms'].apply(textLemmatizing)
selectedData['symptoms'] = selectedData['symptoms'].apply(textCleaning)

# Cleaning and Lemmatizing Diseases
selectedData['diseases'] = selectedData['diseases'].apply(textLemmatizing)
selectedData['diseases'] = selectedData['diseases'].apply(textCleaning)

# Cleaning and Lemmatizing Procedures
selectedData['procedures'] = selectedData['procedures'].apply(textLemmatizing)
selectedData['procedures'] = selectedData['procedures'].apply(textCleaning)

# Merging Symptoms, Diseases & Procedure  
selectedData['alldata']= selectedData['symptoms'] + selectedData['diseases'] + selectedData['procedures']
selectedData = selectedData[['alldata','medical_specialty']]
selectedData.head(n=10)

# Feauture Extraction using TFIDFVectorizer to generate 2000 tf-idf features.
tfidfVectorizer = TfidfVectorizer(analyzer='word', stop_words='english',ngram_range=(1,3), max_df=0.75, use_idf=True, smooth_idf=True, max_features=2000)
tfidfMatrix = tfidfVectorizer.fit_transform(selectedData['alldata'].tolist())
featureNames = sorted(tfidfVectorizer.get_feature_names())
print(featureNames)

# Using TSNE to visualize the tf-idf features for the medical specialty
gc.collect()
tfidfMatrixDense = tfidfMatrix.todense()
targetLabels = selectedData['medical_specialty'].tolist()
tsneValue = TSNE(n_components=2,init='random',random_state=0, perplexity=40).fit_transform(tfidfMatrixDense)
plt.figure(figsize=(16,10))
palette = sns.hls_palette(21, l=.6, s=.9)
sns.scatterplot(
    x=tsneValue[:,0], y=tsneValue[:,1],
    hue=targetLabels,
    legend="full",
    alpha=0.3
)
plt.show()

# Using Principal Component Analysis to reduce the dimentionality of the features
gc.collect()
pca = PCA(n_components=0.99)
tfidfMatrixPCA = pca.fit_transform(tfidfMatrix.toarray())
targetLabels = selectedData['medical_specialty'].tolist()
categoryList = selectedData.medical_specialty.unique()
print('Shape of matrix after PCA is:'+str(tfidfMatrixPCA.shape))
X_train, X_test, y_train, y_test = train_test_split(tfidfMatrixPCA, targetLabels, stratify=targetLabels,random_state=0)  
print('X_train Size is:'+str(X_train.shape))
print('X_test Size is:'+str(X_test.shape))

# Checking the imbalance in the dataset
count = Counter(y_train)
for i,j in count.items():
	per = j / len(y_train) * 100
	print('Medical Specialty Class= %s has Total= %d (%.3f%%)' % (i, j, per))
plt.bar(count.keys(), count.values())
# added for better display
plt.xticks(rotation=90)
plt.show()

# Using Synthetic Minority Over-sampling Technique to balance the train dataset
smote = SMOTE(random_state= 0)
X_train, y_train = smote.fit_resample(X_train,y_train)
print('After SMOTE the X_train Size is:'+str(X_train.shape))
print('After SMOTE the X_test Size is:'+str(X_test.shape))

# Verify the train dataset balancing after Smote
count = Counter(y_train)
for i,j in count.items():
	per = j / len(y_train) * 100
	print('Medical Specialty Class= %s has Total= %d (%.3f%%)' % (i, j, per))
plt.bar(count.keys(), count.values())
# added for better display
plt.xticks(rotation=90)
plt.show()

# Using Logistic Regression Classifier for performing the multiclass classification
lrClassifier = LogisticRegression(penalty= 'elasticnet', solver= 'saga', l1_ratio=0.5, random_state=0).fit(X_train, y_train)
lrPred= lrClassifier.predict(X_test)
targetLabels = categoryList
cmlr = confusion_matrix(y_test, lrPred)

# Plotting the Confusion Matrix
figureSize = plt.figure(figsize=(20,20))
pnt= figureSize.add_subplot(1,1,1)
sns.heatmap(cmlr, annot=True, cmap="Blues",ax = pnt,fmt='g');
pnt.set_xlabel('Predicted Target Labels');pnt.set_ylabel('Actual Target Labels'); 
pnt.set_title('Confusion Matrix For Logistic Regression'); 
pnt.xaxis.set_ticklabels(targetLabels); pnt.yaxis.set_ticklabels(targetLabels);
plt.setp(pnt.get_yticklabels(), rotation=30, horizontalalignment='right')
plt.setp(pnt.get_xticklabels(), rotation=30, horizontalalignment='right')     
plt.show()

# Print the Results
print(classification_report(y_test,lrPred,labels=categoryList))