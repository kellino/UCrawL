from nltk.tokenize import word_tokenize
from nltk.corpus import stopwords
from nltk.stem.porter import PorterStemmer
from gensim import corpora


class MyCorpus:
    """ indexer utilizing the excellent gensim and nltk modules """
    def __init__(self, filename, dictionary):
        self.filename = filename
        self.dictionary = dictionary

    def __iter__(self):
        with open(self.filename) as f:
            for line in f:
                yield self.dictionary.doc2bow(line.split())


def clean_doc(doc):
    stopset = set(stopwords.words('english'))
    p = PorterStemmer()
    tokens = word_tokenize(doc)
    print tokens
    tokens = [token.lower() for token in tokens]
    tokens = [p.stem(word) for word in tokens]
    return str(tokens)

cleaned = ""

if __name__ == '__main__':
    filename = './data/d'
    with open(filename) as f:
        for line in f:
            clean = clean_doc(line)
            cleaned += clean
    # construct dictionary from file
    dictionary = corpora.Dictionary(line.lower().split() for line in cleaned)
    # filter out stopwords and punctuation
    # dictionary.filter_tokens(punctuation + stoplist)
    # # remove now empty elements from dictionary
    dictionary.compactify()
    corpus = MyCorpus(cleaned, dictionary)
    with open('./data/pl.log', 'w') as f:
        for doc in corpus:
            f.write(str(doc) + '\n')
