import csv
import collections

vocabulary = {}
vocab_index = 0
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


vector_format = " {}:{}"
filepath = input("File Path")
outputpath = input("Output Path")
with open(outputpath,"w") as opfile:
	with open(filepath,"r") as datafile:
		reader = csv.reader(datafile, delimiter=',')
		for row_index,row in enumerate(reader):
			translated_row = row[0]
			tweet = row[5]
			tweet_dict = collections.defaultdict(int) #Tracks frequency of each term in the tweet
			vector_dict = {} #Dictionary to store Index:Value for each term for the tweet 
			for word in tweet.split(" "):
				clean_w = word_clean(word)
				if len(clean_w)>0:
					tweet_dict[clean_w]+=1 #Increasing term frequency
			for term in tweet_dict:
				vector_dict[get_word_index(term)] = tweet_dict[term] #Preparing Index:Value representation
			
			for idx in sorted(vector_dict):
				translated_row += vector_format.format(idx,vector_dict[idx])
			print(translated_row,file=opfile)	