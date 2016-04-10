#!/usr/bin/env python2.7
import click
from scraper import Scraper
from threading import Thread


@click.command()
@click.option('--seed', help='Domain to be crawled')
@click.option('--threads', default=1, help='Number of daemon threads')
@click.option('--limit', default=None, help='Maximum number of pages to visit')
def run(threads, seed, limit):
    """ UCrawL: a simple multi-threaded crawler/indexer designed
        with the goal of scraping a single domain """
    scraper = Scraper()
    scraper.frontier.put(seed)
    for i in range(threads):
        worker = Thread(target=scraper.crawl, args=())
        worker.setDaemon(True)
        worker.start()
    scraper.frontier.join()


if __name__ == '__main__':
    run()
