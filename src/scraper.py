#!/usr/bin/env python2.7
from __future__ import print_function
import requests
from bs4 import BeautifulSoup
import urlparse
import robotparser
import re
import Queue


class Scraper():
    def __init__(self, robotParserEnabled=True, restrictedDomain=True):
        self.robotDict = {}
        self.frontier = Queue.Queue(0)
        self.visited = set()
        self.robotParserEnabled = robotParserEnabled
        self.restrictedDomain = restrictedDomain
        self.illegal = [".mp4", ".mp3", ".flv", ".m4a",
                        ".jpg", ".png", ".gif",
                        ".xml", ".pdf", ".gz", ".zip", ".rss"]

    def visit(self, url):
        """ visits a given url and returns all the data """
        # normalize urls
        if (url.startswith("http://") or url.startswith("https://")) is False:
            url = "http://" + url
        domain = urlparse.urlsplit(url)[1].split(':')[0]
        httpDomain = "http://" + domain
        try:
            self.robot_parse(httpDomain, url)
            # if a page has not been visited, check it is a valid page and then
            # extract links and text
            if url not in self.visited:
                r = requests.get(url)
                if r.status_code == 200:
                    c = r.content
                    soup = BeautifulSoup(c.decode('utf-8', 'ignore'), 'lxml')
                    found_links = self.get_links(soup, domain)
                    self.get_text(url, soup)
                    r.close()
                for link in found_links:
                    if link not in self.visited:
                        if link == url:
                            continue
                        else:
                            self.frontier.put(link)
                # self.print_all_links(url, found_links)
        except:
            pass  # robot parser says no

    def robot_parse(self, httpDomain, url):
        # do not attempt to parse any page which the robots.txt forbids
        if self.robotParserEnabled:
            rp = robotparser.RobotFileParser()
            rp.set_url(urlparse.urljoin(httpDomain, "robots.txt"))
            rp.read()
            self.robotDict[httpDomain] = rp
            isParsable = rp.can_fetch("*", url)
            if not isParsable:
                raise Exception("RobotParse")

    def get_links(self, soup, domain):
        # extracts all the (domain) links on a visited page, returning them as
        # a set
        links = soup.find_all('a')
        links = [s.get('href') for s in links]
        links = [unicode(s) for s in links]
        links = [s for s in links if re.search(domain, s)]
        links = [s[:-1] if s.endswith('/') else s for s in links]
        for ext in self.illegal:
            links = [s for s in links if ext not in s]
        foundUrls = set(links)
        return foundUrls

    def print_all_links(self, url, found_links):
        # dump info to stout
        print("{} = [".format(url), end="")
        for link in found_links:
            print("{}, ".format(link), end="")
        print("]\n")

    def get_text(self, url, soup):
        # thanks to Hugh Bothwell and Bumpkin on StackOverflow for this!
        for script in soup(["script", "style"]):
            script.extract()
        text = soup.get_text()
        lines = (line.strip() for line in text.splitlines())
        chunks = (phrase.strip() for line
                  in lines for phrase in line.split("  "))
        text = '\n'.join(chunk for chunk in chunks if chunk)
        print("visited: {}".format(len(self.visited)+1))
        print(url)
        print(text.encode('utf-8'))
        print('\n')

    def crawl(self):
        # main function for the threads
        running = True
        while running:
            url = self.frontier.get()
            self.visit(url)
            self.visited.add(url)
            # when the frontier is empty, finish the process
            self.frontier.task_done()
