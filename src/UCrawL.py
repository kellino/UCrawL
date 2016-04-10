#!/usr/bin/env python2.7
import click
import sys
from scraper import Scraper
from worker import Worker


@click.command()
@click.option('--seed', help='Domain to be crawled')
@click.option('--threads', default=1, help='Number of daemon threads')
@click.option('--limit', default=None, help='Maximum number of pages to visit')
def run(threads, seed, limit):
    sys.tracebacklimit = 0
    """ UCrawL: a simple multi-threaded crawler/indexer designed
        with the goal of scraping a single domain """
    scraper = Scraper()
    scraper.frontier.put(seed)
    thread_list = []
    for i in range(threads):
        worker = Worker(target=scraper.crawl, args=())
        worker.setDaemon(True)
        thread_list.append(worker)
        worker.start()

    while len(thread_list) > 0:
        try:
            thread_list = [worker.join(threads)
                           for i in thread_list if worker is not None]
        except KeyboardInterrupt:
            print "Keyboard interrupt received"
            for worker in thread_list:
                worker.kill_ordered = True
            scraper.frontier.join()
            exit()

if __name__ == '__main__':
    run()
