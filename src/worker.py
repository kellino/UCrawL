import threading


class Worker(threading.Thread):
    def __init__(self, target, args):
        self._target = target
        self._args = args
        threading.Thread.__init__(self)
        self.kill_ordered = False

    def run(self):
        self._target()
