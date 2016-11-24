import csv
import collections

positive_vocabulary = collections.defaultdict(int)
neutral_vocabulary = collections.defaultdict(int)
negative_vocabulary = collections.defaultdict(int)

def word_clean(word):
	clean_w = word.strip()
	if clean_w.startswith("@") or clean_w.startswith("http"):
		clean_w = ""
	else:
		clean_w = "".join([c for c in clean_w if c.isalnum()])
	return clean_w.capitalize()
	
def get_word_index(word):
	global vocab_index
	if word in vocabulary:
		return vocabulary[word]
	else:
		vocab_index +=1
		vocabulary[word] = vocab_index
		return vocab_index 


count_fomat = "{},{}"
filepath = input("File Path")
positive_path = input("positive_path")
negative_path =  input("negative_path")
neutral_path =  input("neutral_path")
with open(filepath,"r") as datafile:
	reader = csv.reader(datafile, delimiter=',')
	for row_index,row in enumerate(reader):
		if row[0] == "0":
			targetd= negative_vocabulary
		elif row[0] == "2":
			targetd = neutral_vocabulary
		elif row[0] == "4":
			targetd = positive_vocabulary
		tweet = row[5]
		for word in tweet.split(" "):
			if len(word) > 0:
				clean_w = word_clean(word)
				targetd[clean_w]+=1
					
with open(positive_path,"w") as pcsv:
	for k,v in positive_vocabulary.items():
		print(count_fomat.format(k,v),file=pcsv)	

with open(negative_path,"w") as negcsv:
	for k,v in negative_vocabulary.items():
		print(count_fomat.format(k,v),file=negcsv)

with open(neutral_path,"w") as neutcsv:
	for k,v in neutral_vocabulary.items():
		print(count_fomat.format(k,v),file=neutcsv)
