#!/usr/bin/env python2.7
import string
from nltk.stem import PorterStemmer
from nltk.tokenize import word_tokenize
from nltk.corpus import stopwords


class Indexer():
    def __init__(self, rem_punc=True, rem_stop=True):
        self.rem_punc = rem_punc
        self.rem_stop = rem_stop
        self.stoplist = stopwords.words('english')
        self.punctunation = list(string.punctuation)
        self.token_dict = dict()
        self.pst = PorterStemmer()
        self.postings_list = dict()

    def get_pages(self):
        with open('./data/ucl', 'r') as ifile:
            contents = ifile.read()
            for page in contents.split('visited:'):
                self.parse_page(page)

    def parse_page(self, page):
        page = unicode(page, errors='ignore')
        lines = page.strip().split()
        if len(lines) > 2:
            title = lines[1]
            # tokenize and make lowercase
            tokens = [word.lower() for word in word_tokenize(str(lines[2:]))]
            # remove punctuation
            if self.rem_punc:
                tokens = [word for word in tokens if word not in self.punctunation]
            # remove stopwords
            if self.rem_stop:
                tokens = [word for word in tokens if word not in self.stoplist]
            # stem (Porter stemmer)
            tokens = [self.pst.stem(word) for word in tokens]
            # add to dictionary
            self.add_to_token_dict(title, tokens[3:])

    def add_to_token_dict(self, title, tokens):
        if tokens:
            words = dict()
            for token in tokens[1:]:
                key = self.pst.stem(token.lower())
                if key in self.token_dict:
                    self.token_dict[key] += 1
                else:
                    self.token_dict[key] = 1
                if key in words:
                    words[key] += 1
                else:
                    words[key] = 1
            self.postings_list[title] = [(k, v) for k, v in words.iteritems()]


if __name__ == '__main__':
    indexer = Indexer()
    indexer.get_pages()
    for k, v in indexer.postings_list.iteritems():
        print k,
        for (x, y) in v:
            print "{}:{}".format(x, y),
        print "\n"
    with open('complete_dictionary', 'w') as cd:
        for k, v in indexer.token_dict.iteritems():
            cd.writelines("{} {}\n".format(k, v))
