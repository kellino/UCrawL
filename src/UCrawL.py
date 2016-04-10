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
    # put the seed in the frontier
    scraper.frontier.put(seed)
    # create threads
    thread_list = []
    for i in range(threads):
        worker = Worker(target=scraper.crawl, args=())
        worker.setDaemon(True)
        thread_list.append(worker)
        worker.start()
    # check for keyboard interrupt
    while len(thread_list) > 0:
        try:
            # listen for keyboard interrupt
            pass
        except KeyboardInterrupt:
            # clean up threads
            for t in thread_list:
                if t is not None:
                    t.join(0.0)
            print "Keyboard interrupt received"
            # kill the threads
            for worker in thread_list:
                worker.kill_ordered = True
            # close the frontier
            scraper.frontier.join()
            # close
            exit()

if __name__ == '__main__':
    run()
