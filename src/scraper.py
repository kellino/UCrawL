#!/usr/bin/env python2.7
from __future__ import print_function
import requests
from bs4 import BeautifulSoup
import urlparse
import robotparser
import re
import Queue
from threading import Thread

robotDict = {}
frontier = Queue.Queue(0)
visited = set()


class Scaper():
    def __init__(self, robotParserEnabled=True, restrictedDomain=True):
        self.robotParserEnabled = robotParserEnabled
        self.restrictedDomain = restrictedDomain
        self.illegal = [".mp4", ".mp3", ".flv", ".m4a",
                        ".jpg", ".png", ".gif",
                        ".xml", ".pdf", ".gz", ".zip", ".rss"]

    def visit(self, url):
        """ visits a given url and returns all the data """
        if (url.startswith("http://") or url.startswith("https://")) is False:
            url = "http://" + url

        domain = urlparse.urlsplit(url)[1].split(':')[0]
        httpDomain = "http://" + domain

        try:
            if self.robotParserEnabled:
                rp = robotparser.RobotFileParser()
                rp.set_url(urlparse.urljoin(httpDomain, "robots.txt"))
                rp.read()
                robotDict[httpDomain] = rp

                isParsable = rp.can_fetch("*", url)
                if not isParsable:
                    raise Exception("RobotParse")

            found_links = self.get_links(url, domain)
            for link in found_links:
                if link not in visited:
                    frontier.put(link)

            print("{} = [".format(url), end="")
            for link in found_links:
                print("{}, ".format(link), end="")
            print("]\n")
        except:
            pass

    def get_links(self, url, domain):
        if url not in visited:
            r = requests.get(url)
            if r.status_code == 200:
                c = r.content
                soup = BeautifulSoup(c.decode('utf-8', 'ignore'), 'lxml')
                links = soup.find_all('a')
                links = [s.get('href') for s in links]
                links = [unicode(s) for s in links]
                links = [s for s in links if re.search(domain, s)]
                links = [s[:-1] if s.endswith('/') else s for s in links]
                for ext in self.illegal:
                    links = [s for s in links if ext not in s]
                foundUrls = set(links)
                r.close()
                return foundUrls
            else:
                r.close()

    def crawl(self):
        while True:
            url = frontier.get()
            self.visit(url)
            visited.add(url)
            frontier.task_done()

if __name__ == '__main__':
    scaper = Scaper()
    frontier.put("ucl.ac.uk")
    # scaper.visit("ucl.ac.uk")
    for i in range(20):
        worker = Thread(target=scaper.crawl, args=())
        worker.setDaemon(True)
        worker.start()
    frontier.join()

    # print("total visited = {}".format(len(visited)))
