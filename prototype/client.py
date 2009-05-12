#! /usr/bin/python
"""
Simple XML-RPC Client
"""

__author__ = 'Andrei Savu <contact@andreisavu.ro>'
__copyright__ = 'Copyright (c) 2009 Andrei Savu'

import xmlrpclib as rpc
import sys
import curses
import threading
import time
import socket

class UI(object):

	def _init_curses(self):
		self.scr = curses.initscr()

	def _end_curses(self):
		curses.endwin()

	def txt(self,y,x,msg):
		try:
			self.scr.addstr(y,x,msg)
			return True
		except curses.error:
			return False

	def __init__(self, name):
		self._init_curses()
		self.name = name
		self.messages = []
		self.txt(0,0, name + ' > ')

	def get_message(self):
		start = len(self.name) + 3
		msg = self.scr.getstr(0, start)
		self.txt(0, start, ' ' * len(msg))
		self.scr.refresh()
		return msg

	def _erase_messages(self):
		count = 1
		for m in self.messages:
			if not self.txt(count, 0, ' ' * len(m)):
				break
			count += 1

	def _draw_messages(self):
		count = 1
		for m in self.messages:
			if not self.txt(count, 0, m):
				break
			count += 1

	def put_message(self, msg):
		y, x = curses.getsyx()
		self._erase_messages()
		self.messages.insert(0, msg)
		self._draw_messages()
		self.scr.move(y,x)
		self.scr.refresh()

	def close(self):
		self._end_curses()

class Fetcher(threading.Thread):

	def __init__(self, id, server, ui):
		threading.Thread.__init__(self)
		self.id = id
		self.ui = ui
		self.server = server
		self.active = True
	
	def run(self):
		last_message_id = -1
		while self.active:
			try:
				msgs = self.server.Chat.get(self.id, last_message_id+1)
			except socket.error, e:
				ui.put_message('Error: Unable to connect to server. Will retry in 5 seconds.')
				msgs = []
				time.sleep(5)

			for m in msgs:
				if m['id'] > last_message_id:
					last_message_id = m['id']
				ui.put_message('%s: %s' % (m['name'], m['msg']))

			time.sleep(1)

	def close(self):
		self.active = False

def parse_cli_params():
	url = 'http://localhost:8000'
	name = 'Guest'
	if len(sys.argv) > 1:
		name = sys.argv[1]
	if len(sys.argv) > 2:
		url = sys.argv[2]
	
	return url, name


def server_connect(url, name):
	s = rpc.ServerProxy(url)
	id = s.Session.start(name)

	if not id:
		print 'Name in use, chose another.'
		sys.exit(0)

	return s, id

if __name__ == '__main__':
	url, name = parse_cli_params()
	try:
		s, id  = server_connect(url, name)
	except socket.error, e:
		print 'Unable to connect to server', url, 'as', name
		sys.exit(1)

	ui = UI(name)

	fetcher = Fetcher(id, s, ui)
	fetcher.start()

	while True:
		try:
			msg = ui.get_message()
			if msg.strip() == '':
				continue

			if msg == '/quit':
				break

			if msg == '/list':
				ui.put_message('Users: %s' % ','.join(s.Chat.getUsers(id)))
				continue

			if msg == '/help':
				ui.put_message('Commands: /help /list /quit')
				continue

			if not s.Chat.post(id, msg):
				ui.put_message('Error: Message send failed. Please reconnect')
		except KeyboardInterrupt, e:
			break 

	fetcher.close()
	fetcher.join()
	s.Session.end(id)
	ui.close()

