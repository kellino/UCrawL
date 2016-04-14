#!/usr/bin/env python2.7
import click
import threading
import UCLogger
from scraper import Scraper


@click.command()
@click.option('--seed', default=None, help='Domain to be crawled')
@click.option('--threads', default=1, help='Number of daemon threads')
@click.option('--limit', default=1000, help='Maximum number of pages to visit')
def run(threads, seed, limit):
    kill_signal = threading.Event()
    scraper = Scraper()
    if seed is None:
        exit(1)
    else:
        scraper.frontier.put(seed)

    # create threads
    def thread_gen(kill_signal):
        t = threading.Thread(target=scraper.crawl, args=(kill_signal, ))
        t.setDaemon(True)
        return t

    try:
        thread_list = [thread_gen(kill_signal) for thread in range(threads)]
        map(threading.Thread.start, thread_list)
    except:
        pass

    running = True
    while running:
        if len(scraper.visited) > limit:
            kill_signal.set()
            map(threading.Thread.join, thread_list)
            scraper.frontier.task_done()
            running = False

if __name__ == '__main__':
    logger = UCLogger.logger()
    run()
