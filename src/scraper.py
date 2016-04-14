#!/usr/bin/env python2.7
from __future__ import print_function
import requests
from bs4 import BeautifulSoup
import logging
import urlparse
import robotparser
import re
import Queue


class RobotParseException(Exception):
    def __init__(self):
        Exception.__init__(self)


class Scraper:
    def __init__(self, robotParserEnabled=True, restrictedDomain=True):
        self.logger = logging.getLogger('UCrawL')
        self.logger.debug('initializing scraper')
        self.robotDict = {}
        self.frontier = Queue.Queue(0)
        self.visited = set()
        self.robotParserEnabled = robotParserEnabled
        self.restrictedDomain = restrictedDomain
        # list of extensions to ignore
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
                    self.save_text(url, soup)
                    r.close()
                for link in found_links:
                    if link not in self.visited:
                        if link == url:
                            continue
                        else:
                            self.frontier.put(link)
                self.print_all_links(url, found_links)
        except RobotParseException:
            self.logger.info('robots.txt forbids scraping of {}'.format(url))
        except:
            self.logger.warn('unable to connect to server')

    def robot_parse(self, httpDomain, url):
        # do not attempt to parse any page which the robots.txt forbids
        if self.robotParserEnabled:
            rp = robotparser.RobotFileParser()
            rp.set_url(urlparse.urljoin(httpDomain, "robots.txt"))
            rp.read()
            self.robotDict[httpDomain] = rp
            isParsable = rp.can_fetch("*", url)
            if not isParsable:
                raise RobotParseException()

    def get_links(self, soup, domain):
        # extracts all the (domain) links on a visited page, returning them as
        # a set
        try:
            links = soup.find_all('a')
            links = [s.get('href') for s in links]
            links = [unicode(s) for s in links]
            links = [s for s in links if re.search(domain, s)]
            links = [s[:-1] if s.endswith('/') else s for s in links]
            for ext in self.illegal:
                links = [s for s in links if ext not in s]
            foundUrls = set(links)
        except:
            self.logger.warn("soup unable to parse links from {}".format(domain))
        return foundUrls

    def print_all_links(self, url, found_links):
        try:
            with open('./../data/link_list', 'a') as f:
                f.write('{} {}\n'.format(url, str(found_links)))
        except IOError:
            self.logger.warn('unable to write/append to file')

    def save_text(self, url, soup):
        # thanks to Hugh Bothwell and Bumpkin on StackOverflow for this!
        for script in soup(["script", "style"]):
            script.extract()
        text = soup.get_text()
        lines = (line.strip() for line in text.splitlines())
        chunks = (phrase.strip() for line
                  in lines for phrase in line.split("  "))
        text = ' '.join(chunk for chunk in chunks if chunk)
        try:
            with open('./../data/ucl_text', 'a') as f:
                f.write("{} {}\n".format(url, text.encode('utf-8')))
        except:
            self.logger.warn('unable to write/append to file')

    def crawl(self, stop_event):
        while not stop_event.wait(1):
            url = self.frontier.get()
            self.visit(url)
            self.visited.add(url)
