#!/usr/bin/env python2.7
from nltk.tokenize import word_tokenize
from nltk.stem import PorterStemmer
from nltk.corpus import stopwords
from collections import OrderedDict


stoplist = stopwords.words('english')
token_dict = dict()
pst = PorterStemmer()
postings_list = OrderedDict()


def get_pages():
    with open('ucl', 'r') as ifile:
        contents = ifile.read()
        for page in contents.split('visited:'):
            parse_page(page)


def parse_page(page):
    page = unicode(page, errors='ignore')
    tokens = [word for word in word_tokenize(page) if word not in stoplist]
    tokens = [pst.stem(word) for word in tokens]
    add_to_token_dict(tokens[3:])


def add_to_token_dict(tokens):
    if tokens:
        title = tokens[0]
        words = dict()
        for token in tokens[1:]:
            key = pst.stem(token.lower())
            if key in token_dict:
                token_dict[key] += 1
            else:
                token_dict[key] = 1
            if key in words:
                words[key] += 1
            else:
                words[key] = 1
        postings_list[title] = [(k, v) for k, v in words.iteritems()]


if __name__ == '__main__':
    get_pages()
    with open('complete_dictionary', 'a') as cd:
        for k, v in token_dict.iteritems():
            cd.writelines("word {}, frequency {}\n".format(k, v))
    for k, v in postings_list.iteritems():
        print k,
        for (x, y) in v:
            print "{}:{}".format(x, y),
        print "\n"
