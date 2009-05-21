#! /usr/bin/python
"""
Simple XML-RPC Chat server
"""

__author__ = 'Andrei Savu <contact@andreisavu.ro>'
__copyright__ = 'Copyright (c) 2009 Andrei Savu'

from SimpleXMLRPCServer import SimpleXMLRPCServer
import random
import time

sessions = {
	'names': {},
	'ids' : {}
}

messages = [ {'id': '0',
	'name': 'Server',
	'msg': 'Welcome to chat server'}
]

def gen_id():
	id = random.randint(1,10000)
	while id in sessions['ids']:
		id = random.randint(1,10000)
	return id

def start_session(name):
	if name == 'Server':
		return False

	if name in sessions['names']:
		id = sessions['names'][name]
		t = sessions['ids'][id]['last_seen']
		if t < (time.time() - 60):
			end_session(id)
		else:
			return False
		
	id = gen_id()
	sessions['names'][name] = id
	sessions['ids'][id] = {'name': name, 'last_seen': time.time()}
	admin_message("%s is online" % name)
	return id

def end_session(id):
	if id in sessions['ids']:
		name = sessions['ids'][id]['name']
		del(sessions['ids'][id])
		del(sessions['names'][name])
		admin_message("%s has left" % name)
	return True

_counter = 1
def get_message_id():
	global _counter
	_counter += 1
	return _counter 

def admin_message(msg):
	messages.append({'id': str(get_message_id()),
		'name': 'Server',
		'msg': msg})

def post_message(id, msg):
	if id not in sessions['ids']:
		return False

	sessions['ids'][id]['last_seen'] = time.time()
	name = sessions['ids'][id]['name']

	global messages
	messages.append({'id': str(get_message_id()),
		'name': name,
		'msg': msg})
	if len(messages) > 50:
		messages = messages[-50:]
	return True

def get_messages(id, last_id):
	if id not in sessions['ids']:
		return []
	sessions['ids'][id]['last_seen'] = time.time()
	return [m for m in messages if int(m['id']) >= last_id]

def get_users(id):
	if id not in sessions['ids']:
		return []
	sessions['ids'][id]['last_seen'] = time.time()
	return sessions['names'].keys()

if __name__ == '__main__':
	import sys
	
	port = 8000
	if len(sys.argv) > 1:
		port = int(sys.argv[1])

	server = SimpleXMLRPCServer(('', port))
	server.register_introspection_functions()

	server.register_function(start_session, 'Session.start')
	server.register_function(end_session, 'Session.end')

	server.register_function(post_message, 'Chat.post')
	server.register_function(get_messages, 'Chat.get')

	server.register_function(get_users, 'Chat.getUsers')

	print 'Chat server waiting for requests on port', port
	try:
		server.serve_forever()
	except KeyboardInterrupt, e:
		print 'Exiting'

