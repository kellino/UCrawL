#!/usr/bin/env python2.7
from nltk.tokenize import word_tokenize
from nltk.stem import PorterStemmer
from nltk.corpus import stopwords


class Indexer():
    def __init__(self):
        self.stoplist = stopwords.words('english')
        self.token_dict = dict()
        self.pst = PorterStemmer()
        self.postings_list = dict()

    def get_pages(self):
        with open('ucl', 'r') as ifile:
            contents = ifile.read()
            for page in contents.split('visited:'):
                self.parse_page(page)

    def parse_page(self, page):
        page = unicode(page, errors='ignore')
        tokens = [word for word in word_tokenize(page)
                  if word not in self.stoplist]
        tokens = [self.pst.stem(word) for word in tokens]
        self.add_to_token_dict(tokens[3:])

    def add_to_token_dict(self, tokens):
        if tokens:
            title = tokens[0]
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
    with open('complete_dictionary', 'a') as cd:
        for k, v in indexer.token_dict.iteritems():
            cd.writelines("word {}, frequency {}\n".format(k, v))
    for k, v in indexer.postings_list.iteritems():
        print k,
        for (x, y) in v:
            print "{}:{}".format(x, y),
        print "\n"
