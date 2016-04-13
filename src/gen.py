import string
from nltk.corpus import stopwords
from gensim import corpora


class MyCorpus:
    def __init__(self, filename, dictionary):
        self.filename = filename
        self.dictionary = dictionary

    def __iter__(self):
        with open(self.filename) as f:
            for line in f:
                yield self.dictionary.doc2bow(line.split())


if __name__ == '__main__':
    punctuation = list(string.punctuation)
    stoplist = stopwords.words('english')
    filename = './data/ucl.log'
    dictionary = corpora.Dictionary(
        line.lower().split() for line in open(filename))
    dictionary.filter_tokens(punctuation + stoplist)
    dictionary.compactify()
    corpus = MyCorpus(filename, dictionary)
    with open('./data/pl.log', 'w') as f:
        for doc in corpus:
            f.write(str(doc) + '\n')
