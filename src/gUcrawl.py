#!/usr/bin/env python2.7
import click
from scraper import Scraper
from threading import Thread


@click.command()
@click.option('--domain', help='Domain to be crawled')
@click.option('--threads', default=1, help='Number of daemon threads')
@click.option('--limit', default=None, help='Maximum number of pages to visit')
def run(threads, domain, limit):
    scraper = Scraper()
    scraper.frontier.put(domain)
    for i in range(threads):
        worker = Thread(target=scraper.crawl, args=())
        worker.setDaemon(True)
        worker.start()
    scraper.frontier.join()


if __name__ == '__main__':
    run()
