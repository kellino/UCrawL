#!/usr/bin/env python2.7
from nltk.corpus import stopwords
from nltk.stem.porter import PorterStemmer
from gensim import corpora


def index(filepath):
    stoplist = set(stopwords.words('english'))
    p = PorterStemmer()

    # repr() needed to avoid unicode errors in raw file
    texts = [[p.stem(repr(word)) for word in document.lower().split()
             if word not in stoplist] for document in open(filepath)]

    dictionary = corpora.Dictionary(texts)
    dictionary.save('ucl.dict')

    corpus = [dictionary.doc2bow(text) for text in texts]

    # write postings list to file
    with open('postings_list', 'w') as f:
        for doc in corpus:
            f.write(str(doc) + '\n')


if __name__ == '__main__':
    index('./data/ucl.log')
