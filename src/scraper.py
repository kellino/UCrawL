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
        # normalize urls
        if (url.startswith("http://") or url.startswith("https://")) is False:
            url = "http://" + url
        domain = urlparse.urlsplit(url)[1].split(':')[0]
        httpDomain = "http://" + domain
        try:
            self.robot_parse(httpDomain, url)
            # if a page has not been visited, check it is a valid page and then
            # extract links and text
            if url not in visited:
                r = requests.get(url)
                if r.status_code == 200:
                    c = r.content
                    soup = BeautifulSoup(c.decode('utf-8', 'ignore'), 'lxml')
                    found_links = self.get_links(soup, domain)
                    self.get_text(soup, domain)
                    r.close()
                for link in found_links:
                    if link not in visited:
                        frontier.put(link)
                # dump info to stout
                # print("{} = [".format(url), end="")
                # for link in found_links:
                    # print("{}, ".format(link), end="")
                # print("]\n")
        except:
            # ignore the exception, as it indicatese a page which the robot
            # does not have permission to parse
            # print("something's gone wrong")
            pass

    def robot_parse(self, httpDomain, url):
        # do not attempt to parse any page which the robots.txt forbids
        if self.robotParserEnabled:
            rp = robotparser.RobotFileParser()
            rp.set_url(urlparse.urljoin(httpDomain, "robots.txt"))
            rp.read()
            robotDict[httpDomain] = rp
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

    def visible(self, element):
        if element.parent.name in ['style', 'script', '[document]', 'head', 'title']:
            return False
        elif re.match("<!--.*-->", element):
            return False
        return True

    def get_text(self, soup, domain):
        title = soup.find_all('title')
        body = soup.findAll(text=True)
        body = filter(self.visible, body)
        print("{}\n{}".format(title, body))

    def crawl(self):
        # main function for the the threads
        while True:
            url = frontier.get()
            self.visit(url)
            visited.add(url)
            frontier.task_done()

if __name__ == '__main__':
    scaper = Scaper()
    frontier.put("ucl.ac.uk")
    # frontier.put("kindersleystudio.co.uk")
    for i in range(4):
        worker = Thread(target=scaper.crawl, args=())
        worker.setDaemon(True)
        worker.start()
    frontier.join()
